package graphicalUserInterface;

import java.util.Vector;

import dataPack.SavableObject;

public class ConfigUser implements SavableObject {

	private static final long serialVersionUID = 1429351747176112333L;

	private String filePath;
	private final Vector<String> lastFilePath;

	private ConfigUser() {
		filePath = "config"; //$NON-NLS-1$
		lastFilePath = new Vector<String>();
	}

	public Vector<String> getLastFilePath() {
		Vector<String> copy = new Vector<String>();
		for (int i = 0; i < lastFilePath.size(); i++)
			copy.add(lastFilePath.elementAt(lastFilePath.size() - 1 - i));

		return copy;
	}

	public void addLastFilePath(String path) {
		if (lastFilePath.contains(path))
			lastFilePath.remove(path);
		if (lastFilePath.size() == 5)
			lastFilePath.remove(0);
		lastFilePath.add(path);
	}

	@Override
	public String getFilePath() {
		return filePath;
	}

	@Override
	public void setFilePath(String path) {
		filePath = path;
	}

}
