package net.bbmsoft.iocfx.log.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Slf4jMinLogger implements MinLogger {

	private final Logger log = LoggerFactory.getLogger("IoCFX");
	
	public Slf4jMinLogger() throws NoClassDefFoundError {
	}

	@Override
	public void info(String message) {
		log.info(message);
	}

	@Override
	public void error(String message) {
		log.error(message);
	}
}
