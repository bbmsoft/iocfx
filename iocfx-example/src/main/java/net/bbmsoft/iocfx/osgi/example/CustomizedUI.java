package net.bbmsoft.iocfx.osgi.example;

import java.net.URL;
import java.util.ResourceBundle;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceScope;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import net.bbmsoft.iocfx.ExitPolicy;
import net.bbmsoft.iocfx.Stage;
import net.bbmsoft.iocfx.fxml.Fxml;

@Component
public class CustomizedUI implements Fxml, Initializable {

	@FXML
	private Region root;
	
	@Reference(scope = ReferenceScope.PROTOTYPE_REQUIRED)
	private Stage stage;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.stage.initExitPolicy(ExitPolicy.DO_NOTHING_ON_STAGE_EXIT, getClass());
		this.stage.setScene(new Scene(this.root));
		this.stage.show();
	}

	@Override
	public URL getLocation() {
		return this.getClass().getResource("FxmlExample.fxml");
	}

}
