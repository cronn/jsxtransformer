package de.cronn.babeltransformer;

import java.io.IOException;

import org.junit.Test;
import org.mozilla.javascript.JavaScriptException;

import de.cronn.jsxtransformer.CachedJsxTransformer;
import de.cronn.jsxtransformer.CachedJsxTransformer.AssetEntry;
import de.cronn.jsxtransformer.CachedJsxTransformer.ContentProvider;

/**
 * Test for BabelTransformer
 * 
 * @author Hanno Fellmann, cronn GmbH
 */
public class JsxCacheTest {
	@Test
	public void testCacheWithoutUglify() throws IOException {
		final CachedJsxTransformer testee = new CachedJsxTransformer(false, false);
		final AssetEntry entry = getCompiled(testee, "/testData/input_jsx.js");
		TestUtils.assertThatEqualsFile(entry.content, "/testData/output_babel.js");
	}

	@Test
	public void testCacheWithUglify() throws IOException {
		final CachedJsxTransformer testee = new CachedJsxTransformer(true, false);
		final AssetEntry entry = getCompiled(testee, "/testData/input_jsx.js");
		TestUtils.assertThatEqualsFile(entry.content, "/testData/output_uglified.js");
	}

	@Test(expected = JavaScriptException.class)
	public void testCacheError() throws IOException {
		final CachedJsxTransformer testee = new CachedJsxTransformer(false, false);
		getCompiled(testee, "/testData/input_jsx_error.js");
	}

	private AssetEntry getCompiled(final CachedJsxTransformer testee, final String key) throws IOException {
		return testee.get(key, new ContentProvider() {
			@Override
			public String get(String key) throws IOException {
				return TestUtils.readFile(key);
			}
		});
	}
}
