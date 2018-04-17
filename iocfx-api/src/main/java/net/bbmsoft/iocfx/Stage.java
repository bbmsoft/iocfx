package net.bbmsoft.iocfx;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCombination;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import javafx.stage.WindowEvent;

public interface Stage {

	public void showAndWait();

	public void initStyle(StageStyle style);

	public StageStyle getStyle();

	public void initModality(Modality modality);

	public Modality getModality();

	public void initOwner(Window owner);

	public Window getOwner();

	public void setFullScreen(boolean value);

	public boolean isFullScreen();

	public ReadOnlyBooleanProperty fullScreenProperty();

	public ObservableList<Image> getIcons();

	public void setTitle(String value);

	public String getTitle();

	public StringProperty titleProperty();

	public void setIconified(boolean value);

	public boolean isIconified();

	public ReadOnlyBooleanProperty iconifiedProperty();

	public void setMaximized(boolean value);

	public boolean isMaximized();

	public ReadOnlyBooleanProperty maximizedProperty();

	public void setAlwaysOnTop(boolean value);

	public boolean isAlwaysOnTop();

	public ReadOnlyBooleanProperty alwaysOnTopProperty();

	public void setResizable(boolean value);

	public boolean isResizable();

	public BooleanProperty resizableProperty();

	public void setMinWidth(double value);

	public double getMinWidth();

	public DoubleProperty minWidthProperty();

	public void setMinHeight(double value);

	public double getMinHeight();

	public DoubleProperty minHeightProperty();

	public void setMaxWidth(double value);

	public double getMaxWidth();

	public DoubleProperty maxWidthProperty();

	public void setMaxHeight(double value);

	public double getMaxHeight();

	public DoubleProperty maxHeightProperty();

	public void toFront();

	public void toBack();

	public void close();

	public void setFullScreenExitKeyCombination(KeyCombination keyCombination);

	public KeyCombination getFullScreenExitKeyCombination();

	public ObjectProperty<KeyCombination> fullScreenExitKeyProperty();

	public void setFullScreenExitHint(String value);

	public String getFullScreenExitHint();

	public ObjectProperty<String> fullScreenExitHintProperty();

	public void initExitPolicy(ExitPolicy policy, Class<?> bundleCLass);

	public void setScene(Scene scene);

	public void show();

	public ReadOnlyBooleanProperty focusedProperty();

	public void setOnCloseRequest(EventHandler<WindowEvent> object);
}
