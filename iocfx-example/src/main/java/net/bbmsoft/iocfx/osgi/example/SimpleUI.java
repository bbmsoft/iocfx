package net.bbmsoft.iocfx.osgi.example;

import org.osgi.service.component.annotations.Component;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import net.bbmsoft.iocfx.StageConsumer;

@Component
public class SimpleUI implements StageConsumer {

	@Override
	public void accept(Stage stage) {
		
		Label label = new Label("Hello IoCFX!\nClose this stage to shut down the application.");
		label.setTextAlignment(TextAlignment.CENTER);
		label.setPrefSize(320, 240);
		label.setAlignment(Pos.CENTER);
		stage.setScene(new Scene(label));
		stage.show();
	}

}
