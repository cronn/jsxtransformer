package de.cronn.babeltransform;

import java.net.URI;
import java.util.List;
import java.util.Objects;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.commonjs.module.Require;
import org.mozilla.javascript.commonjs.module.RequireBuilder;
import org.mozilla.javascript.commonjs.module.provider.SoftCachingModuleScriptProvider;
import org.mozilla.javascript.commonjs.module.provider.UrlModuleSourceProvider;

/**
 * Helper class to execute JavaScript tasks in a defined JavaScript thread
 * context
 *
 * @author Hanno Fellmann, cronn GmbH
 */
public abstract class JsTransformBase2 {
	protected Scriptable exports;

	protected ScriptableObject topLevelScope;

	private List<URI> modulePaths;

	private String transformJs;

	protected void init(final String transformJs, final List<URI> modulePaths) {
		this.transformJs = Objects.requireNonNull(transformJs);
		this.modulePaths = Objects.requireNonNull(modulePaths);
	}

	/**
	 * Helper function to create module path for require
	 * 
	 * @param modulePaths
	 *            requested module paths
	 * @return list of URIs to module paths
	 */
	private SoftCachingModuleScriptProvider buildModulePaths() {
		return new SoftCachingModuleScriptProvider(new UrlModuleSourceProvider(modulePaths, null));
	}

	private void init(final Context ctx) {
		final RequireBuilder builder = new RequireBuilder();
		builder.setModuleScriptProvider(buildModulePaths());

		topLevelScope = ctx.initStandardObjects();
		final Require require = builder.createRequire(ctx, topLevelScope);

		exports = require.requireMain(ctx, transformJs);
	}

	public synchronized String transform(final String input) {
		final Context ctx = Context.enter();
		// Will otherwise crash on huge libraries like babel.js
		ctx.setOptimizationLevel(-1);
		try {
			init(ctx);
			return transformInContext(input, ctx);
		} finally {
			Context.exit();
		}
	}

	protected abstract String transformInContext(String input, Context ctx);
}
