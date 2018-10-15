package net.bbmsoft.iocfx.osgi.example;

import java.net.URL;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceScope;

import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import net.bbmsoft.iocfx.Fxml;
import net.bbmsoft.iocfx.StageService;
import net.bbmsoft.iocfx.StageService.ExitPolicy;

@Component(enabled = false)
public class CustomizedUI implements Fxml.Consumer<Region> {

	@Reference(scope = ReferenceScope.PROTOTYPE_REQUIRED)
	private StageService stageService;

	@Override
	public void accept(Region root) {
		this.stageService.setExitPolicy(ExitPolicy.DO_NOTHING_ON_STAGE_EXIT);
		Stage stage = this.stageService.getStage();
		stage.setScene(new Scene(root));
		stage.show();	
	}
	
	@Override
	public URL getLocation() {
		return this.getClass().getResource("FxmlExample.fxml");
	}

}
