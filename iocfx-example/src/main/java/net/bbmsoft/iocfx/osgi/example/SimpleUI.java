package net.bbmsoft.iocfx.osgi.example;

import java.net.URL;
import java.util.ResourceBundle;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceScope;

import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import net.bbmsoft.iocfx.StageService;

@Component
public class SimpleUI implements Initializable {

	@Reference(scope = ReferenceScope.PROTOTYPE_REQUIRED)
	private StageService stageService;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		Label label = new Label("Hello IoCFX!\nClose this stage to shut down the application.");
		label.setTextAlignment(TextAlignment.CENTER);
		label.setPrefSize(320, 240);
		label.setAlignment(Pos.CENTER);

		Stage stage = this.stageService.getStage();
		stage.setScene(new Scene(label));
		stage.show();
	}

}
