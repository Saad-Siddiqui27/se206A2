package sample;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * the Controller to control the main scene of the GUI.
 */

public class MainController implements Initializable {

	@FXML
	private AnchorPane _rootPane;

	@FXML
	private TextField textField;


	/**
	 * This Method searches for the term using by passing the wikit command into a process builder.
	 */

	public void create() {


		String term = textField.getText();



		if(term.isEmpty()) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setContentText("Please enter a valid term that you want to search");
			alert.setTitle("Enpty term name");

			alert.showAndWait().ifPresent(response -> {
				if (response == ButtonType.OK) {

				}
			});
		}
		else {


			try{



				String command =  "wikit "+ term +"| sed  's/[.!?] */&\\n/g' " ; 
				ProcessBuilder pb = new ProcessBuilder("bash", "-c", command);

				Process process = pb.start();

				BufferedReader stdout = new BufferedReader(new InputStreamReader(process.getInputStream()));
				BufferedReader stderr = new BufferedReader(new InputStreamReader(process.getErrorStream()));


				int exitStatus = process.waitFor();

				StringBuilder sb = new StringBuilder();

				if (exitStatus == 0) {
					String line;
					int count = 1;
					while ((line = stdout.readLine()) != null) {

						sb.append(count++ + ". "+line).append("\n");
					}

				} else {
					String line;
					while ((line = stderr.readLine()) != null) {
						System.err.println(line);
					}
				}

				AppManager.getInstance().saveDefinition(sb.toString());
				AppManager.getInstance().saveText(term.toString());

				//switchScenes("Create.fxml");


				stdout.close();
				stderr.close();



			} catch(Exception e)

			{
				e.printStackTrace();
			}
		}
	}



	public void loadNext () {
		try {
			switchScenes("Create.fxml");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	/**
	 * switches the scene to view the creations.
	 */
	public void List () {
		try {

			switchScenes("Actions.fxml");

		} catch(Exception e)

		{
			e.printStackTrace();
		}
	}


	public void handle() {
		Thread thread = new Thread(new Background());
		thread.start();
	}






	/**
	 * This Method provides the functionality of switching the scenes from the current to the next one.
	 * @param fxml this parameter is the scene to switch to.
	 * @throws IOException
	 */

	public void switchScenes(String fxml) throws IOException {

		Parent pane = FXMLLoader.load(getClass().getResource(fxml));
		Stage stage = (Stage) _rootPane.getScene().getWindow();

		Scene scene = new Scene(pane);
		stage.setScene(scene);
		stage.sizeToScene();
	}










	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub

	}



	/**
	 * This is non Static inner class which extends task to support multi-threading.
	 * It overrides the call method which 
	 * @author msid633
	 *
	 */

	private class Background extends Task<Void>{

		@Override
		protected Void call() throws Exception {
			create();

			return null;
		}

		@Override
		protected void done() {
			Platform.runLater(() -> {
				loadNext();
			});
		}

	}



}
