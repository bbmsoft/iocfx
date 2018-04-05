package net.bbmsoft.iocfx.fxml.impl;

import java.io.IOException;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import net.bbmsoft.iocfx.fxml.Fxml;
import net.bbmsoft.iocfx.platform.Platform;

@Component
public class FxmlLoaderComponent {

	@Reference
	private FXMLLoader loader;

	@Reference
	private Platform platform;

	@Reference(cardinality = ReferenceCardinality.MULTIPLE, policy = ReferencePolicy.DYNAMIC)
	public void addController(Fxml controller) {

		FXMLLoader loader = this.loader;

		this.platform.runLater(() -> load(controller, loader));
	}
	
	public void removeController(Fxml controller) {
		// does nothing, but bndtools complains if not there
	}

	@Reference(cardinality = ReferenceCardinality.MULTIPLE, policy = ReferencePolicy.DYNAMIC)
	public void addInitializable(Initializable init) {

		if (init instanceof Fxml) {
			// ignore, there's a separate method for those
			return;
		}

		this.platform.runLater(() -> init.initialize(null, null));
	}
	
	public void removeInitializable(Initializable init) {
		// does nothing, but bndtools complains if not there
	}

	private void load(Fxml controller, FXMLLoader loader) {

		loader.setLocation(controller.getLocation());
		loader.setController(controller);

		if (controller instanceof Fxml.Root) {
			loader.setRoot(controller);
		}

		try {
			loader.load();
		} catch (IOException e) {
			if (controller instanceof Fxml.ErrorHandler) {
				((Fxml.ErrorHandler) controller).onError(e);
			} else {
				e.printStackTrace();
			}
		}
	}
}
