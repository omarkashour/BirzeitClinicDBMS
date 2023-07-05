
public class Prescription {
	private int pr_id;
	private int patient_id;
	private int phys_id;
	private String medication_name;
	private String dosage;
	private String frequency;

	public Prescription(int pr_id, int patient_id, int phys_id, String medication_name, String dosage,
			String frequency) {
		super();
		this.pr_id = pr_id;
		this.patient_id = patient_id;
		this.phys_id = phys_id;
		this.medication_name = medication_name;
		this.dosage = dosage;
		this.frequency = frequency;
	}
	public Prescription( int patient_id, int phys_id, String medication_name, String dosage,
			String frequency) {
		super();
		this.pr_id = pr_id;
		this.patient_id = patient_id;
		this.phys_id = phys_id;
		this.medication_name = medication_name;
		this.dosage = dosage;
		this.frequency = frequency;
	}

	public int getPr_id() {
		return pr_id;
	}

	public void setPr_id(int pr_id) {
		this.pr_id = pr_id;
	}

	public int getPatient_id() {
		return patient_id;
	}

	public void setPatient_id(int patient_id) {
		this.patient_id = patient_id;
	}

	public int getPhys_id() {
		return phys_id;
	}

	public void setPhys_id(int phys_id) {
		this.phys_id = phys_id;
	}

	public String getMedication_name() {
		return medication_name;
	}

	public void setMedication_name(String medication_name) {
		this.medication_name = medication_name;
	}

	public String getDosage() {
		return dosage;
	}

	public void setDosage(String dosage) {
		this.dosage = dosage;
	}

	public String getFrequency() {
		return frequency;
	}

	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}

}
