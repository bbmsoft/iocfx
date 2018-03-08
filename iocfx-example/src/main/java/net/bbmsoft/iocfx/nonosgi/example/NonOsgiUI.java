package net.bbmsoft.iocfx.nonosgi.example;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import net.bbmsoft.iocfx.StageConsumer;
import net.bbmsoft.iocfx.impl.StageConsumerRegistry;

public class NonOsgiUI implements StageConsumer {

	public static void main(String[] args) {
		StageConsumerRegistry.getInstance().addStageUser(new NonOsgiUI());
	}

	@Override
	public void accept(Stage stage) {

		Label label = new Label("Hello IoCFX!\n " + "Closing this stage has no effect.\n"
				+ "Shutdown policies are only supported in OSGi\n" + "environments. Register a WindowEvent\n"
				+ "handler on the stage to implement your\n" + "custom behavior when the stage is closed.");
		label.setTextAlignment(TextAlignment.CENTER);
		label.setPrefSize(640, 480);
		label.setAlignment(Pos.CENTER);
		stage.setScene(new Scene(label));
		stage.show();
	}
}
