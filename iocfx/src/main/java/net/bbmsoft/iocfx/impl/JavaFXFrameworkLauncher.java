package net.bbmsoft.iocfx.impl;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

import javafx.application.Application;
import javafx.application.Platform;

@Component
public class JavaFXFrameworkLauncher {
	
	static JavaFXFrameworkLauncher frameworkLauncher;
	
	private RootApplication rootApplication;
	private Thread frameworkLauncherThread;
	private StageConsumerRegistry stageUserRegistry;

	@Activate
	public synchronized void launch() {
		
		if(JavaFXFrameworkLauncher.frameworkLauncher != null) {
			throw new IllegalStateException("Framework has already been started! Cannot start a second time.");
		}
		
		JavaFXFrameworkLauncher.frameworkLauncher = this;

		System.out.println("Initializing JavaFX Platform...");
		
		this.frameworkLauncherThread = new Thread(this::launchFramework);
		this.frameworkLauncherThread.start();
	}
	
	@Deactivate
	public synchronized void shutdown() {
		System.out.println("Shutting down JavaFX Platform...");
		Platform.exit();
	}
	
	@Reference
	public synchronized void setStageUserRegistry(StageConsumerRegistry stageUserRegistry) {
		this.stageUserRegistry = stageUserRegistry;
		notifyStageUserRegistry();
	}
	
	public synchronized void unsetStageUserRegistry(StageConsumerRegistry stageUserRegistry) {
		if(this.stageUserRegistry == stageUserRegistry) {
			this.stageUserRegistry = null;
		}
	}
	
	private void launchFramework() {
		Application.launch(RootApplication.class);
	}
	
	synchronized void setRootApplicationInstance(RootApplication rootApplication) {
		
		if(!Platform.isFxApplicationThread()) {
			throw new IllegalStateException("Not on JavaFX Application thread! Current thread is " + Thread.currentThread().getName());
		}
		
		if(this.rootApplication != null) {
			throw new IllegalStateException("Root Application already set!");
		}
		
		if(rootApplication == null) {
			throw new NullPointerException("Cannot set null root application!");
		}
		
		this.rootApplication = rootApplication;
		
		System.out.println("JavaFX Platform successfully initialized.");
		
		notifyStageUserRegistry();
		
	}

	private void notifyStageUserRegistry() {
		if(this.stageUserRegistry != null && this.rootApplication != null) {
			this.stageUserRegistry.platformInitialized();
		}
	}
	
}
