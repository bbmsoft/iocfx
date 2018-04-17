package net.bbmsoft.iocfx;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.collections.ObservableMap;
import javafx.event.Event;
import javafx.event.EventDispatchChain;
import javafx.event.EventDispatcher;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * Provides all public methods of {@link javafx.stage.Window}. Only exists to
 * serve as base interface of {@link Stage}, no instances will be registered as
 * {@code Window} services by IoCFX.
 * 
 * @author Michael Bachmann
 *
 */
public interface Window {

	/**
	 * Set the width and height of this Window to match the size of the content
	 * of this Window's Scene.
	 */
	public void sizeToScene();

	/**
	 * Sets x and y properties on this Window so that it is centered on the
	 * curent screen.
	 * The current screen is determined from the intersection of current window bounds and
	 * visual bounds of all screens.
	 */
	public void centerOnScreen();

	public void setX(double value);

	public double getX();
	
	 /**
     * The horizontal location of this {@code Window} on the screen. Changing
     * this attribute will move the {@code Window} horizontally. If this
     * {@code Window} is an instance of {@code Stage}, changing this attribute
     * will not visually affect the {@code Window} while
     * {@link Stage#fullScreenProperty fullScreen} is true, but will be honored
     * by the {@code Window} once {@link Stage#fullScreenProperty fullScreen}
     * becomes false.
     */
	public ReadOnlyDoubleProperty xProperty();

	public void setY(double value);

	public double getY();
	
	/**
     * The vertical location of this {@code Window} on the screen. Changing this
     * attribute will move the {@code Window} vertically. If this
     * {@code Window} is an instance of {@code Stage}, changing this attribute
     * will not visually affect the {@code Window} while
     * {@link Stage#fullScreenProperty fullScreen} is true, but will be honored
     * by the {@code Window} once {@link Stage#fullScreenProperty fullScreen}
     * becomes false.
     */
	public ReadOnlyDoubleProperty yProperty();

	public void setWidth(double value);

	public double getWidth();
	
	/**
     * The width of this {@code Window}. Changing this attribute will narrow or
     * widen the width of the {@code Window}. This value includes any and all
     * decorations which may be added by the Operating System such as resizable
     * frame handles. Typical applications will set the {@link javafx.scene.Scene}
     * width instead. This {@code Window} will take its width from the scene if
     * it has never been set by the application. Resizing the window by end user
     * does not count as a setting the width by the application. If this
     * {@code Window} is an instance of {@code Stage}, changing this attribute
     * will not visually affect the {@code Window} while
     * {@link Stage#fullScreenProperty fullScreen} is true, but will be honored
     * by the {@code Window} once {@link Stage#fullScreenProperty fullScreen}
     * becomes false.
     * <p>
     * The property is read only because it can be changed externally
     * by the underlying platform and therefore must not be bindable.
     * </p>
     */
	public ReadOnlyDoubleProperty widthProperty();

	public void setHeight(double value);

	public double getHeight();

	/**
     * The height of this {@code Window}. Changing this attribute will shrink
     * or heighten the height of the {@code Window}. This value includes any and all
     * decorations which may be added by the Operating System such as the title
     * bar. Typical applications will set the {@link javafx.scene.Scene} height
     * instead. This window will take its height from the scene if it has never
     * been set by the application. Resizing this window by end user does not
     * count as a setting the height by the application.  If this
     * {@code Window} is an instance of {@code Stage}, changing this attribute
     * will not visually affect the {@code Window} while
     * {@link Stage#fullScreenProperty fullScreen} is true, but will be honored
     * by the {@code Window} once {@link Stage#fullScreenProperty fullScreen}
     * becomes false.
     * <p>
     * The property is read only because it can be changed externally
     * by the underlying platform and therefore must not be bindable.
     * </p>
     */
	public ReadOnlyDoubleProperty heightProperty();

	/**
	 * Requests that this {@code Window} get the input focus.
	 */
	public void requestFocus();

	public boolean isFocused();

