package de.cronn.babeltransform;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;

/**
 * Uglify.js transformer
 *
 * @author Hanno Fellmann, cronn GmbH
 */
public class UglifyTransformer extends JsTransformBase {
	/**
	 * Create a new Uglify.js based transformer
	 * 
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public UglifyTransformer() throws IOException, URISyntaxException {
		init("uglify-js", Arrays.asList(this.getClass().getResource("/transformJs/uglify/").toURI()));
	}

	@Override
	protected String transformInContext(final String jsx, Context ctx) {
		Function transform = (Function) exports.get("convenience", topLevelScope);
		return (String) transform.call(ctx, topLevelScope, exports, new String[] { jsx });
	}
}
