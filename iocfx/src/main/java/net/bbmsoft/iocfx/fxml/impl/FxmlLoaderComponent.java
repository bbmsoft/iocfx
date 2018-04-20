package net.bbmsoft.iocfx.fxml.impl;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

import org.osgi.service.component.ComponentServiceObjects;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.component.annotations.ReferenceScope;

import javafx.fxml.FXMLLoader;
import net.bbmsoft.iocfx.Fxml;
import net.bbmsoft.iocfx.Platform;
import net.bbmsoft.iocfx.log.impl.MinLogger;

@Component
public class FxmlLoaderComponent {

	private final Queue<Fxml> queue;

	@Reference
	private Platform platform;

	@Reference
	private MinLogger log;

	@Reference(scope = ReferenceScope.PROTOTYPE_REQUIRED)
	private ComponentServiceObjects<FXMLLoader> loaderFactory;

	private boolean active;

	public FxmlLoaderComponent() {
		this.queue = new LinkedList<>();
	}

	@Activate
	public synchronized void activate() {

		this.active = true;

		while (!this.queue.isEmpty()) {
			Fxml fxml = this.queue.remove();
			loadOnFxThread(fxml);
		}
	}

	@Deactivate
	public synchronized void deactivate() {
		this.active = false;
	}

	@Reference(cardinality = ReferenceCardinality.MULTIPLE, policy = ReferencePolicy.DYNAMIC)
	public synchronized void addFxml(Fxml fxml) {

		if (this.active) {
			loadOnFxThread(fxml);
		} else {
			this.queue.add(fxml);
		}
	}

	public synchronized void removeFxml(Fxml fxml) {
		this.queue.remove(fxml);
	}

	private void loadOnFxThread(Fxml client) {
		ComponentServiceObjects<FXMLLoader> loaderFactory = this.loaderFactory;
		MinLogger log = this.log;
		this.platform.runOnFxApplicationThread(() -> this.load(client, loaderFactory, log));
	}

	private void load(Fxml fxml, ComponentServiceObjects<FXMLLoader> loaderFactory, MinLogger log) {

		FXMLLoader loader;

		try {
			loader = loaderFactory.getService();
		} catch (IllegalStateException e) {
			this.log.error("FXMLLoader service not available anymore!");
			return;
		}

		try {
			load(fxml, loader);
		} catch (IOException e) {
			handle(fxml, e, log);
		} finally {
			unregisterLoader(loaderFactory, loader);
		}
	}

	private void load(Fxml fxml, FXMLLoader loader) throws IOException {

		loader.setLocation(fxml.getLocation());
		loader.setController(fxml);

		if (fxml instanceof Fxml.Root) {
			loader.setRoot(fxml);
		}

		loader.load();
	}

	private void handle(Fxml fxml, IOException e, MinLogger log) {

		if (fxml instanceof Fxml.ErrorHandler) {
			((Fxml.ErrorHandler) fxml).onError(e);
		} else {
			log.error("Could not load " + fxml.getLocation(), e);
		}
	}

	private void unregisterLoader(ComponentServiceObjects<FXMLLoader> loaderFactory, FXMLLoader loader) {
		
		try {
			loaderFactory.ungetService(loader);
		} catch (IllegalStateException e) {
			// ignore, service has already been unregistered
		}
	}
}