    /**
     * Whether or not this {@code Window} has the keyboard or input focus.
     * <p>
     * The property is read only because it can be changed externally
     * by the underlying platform and therefore must not be bindable.
     * </p>
     */
	public ReadOnlyBooleanProperty focusedProperty();

	/**
	  * Returns an observable map of properties on this node for use primarily
	  * by application developers.
	  *
	  * @return an observable map of properties on this node for use primarily
	  * by application developers
	  *
	  * @since JavaFX 8u40
	  */
	public ObservableMap<Object, Object> getProperties();

	/**
	 * Tests if Window has properties.
	 * @return true if node has properties.
	 *
	 * @since JavaFX 8u40
	 */
	public boolean hasProperties();

	/**
	 * Convenience method for setting a single Object property that can be
	 * retrieved at a later date. This is functionally equivalent to calling
	 * the getProperties().put(Object key, Object value) method. This can later
	 * be retrieved by calling {@link Window#getUserData()}.
	 *
	 * @param value The value to be stored - this can later be retrieved by calling
	 *          {@link Window#getUserData()}.
	 *
	 * @since JavaFX 8u40
	 */
	public void setUserData(Object value);

	/**
	 * Returns a previously set Object property, or null if no such property
	 * has been set using the {@link Window#setUserData(java.lang.Object)} method.
	 *
	 * @return The Object that was previously set, or null if no property
	 *          has been set or if null was set.
	 *
	 * @since JavaFX 8u40
	 */
	public Object getUserData();

	public void setScene(Scene scene);
	
	public Scene getScene();

    /**
     * The {@code Scene} to be rendered on this {@code Window}. There can only
     * be one {@code Scene} on the {@code Window} at a time, and a {@code Scene}
     * can only be on one {@code Window} at a time. Setting a {@code Scene} on
     * a different {@code Window} will cause the old {@code Window} to lose the
     * reference before the new one gains it. You may swap {@code Scene}s on
     * a {@code Window} at any time, even if it is an instance of {@code Stage}
     * and with {@link Stage#fullScreenProperty fullScreen} set to true.
     * If the width or height of this {@code Window} have never been set by the
     * application, setting the scene will cause this {@code Window} to take its
     * width or height from that scene.  Resizing this window by end user does
     * not count as setting the width or height by the application.
     *
     * An {@link IllegalStateException} is thrown if this property is set
     * on a thread other than the JavaFX Application Thread.
     *
     * @defaultValue null
     */
	public ReadOnlyObjectProperty<Scene> sceneProperty();

	public void setOpacity(double value);

	public double getOpacity();

    /**
     * Defines the opacity of the {@code Window} as a value between 0.0 and 1.0.
     * The opacity is reflected across the {@code Window}, its {@code Decoration}
     * and its {@code Scene} content. On a JavaFX runtime platform that does not
     * support opacity, assigning a value to this variable will have no
     * visible difference. A {@code Window} with 0% opacity is fully translucent.
     * Typically, a {@code Window} with 0% opacity will not receive any mouse
     * events.
     *
     * @defaultValue 1.0
     */
	public DoubleProperty opacityProperty();

	public void setOnCloseRequest(EventHandler<WindowEvent> value);

	public EventHandler<WindowEvent> getOnCloseRequest();

    /**
     * Called when there is an external request to close this {@code Window}.
     * The installed event handler can prevent window closing by consuming the
     * received event.
     */
	public ObjectProperty<EventHandler<WindowEvent>> onCloseRequestProperty();

	public void setOnShowing(EventHandler<WindowEvent> value);

	public EventHandler<WindowEvent> getOnShowing();

    /**
     * Called just prior to the Window being shown.
     */
	public ObjectProperty<EventHandler<WindowEvent>> onShowingProperty();

	public void setOnShown(EventHandler<WindowEvent> value);

	public EventHandler<WindowEvent> getOnShown();

    /**
     * Called just after the Window is shown.
     */
	public ObjectProperty<EventHandler<WindowEvent>> onShownProperty();

	public void setOnHiding(EventHandler<WindowEvent> value);

	public EventHandler<WindowEvent> getOnHiding();

