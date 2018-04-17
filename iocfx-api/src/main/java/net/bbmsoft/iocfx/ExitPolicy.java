package net.bbmsoft.iocfx;

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
