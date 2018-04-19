package net.bbmsoft.iocfx;

import javafx.stage.Stage;

/**
 * 
 * This service provides instances of {@link javafx.stage.Stage}. Components
 * declaring a dependency on it like this will recieve their own {@code Stage}
 * instance:
 * 
 * <pre>
 * &#64;Reference(scope = ReferenceScope.PROTOTYPE_REQUIRED)
 * private StageService stageService;
 * </pre>
 * 
 * @author Michael Bachmann
 *
 */
public interface StageService {
	
	/**
	 * Enum to determine the behavior of an application when a Stage is closed.
	 * 
	 * @author Michael Bachmann
	 *
	 */
	public enum ExitPolicy {

		/**
		 * Tries to shut down the OSGi framework when a stage is closed. Use this for
		 * the main window of desktop applications.
		 */
		SHUTDOWN_ON_STAGE_EXIT,

		/**
		 * Stops the bundle that provides the UI contained in that stage. Mainly useful
		 * for debugging purposes.
		 */
		STOP_BUNDLE_ON_STAGE_EXIT,

		/**
		 * Does nothing when the Stage is closed. Use this for secondary and utility
		 * windows or when other applications may run in the same framework.
		 */
		DO_NOTHING_ON_STAGE_EXIT;
	}

	/**
	 * Gets the {@link Stage} instance provided by this service.
	 * 
	 * @return the {@code Stage} instance provided by this service
	 */
	public Stage getStage();

	/**
	 * Specifies what should happen when the {@code Stage} provided by this service
	 * is closed. There are three possibilities:
	 * <ol>
	 * <li>SHUTDOWN_ON_STAGE_EXIT (the default behavior):<br>
	 * tries to shutdown the OSGi framework when the stage is closed. Use this for
	 * the main window of desktop applications</li>
	 * <li>STOP_BUNDLE_ON_STAGE_EXIT:<br>
	 * stop one or several bundles when the stage is closed. For this option a class
	 * from every bundle that should be stopped needs to be provided. Mainly useful
	 * for testing and debugging</li>
	 * <li>DO_NOTHING_ON_STAGE_EXIT:<br>
	 * does nothing when the stage is closed. This should be used for secondary
	 * windows or when several UI applications are running in the same
	 * framework</li>
	 * </ol>
	 * 
	 * @param policy
	 *            the policy to be applied when the {@code Stage} provided by this
	 *            service is closed
	 * @param bundleClasses
	 *            classes from all bundles that should be stopped when the stage is
	 *            closed. Only required for STOP_BUNDLE_ON_STAGE_EXIT
	 * @defaultValue ExitPolicy.SHUTDOWN_ON_STAGE_EXIT
	 */
	public void setExitPolicy(ExitPolicy policy, Class<?>... bundleClasses);
}
