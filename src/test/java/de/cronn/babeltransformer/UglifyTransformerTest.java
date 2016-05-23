package de.cronn.babeltransformer;

import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.Test;

import de.cronn.babeltransform.UglifyTransformer;

/**
 * Test for Uglifier
 * 
 * @author Hanno Fellmann, cronn GmbH
 */
public class UglifyTransformerTest {
	@Test
	public void testCacheWithoutUglify() throws IOException, URISyntaxException {
		UglifyTransformer testee = new UglifyTransformer();
		String content = testee.transform(TestUtils.readFile("/testData/output_babel.js"));
		TestUtils.assertThatEqualsFile(content, "/testData/output_uglified.js");
	}
	
	@Test
	public void multipleUsage() throws IOException, URISyntaxException {
		UglifyTransformer testee = new UglifyTransformer();

		String input = TestUtils.readFile("/testData/output_babel.js");
		for(int i=0; i< 100; i++)
		{
			String content = testee.transform(input);
			TestUtils.assertThatEqualsFile(content, "/testData/output_uglified.js");
		}
	}
}
