package net.bbmsoft.iocfx;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCombination;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.StageStyle;

/**
 * Provides all public methods of {@link javafx.stage.Stage} plus some IoCFX
 * specific functionality like setting an {@link ExitPolicy} for that stage.
 * IoCFX publishes Stages prototype services both as {@link javafx.stage.Stage}
 * and {@link net.bbmsoft.iocfx.Stage}. All instances of this interface provided
 * as services by IoCFX can safely be cast to {@link javafx.stage.Stage} or
 * {@link javafx.stage.Window}.
 * 
 * @author Michael Bachmann
 *
 */
public interface Stage extends Window {

	/**
	 * Shows this stage and waits for it to be hidden (closed) before returning to
	 * the caller. This method temporarily blocks processing of the current event,
	 * and starts a nested event loop to handle other events. This method must be
	 * called on the FX Application thread.
	 * <p>
	 * A Stage is hidden (closed) by one of the following means:
	 * <ul>
	 * <li>the application calls the {@link #hide} or {@link #close} method on this
	 * stage</li>
	 * <li>this stage has a non-null owner window, and its owner is closed</li>
	 * <li>the user closes the window via the window system (for example, by
	 * pressing the close button in the window decoration)</li>
	 * </ul>
	 * 
	 *
	 * <p>
	 * After the Stage is hidden, and the application has returned from the event
	 * handler to the event loop, the nested event loop terminates and this method
	 * returns to the caller.
	 * 
	 * <p>
	 * For example, consider the following sequence of operations for different
	 * event handlers, assumed to execute in the order shown below:
	 * 
	 * <pre>
	 * void evtHander1(...) {
	 *     stage1.showAndWait();
	 *     doSomethingAfterStage1Closed(...)
	 * }
	 *
	 * void evtHander2(...) {
	 *     stage1.hide();
	 *     doSomethingElseHere(...)
	 * }
	 * </pre>
	 * 
	 * evtHandler1 will block at the call to showAndWait. It will resume execution
	 * after stage1 is hidden and the current event handler, in this case
	 * evtHandler2, returns to the event loop. This means that doSomethingElseHere
	 * will execute before doSomethingAfterStage1Closed.
	 * 
	 *
	 * <p>
	 * More than one stage may be shown with showAndWait. Each call will start a new
	 * nested event loop. The stages may be hidden in any order, but a particular
	 * nested event loop (and thus the showAndWait method for the associated stage)
	 * will only terminate after all inner event loops have also terminated.
	 * 
	 * <p>
	 * For example, consider the following sequence of operations for different
	 * event handlers, assumed to execute in the order shown below:
	 * 
	 * <pre>
	 * void evtHander1() {
	 *     stage1.showAndWait();
	 *     doSomethingAfterStage1Closed(...)
	 * }
	 *
	 * void evtHander2() {
	 *     stage2.showAndWait();
	 *     doSomethingAfterStage2Closed(...)
	 * }
	 *
	 * void evtHander3() {
	 *     stage1.hide();
	 *     doSomethingElseHere(...)
	 * }
	 *
	 * void evtHander4() {
	 *     stage2.hide();
	 *     doSomethingElseHereToo(...)
	 * }
	 * </pre>
	 * evtHandler1 will block at the call to stage1.showAndWait, starting up a
	 * nested event loop just like in the previous example. evtHandler2 will then
	 * block at the call to stage2.showAndWait, starting up another (inner) nested
	 * event loop. The first call to stage1.showAndWait will resume execution after
	 * stage1 is hidden, but only after the inner nested event loop started by
	 * stage2.showAndWait has terminated. This means that the call to
	 * stage1.showAndWait won't return until after evtHandler2 has returned. The
	 * order of execution is: stage1.showAndWait, stage2.showAndWait, stage1.hide,
	 * doSomethingElseHere, stage2.hide, doSomethingElseHereToo,
	 * doSomethingAfterStage2Closed, doSomethingAfterStage1Closed.
	 * 
	 *
	 * <p>
	 * This method must not be called on the primary stage or on a stage that is
	 * already visible. Additionally, it must either be called from an input event
	 * handler or from the run method of a Runnable passed to
	 * {@link javafx.application.Platform#runLater Platform.runLater}. It must not
	 * be called during animation or layout processing.
	 * 
	 *
	 * @throws IllegalStateException
	 *             if this method is called on a thread other than the JavaFX
	 *             Application Thread.
	 * @throws IllegalStateException
	 *             if this method is called during animation or layout processing.
	 * @throws IllegalStateException
	 *             if this method is called on the primary stage.
	 * @throws IllegalStateException
	 *             if this stage is already showing.
	 * @since JavaFX 2.2
	 */
	public void showAndWait();

