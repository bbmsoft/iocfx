package net.bbmsoft.iocfx.impl;

import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import net.bbmsoft.iocfx.ExitPolicy;
import net.bbmsoft.iocfx.StageConfig;
import net.bbmsoft.iocfx.StageConsumer;

@Component(service = StageConsumerRegistry.class)
public class StageConsumerRegistry {

	private static final class StaticSingletonHolder {

		private static final StageConsumerRegistry instance;

		static {
			instance = new StageConsumerRegistry();
			JavaFXFrameworkLauncher launcher = new JavaFXFrameworkLauncher();
			launcher.setStageUserRegistry(instance);
			launcher.launch();
		}
	}

	public static final StageConsumerRegistry getInstance() {
		return StaticSingletonHolder.instance;
	}

	private final Queue<StageConsumer> queuedStageUsers;
	private final Map<StageConsumer, Stage> stages;

	private boolean initialized;

	public StageConsumerRegistry() {
		this.queuedStageUsers = new LinkedList<>();
		this.stages = new ConcurrentHashMap<>();
	}

	public synchronized void platformInitialized() {
		System.out.println("Starting StageUserRegistry...");
		this.initialized = true;
		while (!this.queuedStageUsers.isEmpty()) {
			addStageUser(this.queuedStageUsers.remove());
		}
	}

	@Reference(cardinality = ReferenceCardinality.MULTIPLE, policy = ReferencePolicy.DYNAMIC)
	public synchronized void addStageUser(StageConsumer stageUser) {

		if (this.initialized) {
			Platform.runLater(() -> provideStage(stageUser));
		} else {
			this.queuedStageUsers.add(stageUser);
		}
	}

	public synchronized void removeStageUser(StageConsumer stageUser) {

		if (this.initialized) {
			Platform.runLater(() -> removeStage(stageUser));
		} else {
			this.queuedStageUsers.remove(stageUser);
		}
	}

	private void provideStage(StageConsumer stageUser) {

		if (this.stages.get(stageUser) != null) {
			throw new IllegalStateException("Stage user " + stageUser + " already has a stage!");
		}

		StageStyle stageStyle = StageStyle.DECORATED;
		ExitPolicy exitPolicy = ExitPolicy.SHUTDOWN_ON_STAGE_EXIT;

		if (stageUser instanceof StageConfig) {
			stageStyle = Objects.requireNonNull(((StageConfig) stageUser).getStageStyle());
			exitPolicy = Objects.requireNonNull(((StageConfig) stageUser).getExitPolicy());
		}

		Stage stage = new Stage(stageStyle);
		this.stages.put(stageUser, stage);

		switch (exitPolicy) {
		case SHUTDOWN_ON_STAGE_EXIT:
			shutdownOnStageExit(stageUser, stage);
			break;
		case STOP_BUNDLE_ON_STAGE_EXIT:
			stopBundleOnStageExit(stageUser, stage);
			break;
		case DO_NOTHING_ON_STAGE_EXIT:
			break;
		default:
			throw new IllegalStateException("Unknown exit polic: " + exitPolicy);
		}

		stageUser.accept(stage);
	}

	private void stopBundleOnStageExit(StageConsumer stageUser, Stage stage) {
		try {
			ShutdownPolicyHandler.stopBundleOnStageExit(stageUser, stage);
		} catch (ClassNotFoundException | NoClassDefFoundError e) {
			// this will happen when OSGi is not on the classpath
		}
	}

	private void shutdownOnStageExit(StageConsumer stageUser, Stage stage) {
		try {
			ShutdownPolicyHandler.shutdownOnStageExit(stageUser, stage);
		} catch (ClassNotFoundException | NoClassDefFoundError e) {
			// this will happen when OSGi is not on the classpath
		}
	}

	private void removeStage(StageConsumer stageUser) {

		Stage stage = this.stages.remove(stageUser);

		if (stage != null) {
			stage.hide();
		}
	}
}
