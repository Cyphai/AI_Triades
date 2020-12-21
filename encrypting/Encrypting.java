package encrypting;

import graphicalUserInterface.DialogHandlerFrame;
import graphicalUserInterface.ExecutionMode;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import translation.Messages;

public class Encrypting {
	static public final int keySize = 16;
	static public final String algorithm = "AES"; //$NON-NLS-1$

	private static Encrypting singleton = null;
	private SecretKeySpec currentKey;

	private Encrypting() {
		setProgramKey();
	}

	public boolean saveEncryptedObject(Object object, String path) {
		return saveEncryptedObject(object, path, currentKey);
	}

	public boolean saveEncryptedObject(Object object, String path, SecretKeySpec key) {
		try {
			System.out.println("Save crypted : " + path + " (" + object + ")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			Cipher cipher = Cipher.getInstance(algorithm);
			cipher.init(Cipher.ENCRYPT_MODE, key);

			//convert object to byte[]
			ByteArrayOutputStream bStream = new ByteArrayOutputStream();
			ObjectOutputStream oStream = new ObjectOutputStream(bStream);
			oStream.writeObject(object);
			byte[] clearObject = bStream.toByteArray();

			byte[] cryptedObject = cipher.doFinal(clearObject);
			File tmpFile = File.createTempFile("temp", "tmp"); //$NON-NLS-1$ //$NON-NLS-2$

			OutputStream oos = new BufferedOutputStream(new FileOutputStream(tmpFile));
			oos.write(cryptedObject);
			oos.close();

			return renameFile(tmpFile, new File(path));
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("Impossible de sauver " + object + " dans le fichier " + path); //$NON-NLS-1$ //$NON-NLS-2$
		return false;
	}

	public static boolean renameFile(File source, File dest){
		if(source.exists() == false) {
			System.out.println("Source n'existe pas : " + source); //$NON-NLS-1$
			return false;
		}

		if(dest.exists()) {
			if(dest.delete() == false) {
				System.out.println("Impossible de supprimer " + dest); //$NON-NLS-1$
				return false;
			}
		}

		if(source.renameTo(dest) == false) {
			if(copyFile(source, dest) == false) {
				System.out.println("Impossible de renomer le fichier \"" + source.getAbsolutePath() + "\" en \"" + dest.getAbsolutePath() + "\""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				return false;
			}
		}

		return true;
	}

	private static boolean copyFile(File source, File dest) {
		try{
			// Declaration et ouverture des flux
			java.io.FileInputStream sourceFile = new java.io.FileInputStream(source);

			try{
				java.io.FileOutputStream destinationFile = null;

				try{
					destinationFile = new FileOutputStream(dest);

					// Lecture par segment de 0.5Mo 
					byte buffer[] = new byte[512 * 1024];
					int nbLecture;

					while ((nbLecture = sourceFile.read(buffer)) != -1){
						destinationFile.write(buffer, 0, nbLecture);
					}
				} finally {
					destinationFile.close();
				}
			} finally {
				sourceFile.close();
			}
		} catch (IOException e){
			e.printStackTrace();
			System.out.println("Impossible de copier le fichier " + source + " vers " + dest); //$NON-NLS-1$ //$NON-NLS-2$
			return false; // Erreur
		}

		return true; // Résultat OK  
	}

	public Object loadEncryptedObject(String path) {
		try {
			if(path.contains(".dte")) {
				return loadEncryptedObject(path, Key.getProgramKey());
			} else {
				return loadEncryptedObject(path, currentKey);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private Object loadEncryptedObject(String path, SecretKeySpec key) {
		System.out.println("load crypted : " + path); //$NON-NLS-1$
		Exception exception = null;
		try {
			Object result = loadEncryptedObjectInter(path, key);
			setKey(key);
			return result;
		} catch (Exception e) {
			if(ExecutionMode.isIntern() && path.contains(".dte")) {
				try {
					
//					DialogHandlerFrame.showInformationDialog("Code commenté pour eviter l'inclusion de KeyChooserView");
					SecretKeySpec userKey = KeyChooserView.createView();
					Object result = loadEncryptedObjectInter(path, userKey);
					Encrypting.getInstance().setKey(userKey);
					return result;
				} catch (Exception e2){
					exception = e2;
				}
			} else {
				exception = e;
			}
		} finally {
			if(exception != null) {

				if (!path.endsWith(".dtt"))
				{
					DialogHandlerFrame.showErrorDialog(Messages.getString("Encrypting.0")+path +":\n"+exception.getLocalizedMessage()); //$NON-NLS-1$ //$NON-NLS-2$

					System.out.println(exception.getLocalizedMessage()+" Caused by : "+exception.getCause()); //$NON-NLS-1$
					exception.printStackTrace();
					if(ExecutionMode.isIntern() == false) {
						System.exit(0);
					}		
				}
			}
		}

		return null;
	}

	
	private Object loadEncryptedObjectInter(String path, SecretKeySpec key) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IOException, IllegalBlockSizeException, BadPaddingException, ClassNotFoundException {
		File file = new File(path);

		//TODO voir pour ajouter un LockFile ou pas pour lancer qu'une instance. 
		//				FileLock fileLock = new FileInputStream(file).getChannel().tryLock(0L, Long.MAX_VALUE, true);

		InputStream in = new BufferedInputStream(new FileInputStream(file));

		Cipher cipher = Cipher.getInstance(algorithm);
		cipher.init(Cipher.DECRYPT_MODE, key);

		byte[] encryptedObject = new byte[(int)file.length()];
		in.read(encryptedObject);
		in.close();

		byte[] clearObject = cipher.doFinal(encryptedObject);

		File tmpFile = File.createTempFile("dtp", "tmp"); //$NON-NLS-1$ //$NON-NLS-2$
		OutputStream out = new BufferedOutputStream(new FileOutputStream(tmpFile));
		out.write(clearObject);
		out.close();

		ObjectInputStream input = new ObjectInputStream(new BufferedInputStream(new FileInputStream(tmpFile)));
//		{
//			protected Class<?> resolveClass(ObjectStreamClass desc)
//					throws IOException, ClassNotFoundException
//				    {
//						System.out.println(desc.getName());
//						return super.resolveClass(desc);
//				    }
//		};
		Object result = input.readObject();
		input.close();
		tmpFile.delete();

		return result;		
	}

	public void setKey(SecretKeySpec newKey) {
		currentKey = newKey;
	}

	public void setProgramKey() {
		setKey(Key.getProgramKey());
	}

	static public Encrypting getInstance() {
		if(singleton == null) {
			singleton = new Encrypting();
		}

		return singleton;
	}
}
