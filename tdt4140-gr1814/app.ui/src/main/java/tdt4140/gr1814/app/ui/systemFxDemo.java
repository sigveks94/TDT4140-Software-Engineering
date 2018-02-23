package tdt4140.gr1814.app.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class systemFxDemo extends Application{

	public void onChangeButtonAction(event e){
		  Node node=(Node) event.getSource();
		  Stage stage=(Stage) node.getScene().getWindow();
		  Parent root = FXMLLoader.load(getClass().getResource("second.fxml"));/* Exception */
		  Scene scene = new Scene(root);
		  stage.setScene(scene);
		  stage.show();
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		
	}
}


