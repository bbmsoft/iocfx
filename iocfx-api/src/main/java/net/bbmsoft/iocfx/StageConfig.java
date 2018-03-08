package net.bbmsoft.iocfx;

import javafx.stage.StageStyle;

public interface StageConfig {

	public ExitPolicy getExitPolicy();
	
	public StageStyle getStageStyle();
}
