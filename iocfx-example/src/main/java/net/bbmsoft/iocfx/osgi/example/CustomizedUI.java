package net.bbmsoft.iocfx.osgi.example;

import java.net.URL;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceScope;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import net.bbmsoft.iocfx.ExitPolicy;
import net.bbmsoft.iocfx.Fxml;
import net.bbmsoft.iocfx.StageService;

@Component
public class CustomizedUI implements Fxml {

	@Reference(scope = ReferenceScope.PROTOTYPE_REQUIRED)
	private StageService stageService;

	@FXML
	private Region root;

	@FXML
	void initialize() {

		assert this.root != null : "'this.root' has not been properly injected! Please check your FXML file.";

		this.stageService.setExitPolicy(ExitPolicy.DO_NOTHING_ON_STAGE_EXIT);
		Stage stage = this.stageService.getStage();
		stage.setScene(new Scene(this.root));
		stage.show();
	}

	@Override
	public URL getLocation() {
		return this.getClass().getResource("FxmlExample.fxml");
	}

}
