package de.cronn.babeltransformer;

import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.Test;

import de.cronn.babeltransform.BabelTransformer;

/**
 * Test for JsxCache
 * 
 * @author Hanno Fellmann, cronn GmbH
 */
public class BabelTransformerTest {
	@Test
	public void testCacheWithoutUglify() throws IOException, URISyntaxException {
		BabelTransformer testee = new BabelTransformer();
		String content = testee.transform(TestUtils.readFile("/testData/input_jsx.js"));
		TestUtils.assertThatEqualsFile(content, "/testData/output_babel.js");
	}
	
	@Test
	public void multipleUsage() throws IOException, URISyntaxException {
		BabelTransformer testee = new BabelTransformer();

		String input = TestUtils.readFile("/testData/input_jsx.js");
		for(int i=0; i< 100; i++)
		{
			String content = testee.transform(input);
			TestUtils.assertThatEqualsFile(content, "/testData/output_babel.js");
		}
	}
}
