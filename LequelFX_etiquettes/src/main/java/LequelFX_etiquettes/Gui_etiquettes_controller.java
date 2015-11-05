package LequelFX_etiquettes;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;

public class Gui_etiquettes_controller implements Initializable {
	
	@FXML
	private ChoiceBox<String> liste_disques_choiceBox;
	@FXML
	private Button rafraichir_button;
	@FXML
	private Button etiquette_button;
	
	private ObservableList<String> collec_disques;
	private Path chemin_du_disque;
	private Map<Integer, ArrayList<String>> listes;
	
	@FXML
	public void on_etiquette_button(){
		
		Path p  = Paths.get(liste_disques_choiceBox.getSelectionModel().getSelectedItem());
		
		System.out.println(chemin_du_disque.resolve(p).toString());
		
		listes = FreeMarkerMaker.odt2pdf(chemin_du_disque.resolve(p), listes);
	}
	
	@FXML
	public void on_rafraichir_button(){
		refreshList();
	}
	
    private void refreshList(){
		
		collec_disques.clear();
		
		collec_disques.add("choisir un disque");
		
		if (new File("/Volumes").isDirectory()){
			collec_disques.addAll(new File("/Volumes").list());
			chemin_du_disque = Paths.get("/Volumes");
		}
		else if(new File("/run/media/autor").isDirectory() && new File("/run/media/autor").list().length > 0) {
			collec_disques.addAll(new File("/run/media/autor").list());
			chemin_du_disque = Paths.get("/run/media/autor");
		}
		else{
			collec_disques.addAll(new File("/mnt").list());
			chemin_du_disque = Paths.get("/mnt");
		}
		
		
		liste_disques_choiceBox.getSelectionModel().select(0);
	}

	public void initialize(URL location, ResourceBundle resources) {
		
		collec_disques = FXCollections.observableArrayList();
		
		listes = new HashMap<Integer, ArrayList<String>>();
		for (int i=0; i < 10; i++){
			listes.put(i, new ArrayList<String>());
		}
		
		liste_disques_choiceBox.setItems(collec_disques);
		refreshList();

		
	}

}
