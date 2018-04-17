package net.bbmsoft.iocfx.fxml;

import java.io.IOException;
import java.net.URL;

/**
 * Service interface for components that need an FXML file to be loaded. Any
 * component implementing this interface will be discovered by IoCFX and the
 * FXML file referred to by the return value of {@link #getLocation()} will be
 * loaded with the {@code Fxml} instance as controller.
 * <p>
 * If the {@code Fxml} instance also implements the {@link Root} interface, it
 * will also be set as the root of the FXML object.
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
	public interface Root {

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
