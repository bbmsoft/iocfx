package net.bbmsoft.iocfx.impl;

import java.util.HashMap;
import java.util.Map;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.FrameworkUtil;

import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class ShutdownPolicyHandler {

	private static final Map<Stage, EventHandler<WindowEvent>> eventHandlers = new HashMap<>();

	/**
	 * Adds an event handler to the stage that will stop the bundle the stage was
	 * created for when the stage is closed and removes any existing event handlers
	 * enforcing a different policy.
	 * 
	 * @param bundleClass
	 *            a class from the bundle that needs to be stopped when the stage is
	 *            closed
	 * @param stage
	 *            the stage
	 */
	public static synchronized void stopBundleOnStageExit(Class<?> bundleClass, Stage stage) {

		Bundle bundle = FrameworkUtil.getBundle(bundleClass);

		if (bundle == null) {
			// uh oh, looks like we are not in an OSGi environment
			// shutdown policies are not supported here and need to be implemented using a
			// WindowEvent handler on the provided stage
			return;
		}

		removeExistingHandler(stage);

		EventHandler<WindowEvent> eventHandler = e -> {
			try {
				e.consume();
				stage.hide();
				bundle.stop();
			} catch (BundleException e1) {
				System.err.println("Could not stop the stage user's bundle.");
				e1.printStackTrace();
			}
		};

		eventHandlers.put(stage, eventHandler);

		stage.addEventHandler(WindowEvent.WINDOW_CLOSE_REQUEST, eventHandler);
	}

	private static void removeExistingHandler(Stage stage) {
		EventHandler<WindowEvent> oldHandler = eventHandlers.remove(stage);
		if (oldHandler != null) {
			stage.removeEventHandler(WindowEvent.WINDOW_CLOSE_REQUEST, oldHandler);
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
	public static void shutdownOnStageExit(Stage stage) {

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
				System.err.println("Could not stop the system bundle.");
				e1.printStackTrace();
			}
		};

		eventHandlers.put(stage, eventHandler);

		stage.addEventHandler(WindowEvent.WINDOW_CLOSE_REQUEST, eventHandler);
	}

	/**
	 * Removes any existing event handlers enforcing a different policy.
	 * 
	 * @param stage
	 *            the stage
	 */
	public static void doNothingOnStageExit(Stage stage) {
		removeExistingHandler(stage);
	}
}
