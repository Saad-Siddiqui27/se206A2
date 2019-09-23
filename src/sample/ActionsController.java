package sample;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * This class has the functionality to Play and Delete the creations. it also Lists the creations.
 * @author msid633
 *
 */

public class ActionsController {

	@FXML
	private AnchorPane _playPane;

	@FXML
	private ListView<String> _listView;


	Bash pro;


	/**
	 * play the Video that is selected form the list.
	 */

	public void Play(){
		try{

			String topics; 
			topics = _listView.getSelectionModel().getSelectedItem();



			pro.probash("ffplay -autoexit "+"./Creations/"+topics+".mkv 2> /dev/null");



		} catch(Exception e)

		{
			e.printStackTrace();
		}
	}


	/**
	 * This method deletes the existing creations.
	 * it also prompts the user if they really want to delete the creation.
	 */
	public void delete(){

		String term; 
		term = _listView.getSelectionModel().getSelectedItem();
		if(term!=null) {
			Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
			alert.setContentText("Are you sure that you want to delete the creation?");
			alert.setTitle("Delete the creation?");

			alert.showAndWait().ifPresent(response -> {
				if (response == ButtonType.OK) {

					try{

						String topics; 
						topics = _listView.getSelectionModel().getSelectedItem();
						pro.probash("rm "+ "./Creations/"+ topics+".mkv");

						initialize();



					} catch(Exception e)

					{
						e.printStackTrace();
					}


				}
			});
		}



	}



	/**
	 * switches the scene to the Main menu. this method is linked to the Main Menu method. It uses the switch scenes method.
	 */
	public void MainMenu () {
		try{
			switchScenes("MainMenu.fxml");
		} catch(Exception e)

		{
			e.printStackTrace();
		}
	}

	@FXML
	public void initialize() {

		_listView.getItems().clear();
		pro = new Bash();
		pro.probash("ls ./Creations/*.mkv 2> /dev/null");
		List<String> str = pro.getStd();

		for (int i = 0; i < str.size(); i++) {

			_listView.getItems().add(str.get(i).substring(12,str.get(i).length()-4));

		}
		_listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);


	}



	/**
	 * This Method provides the functionality of switching the scenes from the current to the next one.
	 * @param fxml this parameter is the scene to switch to.
	 * @throws IOException
	 */
	public void switchScenes(String fxml) throws IOException {

		Parent pane = FXMLLoader.load(getClass().getResource(fxml));
		Stage stage = (Stage) _playPane.getScene().getWindow();


		Scene scene = new Scene(pane);
		stage.setScene(scene);
		stage.sizeToScene();
	}


	public void playHandle() {

		Thread thread = new Thread(new Background());
		thread.start();

	}



	/**
	 * This is non Static inner class which extends task to support multi threading.
	 * It overrides the call method which 
	 * @author msid633
	 *
	 */

	private class Background extends Task<Void>{

		@Override
		protected Void call() throws Exception {
			Play();

			return null;
		}


	}





}
