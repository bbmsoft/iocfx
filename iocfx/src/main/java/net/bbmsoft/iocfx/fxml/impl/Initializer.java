package net.bbmsoft.iocfx.fxml.impl;

import java.util.LinkedList;
import java.util.Queue;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import javafx.fxml.Initializable;
import net.bbmsoft.iocfx.Fxml;
import net.bbmsoft.iocfx.Platform;

@Component
public class Initializer {

	private final Queue<Initializable> queue;

	@Reference
	private Platform platform;

	private boolean active;

	public Initializer() {
		this.queue = new LinkedList<>();
	}

	@Activate
	public synchronized void activate() {

		this.active = true;

		while (!this.queue.isEmpty()) {
			Initializable initializable = this.queue.remove();
			this.platform.runOnFxApplicationThread(() -> initializable.initialize(null, null));
		}
	}

	@Deactivate
	public void deactivate() {
		this.active = false;
	}

	@Reference(cardinality = ReferenceCardinality.MULTIPLE, policy = ReferencePolicy.DYNAMIC)
	public synchronized void addInitializable(Initializable initializable) {

		if (initializable instanceof Fxml) {
			// these are already handled by the FxmlLoaderComponent
			return;
		}

		if (this.active) {
			this.platform.runLater(() -> initializable.initialize(null, null));
		} else {
			this.queue.add(initializable);
		}
	}

	public synchronized void removeInitializable(Initializable initializable) {
		this.queue.remove(initializable);
	}
}
