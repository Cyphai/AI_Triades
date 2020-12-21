package dataPack;

import java.io.Serializable;

public interface SavableObject extends Serializable {

	public String getFilePath();

	public void setFilePath(String _filePath);
}
