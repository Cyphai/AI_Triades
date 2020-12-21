package tools;

import java.io.Serializable;
import java.util.Vector;

public class LastObjectUsed<T> implements Serializable {
	private static final long serialVersionUID = -2027869057319634120L;

	private int maxSize;
	private Vector<T> lastObjects;
	
	public LastObjectUsed() {
		this(10);
	}
	
	public LastObjectUsed(int size) {
		maxSize = size;
		lastObjects = new Vector<T>();
	}

	public int getMaxSize() {
		return maxSize;
	}

	public void setMaxSize(int maxSize) {
		this.maxSize = maxSize;
	}

	public Vector<T> getLastObjects() {
		return lastObjects;
	}

	public void setLastObjects(Vector<T> lastObject) {
		this.lastObjects = lastObject;
	}
	
	public void addLastObject(T object) {
		if(object == null) {
			return;
		}
		
		if(lastObjects.contains(object)) {
			lastObjects.remove(object);
		}
		
		while(lastObjects.size() >= maxSize) {
			lastObjects.remove(lastObjects.lastElement());
		}
		
		lastObjects.add(0, object);
	}
}
