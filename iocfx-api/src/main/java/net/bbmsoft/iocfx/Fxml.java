package net.bbmsoft.iocfx;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXMLLoader;

/**
 * Service interface for components that need an FXML file to be loaded. Any
 * component implementing this interface will be discovered by IoCFX and the
 * FXML file referred to by the return value of {@link #getLocation()} will be
 * loaded.
 * <p>
 * If the {@code Fxml} instance implements the {@link Root} interface, it will
 * be set as the root of the FXML object.
 * <p>
 * If the {@code Fxml} instance implements the {@link Controller} interface, it
 * will be set as the controller of the FXML object.
 * <p>
 * If the {@code Fxml} instance implements the {@link Consumer} interface, the
 * loaded object will be passed to its {@link Consumer#accept(Object) accept}
 * method.
 * <p>
 * If the {@code Fxml} instance implements the {@link Resources} interface, the
 * provided {@link ResourceBundle} will be used while loading the FXML file.
 * method.
 * <p>
 * If a component needs to have direct control over the {@link FXMLLoader}
 * instance, it might not be a good idea to implement this interface. Instead
 * declare a dependency on an {@link FXMLLoader} prototype service like this:
 * 
 * <pre>
 * &#64;Reference(scope = ReferenceScope.PROTOTYPE_REQUIRED)
 * private FXMLLoader loader;
 * </pre>
 * 
 * IoCFX will provide the component with its own instance of an
 * {@code FXMLLoader} that is able to access classes used in the FXML via OSGi
 * compatible bundle imports, thus avoiding {@link ClassNotFoundException
 * ClassNotFoundExceptions} which are otherwise a common problem when using an
 * off-the-shelf {@code FXMLLoader} in an OSGi environment.
 * 
 * @author Michael Bachmann
 *
 */
public interface Fxml {

	/**
	 * Marker interface for components that need to be set as the document root of
	 * the FXML file they want to be loaded.
	 * 
	 * @author Michael Bachmann
	 *
	 */
	public interface Root extends Fxml {

	}

	/**
	 * Marker interface for components that need to be set as the controller of the
	 * FXML file they want to be loaded.
	 * 
	 * @author Michael Bachmann
	 *
	 */
	public interface Controller extends Fxml {

	}

	/**
	 * Fxml components implementing the Resources interface can supply a
	 * {@link ResourceBundle} that will be used by the FXMLLoader.
	 * 
	 * @author Michael Bachmann
	 *
	 */
	public interface Resources extends Fxml {

		/**
		 * Provide a {@link ResourceBundle} that will be used to resolve resource key
		 * attribute values in the FXML file.
		 * 
		 * @return a {@code ResourceBundle}
		 */
		public ResourceBundle getResources();
	}

	/**
	 * Fxml components implementing the Consumer interface will be passed the loaded
	 * object once the fxml file has been successfully loaded. This is guaranteed to
	 * happen on the on the JavaFX Application Thread.
	 * 
	 * @author Michael Bachmann
	 *
	 * @param <T>
	 *            type of the FXML's root object
	 */
	public interface Consumer<T> extends Fxml {

		/**
		 * The root object loaded from the FXML file will be passed to this method on
		 * the JavaFX Application Thread.
		 * 
		 * @param root
		 *            the root object loaded from the FXML file
		 */
		public void accept(T root);
	}

	/**
	 * If an {@code Fxml} instance implements this interface, any {@link IOException
	 * IOExceptions} that happen while loading the FXML file will be caught and
	 * propagated to the {@link #onError(IOException)} method.
	 * 
	 * @author Michael Bachmann
	 *
	 */
	public interface ErrorHandler {

		/**
		 * Handle {@link IOException} that happen while loading the FXML file.
		 * 
		 * @param e
		 *            the exception that needs to be handled
		 */
		public void onError(IOException e);
	}

	/**
	 * Provide a {@link URL} that points to the FXML file that needs to be loaded.
	 * Typically this would reside in the same bundle as the class implementing the
	 * {@code Fxml} interface.
	 * 
	 * @return the FXML file resource to be loaded
	 */
	public URL getLocation();
}