	/**
	 * Attempts to show this Window by setting visibility to true
	 *
	 * @throws IllegalStateException
	 *             if this method is called on a thread other than the JavaFX
	 *             Application Thread.
	 */
	public void show();

	/**
	 * Specifies the style for this stage. This must be done prior to making the
	 * stage visible. The style is one of: StageStyle.DECORATED,
	 * StageStyle.UNDECORATED, StageStyle.TRANSPARENT, or StageStyle.UTILITY.
	 *
	 * @param style
	 *            the style for this stage.
	 *
	 * @throws IllegalStateException
	 *             if this property is set after the stage has ever been made
	 *             visible.
	 *
	 * @defaultValue StageStyle.DECORATED
	 */
	public void initStyle(StageStyle style);

	/**
	 * Retrieves the style attribute for this stage.
	 *
	 * @return the stage style.
	 */
	public StageStyle getStyle();

	/**
	 * Specifies the modality for this stage. This must be done prior to making the
	 * stage visible. The modality is one of: Modality.NONE, Modality.WINDOW_MODAL,
	 * or Modality.APPLICATION_MODAL.
	 *
	 * @param modality
	 *            the modality for this stage.
	 *
	 * @throws IllegalStateException
	 *             if this property is set after the stage has ever been made
	 *             visible.
	 *
	 * @throws IllegalStateException
	 *             if this stage is the primary stage.
	 *
	 * @defaultValue Modality.NONE
	 */
	public void initModality(Modality modality);

	/**
	 * Retrieves the modality attribute for this stage.
	 *
	 * @return the modality.
	 */
	public Modality getModality();

	/**
	 * Specifies the owner Window for this stage, or null for a top-level, unowned
	 * stage. This must be done prior to making the stage visible.
	 *
	 * @param owner
	 *            the owner for this stage.
	 *
	 * @throws IllegalStateException
	 *             if this property is set after the stage has ever been made
	 *             visible.
	 *
	 * @throws IllegalStateException
	 *             if this stage is the primary stage.
	 *
	 * @defaultValue null
	 */
	public void initOwner(javafx.stage.Window owner);

	/**
	 * Retrieves the owner Window for this stage, or null for an unowned stage.
	 *
	 * @return the owner Window.
	 */
	public javafx.stage.Window getOwner();

	public void setFullScreen(boolean value);

	public boolean isFullScreen();

	/**
	 * Specifies whether this {@code Stage} should be a full-screen, undecorated
	 * window.
	 * <p>
	 * The implementation of full-screen mode is platform and profile-dependent.
	 * </p>
	 * <p>
	 * When set to {@code true}, the {@code Stage} will attempt to enter full-screen
	 * mode when visible. Set to {@code false} to return {@code Stage} to windowed
	 * mode. An {@link IllegalStateException} is thrown if this property is set on a
	 * thread other than the JavaFX Application Thread.
	 * </p>
	 * <p>
	 * The full-screen mode will be exited (and the {@code fullScreen} attribute
	 * will be set to {@code false}) if the full-screen {@code Stage} loses focus or
	 * if another {@code Stage} enters full-screen mode on the same {@link Screen}.
	 * Note that a {@code Stage} in full-screen mode can become invisible without
	 * losing its full-screen status and will again enter full-screen mode when the
	 * {@code Stage} becomes visible.
	 * </p>
	 * If the platform supports multiple screens an application can control which
	 * {@code Screen} the Stage will enter full-screen mode on by setting its
	 * position to be within the bounds of that {@code Screen} prior to entering
	 * full-screen mode.
	 * <p>
	 * However once in full-screen mode, {@code Stage}'s {@code x}, {@code y},
	 * {@code width}, and {@code height} variables will continue to represent the
	 * non-full-screen position and size of the window; the same for
	 * {@code iconified}, {@code resizable}, {@code style}, and {@code
	 * opacity}. If changes are made to any of these attributes while in full-screen
	 * mode, upon exiting full-screen mode the {@code Stage} will assume those
	 * attributes.
	 * </p>
	 * <p>
	 * In case that more {@code Stage} modes are set simultaneously their order of
	 * importance is {@code iconified}, fullScreen, {@code maximized} (from
	 * strongest to weakest).
	 * </p>
	 * <p>
	 * The property is read only because it can be changed externally by the
	 * underlying platform and therefore must not be bindable.
	 * </p>
	 *
	 * The user can unconditionally exit full-screen mode at any time by pressing
	 * {@code ESC}.
	 * <p>
	 * There are differences in behavior between applications if a security manager
	 * is present. Applications with permissions are allowed to enter full-screen
	 * mode unrestricted. Applications without the proper permissions will have the
	 * following restrictions:
	 * </p>
	 * <ul>
	 * <li>Applications can only enter full-screen mode in response to user input.
	 * More specifically, entering is allowed from mouse
	 * ({@code Node.mousePressed/mouseReleased/mouseClicked}) or keyboard
	 * ({@code Node.keyPressed/keyReleased/keyTyped}) event handlers. It is not
	 * allowed to enter full-screen mode in response to {@code ESC} key. Attempting
	 * to enter full-screen mode from any other context will be ignored.
	 * <p>
	 * If {@code Stage} was constructed as full-screen but not visible it will enter
	 * full-screen mode upon becoming visible, with the same limitations to when
	 * this is allowed to happen as when setting {@code fullScreen} to {@code true}.
	 * </p>
	 * </li>
	 * <li>If the application was allowed to enter full-screen mode it will have
	 * limited keyboard input. It will only receive KEY_PRESSED and KEY_RELEASED
	 * events from the following keys:
	 * {@code UP, DOWN, LEFT, RIGHT, SPACE, TAB, PAGE_UP, PAGE_DOWN, HOME, END, ENTER}
	 * </li>
	 * </ul>
	 * 
	 * @defaultValue false
	 */
	public ReadOnlyBooleanProperty fullScreenProperty();

