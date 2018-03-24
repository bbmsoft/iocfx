package net.bbmsoft.iocfx.impl;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import net.bbmsoft.iocfx.platform.impl.PlatformWrapper;

public class RootApplication extends Application {

	@Override
	public synchronized void start(Stage primaryStage) throws Exception {
		Platform.setImplicitExit(false);
		JavaFXFrameworkLauncher.frameworkLauncher.setRootApplicationInstance(this);

		try {
			registerPlatformService();
		} catch (ClassNotFoundException | NoClassDefFoundError e) {
			// this will naturally happen in non-OSGi environments
			System.err.println("Not in an OSGi environment, cannot register Platform service.");
		}
	}

	private void registerPlatformService() throws ClassNotFoundException {

		Bundle bundle = FrameworkUtil.getBundle(this.getClass());
		// the bundle is null when we are not in an OSGi environment
		if (bundle != null) {
			bundle.getBundleContext().registerService(net.bbmsoft.iocfx.platform.Platform.class, new PlatformWrapper(),
					null);
		}
	}

	@Override
	public synchronized void stop() throws Exception {
		System.out.println("JavaFX Platform successfully shut down.");
	}
}
