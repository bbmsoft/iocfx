package net.bbmsoft.iocfx.osgi.example;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import net.bbmsoft.iocfx.Fxml;
import net.bbmsoft.iocfx.Platform;

@Component(enabled = true)
public class FxmlApplicationUI implements Fxml.Application, Fxml.Controller {

	@Reference
	private Platform platform;
	
	@Override
	public void prepareStage(Stage primaryStage) {
		primaryStage.initStyle(StageStyle.UNDECORATED);
	}

	@FXML
	void quit() {
		this.platform.exit();
	}

}
