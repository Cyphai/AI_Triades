package encrypting;

import graphicalUserInterface.IconDatabase;

import javax.crypto.spec.SecretKeySpec;

public class KeyI extends Key {
	@Override
	public SecretKeySpec createKey() {
		try {
			return new SecretKeySpec(getKeyBuffer(IconDatabase.defaultIconsDirectory, 0), 0, Encrypting.keySize, Encrypting.algorithm);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
