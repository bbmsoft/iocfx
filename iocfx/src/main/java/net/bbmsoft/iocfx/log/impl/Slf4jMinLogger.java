package net.bbmsoft.iocfx.log.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Slf4jMinLogger implements MinLogger {

	private final Logger log;
	
	public Slf4jMinLogger() throws NoClassDefFoundError, ClassNotFoundException {
		this.log = LoggerFactory.getLogger("IoCFX");
	}

	@Override
	public void info(String message) {
		this.log.info(message);
	}

	@Override
	public void error(String message) {
		this.log.error(message);
	}
	
	@Override
	public void error(String message, Throwable e) {
		this.log.error(message, e);
	}
}
