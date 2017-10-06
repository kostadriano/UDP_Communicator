package View;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

public class MainApp extends Application {

	private static final BorderPane root = new BorderPane();

	public void start(Stage primaryStage) {
		try {
			AnchorPane pane = FXMLLoader.load(getClass().getResource("Interface.fxml"));
			root.setCenter(pane);

			Scene scene = new Scene(root, 608, 419);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
