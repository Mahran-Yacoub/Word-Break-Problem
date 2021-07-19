

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ShowError {

	static Stage stage ; 
	public void showError(String error) {

		try {

			Error.errorMessage = error ;
			Parent root = FXMLLoader.load(getClass().getResource("ErrorMessage.fxml"));
			Scene scene = new Scene(root, 300, 300);
			stage.setScene(scene);
			stage.setTitle("ERROR");
			stage.show();

		} catch (IOException e) {

			e.printStackTrace();
		}

	}

}
