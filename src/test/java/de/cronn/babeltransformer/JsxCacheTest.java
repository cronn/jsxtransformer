package de.cronn.babeltransformer;

import java.io.IOException;

import org.junit.Test;
import org.mozilla.javascript.JavaScriptException;

import de.cronn.babeltransform.CachedJsxCompiler;
import de.cronn.babeltransform.CachedJsxCompiler.AssetEntry;
import de.cronn.babeltransform.CachedJsxCompiler.ContentProvider;

/**
 * Test for BabelTransformer
 * 
 * @author Hanno Fellmann, cronn GmbH
 */
public class JsxCacheTest {
	@Test
	public void testCacheWithoutUglify() throws IOException {
		final CachedJsxCompiler testee = new CachedJsxCompiler(false, false);
		final AssetEntry entry = getCompiled(testee, "/testData/input_jsx.js");
		TestUtils.assertThatEqualsFile(entry.content, "/testData/output_babel.js");
	}

	@Test
	public void testCacheWithUglify() throws IOException {
		final CachedJsxCompiler testee = new CachedJsxCompiler(true, false);
		final AssetEntry entry = getCompiled(testee, "/testData/input_jsx.js");
		TestUtils.assertThatEqualsFile(entry.content, "/testData/output_uglified.js");
	}

	@Test(expected = JavaScriptException.class)
	public void testCacheError() throws IOException {
		final CachedJsxCompiler testee = new CachedJsxCompiler(false, false);
		getCompiled(testee, "/testData/input_jsx_error.js");
	}

	private AssetEntry getCompiled(final CachedJsxCompiler testee, final String key) throws IOException {
		return testee.get(key, new ContentProvider() {
			@Override
			public String get(String key) throws IOException {
				return TestUtils.readFile(key);
			}
		});
	}
}
