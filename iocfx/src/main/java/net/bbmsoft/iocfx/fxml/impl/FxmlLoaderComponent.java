package net.bbmsoft.iocfx.fxml.impl;

import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

import org.osgi.service.component.ComponentServiceObjects;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.component.annotations.ReferenceScope;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import net.bbmsoft.iocfx.Fxml;
import net.bbmsoft.iocfx.Fxml.Application;
import net.bbmsoft.iocfx.Platform;
import net.bbmsoft.iocfx.StageService;
import net.bbmsoft.iocfx.StageService.ExitPolicy;
import net.bbmsoft.iocfx.log.impl.MinLogger;

/**
 * Tracks and manages {@link Fxml} services. If a component publishes itself as
 * an {@link Fxml} service, this component will make sure its layout gets loaded
 * on the JavaFX Application thread as soon as the JavaFX Platform is available.
 * 
 * @author Michael Bachmann
 *
 */
@Component
public class FxmlLoaderComponent {

	private final Set<Fxml> fxmls;

	@Reference
	private Platform platform;

	@Reference
	private MinLogger log;

	@Reference(scope = ReferenceScope.PROTOTYPE_REQUIRED)
	private ComponentServiceObjects<FXMLLoader> loaderFactory;

	@Reference(scope = ReferenceScope.PROTOTYPE_REQUIRED)
	private ComponentServiceObjects<StageService> stageService;

	private boolean active;

	public FxmlLoaderComponent() {
		this.fxmls = new HashSet<>();
	}

	@Activate
	public synchronized void activate() {

		this.active = true;

		for (Fxml fxml : this.fxmls) {
			loadOnFxThread(fxml);
		}
	}

	@Deactivate
	public synchronized void deactivate() {
		this.active = false;
		this.fxmls.clear();
	}

	@Reference(cardinality = ReferenceCardinality.MULTIPLE, policy = ReferencePolicy.DYNAMIC)
	public synchronized void addFxml(Fxml fxml) {

		if (this.fxmls.add(fxml) && this.active) {
			loadOnFxThread(fxml);
		}
	}

	public synchronized void removeFxml(Fxml fxml) {
		this.fxmls.remove(fxml);
	}

	@Reference(cardinality = ReferenceCardinality.MULTIPLE, policy = ReferencePolicy.DYNAMIC)
	public synchronized void addFxmlRoot(Fxml.Root fxml) {

		if (this.fxmls.add(fxml) && this.active) {
			loadOnFxThread(fxml);
		}
	}

	public synchronized void removeFxmlRoot(Fxml.Root fxml) {
		this.fxmls.remove(fxml);
	}

	@Reference(cardinality = ReferenceCardinality.MULTIPLE, policy = ReferencePolicy.DYNAMIC)
	public synchronized void addFxmlController(Fxml.Controller fxml) {

		if (this.fxmls.add(fxml) && this.active) {
			loadOnFxThread(fxml);
		}
	}

	public synchronized void removeFxmlController(Fxml.Controller fxml) {
		this.fxmls.remove(fxml);
	}

	@Reference(cardinality = ReferenceCardinality.MULTIPLE, policy = ReferencePolicy.DYNAMIC)
	public synchronized void addFxmlConsumer(Fxml.Consumer<?> fxml) {

		if (this.fxmls.add(fxml) && this.active) {
			loadOnFxThread(fxml);
		}
	}

	public synchronized void removeFxmlConsumer(Fxml.Consumer<?> fxml) {
		this.fxmls.remove(fxml);
	}

	@Reference(cardinality = ReferenceCardinality.MULTIPLE, policy = ReferencePolicy.DYNAMIC)
	public synchronized void addFxmlApplication(Fxml.Application fxml) {

		if (this.fxmls.add(fxml) && this.active) {
			loadOnFxThread(fxml);
		}
	}

	public synchronized void removeFxmlApplication(Fxml.Application fxml) {
		this.fxmls.remove(fxml);
	}

	@Reference(cardinality = ReferenceCardinality.MULTIPLE, policy = ReferencePolicy.DYNAMIC)
	public synchronized void addFxmlResources(Fxml.Resources fxml) {

		if (this.fxmls.add(fxml) && this.active) {
			loadOnFxThread(fxml);
		}
	}

	public synchronized void removeFxmlResources(Fxml.Resources fxml) {
		this.fxmls.remove(fxml);
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

	@SuppressWarnings("unchecked")
	private <T> void load(Fxml fxml, FXMLLoader loader) throws IOException {

		URL location = fxml.getLocation();

		if (location != null) {
			log.info("Loading FXML " + location + " for component " + fxml);
		} else {
			log.error("Fxml " + fxml + " didn't provide a valid location!");
		}

		loader.setLocation(location);

		if (fxml instanceof Fxml.Resources) {
			ResourceBundle resources = ((Fxml.Resources) fxml).getResources();
			loader.setResources(resources);
		}

		if (fxml instanceof Fxml.Controller) {
			loader.setController(fxml);
		}

		if (fxml instanceof Fxml.Root) {
			loader.setRoot(fxml);
		}

		T object = loader.load();

		if (fxml instanceof Fxml.Consumer) {
			((Fxml.Consumer<T>) fxml).accept(object);
		}

		if (fxml instanceof Application) {
			if (object instanceof Region) {

				StageService stageService = this.stageService.getService();
				Stage stage = stageService.getStage();

				ExitPolicy exitPolicy = ((Application) fxml).getExitPolicy();
				exitPolicy = exitPolicy != null ? exitPolicy : ExitPolicy.SHUTDOWN_ON_STAGE_EXIT;
				stageService.setExitPolicy(exitPolicy, fxml.getClass());
				stage.setScene(new Scene((Region) object));
				((Application) fxml).prepareStage(stage);
				stage.show();

			} else {
				throw new IllegalStateException("An application root must be an instance of " + Region.class
						+ " but was a " + object.getClass());
			}
		}

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
