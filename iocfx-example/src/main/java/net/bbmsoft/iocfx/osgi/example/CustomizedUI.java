package net.bbmsoft.iocfx.osgi.example;

import java.io.IOException;

import org.osgi.service.component.annotations.Component;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import net.bbmsoft.iocfx.ExitPolicy;
import net.bbmsoft.iocfx.StageConfig;
import net.bbmsoft.iocfx.StageConsumer;

@Component
public class CustomizedUI implements StageConsumer, StageConfig {

	@Override
	public void accept(Stage stage) {

		Region root;
		
		try {
			root = new FXMLLoader(this.getClass().getResource("FxmlExample.fxml")).load();
		} catch (IOException e) {
			e.printStackTrace();
			root = new Label("Could not load FXML.");
		}
		
		stage.setScene(new Scene(root));
		stage.show();
	}

	@Override
	public ExitPolicy getExitPolicy() {
		return ExitPolicy.DO_NOTHING_ON_STAGE_EXIT;
	}

	@Override
	public StageStyle getStageStyle() {
		return StageStyle.UNIFIED;
	}

}
