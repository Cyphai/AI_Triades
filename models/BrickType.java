package models;

import java.io.Serializable;

public class BrickType implements Serializable {

	private static final long serialVersionUID = -59990001611121786L;

	protected String name;
	protected Integer brickTypeId;

	public Integer getBrickTypeId() {
		return brickTypeId;
	}

	public void setBrickTypeId(Integer brickTypeId) {
		this.brickTypeId = brickTypeId;
	}

	public BrickType(String _name, Integer id) {
		name = _name;
		brickTypeId = id;
	}

	public boolean equals(BrickType other) {
		return other.name == name;
	}

	public String toString() {
		return name;
	}

}
