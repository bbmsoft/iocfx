package net.bbmsoft.iocfx.platform.impl;

import javafx.application.ConditionalFeature;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyBooleanProperty;

public class PlatformWrapper implements net.bbmsoft.iocfx.platform.Platform {

	@Override
	public ReadOnlyBooleanProperty accessibilityActiveProperty() {
		return Platform.accessibilityActiveProperty();
	}

	@Override
	public boolean isAccessibilityActive() {
		return Platform.isAccessibilityActive();
	}

	@Override
	public void exit() {
		Platform.exit();
	}

	@Override
	public boolean isFxApplicationThread() {
		return Platform.isFxApplicationThread();
	}

	@Override
	public boolean isImplicitExit() {
		return Platform.isImplicitExit();
	}

	@Override
	public boolean isSupported(ConditionalFeature feature) {
		return Platform.isSupported(feature);
	}

	@Override
	public void runLater(Runnable runnable) {
		Platform.runLater(runnable);
	}

	@Override
	public void setImplicitExit(boolean implicitExit) {
		Platform.setImplicitExit(implicitExit);
	}
}
