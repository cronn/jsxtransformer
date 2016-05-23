package de.cronn.babeltransform;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.io.IOUtils;

/**
 * Class for caching javascript assets in jsx transformed and minified form
 *
 * @author Hanno Fellmann, cronn GmbH
 */
public class JsxCache {
	private final BabelTransformer jsxTransformer;

	private final UglifyTransformer uglifier;

	private final ConcurrentHashMap<String, AssetEntry> jsCache = new ConcurrentHashMap<String, AssetEntry>();

	private final boolean diskCache;

	/**
	 * Create a new JSX compile cache
	 * 
	 * @param uglify
	 *            enable uglifier
	 * @param useDiskCache
	 *            enable disk cache. If true, compiled content will be cached on
	 *            disk to be loaded faster after restart.
	 * @throws IOException
	 *             on input file read/write error
	 * @throws URISyntaxException
	 *             on URI formatting error for javascript libraries
	 */
	public JsxCache(boolean uglify, boolean useDiskCache) throws IOException, URISyntaxException {
		this.diskCache = useDiskCache;
		jsxTransformer = new BabelTransformer();
		if (uglify) {
			uglifier = new UglifyTransformer();
		} else {
			uglifier = null;
		}
	}

	/**
	 * Entry in asset Cache.
	 */
	public static class AssetEntry {
		/**
		 * The compiled cache content.
		 */
		public final String content;

		/**
		 * Etag based on the uncompiled input content
		 */
		public final String etag;

		public final long lastModified;

		private AssetEntry(final String content, final long lastModified, final String etag) {
			super();
			this.content = content;
			this.lastModified = lastModified;

			this.etag = "\"" + etag + "\"";
		}
	}

	/**
	 * Get compiled JSX file from cache
	 * 
	 * Gets the specified JSX file from cache or recompiles it if it
	 * non-existent or changed since the last compile.
	 * 
	 * @param file
	 *            The input JSX file.
	 * @return The compiled content as JavaScript.
	 * @throws IOException
	 *             on compile or file read/write error.
	 * @throws JavaScriptException on compilation error
	 */

	public AssetEntry get(final File file) throws IOException {
		final AssetEntry entry = jsCache.get(file.getPath());

		if (entry == null || (entry.lastModified < file.lastModified())) {
			return getNewEntry(file.getPath(), file);
		} else {
			return entry;
		}
	}

	private AssetEntry getNewEntry(final String fileName, final File file) throws IOException {
		String newContent = null;

		final String content = IOUtils.toString(new FileInputStream(file), Charset.defaultCharset());
		final String etag = UUID.nameUUIDFromBytes(content.getBytes()).toString();
		final String cachePrefix = "//" + etag + "\n";

		final File cacheFile = new File(file.getAbsolutePath() + ".cache");
		if (diskCache) {
			if (!file.getName().startsWith("test") && cacheFile.exists()) {
				final String cacheContent = IOUtils.toString(new FileInputStream(cacheFile), "UTF-8");
				if (cacheContent.startsWith(cachePrefix)) {
					newContent = cacheContent.substring(cachePrefix.length());
				}
			}
		}
		if (newContent == null) {
			newContent = jsxTransformer.transform(content);
			if (uglifier != null) {
				newContent = uglifier.transform(newContent);
			}

			if (diskCache) {
				final FileOutputStream fos = new FileOutputStream(cacheFile);
				fos.write(cachePrefix.getBytes());
				fos.write(newContent.getBytes());
				fos.close();
			}
		}

		final AssetEntry entry = new AssetEntry(newContent, file.lastModified(), etag);
		jsCache.put(fileName, entry);
		return entry;
	}
}
