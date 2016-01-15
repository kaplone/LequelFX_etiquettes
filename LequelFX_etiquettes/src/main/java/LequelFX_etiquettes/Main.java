package LequelFX_etiquettes;

	
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			Scene scene = new Scene((Parent) JfxUtils.loadFxml("GUI_etiquettes.fxml"), 800, 600);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			
			primaryStage.getIcons().add(new Image(getClass().getResource("LequelFX_etiquette_01.png").toExternalForm()));
			primaryStage.setTitle("LequelFX_etiquettes");
			
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
