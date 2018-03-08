package net.bbmsoft.iocfx;

import java.util.function.Consumer;

import org.osgi.annotation.versioning.ConsumerType;

import javafx.stage.Stage;

@ConsumerType
public interface StageConsumer extends Consumer<Stage> {

	/**
	 * A stage must be provided to this method as soon as the JavaFX framework is
	 * booted. Every instance of this interface must be provided with its own stage.
	 * Stages cannot be shared among several instances.
	 * <p>
	 * This method must always be called on the JavaFX Application thread.
	 */
	@Override
	public void accept(Stage stage);
}
