package net.bbmsoft.iocfx.osgi.example;

import java.io.IOException;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import net.bbmsoft.iocfx.ExitPolicy;
import net.bbmsoft.iocfx.StageConfig;
import net.bbmsoft.iocfx.StageConsumer;
import net.bbmsoft.iocfx.platform.Platform;

@Component
public class CustomizedUI implements StageConsumer, StageConfig {

	@Reference
	private Platform platform;

	@Activate
	public void activate() {
		
		System.out.println(this.getClass().getSimpleName() + " depends on " + Platform.class.getName()
				+ " so it will be activated only when the JavaFX platform is available.");
		System.out.println(
				"Note however that depending on the order in which bundles get activated, the @Activate method will NOT necessarily be called from the JavaFX Application thread! In this case it was called from the "
						+ Thread.currentThread().getName());
	}

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
