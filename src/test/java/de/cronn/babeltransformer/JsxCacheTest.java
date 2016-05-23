package de.cronn.babeltransformer;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.Test;
import org.mozilla.javascript.JavaScriptException;

import de.cronn.babeltransform.JsxCache;
import de.cronn.babeltransform.JsxCache.AssetEntry;

/**
 * Test for BabelTransformer
 * 
 * @author Hanno Fellmann, cronn GmbH
 */
public class JsxCacheTest {
	@Test
	public void testCacheWithoutUglify() throws IOException, URISyntaxException {
		JsxCache testee = new JsxCache(false, false);
		AssetEntry entry = testee.get(new File(getClass().getResource("/testData/input_jsx.js").getFile()));
		TestUtils.assertThatEqualsFile(entry.content, "/testData/output_babel.js");
	}

	@Test
	public void testCacheWithUglify() throws IOException, URISyntaxException {
		JsxCache testee = new JsxCache(true, false);
		AssetEntry entry = testee.get(getTestDataFile("/testData/input_jsx.js"));
		TestUtils.assertThatEqualsFile(entry.content, "/testData/output_uglified.js");
	}

	File getTestDataFile(String fileName) {
		return new File(getClass().getResource(fileName).getFile());
	}

	@Test(expected = JavaScriptException.class)
	public void testCacheError() throws IOException, URISyntaxException {
		JsxCache testee = new JsxCache(false, false);
		testee.get(new File(getClass().getResource("/testData/input_jsx_error.js").getFile()));
	}
}
