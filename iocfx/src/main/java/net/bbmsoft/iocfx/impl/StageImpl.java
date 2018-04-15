package net.bbmsoft.iocfx.impl;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ServiceScope;

import net.bbmsoft.iocfx.ExitPolicy;
import net.bbmsoft.iocfx.Stage;
import net.bbmsoft.iocfx.platform.Platform;


@Component(scope = ServiceScope.PROTOTYPE, service = {javafx.stage.Stage.class, Stage.class})
public class StageImpl extends javafx.stage.Stage implements Stage {
	
	@Reference
	private Platform platform;
	
	@Reference
	private ShutdownPolicyHandler policyHandler;
	
	@Activate
	public void activate() {
		this.initExitPolicy(ExitPolicy.SHUTDOWN_ON_STAGE_EXIT, null);
	}

	@Override
	public void initExitPolicy(ExitPolicy policy, Class<?> bundleClass) {

		switch (policy) {
		case DO_NOTHING_ON_STAGE_EXIT:
			this.policyHandler.doNothingOnStageExit(this);
			break;
		case SHUTDOWN_ON_STAGE_EXIT:
			this.policyHandler.shutdownOnStageExit(this);
			break;
		case STOP_BUNDLE_ON_STAGE_EXIT:
			this.policyHandler.stopBundleOnStageExit(bundleClass, this);
			break;
		default:
			throw new IllegalStateException("Unknown exit policy: " + policy);
		}
	}

}