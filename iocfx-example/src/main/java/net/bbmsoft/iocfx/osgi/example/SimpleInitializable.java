package net.bbmsoft.iocfx.osgi.example;

import java.net.URL;
import java.util.ResourceBundle;

import org.osgi.service.component.annotations.Component;

import javafx.fxml.Initializable;

@Component(enabled = true)
public class SimpleInitializable implements Initializable {

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		System.out.println("Hello from " + Thread.currentThread().getName());
	}

}
