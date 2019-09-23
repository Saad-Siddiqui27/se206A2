package sample;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

/**
 * This class provides the functionality of creating the audio, video and the final creation.
 * @author msid633
 *
 */
public class CreateController {

	@FXML
	private Pane _create;

	@FXML
	private TextArea _tArea;

	@FXML
	private TextField _tField2;

	@FXML
	private TextField _textField4;


	/**
	 * A bunch of files that are used in the creation of the audio.was, video, and the merged files.
	 */

	File file = new File("file");
	File file2 = new File("file2");
	File file3 = new File("audio.wav");
	File file5 = new File("video.mp4");
	File file4 = file3;




	Bash process = new Bash();

	/**
	 * A method that is linked to the Create button on the Create Scene.
	 * it Creates the audio and video files and merges them to create the creation for the user.
	 */
	public void create2() {




		int lines = Integer.parseInt(_tField2.getText());
		int a = _tArea.getText().split("\n").length;



		if(lines<1 || lines>a) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setContentText("Please enter a range between the number of lines provided");
			alert.setTitle("invalid number of lines");

			alert.showAndWait().ifPresent(response -> {
				if (response == ButtonType.OK) {

				}
			});
		}

		else {
			String term = (AppManager.getInstance().getText());


			process.probash("wikit "+term+" | sed  's/[.!?] */&\\n/g' >"+file );

			process.probash("head -n "+lines+ " "+ file +">"+ file2);

			process.probash("espeak -f "+file2+" -w audio.wav");
			process.probash("length=`soxi -D "+file3+"`");


			try {
				AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file4);
				AudioFormat format = audioInputStream.getFormat();
				long frames = audioInputStream.getFrameLength();
				double durationInSeconds = (frames+0.0) / format.getFrameRate();  

				String cmd = "ffmpeg -y -f lavfi -i color=c=blue:s=320x240:d="+durationInSeconds+" -vf "+"\"drawtext=fontfile=caveman.ttf:fontsize=30: fontcolor=white:x=(w-text_w)/2:y=(h-text_h)/2:text='"+term+"'\" "+ file5;


				process.probash(cmd);
			}catch(Exception e) {
				e.printStackTrace();
			}


			String userText = _textField4.getText();



			/**
			 * check if the user hasn't input anything or has space in between their creation name or any other invalid characters.
			 */

			Pattern pattern = Pattern.compile("[\\s~#@*+%{}<>\\\\]");
			Matcher matcher = pattern.matcher(userText);
			boolean found = matcher.find();

			if (found || userText.isEmpty() )
			{
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				alert.setContentText("Please enter a valid name of your creation");
				alert.setTitle("invalid creation name");

				alert.showAndWait().ifPresent(response -> {
					if (response == ButtonType.OK) {

					}
				});

			}
			else if(new File("./Creations/", userText+".mkv").exists()){


				Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
				alert.setContentText("File already Exists. Do you want to OverWrite it?");
				alert.setTitle("File already exists");

				//check here
				alert.showAndWait().ifPresent(response -> {
					if (response == ButtonType.OK) {

						creation();


					}
				});



			}
			else {
				creation();

			}
		}
	}


	/**
	 * A method which is called by the creation2 method. it reduces code duplication by providing the functionality of merging the audio and video files in different places
	 * of the creation2 method. this is done because of error checking
	 */
	public void creation() {

		String cmd2 = "ffmpeg -i "+file5 +" -i "+ file4+" "+ _textField4.getText()+".mkv 2> /dev/null";
		process.probash(cmd2);

		File creations = new File("Creations");

		if(! creations.exists()) {
			process.probash("mkdir Creations");
		}


		process.probash("mv "+_textField4.getText()+".mkv "+creations);

		file.delete();
		file2.delete();
		file3.delete();
		file4.delete();
		file5.delete();


		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setContentText("Your Creation has been Created");
		alert.setTitle("Processing done");


		alert.showAndWait().ifPresent(response -> {
			if (response == ButtonType.OK) {


			}
		});



	}




	//	public void createHandle() {
	//		Thread thread = new Thread(new Background()) ;
	//		thread.start();
	//	}



	/**
	 * this Method enables the Main Menu button to switch back to the Main Menu from the Create Scene.
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

		_tArea.setText(AppManager.getInstance().getDefinition());

	}


	/**
	 * This Method provides the functionality of switching the scenes from the current to the next one.
	 * @param fxml this parameter is the scene to switch to.
	 * @throws IOException
	 */
	public void switchScenes(String fxml) throws IOException {

		Parent pane = FXMLLoader.load(getClass().getResource(fxml));
		Stage stage = (Stage) _create.getScene().getWindow();

		Scene scene = new Scene(pane);
		stage.setScene(scene);
		stage.sizeToScene();
	}




	//	private class Background extends Task<Void>{
	//
	//		@Override
	//		protected Void call() throws Exception {
	//			create2();
	//
	//			return null;
	//		}
	//
	//	}



}
