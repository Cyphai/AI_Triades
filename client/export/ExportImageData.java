package client.export;

import graphicalUserInterface.IconDatabase;

import java.io.Serializable;

import javax.swing.Icon;

public class ExportImageData implements Serializable {
	private static final long serialVersionUID = -8232182755856294608L;

	private int imageIndex;
	private String imagePath;
	
	public ExportImageData() {
		imageIndex = -1;
		imagePath = null;
	}

	public ExportImageData(ExportImageData imageData) {
		imageIndex = imageData.imageIndex;
		imagePath = imageData.imagePath;
	}

	public Icon getIcon() {
		if(imagePath !=null) {
			return IconDatabase.getResizeIcon(imagePath);
		}

		if(imageIndex >= 0) {
			return IconDatabase.vectorExportedIcons.elementAt(imageIndex);
		}

		return null;
	}
	
	public void setImageIndex(int index) {
		imageIndex = index;
	}
	
	public int getImageIndex() {
		return imageIndex;
	}
	
	public void setImagePath(String path) {
		imagePath = path;
	}
	
	public String getImagePath() {
		return imagePath;
	}
	
	public void clear() {
		imageIndex = -1;
		imagePath = null;
	}

	public void set(ExportImageData data) {
		imageIndex = data.imageIndex;
		imagePath = data.imagePath;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj != null && obj instanceof ExportImageData) {
			ExportImageData other = (ExportImageData)obj;
			return (imageIndex == other.imageIndex) && (imagePath == null ? other.imagePath == null : imagePath.equals(other.imagePath));
		}
		
		return false;
	}
	
	@Override
	public String toString()
	{
		return "ImageData : path = "+imagePath+" & index = "+imageIndex; //$NON-NLS-1$ //$NON-NLS-2$
	}
}
