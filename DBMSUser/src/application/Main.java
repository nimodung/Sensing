package application;
	

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) throws Exception{
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
		Parent root = loader.load();
		
		LoginController controller = loader.getController(); //root.fxml에 적용된 컨트롤러 객체를 넣어주는 것
		controller.setPrimaryStage(primaryStage);
		
		
		Scene scene = new Scene(root);
		
		primaryStage.setScene(scene);
		primaryStage.show();
		
		
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
