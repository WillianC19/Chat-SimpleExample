package br.coffea.chat.client.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class ClientConfigFile {

	private static final String CONFIG_FILE = "client_config.txt";
	private static final String PROP_SERVER = "server";
	private static final String PROP_PORT = "port";
	private static final String DEFAULT_SERVER = "localhost";
	private static final int DEFAULT_PORT = 1909;
	private static Properties props;
	
	private ClientConfigFile() throws IOException {}
	
	static {
		props = new Properties();
		File file = new File(CONFIG_FILE);
		
		if (file.exists()) {
			FileInputStream fis = null;
			try {
				fis = new FileInputStream(file);
				props.load(fis);
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (fis != null) {
					try {
						fis.close();
					} catch (IOException e) {
					}
				}
			}
		} else {
			setServer(DEFAULT_SERVER);
			setPort(DEFAULT_PORT);
		}
	}
	
	public static void setServer(String server) {
		props.setProperty(PROP_SERVER, server);
	}
	
	public static void setPort(int port) {
		props.setProperty(PROP_PORT, String.valueOf(port));
	}
	
	public static int getPort() {
		return Integer.parseInt(props.getProperty(PROP_PORT));
	}
	
	public static String getServer() {
		return props.getProperty(PROP_SERVER);
	}
	
	public static void save() throws IOException {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(CONFIG_FILE);
			props.store(fos, null);
		} finally {
			if (fos != null) {
				fos.close();
			}
		}
	}
	
}
