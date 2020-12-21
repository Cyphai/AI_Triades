package encrypting;

import graphicalUserInterface.ExecutionMode;
import graphicalUserInterface.IconDatabase;

import java.io.InputStream;

import javax.crypto.spec.SecretKeySpec;

abstract public class Key {
	private SecretKeySpec key;

	abstract protected SecretKeySpec createKey();
	
	public Key() {
		key = null;
	}
	
	public SecretKeySpec getKey() {
		if(key == null) {
			key = createKey();
		}
		
		return key;
	}

	protected byte[] getKeyBuffer(String path, int offset) {
		try {
			InputStream in = IconDatabase.class.getResourceAsStream(path);

			byte[] buffer = new byte[Encrypting.keySize];

			int tmpSize = 1024;
			byte[] tmp = new byte[tmpSize];
			int count = 0;
			while(count < offset) {
				count += in.read(tmp, 0, Math.min(tmpSize, offset - count));
			}

			in.read(buffer, 0, Encrypting.keySize);
		
			System.out.print("Key : ");
			for(int i = 0 ; i < Encrypting.keySize ; i++) {
				System.out.print(buffer[i] + " ");
			}
			System.out.println();

			return buffer;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	static public SecretKeySpec getProgramKey() {
		if(ExecutionMode.isIntern()) {
			System.out.println("getProgramKey Intern");
			return new KeyI().getKey();
		} else {
			System.out.println("getProgramKey Extern");
			return new KeyE().getKey();
		}
	}
}
