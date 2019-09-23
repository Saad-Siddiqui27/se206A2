package sample;

/**
 * A singleton class which is incorporated in the design to improve the functionality of the program.
 * @author msid633
 *
 */
public class AppManager {
	private static AppManager _instance;

	private String _definition;
	private String _text;

	/*
	 * a private constructor so that it cannot be instantiated from anywhere else.
	 */
	private AppManager() {}

	public static AppManager getInstance() {
		if (_instance == null) {
			_instance = new AppManager();
		}

		return _instance;
	}

	// tODO:  scene transitioning code


	public void saveDefinition(String definition) {
		_definition = definition;
	}

	public String getDefinition() {
		return _definition;
	}


	public void saveText(String text) {
		_text = text;
	}

	public String getText() {
		return _text;
	}


}
