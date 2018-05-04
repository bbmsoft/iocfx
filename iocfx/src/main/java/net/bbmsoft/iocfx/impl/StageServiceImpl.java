package net.bbmsoft.iocfx.impl;

import java.util.concurrent.atomic.AtomicReference;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ServiceScope;

import javafx.stage.Stage;
import net.bbmsoft.iocfx.Platform;
import net.bbmsoft.iocfx.StageService;

@Component(scope = ServiceScope.PROTOTYPE)
public class StageServiceImpl implements StageService {

	private final AtomicReference<Stage> stage;

	@Reference
	private Platform platform;

	@Reference
	private ShutdownPolicyHandler policyHandler;

	public StageServiceImpl() {
		this.stage = new AtomicReference<Stage>();
	}

	@Override
	public Stage getStage() {
		return this.stage.get();
	}

	@Activate
	public void activate() throws InterruptedException {
		this.platform.runAndWait(this::createStage);
		this.setExitPolicy(ExitPolicy.SHUTDOWN_ON_STAGE_EXIT);
	}

	@Deactivate
	public void deactivate() {
		this.platform.runOnFxApplicationThread(this.stage.get()::close);
		this.setExitPolicy(ExitPolicy.DO_NOTHING_ON_STAGE_EXIT);
	}

	@Override
	public void setExitPolicy(ExitPolicy policy, Class<?>... bundleClasses) {

		switch (policy) {
		case DO_NOTHING_ON_STAGE_EXIT:
			this.policyHandler.doNothingOnStageExit(this.getStage());
			break;
		case SHUTDOWN_ON_STAGE_EXIT:
			this.policyHandler.shutdownOnStageExit(this.getStage());
			break;
		case STOP_BUNDLE_ON_STAGE_EXIT:
			this.policyHandler.stopBundleOnStageExit(this.getStage(), bundleClasses);
			break;
		default:
			throw new IllegalStateException("Unknown exit policy: " + policy);
		}
	}

	private void createStage() {
		this.stage.set(new Stage());
	}

}