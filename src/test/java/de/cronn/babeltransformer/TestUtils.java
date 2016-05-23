package de.cronn.babeltransformer;

import java.io.IOException;
import java.nio.charset.Charset;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;

/**
 * Test utils
 *
 * @author Hanno Fellmann, cronn GmbH
 * 
 */
class TestUtils {
	static void assertThatEqualsFile(String content, String testDataFile) throws IOException {
		String expected = readFile(testDataFile);

		Assert.assertEquals(prepareString(expected), prepareString(content));
	}

	static String readFile(String testDataFile) throws IOException {
		return IOUtils.toString(TestUtils.class.getResourceAsStream(testDataFile), Charset.defaultCharset())
				.trim();
	}

	static String prepareString(String content) {
		return content.trim().replace("\r\n", "\n");
	}
}
