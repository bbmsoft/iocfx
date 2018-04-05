package net.bbmsoft.iocfx.impl;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ServiceScope;

import net.bbmsoft.iocfx.ExitPolicy;
import net.bbmsoft.iocfx.platform.Platform;


@Component(scope = ServiceScope.PROTOTYPE, service = javafx.stage.Stage.class)
public class Stage extends javafx.stage.Stage {

	@Reference
	private Platform platform;
	
	@Activate
	public void activate() {
		this.platform.setExitPolicy(this, ExitPolicy.SHUTDOWN_ON_STAGE_EXIT, getClass());
	}
}