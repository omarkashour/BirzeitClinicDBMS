public class Patient implements Comparable<Patient> {
	private int patient_id;
	private String first_name;
	private String last_name;
	private String address;
	private String date_of_birth;
	private String email_address;
	private String phone_number;
	private String gender;
	private double weight;
	private double height;

	public Patient() {
		
	}
	public Patient(int patient_id, String first_name, String last_name, String address, String date_of_birth,
			String email_address, String phone_number, String gender, double weight, double height) {
		super();
		this.patient_id = patient_id;
		this.first_name = first_name;
		this.last_name = last_name;
		this.address = address;
		this.date_of_birth = date_of_birth;
		this.email_address = email_address;
		this.phone_number = phone_number;
		this.gender = gender;
		this.weight = weight;
		this.height = height;
	}

	public int getPatient_id() {
		return patient_id;
	}

	public void setPatient_id(int patient_id) {
		this.patient_id = patient_id;
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

	public String getDate_of_birth() {
		return date_of_birth;
	}

	public void setDate_of_birth(String date_of_birth) {
		this.date_of_birth = date_of_birth;
	}

	public String getEmail_address() {
		return email_address;
	}

	public void setEmail_address(String email_address) {
		this.email_address = email_address;
	}

	public String getPhone_number() {
		return phone_number;
	}

	public void setPhone_number(String phone_number) {
		this.phone_number = phone_number;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	@Override
	public int compareTo(Patient o) {
		if (this.patient_id > o.patient_id)
			return 1;
		else if (this.patient_id < o.patient_id)
			return -1;

		return 0;
	}

}
