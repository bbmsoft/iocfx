package net.bbmsoft.iocfx.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.FrameworkUtil;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import net.bbmsoft.iocfx.log.impl.MinLogger;

/**
 * Adds the listeners to the stage that are required to enforce the specified
 * exit ploicy.
 * 
 * @author Michael Bachmann
 *
 */
@Component(service = ShutdownPolicyHandler.class)
public class ShutdownPolicyHandler {

	private static final EventType<WindowEvent> EXIT_POLICY_TRIGGER_EVENT = WindowEvent.WINDOW_HIDING;

	@Reference
	private MinLogger log;

	private final Map<Stage, EventHandler<WindowEvent>> eventHandlers = new HashMap<>();

	/**
	 * Adds an event handler to the stage that will stop the bundle the stage was
	 * created for when the stage is closed and removes any existing event handlers
	 * enforcing a different policy.
	 * 
	 * @param bundleClasses
	 *            classes from the bundles that need to be stopped when the stage is
	 *            closed
	 * @param stage
	 *            the stage
	 */
	public synchronized void stopBundleOnStageExit(Stage stage, Class<?>[] bundleClasses) {

		Objects.requireNonNull(bundleClasses,
				"Cannot init exit policy STOP_BUNDLE_ON_STAGE_EXIT without a class from the bundle to be stopped!");

		Bundle[] bundles = new Bundle[bundleClasses.length];
		for (int i = 0; i < bundles.length; i++) {
			bundles[i] = FrameworkUtil.getBundle(bundleClasses[i]);
		}

		removeExistingHandler(stage);

		EventHandler<WindowEvent> eventHandler = e -> {

			e.consume();
			stage.hide();

			for (Bundle b : bundles) {
				if (b != null) {
					try {
						b.stop();
					} catch (BundleException e1) {
						log.error("Could not stop the stage user's bundle.");
						e1.printStackTrace();
					}
				}
			}
		};

		this.eventHandlers.put(stage, eventHandler);

		stage.addEventHandler(EXIT_POLICY_TRIGGER_EVENT, eventHandler);
	}

	private void removeExistingHandler(Stage stage) {
		EventHandler<WindowEvent> oldHandler = eventHandlers.remove(stage);
		if (oldHandler != null) {
			stage.removeEventHandler(EXIT_POLICY_TRIGGER_EVENT, oldHandler);
		}
	}

	/**
	 * Adds an event handler to the stage that will shut down the OSGi framework
	 * when the stage is closed and removes any existing event handlers enforcing a
	 * different policy.
	 * 
	 * @param stage
	 *            the stage
	 */
	public synchronized void shutdownOnStageExit(Stage stage) {

		Bundle bundle = FrameworkUtil.getBundle(ShutdownPolicyHandler.class);

		if (bundle == null) {
			// uh oh, looks like we are not in an OSGi environment
			// shutdown policies are not supported here and need to be implemented using a
			// WindowEvent handler on the provided stage
			return;
		}

		BundleContext bundleContext = bundle.getBundleContext();
		Bundle systemBundle = bundleContext.getBundle(0);

		removeExistingHandler(stage);

		EventHandler<WindowEvent> eventHandler = e -> {
			try {
				e.consume();
				stage.hide();
				systemBundle.stop();
			} catch (BundleException e1) {
				log.error("Could not stop the system bundle.");
				e1.printStackTrace();
			}
		};

		eventHandlers.put(stage, eventHandler);

		stage.addEventHandler(EXIT_POLICY_TRIGGER_EVENT, eventHandler);
	}

	/**
	 * Removes any existing event handlers enforcing a different policy.
	 * 
	 * @param stage
	 *            the stage
	 */
	public synchronized void doNothingOnStageExit(Stage stage) {
		removeExistingHandler(stage);
	}

	@Deactivate
	public synchronized void deactivate() {
		this.eventHandlers.clear();
	}
}
