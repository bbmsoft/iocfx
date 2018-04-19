package net.bbmsoft.iocfx.fxml.impl;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

import org.osgi.service.component.ComponentServiceObjects;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.component.annotations.ReferenceScope;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import net.bbmsoft.iocfx.Fxml;
import net.bbmsoft.iocfx.Platform;

@Component
public class FxmlLoaderComponent {

	private final Queue<Fxml> queue;

	private ComponentServiceObjects<FXMLLoader> loaderFactory;

	private Platform platform;

	public FxmlLoaderComponent() {
		this.queue = new LinkedList<>();
	}

	@Reference
	public synchronized void setPlatform(Platform platform) {

		this.platform = platform;
		loadQueue();
	}

	@Reference(scope = ReferenceScope.PROTOTYPE_REQUIRED)
	public synchronized void setLoader(ComponentServiceObjects<FXMLLoader> loader) {

		this.loaderFactory = loader;
		loadQueue();
	}

	private void loadQueue() {

		Platform platform = this.platform;
		ComponentServiceObjects<FXMLLoader> loaderFactory = this.loaderFactory;

		if (platform != null && loaderFactory != null) {
			
			while (!this.queue.isEmpty()) {
				Fxml fxml = this.queue.remove();
				load(fxml, platform, loaderFactory);
			}
		}
	}

	private void load(Fxml fxml, Platform platform, ComponentServiceObjects<FXMLLoader> loaderFactory) {
		
		FXMLLoader loader = loaderFactory.getService();

		try {
			platform.runAndWait(() -> load(fxml, loader));
		} catch (Throwable e) {
			loaderFactory.ungetService(loader);
		}
	}

	@Reference(cardinality = ReferenceCardinality.MULTIPLE, policy = ReferencePolicy.DYNAMIC)
	public synchronized void addController(Fxml controller) {

		Platform platform = this.platform;
		ComponentServiceObjects<FXMLLoader> loaderFactory = this.loaderFactory;

		if (platform != null && loaderFactory != null) {
			load(controller, platform, loaderFactory);
		} else {
			this.queue.add(controller);
		}
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

		this.platform.runOnFxApplicationThread(() -> init.initialize(null, null));
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