    /**
     * Called just prior to the Window being hidden.
     */
	public ObjectProperty<EventHandler<WindowEvent>> onHidingProperty();

	public void setOnHidden(EventHandler<WindowEvent> value);

	public EventHandler<WindowEvent> getOnHidden();

    /**
     * Called just after the Window has been hidden.
     * When the {@code Window} is hidden, this event handler is invoked allowing
     * the developer to clean up resources or perform other tasks when the
     * {@link Window} is closed.
     */
	public ObjectProperty<EventHandler<WindowEvent>> onHiddenProperty();

	public boolean isShowing();

    /**
     * Whether or not this {@code Window} is showing (that is, open on the
     * user's system). The Window might be "showing", yet the user might not
     * be able to see it due to the Window being rendered behind another Window
     * or due to the Window being positioned off the monitor.
     *
     * @defaultValue false
     */
	public ReadOnlyBooleanProperty showingProperty();

	/**
	 * Attempts to hide this Window by setting the visibility to false.
	 *
	 * @throws IllegalStateException if this method is called on a thread
	 * other than the JavaFX Application Thread.
	 */
	public void hide();

	public void setEventDispatcher(EventDispatcher value);

	public EventDispatcher getEventDispatcher();
	
    /**
     * Specifies the event dispatcher for this node. The default event
     * dispatcher sends the received events to the registered event handlers and
     * filters. When replacing the value with a new {@code EventDispatcher},
     * the new dispatcher should forward events to the replaced dispatcher
     * to maintain the node's default event handling behavior.
     */
	public ObjectProperty<EventDispatcher> eventDispatcherProperty();

	/**
	 * Registers an event handler to this node. The handler is called when the
	 * node receives an {@code Event} of the specified type during the bubbling
	 * phase of event delivery.
	 *
	 * @param <T> the specific event class of the handler
	 * @param eventType the type of the events to receive by the handler
	 * @param eventHandler the handler to register
	 * @throws NullPointerException if the event type or handler is null
	 */
	public <T extends Event> void addEventHandler(EventType<T> eventType, EventHandler<? super T> eventHandler);

	/**
	 * Unregisters a previously registered event handler from this node. One
	 * handler might have been registered for different event types, so the
	 * caller needs to specify the particular event type from which to
	 * unregister the handler.
	 *
	 * @param <T> the specific event class of the handler
	 * @param eventType the event type from which to unregister
	 * @param eventHandler the handler to unregister
	 * @throws NullPointerException if the event type or handler is null
	 */
	public <T extends Event> void removeEventHandler(EventType<T> eventType, EventHandler<? super T> eventHandler);

	/**
	 * Registers an event filter to this node. The filter is called when the
	 * node receives an {@code Event} of the specified type during the capturing
	 * phase of event delivery.
	 *
	 * @param <T> the specific event class of the filter
	 * @param eventType the type of the events to receive by the filter
	 * @param eventFilter the filter to register
	 * @throws NullPointerException if the event type or filter is null
	 */
	public <T extends Event> void addEventFilter(EventType<T> eventType, EventHandler<? super T> eventFilter);

	/**
	 * Unregisters a previously registered event filter from this node. One
	 * filter might have been registered for different event types, so the
	 * caller needs to specify the particular event type from which to
	 * unregister the filter.
	 *
	 * @param <T> the specific event class of the filter
	 * @param eventType the event type from which to unregister
	 * @param eventFilter the filter to unregister
	 * @throws NullPointerException if the event type or filter is null
	 */
	public <T extends Event> void removeEventFilter(EventType<T> eventType, EventHandler<? super T> eventFilter);

	/**
	 * Fires the specified event.
	 * <p>
	 * This method must be called on the FX user thread.
	 *
	 * @param event the event to fire
	 */
	public void fireEvent(Event event);

	/**
	 * Construct an event dispatch chain for this window.
	 *
	 * @param tail the initial chain to build from
	 * @return the resulting event dispatch chain for this window
	 */
	public EventDispatchChain buildEventDispatchChain(EventDispatchChain tail);
}
