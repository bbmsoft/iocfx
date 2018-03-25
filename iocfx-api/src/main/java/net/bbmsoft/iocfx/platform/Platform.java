package net.bbmsoft.iocfx.platform;

import javafx.application.Application;
import javafx.application.ConditionalFeature;
import javafx.beans.property.ReadOnlyBooleanProperty;

/**
 * Wraps the functionality of {@link javafx.application.Platform} into an OSGi
 * service. IoCFX will make sure this service does not become available before
 * the JavaFX platform has been launched, so any DS declaring a dependency on
 * {@link net.bbmsoft.iocfx.platform.Platform} will only be activated when it is
 * safe to use the JavaFX Platform.
 * 
 * @author Michael Bachmann
 *
 */
public interface Platform {

	/**
	 * Indicates whether or not accessibility is active. This property is typically
	 * set to true the first time an assistive technology, such as a screen reader,
	 * requests information about any JavaFX window or its children.
	 *
	 * <p>
	 * This method may be called from any thread.
	 * </p>
	 *
	 * @return the read-only boolean property indicating if accessibility is active
	 *
	 */
	public ReadOnlyBooleanProperty accessibilityActiveProperty();

	/**
	 * Causes the JavaFX application to terminate. If this method is called after
	 * the Application start method is called, then the JavaFX launcher will call
	 * the Application stop method and terminate the JavaFX application thread. The
	 * launcher thread will then shutdown. If there are no other non-daemon threads
	 * that are running, the Java VM will exit. If this method is called from the
	 * Preloader or the Application init method, then the Application stop method
	 * may not be called.
	 *
	 * <p>
	 * This method may be called from any thread.
	 * </p>
	 *
	 * <p>
	 * Note: if the application is embedded in a browser, then this method may have
	 * no effect.
	 */
	public void exit();

	public boolean isAccessibilityActive();

	/**
	 * Returns true if the calling thread is the JavaFX Application Thread. Use this
	 * call the ensure that a given task is being executed (or not being executed)
	 * on the JavaFX Application Thread.
	 *
	 * @return true if running on the JavaFX Application Thread
	 */
	public boolean isFxApplicationThread();

	/**
	 * Gets the value of the implicitExit attribute.
	 *
	 * <p>
	 * This method may be called from any thread.
	 * </p>
	 *
	 * @return the implicitExit attribute
	 */
	public boolean isImplicitExit();

	/**
	 * Queries whether a specific conditional feature is supported by the platform.
	 * <p>
	 * For example:
	 * 
	 * <pre>
	 * // Query whether filter effects are supported
	 * if (Platform.isSupported(ConditionalFeature.EFFECT)) {
	 * 	// use effects
	 * }
	 * </pre>
	 *
	 * @param feature
	 *            the conditional feature in question.
	 */
	public boolean isSupported(ConditionalFeature feature);

	/**
	 * Run the specified Runnable on the JavaFX Application Thread at some
	 * unspecified time in the future. This method, which may be called from any
	 * thread, will post the Runnable to an event queue and then return immediately
	 * to the caller. The Runnables are executed in the order they are posted. A
	 * runnable passed into the runLater method will be executed before any Runnable
	 * passed into a subsequent call to runLater. If this method is called after the
	 * JavaFX runtime has been shutdown, the call will be ignored: the Runnable will
	 * not be executed and no exception will be thrown.
	 *
	 * <p>
	 * NOTE: applications should avoid flooding JavaFX with too many pending
	 * Runnables. Otherwise, the application may become unresponsive. Applications
	 * are encouraged to batch up multiple operations into fewer runLater calls.
	 * Additionally, long-running operations should be done on a background thread
	 * where possible, freeing up the JavaFX Application Thread for GUI operations.
	 * </p>
	 *
	 * <p>
	 * This method must not be called before the FX runtime has been initialized.
	 * For standard JavaFX applications that extend {@link Application}, and use
	 * either the Java launcher or one of the launch methods in the Application
	 * class to launch the application, the FX runtime is initialized by the
	 * launcher before the Application class is loaded. For Swing applications that
	 * use JFXPanel to display FX content, the FX runtime is initialized when the
	 * first JFXPanel instance is constructed. For SWT application that use FXCanvas
	 * to display FX content, the FX runtime is initialized when the first FXCanvas
	 * instance is constructed.
	 * </p>
	 *
	 * @param runnable
	 *            the Runnable whose run method will be executed on the JavaFX
	 *            Application Thread
	 *
	 * @throws IllegalStateException
	 *             if the FX runtime has not been initialized
	 */
	public void runLater(Runnable runnable);

	/**
	 * Sets the implicitExit attribute to the specified value. If this attribute is
	 * true, the JavaFX runtime will implicitly shutdown when the last window is
	 * closed; the JavaFX launcher will call the {@link Application#stop} method and
	 * terminate the JavaFX application thread. If this attribute is false, the
	 * application will continue to run normally even after the last window is
	 * closed, until the application calls {@link #exit}. The default value is true.
	 *
	 * <p>
	 * This method may be called from any thread.
	 * </p>
	 *
	 * @param implicitExit
	 *            a flag indicating whether or not to implicitly exit when the last
	 *            window is closed.
	 */
	public void setImplicitExit(boolean implicitExit);

}
