package net.bbmsoft.iocfx.fxml;

import java.io.IOException;
import java.net.URL;

public interface Fxml {
	
	public interface Root {
		
	}
	
	public interface ErrorHandler {
		
		public void onError(IOException e);
	}

	public URL getLocation();
}
