package net.bbmsoft.iocfx.impl;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.FrameworkUtil;

import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import net.bbmsoft.iocfx.StageConsumer;

public class ShutdownPolicyHandler {

	/**
	 * Adds an event handler to the stage that will stop the bundle the stage was
	 * created for when the stage is closed.
	 * 
	 * @param stageUser
	 *            the {@link StageConsumer} the stage was created for
	 * @param stage
	 *            the stage
	 * @throws ClassNotFoundException
	 *             if OSGi is not on the classpath
	 */
	public static void stopBundleOnStageExit(StageConsumer stageUser, Stage stage) throws ClassNotFoundException {

		Bundle bundle = FrameworkUtil.getBundle(stageUser.getClass());

		if (bundle == null) {
			// uh oh, looks like we are not in an OSGi environment
			// shutdown policies are not supported here and need to be implemented using a
			// WindowEvent handler on the provided stage
			return;
		}

		stage.addEventHandler(WindowEvent.WINDOW_HIDING, e -> {
			try {
				bundle.stop();
			} catch (BundleException e1) {
				System.err.println("Could not stop the stage user's bundle.");
				e1.printStackTrace();
			}
		});
	}

	/**
	 * Adds an event handler to the stage that will shut down the OSGi framework
	 * when the stage is closed.
	 * 
	 * @param stageUser
	 *            the {@link StageConsumer} the stage was created for
	 * @param stage
	 *            the stage
	 * @throws ClassNotFoundException
	 *             if OSGi is not on the classpath
	 */

	public static void shutdownOnStageExit(StageConsumer stageUser, Stage stage) throws ClassNotFoundException {

		Bundle bundle = FrameworkUtil.getBundle(stageUser.getClass());

		if (bundle == null) {
			// uh oh, looks like we are not in an OSGi environment
			// shutdown policies are not supported here and need to be implemented using a
			// WindowEvent handler on the provided stage
			return;
		}

		BundleContext bundleContext = bundle.getBundleContext();
		Bundle systemBundle = bundleContext.getBundle(0);

		stage.addEventHandler(WindowEvent.WINDOW_HIDING, e -> {
			try {
				systemBundle.stop();
			} catch (BundleException e1) {
				System.err.println("Could not stop the system bundle.");
				e1.printStackTrace();
			}
		});
	}
}
