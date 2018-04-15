package net.bbmsoft.iocfx;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCombination;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import javafx.stage.Window;

public interface Stage {

        
        public void setIconified(Stage stage, boolean iconified) ;

        
        public void setMaximized(Stage stage, boolean maximized) ;

        
        public void setResizable(Stage stage, boolean resizable) ;

        
        public void setFullScreen(Stage stage, boolean fs);

        
        public void setAlwaysOnTop(Stage stage, boolean aot);
        
        public void showAndWait() 


        public void initStyle(StageStyle style) ;

        public StageStyle getStyle() ;


        public void initModality(Modality modality) ;

        public Modality getModality() ;


        public void initOwner(Window owner) ;

        public Window getOwner() ;

        public void setFullScreen(boolean value) ;

        public boolean isFullScreen() ;

        public ReadOnlyBooleanProperty fullScreenProperty();

        public ObservableList<Image> getIcons();

        public void setTitle(String value) ;

        public String getTitle() ;

        public StringProperty titleProperty() ;

        public void setIconified(boolean value) ;

        public boolean isIconified();

        public ReadOnlyBooleanProperty iconifiedProperty() ;

        public void setMaximized(boolean value) ;

        public boolean isMaximized();

        public ReadOnlyBooleanProperty maximizedProperty() ;

        public void setAlwaysOnTop(boolean value) ;

        public boolean isAlwaysOnTop() ;

        public ReadOnlyBooleanProperty alwaysOnTopProperty() ;


        public void setResizable(boolean value) ;

        public boolean isResizable() ;

        public BooleanProperty resizableProperty() ;

        public void setMinWidth(double value) ;

        public double getMinWidth() ;

        public DoubleProperty minWidthProperty() ;

        public void setMinHeight(double value) ;

        public double getMinHeight() ;

        public DoubleProperty minHeightProperty() ;

        public void setMaxWidth(double value) ;

        public double getMaxWidth() ;

        public DoubleProperty maxWidthProperty() ;
        
        public void setMaxHeight(double value) {
            maxHeightProperty().set(value);
        }

        public double getMaxHeight() {
            return maxHeight == null ? Double.MAX_VALUE : maxHeight.get();
        }

        public DoubleProperty maxHeightProperty() {
            if (maxHeight == null) {
                maxHeight = new DoublePropertyBase(Double.MAX_VALUE) {

                    @Override
                    protected void invalidated() {
                        if (impl_peer != null) {
                            impl_peer.setMaximumSize(
                                    (int) Math.floor(getMaxWidth()),
                                    (int) Math.floor(get()));
                        }
                        if (getHeight() > getMaxHeight()) {
                            setHeight(getMaxHeight());
                        }
                    }

                    @Override
                    public Object getBean() {
                        return Stage.this;
                    }

                    @Override
                    public String getName() {
                        return "maxHeight";
                    }
                };
            }
            return maxHeight;
        }


        /**
         * @treatAsPrivate implementation detail
         * @deprecated This is an internal API that is not intended for use and will be removed in the next version
         */
        @Deprecated
        @Override protected void impl_visibleChanging(boolean value) {
            super.impl_visibleChanging(value);
            Toolkit toolkit = Toolkit.getToolkit();
            if (value && (impl_peer == null)) {
                // Setup the peer
                Window window = getOwner();
                TKStage tkStage = (window == null ? null : window.impl_getPeer());
                Scene scene = getScene();
                boolean rtl = scene != null && scene.getEffectiveNodeOrientation() == NodeOrientation.RIGHT_TO_LEFT;

                StageStyle stageStyle = getStyle();
                if (stageStyle == StageStyle.TRANSPARENT) {
                    SecurityManager securityManager =
                            System.getSecurityManager();
                    if (securityManager != null) {
                        try {
                            securityManager.checkPermission(new AllPermission());
                        } catch (final SecurityException e) {
                            stageStyle = StageStyle.UNDECORATED;
                        }
                    }
                }
                impl_peer = toolkit.createTKStage(this, isSecurityDialog(), stageStyle, isPrimary(),
                                                  getModality(), tkStage, rtl, acc);
                impl_peer.setMinimumSize((int) Math.ceil(getMinWidth()),
                        (int) Math.ceil(getMinHeight()));
                impl_peer.setMaximumSize((int) Math.floor(getMaxWidth()),
                        (int) Math.floor(getMaxHeight()));
                peerListener = new StagePeerListener(this, STAGE_ACCESSOR);
               // Insert this into stages so we have a references to all created stages
                stages.add(this);
            }
        }


