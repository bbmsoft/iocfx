package net.bbmsoft.iocfx.log.impl;

import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;

@Component
public class LoggerCreator {

	@Activate
	public void activate(BundleContext ctx) {

		MinLogger logger;

		try {
			logger = new Slf4jMinLogger();
		} catch (NoClassDefFoundError | ClassNotFoundException e) {
			
			logger = new MinLogger() {

				@Override
				public void info(String message) {
					System.out.println(message);
				}

				@Override
				public void error(String message) {
					System.err.println(message);
				}

				@Override
				public void error(String message, Throwable e) {
					System.err.println(message);
					e.printStackTrace();
				}
			};
		}

		ctx.registerService(MinLogger.class, logger, null);
	}

}
