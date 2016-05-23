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
	static void assertThatEqualsFile(final String content, final String testDataFile) throws IOException {
		final String expected = readFile(testDataFile);

		Assert.assertEquals(prepareString(expected), prepareString(content));
	}

	static String readFile(final String testDataFile) throws IOException {
		return IOUtils.toString(TestUtils.class.getResourceAsStream(testDataFile), Charset.defaultCharset()).trim();
	}

	static String prepareString(final String content) {
		return content.trim().replace("\r\n", "\n");
	}
}
