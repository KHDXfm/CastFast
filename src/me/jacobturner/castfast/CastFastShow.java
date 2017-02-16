package me.jacobturner.castfast;

public class CastFastShow {
	private String name;
	private String[] DJ;
	private String[] email;
	private String airs;
	
	public CastFastShow() {
	}
	
	public CastFastShow(String name, String[] DJ, String[] email, String airs) {
		this.name = name;
		this.DJ = DJ;
		this.email = email;
		this.airs = airs;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String[] getDJ() {
		return DJ;
	}

	public void setDJ(String[] DJ) {
		this.DJ = DJ;
	}

	public String[] getEmail() {
		return email;
	}

	public void setEmail(String[] email) {
		this.email = email;
	}

	public String getAirs() {
		return airs;
	}

	public void setAirs(String airs) {
		this.airs = airs;
	}

}
