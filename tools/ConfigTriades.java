package tools;

import java.io.File;
import java.util.Locale;

import client.export.ExportImageData;

public class ConfigTriades extends Config {

	public static int defaultPort = 24085;
	

	public Locale usedLocale;
	public String localeCode;

	private LastObjectUsed<ExportImageData> lastImages;
	
	public ConfigTriades()
	{

		lastImages = new LastObjectUsed<ExportImageData>();
		setPort(defaultPort);
	}
	

	public LastObjectUsed<ExportImageData> getLastImages() {
		return lastImages;
	}

	public void setLastImages(LastObjectUsed<ExportImageData> lastImages) {
		this.lastImages = lastImages;
	}

	@Override
	protected void valideData() {
		for (int i = 0 ; i < lastImages.getLastObjects().size();) {
			ExportImageData data = lastImages.getLastObjects().elementAt(i);
			boolean removeData = false;
			if(data != null) {
				if(data.getImageIndex() < 0) {
					String path = data.getImagePath();
					if(path != null) {
						File file = new File(path);
						if(file.exists() == false) {
							removeData = true;
						}
					} else {
						removeData = true;
					}
				} 
			} else {
				removeData = true;
			}
			
			if(removeData) {
				lastImages.getLastObjects().remove(i);
			} else {
				i++;
			}
		}
	}
	

	
	static public ConfigTriades getInstance() {
		return (ConfigTriades)Config.buildInstance(ConfigTriades.class, settingsDirectory + "configTriades"); //$NON-NLS-1$
	}

	@Override
	protected void updateInstance() {
		
	}
	
	public Locale getUsedLocale()
	{
		if (usedLocale == null)
		{
			if (localeCode != null && !localeCode.isEmpty())
			{
				usedLocale = new Locale(localeCode);
			}
			else
			{
				usedLocale = Locale.getDefault();
				localeCode = usedLocale.getLanguage();
			}
		}
			return usedLocale;
	}
	
	public void setUsedLocale(Locale newLocale)
	{
		usedLocale = newLocale;
		localeCode = usedLocale.getLanguage();
	}

	public void setLocaleCode(String code)
	{
		localeCode = code;
		usedLocale = new Locale(code);
	}
	
	public String getLocaleCode()
	{
		return localeCode;
	}

}
