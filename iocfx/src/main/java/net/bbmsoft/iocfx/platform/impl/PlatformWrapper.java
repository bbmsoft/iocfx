package net.bbmsoft.iocfx.platform.impl;

import java.util.concurrent.CountDownLatch;

import javafx.application.ConditionalFeature;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyBooleanProperty;

public class PlatformWrapper implements net.bbmsoft.iocfx.Platform {

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

	@Override
	public void runAndWait(Runnable runnable) throws InterruptedException {

		if (this.isFxApplicationThread()) {
			runnable.run();
			return;
		}

		CountDownLatch latch = new CountDownLatch(1);

		this.runLater(() -> {
			try {
				runnable.run();
			} finally {
				latch.countDown();
			}
		});

		latch.await();
	}

	@Override
	public void runOnFxApplicationThread(Runnable runnable) {

		if (this.isFxApplicationThread()) {
			runnable.run();
		} else {
			this.runLater(runnable);
		}
	}

	@Override
	public void assertFxApplicationThread() {

		// TODO check config

		if (!this.isFxApplicationThread()) {
			throw new IllegalStateException(
					"Not on FX Application Thread. Current thread is " + Thread.currentThread().getName());
		}
	}

}
