package dataPack;

import java.io.Serializable;


public class CompanyInfo implements Serializable {
	private static final long serialVersionUID = 6874655645781201215L;

	private String name;
	private String email;
	private String phone;
	private String manager;

	public String getName() {
		return name;
	}
	
	public String getPhone() {
		return phone;
	}

	public String getEmail() {
		return email;
	}

	public String getManager() {
		return manager;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setManager(String manager) {
		this.manager = manager;
	}
}
