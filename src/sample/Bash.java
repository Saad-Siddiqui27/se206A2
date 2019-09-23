package sample;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * A Class to process the Bash commands.
 * @author msid633
 *
 */
public class Bash {


	private List<String> _stdOut = new ArrayList<String>(); 


	/**
	 * This method gives the functionality of processing the bash commands.
	 * @param command. This parameter is the Bash command that we want to process.
	 */
	public void  probash(String command) {

		try {

			ProcessBuilder pb = new ProcessBuilder("bash", "-c", command);

			Process process = pb.start();

			BufferedReader stdout = new BufferedReader(new InputStreamReader(process.getInputStream()));
			BufferedReader stderr = new BufferedReader(new InputStreamReader(process.getErrorStream()));




			int exitStatus = process.waitFor();

			if (exitStatus == 0) {
				String line;
				while ((line = stdout.readLine()) != null) {

					_stdOut.add(line);

				}

			} else {
				String line;
				while ((line = stderr.readLine()) != null) {
					System.err.println(line);
				}
			}


			stdout.close();
			stderr.close();

		} catch(Exception e)

		{
			e.printStackTrace();
		}
	}

	/**
	 * this is a getter method
	 * @return Returns the output that is read by java from the bash output.
	 */

	public List<String> getStd() {
		return _stdOut;
	}


}

