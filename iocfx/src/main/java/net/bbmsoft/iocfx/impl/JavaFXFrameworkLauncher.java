package net.bbmsoft.iocfx.impl;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import net.bbmsoft.iocfx.log.impl.MinLogger;
import net.bbmsoft.iocfx.platform.impl.PlatformWrapper;

/**
 * This component will attempt to initialize the JavaFX Platform and register a
 * {@link net.bbmsoft.iocfx.Platform} as soon as the JavaFX Platform is
 * guaranteed to be available.
 * 
 * @author Michael Bachmann
 *
 */
@Component
public class JavaFXFrameworkLauncher {

	@Reference
	private MinLogger log;

	@Activate
	public synchronized void activate() {
		log.info("Initializing JavaFX Platform...");
		new Thread(this::launchFramework, "JavaFX Platform Launcher Thread").start();
	}

	private void launchFramework() {
		try {
			Application.launch(RootApplication.class);
		} catch (IllegalStateException e) {
			// this will happen if this bundle has been activated after something else has
			// already started the JavaFX Platform so we will just register the Platform
			// service from the JavaFX Application Thread
			log.error("JavaFX Platform already running.");
			RootApplication.registerPlatformService();
		}
	}

	public static class RootApplication extends Application {

		@Override
		public synchronized void start(Stage primaryStage) {
			registerPlatformService();
		}

		private static void registerPlatformService() {

			Platform.setImplicitExit(false);

			Bundle bundle = FrameworkUtil.getBundle(JavaFXFrameworkLauncher.class);
			BundleContext ctx = bundle.getBundleContext();

			ctx.registerService(net.bbmsoft.iocfx.Platform.class, new PlatformWrapper(), null);
		}
	}

}
