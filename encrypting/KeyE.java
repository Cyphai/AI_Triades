package encrypting;

import graphicalUserInterface.IconDatabase;
import graphicalUserInterface.Program;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import javax.crypto.spec.SecretKeySpec;

public class KeyE extends Key {
	
	static public final int programIndex = 24;
	static public final int programOffset = 3879;
	
	private final int index;
	private final int offset;
	
	public KeyE() {
		this(programIndex, programOffset);
	}
	
	public KeyE(int index, int offset) {
		super();
		this.index = index;
		this.offset = offset;
	}
	
	@Override
	public SecretKeySpec createKey() {
		return new SecretKeySpec(getKeyBuffer(IconDatabase.originalExportedIcons.elementAt(index), offset), 0, Encrypting.keySize, Encrypting.algorithm);
	}
	
	static public KeyE createRandomKey() {
		Random random = new Random();
		int index = random.nextInt(IconDatabase.originalExportedIcons.size());
		
		int bufferSize = 2000;
		int count = 0;
		int tmpCount = 1;
		byte[] buffer = new byte[bufferSize];
		
		try {
			InputStream in = Program.myMainFrame.getClass().getResourceAsStream(IconDatabase.originalExportedIcons.elementAt(index));
			while(tmpCount > 0) {
				tmpCount = in.read(buffer, 0, bufferSize);
				count += tmpCount;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		int offset = random.nextInt(count/2 - Encrypting.keySize - 5) + count/2;
		
//		System.out.println("Index = " + index + " - Offset = " + offset);
		
		return new KeyE(index, offset);
	}
}