        /**
         * @treatAsPrivate implementation detail
         * @deprecated This is an internal API that is not intended for use and will be removed in the next version
         */
        @Deprecated
        @Override protected void impl_visibleChanged(boolean value) {
            super.impl_visibleChanged(value);

            if (value) {
                // Finish initialization
                impl_peer.setImportant(isImportant());
                impl_peer.setResizable(isResizable());
                impl_peer.setFullScreen(isFullScreen());
                impl_peer.setAlwaysOnTop(isAlwaysOnTop());
                impl_peer.setIconified(isIconified());
                impl_peer.setMaximized(isMaximized());
                impl_peer.setTitle(getTitle());

                List<Object> platformImages = new ArrayList<Object>();
                for (Image icon : icons) {
                    platformImages.add(icon.impl_getPlatformImage());
                }
                if (impl_peer != null) {
                    impl_peer.setIcons(platformImages);
                }
            }

            if (!value) {
                // Remove form active stage list
                stages.remove(this);
            }

            if (!value && inNestedEventLoop) {
                inNestedEventLoop = false;
                Toolkit.getToolkit().exitNestedEventLoop(this, null);
            }
        }

        /**
         * Bring the {@code Window} to the foreground.  If the {@code Window} is
         * already in the foreground there is no visible difference.
         */
        public void toFront() {
            if (impl_peer != null) {
                impl_peer.toFront();
            }
        }

        /**
         * Send the {@code Window} to the background.  If the {@code Window} is
         * already in the background there is no visible difference.  This action
         * places this {@code Window} at the bottom of the stacking order on
         * platforms that support stacking.
         */
        public void toBack() {
            if (impl_peer != null) {
                impl_peer.toBack();
            }
        }

        /**
         * Closes this {@code Stage}.
         * This call is equivalent to {@code hide()}.
         */
        public void close() {
            hide();
        }

        @Override
        Window getWindowOwner() {
            return getOwner();
        }


        private ObjectProperty<KeyCombination> fullScreenExitCombination =
                new SimpleObjectProperty<KeyCombination>(this, "fullScreenExitCombination", null);

        /**
         * Specifies the KeyCombination that will allow the user to exit full screen
         * mode. A value of KeyCombination.NO_MATCH will not match any KeyEvent and
         * will make it so the user is not able to escape from Full Screen mode.
         * A value of null indicates that the default platform specific key combination
         * should be used.
         * <p>
         * An internal copy of this value is made when entering FullScreen mode and will be
         * used to trigger the exit from the mode. If an application does not have
         * the proper permissions, this setting will be ignored.
         * </p>
         * @param keyCombination the key combination to exit on
         * @since JavaFX 8.0
         */
        public void setFullScreenExitKeyCombination(KeyCombination keyCombination) {
            fullScreenExitCombination.set(keyCombination);
        }

        /**
         * Get the current sequence used to exit Full Screen mode.
         * @return the current setting (null for system default)
         * @since JavaFX 8.0
         */
        public KeyCombination getFullScreenExitKeyCombination() {
            return fullScreenExitCombination.get();
        }

        /**
         * Get the property for the Full Screen exit key combination.
         * @return the property.
         * @since JavaFX 8.0
         */
        public ObjectProperty<KeyCombination> fullScreenExitKeyProperty() {
            return fullScreenExitCombination;
        }

        private ObjectProperty<String> fullScreenExitHint =
                new SimpleObjectProperty<String>(this, "fullScreenExitHint", null);

        /**
         * Specifies the text to show when a user enters full screen mode, usually
         * used to indicate the way a user should go about exiting out of full
         * screen mode. A value of null will result in the default per-locale
         * message being displayed.
         * If set to the empty string, then no message will be displayed.
         * <p>
         * If an application does not have the proper permissions, this setting
         * will be ignored.
         * </p>
         * @param value the string to be displayed.
         * @since JavaFX 8.0
         */
        public void setFullScreenExitHint(String value) {
            fullScreenExitHint.set(value);
        }

        public String getFullScreenExitHint() {
            return fullScreenExitHint.get();
        }

        public ObjectProperty<String> fullScreenExitHintProperty() {
            return fullScreenExitHint;
        }
}
