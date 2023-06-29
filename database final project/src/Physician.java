
public class Physician {
	private int phys_id;
	private String first_name;
	private String last_name;
	private String address;
	private String phone_number;
	private String speciality;
	private String email_address;
	private String gender;

	public Physician(int phys_id, String first_name, String last_name, String address,String phone_number, String speciality,
			String email_address, String gender) {
		super();
		this.phys_id = phys_id;
		this.first_name = first_name;
		this.last_name = last_name;
		this.address = address;
		this.phone_number = phone_number;
		this.speciality = speciality;
		this.email_address = email_address;
		this.gender = gender;
	}

	public int getPhys_id() {
		return phys_id;
	}

	public void setPhys_id(int phys_id) {
		this.phys_id = phys_id;
	}

	public String getFirst_name() {
		return first_name;
	}

	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	public String getLast_name() {
		return last_name;
	}

	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	

	public String getPhone_number() {
		return phone_number;
	}

	public void setPhone_number(String phone_number) {
		this.phone_number = phone_number;
	}

	public String getSpeciality() {
		return speciality;
	}

	public void setSpeciality(String speciality) {
		this.speciality = speciality;
	}

	public String getEmail_address() {
		return email_address;
	}

	public void setEmail_address(String email_address) {
		this.email_address = email_address;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

}
