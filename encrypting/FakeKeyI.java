package encrypting;

import javax.crypto.spec.SecretKeySpec;


public class FakeKeyI extends Key{

	@Override
	protected SecretKeySpec createKey() {
		return null;
	}

}
