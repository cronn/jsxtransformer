package de.cronn.babeltransform;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.NativeObject;

/**
 * Babel.js transformer
 *
 * @author Hanno Fellmann, cronn GmbH
 */
public class BabelTransformer extends JsTransformBase {
	private final String presets = "'es2015', 'react'";

	/**
	 * Create a new Babel.js based transformer
	 */
	public BabelTransformer() throws IOException, URISyntaxException {
		init("babel", Arrays.asList(getClass().getResource("/transformJs/").toURI()));
	}

	protected String transformInContext(final String jsx, final Context ctx) {
		final Function transform = (Function) exports.get("transform", topLevelScope);

		ctx.evaluateString(topLevelScope, "var babelArgs = { retainLines: true, presets: [" + presets + "] };", "args",
				1, null);
		final Object args = topLevelScope.get("babelArgs");

		final NativeObject obj = (NativeObject) transform.call(ctx, topLevelScope, exports, new Object[] { jsx, args });
		return (String) obj.get("code");
	}
}
