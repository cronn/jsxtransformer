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
public abstract class JsTransformBase {
	protected Scriptable exports;

	protected ScriptableObject topLevelScope;

	private List<String> modulePaths;

	protected void init(final String transformJs, final List<String> modulePaths) {
		this.modulePaths = Objects.requireNonNull(modulePaths);

		final Context ctx = Context.enter();

		// Will otherwise crash on huge libraries like babel.js
		ctx.setOptimizationLevel(-1);

		try {
			final RequireBuilder builder = new RequireBuilder();
			builder.setSandboxed(false);
			builder.setModuleScriptProvider(buildModulePaths());

			topLevelScope = ctx.initStandardObjects();
			final Require require = builder.createRequire(ctx, topLevelScope);
			exports = require.requireMain(ctx, transformJs);
		} finally {
			Context.exit();
		}
	}

	/**
	 * Helper function to create module path for require
	 * 
	 * @param modulePaths
	 *            requested module paths
	 * @return list of URIs to module paths
	 */
	private SoftCachingModuleScriptProvider buildModulePaths() {
		return new SoftCachingModuleScriptProvider(new ClasspathModuleProvider(modulePaths));
	}

	public synchronized String transform(final String input) {

		final Context ctx = Context.enter();
		ctx.setOptimizationLevel(-1);
		try {
			return transformInContext(input, ctx);
		} finally {
			Context.exit();
		}
	}

	protected abstract String transformInContext(String input, Context ctx);
}