	/**
	 * Gets the icon images to be used in the window decorations and when minimized.
	 * The images should be different sizes of the same image and the best size will
	 * be chosen, eg. 16x16, 32,32.
	 * 
	 * @return An observable list of icons of this window
	 */
	public ObservableList<Image> getIcons();

	public void setTitle(String value);

	public String getTitle();

	/**
	 * Defines the title of the {@code Stage}.
	 *
	 * @defaultValue empty string
	 */
	public StringProperty titleProperty();

	public void setIconified(boolean value);

	public boolean isIconified();

	/**
	 * Defines whether the {@code Stage} is iconified or not.
	 * <p>
	 * In case that more {@code Stage} modes are set simultaneously their order of
	 * importance is iconified} {@code fullScreen}, {@code maximized} (from
	 * strongest to weakest).
	 * </p>
	 * <p>
	 * On some mobile and embedded platforms setting this property to true will hide
	 * the {@code Stage} but not show an icon for it.
	 * </p>
	 * <p>
	 * The property is read only because it can be changed externally by the
	 * underlying platform and therefore must not be bindable.
	 * </p>
	 *
	 * @defaultValue false
	 */
	public ReadOnlyBooleanProperty iconifiedProperty();

	public void setMaximized(boolean value);

	public boolean isMaximized();

	/**
	 * Defines whether the {@code Stage} is maximized or not.
	 * <p>
	 * In case that more {@code Stage} modes are set simultaneously their order of
	 * importance is {@code iconified}, {@code fullScreen}, maximized (from
	 * strongest to weakest).
	 * </p>
	 * <p>
	 * The property is read only because it can be changed externally by the
	 * underlying platform and therefore must not be bindable.
	 * </p>
	 *
	 * @defaultValue false
	 * @since JavaFX 8.0
	 */
	public ReadOnlyBooleanProperty maximizedProperty();

	public void setAlwaysOnTop(boolean value);

	public boolean isAlwaysOnTop();

	/**
	 * Defines whether this {@code Stage} is kept on top of other windows.
	 * <p>
	 * If some other window is already always-on-top then the relative order between
	 * these windows is unspecified (depends on platform).
	 * </p>
	 * <p>
	 * There are differences in behavior between applications if a security manager
	 * is present. Applications with permissions are allowed to set "always on top"
	 * flag on a Stage. In applications without the proper permissions, an attempt
	 * to set the flag will be ignored and the property value will be restored to
	 * "false".
	 * </p>
	 * <p>
	 * The property is read only because it can be changed externally by the
	 * underlying platform and therefore must not be bindable.
	 * </p>
	 *
	 * @defaultValue false
	 * @since JavaFX 8u20
	 */
	public ReadOnlyBooleanProperty alwaysOnTopProperty();

	public void setResizable(boolean value);

	public boolean isResizable();

