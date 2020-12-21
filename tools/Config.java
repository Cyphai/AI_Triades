package tools;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;


abstract public class Config {
	static public final String settingsDirectory =  new File("settings" + File.separatorChar).getAbsolutePath() + File.separator; //$NON-NLS-1$

	static protected String configFilePath;

	private boolean useLocalhost;
	private boolean useLockFile;
	private int port;
	private String mail;
	

	static private Config singleton = null;

	protected Config() {
		this.useLocalhost = true;
		this.useLockFile = false;
		mail = "";
	}
	
	public boolean getUseLocalhost() {
		return useLocalhost;
	}

	public void setUseLocalhost(boolean useLocalhost) {
		this.useLocalhost = useLocalhost;
	}

	public boolean getUseLockFile() {
		return useLockFile;
	}
	

	


	public void setUseLockFile(boolean useLockFile) {
		this.useLockFile = useLockFile;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	abstract protected void valideData();

	public boolean save() {
		try {
			System.out.println("Save config : "  + configFilePath); //$NON-NLS-1$

			createFiles();
			
			XMLEncoder encoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(configFilePath)));
			encoder.writeObject(this);
			encoder.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} 

		return true;
	}

	abstract protected void updateInstance();
	
	static protected Config buildInstance(Class<? extends Config> configClass, String path) {
		if(singleton == null) {
			configFilePath = path;
			File file = new File(configFilePath);
			if(file.exists() == false) {
				try {
					singleton = configClass.newInstance();
					singleton.updateInstance();
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
			} else {
				try {
					XMLDecoder decoder = new XMLDecoder(new BufferedInputStream(new FileInputStream(configFilePath)));
					singleton = (Config)decoder.readObject();
				} catch (Exception e) {
					e.printStackTrace();

					try {
						singleton = configClass.newInstance();
					} catch (Exception e1) {
						e1.printStackTrace();
						return null;
					}
				}
			}
			
			singleton.valideData();
		}
		
		return singleton;
	}
	
	private static void createFiles() {
		//Create config file if not exist
		File file = new File(configFilePath);
		if(file.exists() == false) {
			File directory = new File(file.getParent());
			directory.mkdirs();
			try {
				if(file.createNewFile()) {
					System.out.println("Impossible de créer le fichier de config ! " + configFilePath); //$NON-NLS-1$
				}
			} catch (IOException e) {
				System.out.println("Impossible de créer le fichier de config ! " + configFilePath); //$NON-NLS-1$
				e.printStackTrace();
			}
		}
	}
}
