package net.bbmsoft.iocfx.fxml.impl;

import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.ServiceScope;

import javafx.fxml.FXMLLoader;

/**
 * An {@link FXMLLoader} that gets published as an OSGi service. It uses a
 * customized class loader that first attempts to load classes used in fxml from
 * the system bundle (which should succeed for any classes that are part of
 * JavaFX itself) and if that does not succeed, uses the call stack to find
 * other bundles that might have access to the required classes and delegates
 * class loading to those.
 * 
 * @author Michael Bachmann
 */
@Component(scope = ServiceScope.PROTOTYPE, service = FXMLLoader.class)
public class OsgiFxmlLoader extends FXMLLoader {

	private static class Helper extends SecurityManager {
		@Override
		protected Class<?>[] getClassContext() {
			return super.getClassContext();
		}
	}

	private static final Helper HELPER = new Helper();

	private class OsgiClassLoader extends ClassLoader {

		private BundleContext ctx;

		public OsgiClassLoader(BundleContext ctx) {
			this.ctx = ctx;
		}

		@Override
		public synchronized Class<?> loadClass(String name) throws ClassNotFoundException {

			if (this.ctx == null) {
				throw new ClassNotFoundException("Classloader already closed!");
			}

			Class<?> clazz = null;
			Bundle bundle = null;

			bundle = ctx.getBundle(0);
			if (bundle != null) {
				clazz = loadClassFromBundle(name, bundle);
				if (clazz != null) {
					return clazz;
				}
			}

			Class<?>[] classContext = OsgiFxmlLoader.HELPER.getClassContext();

			/*
			 * unsuccessful class loading is expensive and the classContext may contain
			 * classes multiple times as well as lots of classes from the same bundle, so we
			 * remember which classes and bundles we already tried and don't use those again
			 */
			Set<Class<?>> classes = new HashSet<>();
			Set<Bundle> bundles = new HashSet<>();

			for (Class<?> c : classContext) {
				if (classes.add(c)) {
					bundle = FrameworkUtil.getBundle(c);
					if (bundle != null && bundles.add(bundle)) {
						clazz = loadClassFromBundle(name, bundle);
						if (clazz != null) {
							return clazz;
						}
					}
				}
			}

			throw new ClassNotFoundException();
		}

		private Class<?> loadClassFromBundle(String className, Bundle bundle) {

			try {
				return bundle.loadClass(className);
			} catch (ClassNotFoundException e) {
				return null;
			}
		}

		public synchronized void close() {
			this.ctx = null;
		}

	}

	private OsgiClassLoader classLoader;

	@Activate
	public void activate(BundleContext ctx) {
		this.setClassLoader(this.classLoader = new OsgiClassLoader(ctx));
	}

	@Deactivate
	public void deactivate() {
		this.classLoader.close();
	}
}