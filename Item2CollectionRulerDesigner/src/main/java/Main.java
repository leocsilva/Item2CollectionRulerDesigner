import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {


	public static void main(String[] args) throws Exception {
		launch();

	}
	


	@Override
	public void start(Stage tela) throws Exception {
		URL arquivoFXML = this.getClass().getResource(
			    "tela.fxml");
		Parent fxmlParent = (Parent) FXMLLoader.load(arquivoFXML);
		tela.setScene(new Scene(fxmlParent, 1024, 728));
		tela.setTitle("Item2CollectionRuler Designer");
		tela.show();
	}

	
}
