package translation;

import graphicalUserInterface.DialogHandlerFrame;
import graphicalUserInterface.Program;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import tools.ConfigCreator;
import tools.ConfigTriades;



public class Messages {
	/*** strings files names ***/

	private static ArrayList<Locale> availableLocales;

	/*** FR ***/
	private static final ResourceBundle RESOURCE_CREATOR = ResourceBundle
			.getBundle("translation/messages_creator");

	/*** EN ***/

	/*** Current language set ***/
	private static ResourceBundle RESOURCE_TRIADE = null;
	private static ResourceBundle DEFAULT_RESOURCE_TRIADE = ResourceBundle
			.getBundle("translation/messages"); 

	private static void initBundle(){
		Locale usedLocale = null;
		if (Program.isTriades() || Program.isTriadesLoading())
			usedLocale = ConfigTriades.getInstance().getUsedLocale();
		else
			usedLocale = Locale.FRENCH;
		if (usedLocale == null)
			usedLocale = Locale.getDefault();


		try {

			URL url = new File("language").toURI().toURL();
			URL[] urls = new URL[]{url};


			RESOURCE_TRIADE = ResourceBundle.getBundle("messages", usedLocale, new URLClassLoader(urls));
		} catch (Exception e) {
			System.err.println("Erreur lors du chargement de la traduction, chargement du fichier par défaut.");
			e.printStackTrace();
			RESOURCE_TRIADE = DEFAULT_RESOURCE_TRIADE;
		} 

	}


	private Messages() {
	}

	public static ArrayList<Locale> getAvailableLocales()
	{
		if (availableLocales == null)
		{
			availableLocales = new ArrayList<Locale>();
			try{	  
				File folder = new File("language");
				File[] fileList = folder.listFiles(); 
				for (File f : fileList)
				{
					if (f.isFile())
					{
						if (f.getName().startsWith("messages_") && f.getName().endsWith(".properties"))
						{
							String name = f.getName().substring(9, f.getName().length()-11);
							Locale locale;
							if (name.equals("fr"))
								locale = Locale.FRENCH;
							else if (name.equals("en"))
								locale = Locale.ENGLISH;
							else if (name.equals("it"))
									locale = Locale.ITALIAN;
							else
								locale = new Locale(name);
							availableLocales.add(locale);
							System.out.println("Messages.getAvailableLocales : added locale : "+locale.getDisplayName()+" ("+name+"-"+locale.getISO3Country()+")");
						}
						else
						{
							System.err.println("Messages.getAvailableLocale : unrecognized translation file"); //TODEL
						}
					}
				}
			}
			catch(Exception e)
			{
				JOptionPane.showMessageDialog(null, "Error during language ressources loading, try to reinstall the software.");
				System.exit(-1);
			}
		}
		return availableLocales;
	}

	public static String getString(String key) {
		if (RESOURCE_TRIADE == null)
			initBundle();
		try {
			return RESOURCE_TRIADE.getString(key);
		} catch (MissingResourceException e) {
			try{
				String result = DEFAULT_RESOURCE_TRIADE.getString(key);
				System.err.println("Warning, missing ressource in used translation, value for key : "+key+", french translation : "+result);
				return result;
			}
			catch(MissingResourceException e2)
			{
				if (!Program.isTriades())
				{
					try {
						return RESOURCE_CREATOR.getString(key);
					} catch (MissingResourceException er) {
						System.err.println("Phrase manquante dans les deux fichiers, clé : "+key);
						return '!' + key + '!';
					}
				}
				System.err.println("Phrase manquante dans la trad par défaut de triade : "+key);
				//e.printStackTrace();
				return '!' + key + '!';
			}
		}
	}

		public static void checkAllTriadesMessages() {
			boolean ok = true;
			for(Enumeration<String> iterator = DEFAULT_RESOURCE_TRIADE.getKeys() ; iterator.hasMoreElements() ; ) {
				String key = iterator.nextElement();
				try {
					RESOURCE_TRIADE.getString(key);
				} catch (MissingResourceException e) {
					System.out.println(key + "=" + RESOURCE_TRIADE.getString(key));
					ok = false;
				}
			}
	
			if(ok == false) {
				DialogHandlerFrame.showErrorDialog("Strings manquantes dans la traduction anglaise de Triades !");
			}
		}
}
