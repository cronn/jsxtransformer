package de.cronn.babeltransformer;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.junit.Test;

import de.cronn.jsxtransformer.BabelTransformer;

/**
 * Test for JsxCache
 * 
 * @author Hanno Fellmann, cronn GmbH
 */
public class BabelTransformerTest {
	@Test
	public void testTransform() throws IOException, URISyntaxException {
		final BabelTransformer testee = new BabelTransformer();
		final String content = testee.transform(TestUtils.readFile("/testData/input_jsx.js"));
		TestUtils.assertThatEqualsFile(content, "/testData/output_babel.js");
	}

	@Test
	public void multipleUsage() throws IOException, URISyntaxException {
		final BabelTransformer testee = new BabelTransformer();

		final String input = TestUtils.readFile("/testData/input_jsx.js");
		for (int i = 0; i < 100; i++) {
			final String content = testee.transform(input);
			TestUtils.assertThatEqualsFile(content, "/testData/output_babel.js");
		}
	}

	@Test
	public void multipleThreads() throws IOException, URISyntaxException, InterruptedException, ExecutionException {
		final BabelTransformer testee = new BabelTransformer();

		final ArrayList<Future<Void>> futures = new ArrayList<Future<Void>>();

		final String input = TestUtils.readFile("/testData/input_jsx.js");
		final ExecutorService threadPool = Executors.newFixedThreadPool(5);
		for (int i = 0; i < 5; i++) {
			futures.add(threadPool.submit(new Callable<Void>() {
				public Void call() throws Exception {
					for (int i = 0; i < 20; i++) {
						final String content = testee.transform(input);
						TestUtils.assertThatEqualsFile(content, "/testData/output_babel.js");
					}
					return null;
				}
			}));
		}
		for (final Future<Void> f : futures) {
			f.get();
		}
		threadPool.shutdown();
	}
}
