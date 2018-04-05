package net.bbmsoft.iocfx.impl;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

@Component
public class JavaFXFrameworkLauncher {

	static JavaFXFrameworkLauncher frameworkLauncher;

	private RootApplication rootApplication;
	private Thread frameworkLauncherThread;

	@Activate
	public synchronized void launch() {

		if (JavaFXFrameworkLauncher.frameworkLauncher != null) {
			throw new IllegalStateException("Framework has already been started! Cannot start a second time.");
		}

		JavaFXFrameworkLauncher.frameworkLauncher = this;

		System.out.println("Initializing JavaFX Platform...");

		this.frameworkLauncherThread = new Thread(this::launchFramework);
		this.frameworkLauncherThread.setName("JavaFX Platform Launcher Thread");
		this.frameworkLauncherThread.start();
	}

	@Deactivate
	public synchronized void shutdown() {
		
		// TODO check config if platform should be shut down
		
		boolean shutdownPlatform = false;
		
		if(shutdownPlatform) {
			System.out.println("Shutting down JavaFX Platform...");
			Platform.exit();
		}
	}

	private void launchFramework() {
		try {
			Application.launch(RootApplication.class);
		} catch (IllegalStateException e) {
			// this may happen if this bundle has been activated, then deactivated and is
			// then reactivated or if some other bundle has started the JavaFX Platform
			Platform.runLater(this::fallbackPlatformStart);
		}
	}
	
	private void fallbackPlatformStart() {
		Stage primaryStage = new Stage();
		RootApplication rootApplication = new RootApplication();
		rootApplication.start(primaryStage);
	}

	synchronized void setRootApplicationInstance(RootApplication rootApplication) {

		if (!Platform.isFxApplicationThread()) {
			throw new IllegalStateException(
					"Not on JavaFX Application thread! Current thread is " + Thread.currentThread().getName());
		}

		if (this.rootApplication != null) {
			throw new IllegalStateException("Root Application already set!");
		}

		if (rootApplication == null) {
			throw new NullPointerException("Cannot set null root application!");
		}

		this.rootApplication = rootApplication;

		System.out.println("JavaFX Platform successfully initialized.");

	}

}
