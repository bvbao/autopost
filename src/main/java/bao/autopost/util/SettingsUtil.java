package bao.autopost.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class SettingsUtil {

	public static final String DEFAULT_USERNAME_KEY = "defaultUsername";

	public static final String DEFAULT_PASSWORD_KEY = "defaultPassword";

	public static final String MAX_TASK = "maxTask";

	public static final String AUTO_SUBMISSION = "autoSubmission";

	public static final String TYPES = "types";

	private static final String SEPARATOR_PATTERN = ", *";

	private static final String FILE_PATH = "settings.properties";

	private static Properties props;

	public static void savePropertiesFile(String[] keys, String[] values) {
		OutputStream output = null;
		try {
			output = new FileOutputStream(FILE_PATH);
			int n = keys.length;
			for (int i = 0; i < n; ++i) {
				props.setProperty(keys[i], values[i]);
			}
			props.store(output, null);
		} catch (IOException io) {
			io.printStackTrace();
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private static Properties loadPropertiesFile() {
		if (props != null) {
			return props;
		}
		props = new Properties();
		InputStream input = null;
		try {
			input = new FileInputStream(FILE_PATH);
			props.load(input);
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
		return props;
	}

	public static String[] getUsernameAndPassword() {
		Properties prop = loadPropertiesFile();
		return new String[] { (String) prop.get(DEFAULT_USERNAME_KEY), (String) prop.get(DEFAULT_PASSWORD_KEY) };
	}

	public static int getMaxThread() {
		return Integer.valueOf((String) loadPropertiesFile().get(MAX_TASK));
	}
	
	public static String getSiteTypes(){
		return (String) loadPropertiesFile().get(TYPES);
	}

	public static boolean getAutoSubmit() {
		return Boolean.valueOf((String) loadPropertiesFile().get(AUTO_SUBMISSION));
	}

	public static String[] getWebsitetypes() {
		Properties prop = loadPropertiesFile();
		String typeString = (String) prop.get(TYPES);
		return typeString.split(SEPARATOR_PATTERN);
	}
}
