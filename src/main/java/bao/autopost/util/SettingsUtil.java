package bao.autopost.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class SettingsUtil {

	private static final String FILE_PATH = "settings.properties";

	private static final String DEFAULT_USERNAME_KEY = "defaultUsername";

	private static final String DEFAULT_PASSWORD_KEY = "defaultPassword";
	
	private static final String MAX_TASK = "maxTask";
	
	private static final String TYPES = "types";
	
	private static final String SEPARATOR = ", *";

	// private void savePropertiesFile(String[] keys, String[] values) {
	// Properties prop = new Properties();
	// OutputStream output = null;
	// try {
	// output = new FileOutputStream(FILE_PATH);
	// int n = keys.length;
	// for (int i = 0; i < n; ++i) {
	// prop.setProperty(keys[i], values[i]);
	// }
	// prop.store(output, null);
	// } catch (IOException io) {
	// io.printStackTrace();
	// } finally {
	// if (output != null) {
	// try {
	// output.close();
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// }
	// }
	// }

	private static Properties loadPropertiesFile() {
		Properties prop = new Properties();
		InputStream input = null;
		try {
			input = new FileInputStream(FILE_PATH);
			prop.load(input);
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return prop;
	}

	public static String[] getUsernameAndPassword() {
		Properties prop = loadPropertiesFile();
		return new String[] { (String) prop.get(DEFAULT_USERNAME_KEY), (String) prop.get(DEFAULT_PASSWORD_KEY) };
	}

	public static int getMaxThread() {
		return Integer.valueOf((String)loadPropertiesFile().get(MAX_TASK));
	}
	
	public static String[] getWebsitetypes(){
		Properties prop = loadPropertiesFile();
		String typeString = (String)prop.get(TYPES);
		return typeString.split(SEPARATOR);		
	}
}
