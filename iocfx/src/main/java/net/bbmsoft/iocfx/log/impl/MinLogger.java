package net.bbmsoft.iocfx.log.impl;

public interface MinLogger {

	public void info(String message);
	
	public void error(String message);

	public void error(String message, Throwable e);
}
