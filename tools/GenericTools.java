package tools;

import graphicalUserInterface.DialogHandlerFrame;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import javax.swing.ProgressMonitor;

import translation.Messages;

public class GenericTools {

	public static int modOnDigits(int value, int modulo)
	{
		double buffer = value;
		int result = 0;
		int counter = 0;
		while (buffer >= 1)
		{
			int mod = ((int)buffer) % 10;
			mod %= modulo;
			result += (int) (mod*Math.pow(10, counter));
			counter ++;
			buffer /= 10;
		}
		return result;
	}
	
    public static boolean downloadFile(String address, String pathToWrite)
    {
    	boolean result = true;
        InputStream input = null;
        FileOutputStream writeFile = null;
        try
        {
            URL url = new URL(address);
            URLConnection connection = url.openConnection();
            int fileLength = connection.getContentLength();
            
            if (fileLength == -1)
            {
                DialogHandlerFrame.showErrorDialog(Messages.getString("Version_1_0_3.GenericTools.0")); //$NON-NLS-1$
            	return false;
            }

            ProgressMonitor monitor = new ProgressMonitor(null, Messages.getString("Version_1_0_3.GenericTools.3"), null, 0, fileLength); //$NON-NLS-1$
            int progress = 0;
            
            input = connection.getInputStream();
            writeFile = new FileOutputStream(pathToWrite);
            byte[] buffer = new byte[1024];
            int read;

            while ((read = input.read(buffer)) > 0)
            {
                writeFile.write(buffer, 0, read);
                progress += read;
                monitor.setProgress(progress);
                if (monitor.isCanceled())
                {
                	result = false;
                	break;
                }
            }
            
            writeFile.flush();
            
        }
        catch (IOException e)
        {
        	DialogHandlerFrame.showErrorDialog(Messages.getString("Version_1_0_3.GenericTools.1")+e.getMessage()); //$NON-NLS-1$
        	e.printStackTrace();
        	result = false;
        }
        finally
        {
            try
            {
                writeFile.close();
                input.close();
                
                if (!result)
                {
                	File f = new File(pathToWrite);
                	if (f.exists())
                		f.delete();
                	DialogHandlerFrame.showErrorDialog(Messages.getString("Version_1_0_3.GenericTools.2")); //$NON-NLS-1$
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        return result;
    }
    
}
