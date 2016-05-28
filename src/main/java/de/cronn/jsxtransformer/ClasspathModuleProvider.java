package de.cronn.jsxtransformer;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.mozilla.javascript.commonjs.module.provider.ModuleSource;
import org.mozilla.javascript.commonjs.module.provider.ModuleSourceProviderBase;

/**
 * Special module provider for Rhino to load from classpath, which is not
 * supported by java.net.URI
 *
 * @author Hanno Fellmann, cronn GmbH
 */
public class ClasspathModuleProvider extends ModuleSourceProviderBase {
	private String modulePath;

	public ClasspathModuleProvider(String modulePath) {
		this.modulePath = modulePath;
	}

	@Override
	protected ModuleSource loadFromPrivilegedLocations(String moduleId, Object validator)
			throws IOException, URISyntaxException {
		return createModuleSource(new URI(moduleId + ".js"), new URI("/"), modulePath + moduleId + ".js");
	}

	@Override
	protected ModuleSource loadFromFallbackLocations(String moduleId, Object validator)
			throws IOException, URISyntaxException {
		return null;
	}

	@Override
	protected ModuleSource loadFromUri(URI uri, URI base, Object validator) throws IOException, URISyntaxException {
		// Get relative path to current path
		return createModuleSource(new URI(uri + ".js"), base, modulePath + uri.toString() + ".js");
	}

	private ModuleSource createModuleSource(URI uri, URI base, String path) {
		InputStream stream = getClass().getResourceAsStream(path);
		if (stream != null) {
			return new ModuleSource(new InputStreamReader(stream, StandardCharsets.UTF_8), null, uri, base, null);
		}
		return null;
	}

}
