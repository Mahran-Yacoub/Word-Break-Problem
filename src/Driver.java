

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Driver extends Application {

	static Stage mainStage;
	static Stage tableStage ;

	@Override
	public void start(Stage primaryStage) throws Exception {

		ShowError.stage = new Stage();
		
		Parent root = FXMLLoader.load(getClass().getResource("GUI.fxml"));
		Scene scene = new Scene(root, 680, 490);
		primaryStage.setScene(scene);
		primaryStage.setTitle("WORD BREAK PROBLEM");
		mainStage = primaryStage;
		tableStage = new Stage();
		primaryStage.show();
		
	}

	public static void main(String[] args) {

		Application.launch(args);

	}
}
