package de.cronn.babeltransform;

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
 * Special module provider for Rhino to load from classpath,
 * which is not supported by java.net.URI
 *
 * @author Hanno Fellmann, cronn GmbH
 */
public class ClasspathModuleProvider extends ModuleSourceProviderBase {
	private List<String> modulePaths;

	public ClasspathModuleProvider(List<String> modulePaths) {
		this.modulePaths = modulePaths;
	}

	@Override
	protected ModuleSource loadFromPrivilegedLocations(String moduleId, Object validator)
			throws IOException, URISyntaxException {
		for (String path : modulePaths) {
			InputStream stream = getClass().getResourceAsStream(path + moduleId + ".js");
			if (stream != null) {
				URI uri = getClass().getResource(path + moduleId + ".js").toURI();
				URI base = getClass().getResource(path).toURI();
				return new ModuleSource(new InputStreamReader(stream, StandardCharsets.UTF_8), null, uri, base, null);
			}
		}
		return null;
	}

	@Override
	protected ModuleSource loadFromFallbackLocations(String moduleId, Object validator)
			throws IOException, URISyntaxException {
		return null;
	}

	@Override
	protected ModuleSource loadFromUri(URI uri, URI base, Object validator) throws IOException, URISyntaxException {
		String moduleId = base.relativize(uri).toString();
		return loadFromPrivilegedLocations(moduleId, null);
	}

}
