package net.bbmsoft.iocfx.impl;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

public class RootApplication extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		Platform.setImplicitExit(false);
		JavaFXFrameworkLauncher.frameworkLauncher.setRootApplicationInstance(this);
	}
	
	@Override
	public void stop() throws Exception {
		System.out.println("JavaFX Platform successfully shut down.");
	}
}
