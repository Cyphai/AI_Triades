package tools;

import java.io.File;

public class ConfigCreator extends Config {

	public static int defaultPort = 58201;
	
	private LastObjectUsed<String> lastDatapack;
	
	public ConfigCreator() {
		lastDatapack = new LastObjectUsed<String>();
		setPort(defaultPort);
	}
	
	public LastObjectUsed<String> getLastDatapack() {
		return lastDatapack;
	}

	public void setLastDatapack(LastObjectUsed<String> lastDatapack) {
		this.lastDatapack = lastDatapack;
	}

	@Override
	protected void valideData() {
		for(int i = 0 ; i < lastDatapack.getLastObjects().size();) {
			boolean removePath = false;
			String path = lastDatapack.getLastObjects().elementAt(i);
			
			if(path != null) {
				File file = new File(path);
				if(file.exists() == false) {
					removePath = true;
				}
			} else {
				removePath = true;
			}
			
			if(removePath) {
				lastDatapack.getLastObjects().remove(i);
			} else {
				i++;
			}
		}
	}
	
	static public ConfigCreator getInstance() {
		return (ConfigCreator)Config.buildInstance(ConfigCreator.class, settingsDirectory + "configCreator"); //$NON-NLS-1$
	}

	@Override
	protected void updateInstance() {
		
	}
}