	/**
	 * Defines whether the {@code Stage} is resizable or not by the user.
	 * Programatically you may still change the size of the Stage. This is a hint
	 * which allows the implementation to optionally make the Stage resizable by the
	 * user.
	 * <p>
	 * <b>Warning:</b> Since 8.0 the property cannot be bound and will throw
	 * {@code RuntimeException} on an attempt to do so. This is because the setting
	 * of resizable is asynchronous on some systems or generally might be set by the
	 * system / window manager. <br>
	 * Bidirectional binds are still allowed, as they don't block setting of the
	 * property by the system.
	 *
	 * @defaultValue true
	 */
	public BooleanProperty resizableProperty();

	public void setMinWidth(double value);

	public double getMinWidth();

	/**
	 * Defines the minimum width of this {@code Stage}.
	 *
	 * @defaultValue 0
	 * @since JavaFX 2.1
	 */
	public DoubleProperty minWidthProperty();

	public void setMinHeight(double value);

	public double getMinHeight();

	/**
	 * Defines the minimum height of this {@code Stage}.
	 *
	 * @defaultValue 0
	 * @since JavaFX 2.1
	 */
	public DoubleProperty minHeightProperty();

	public void setMaxWidth(double value);

	/**
	 * Defines the maximum width of this {@code Stage}.
	 *
	 * @defaultValue Double.MAX_VALUE
	 * @since JavaFX 2.1
	 */
	public double getMaxWidth();

	public DoubleProperty maxWidthProperty();

	public void setMaxHeight(double value);

	/**
	 * Defines the maximum height of this {@code Stage}.
	 *
	 * @defaultValue Double.MAX_VALUE
	 * @since JavaFX 2.1
	 */
	public double getMaxHeight();

	public DoubleProperty maxHeightProperty();

	/**
	 * Bring the {@code Window} to the foreground. If the {@code Window} is already
	 * in the foreground there is no visible difference.
	 */
	public void toFront();

	/**
	 * Send the {@code Window} to the background. If the {@code Window} is already
	 * in the background there is no visible difference. This action places this
	 * {@code Window} at the bottom of the stacking order on platforms that support
	 * stacking.
	 */
	public void toBack();

	/**
	 * Closes this {@code Stage}. This call is equivalent to {@code hide()}.
	 */
	public void close();

	/**
	 * Specifies the KeyCombination that will allow the user to exit full screen
	 * mode. A value of KeyCombination.NO_MATCH will not match any KeyEvent and will
	 * make it so the user is not able to escape from Full Screen mode. A value of
	 * null indicates that the default platform specific key combination should be
	 * used.
	 * <p>
	 * An internal copy of this value is made when entering FullScreen mode and will
	 * be used to trigger the exit from the mode. If an application does not have
	 * the proper permissions, this setting will be ignored.
	 * </p>
	 * 
	 * @param keyCombination
	 *            the key combination to exit on
	 * @since JavaFX 8.0
	 */
	public void setFullScreenExitKeyCombination(KeyCombination keyCombination);

	/**
	 * Get the current sequence used to exit Full Screen mode.
	 * 
	 * @return the current setting (null for system default)
	 * @since JavaFX 8.0
	 */
	public KeyCombination getFullScreenExitKeyCombination();

	/**
	 * Get the property for the Full Screen exit key combination.
	 * 
	 * @return the property.
	 * @since JavaFX 8.0
	 */
	public ObjectProperty<KeyCombination> fullScreenExitKeyProperty();

	public void setFullScreenExitHint(String value);

	public String getFullScreenExitHint();

	/**
	 * Specifies the text to show when a user enters full screen mode, usually used
	 * to indicate the way a user should go about exiting out of full screen mode. A
	 * value of null will result in the default per-locale message being displayed.
	 * If set to the empty string, then no message will be displayed.
	 * <p>
	 * If an application does not have the proper permissions, this setting will be
	 * ignored.
	 * 
	 * @since JavaFX 8.0
	 */
	public ObjectProperty<String> fullScreenExitHintProperty();

	/**
	 * Specifies the behavior of the application when this stage is closed. By
	 * default the stage will attempt to shut down the OSGi framework when it is
	 * closed.
	 * <p>
	 * Alternatively the stage can stop the bundle using that stage or do nothing
	 * when the stage is closed.
	 *
	 * @param policy
	 *            the ExitPolicy to be set.
	 * @param bundleCLass
	 *            a class from the bundle that is to be stopped when
	 *            ExitPolicy.STOP_BUNDLE_ON_STAGE_EXIT is used. May be {@code null}
	 *            for other values of {@code policy}
	 *
	 * @defaultValue ExitPolicy.SHUTDOWN_ON_STAGE_EXIT
	 */
	public void initExitPolicy(ExitPolicy policy, Class<?> bundleCLass);

}
