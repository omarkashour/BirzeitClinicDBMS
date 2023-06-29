
public class MedicalRecord implements Comparable<MedicalRecord> {
	private int record_id;
	private int patient_id;
	private String illness_history;
	private String allergies;
	private String surgeries;
	private String medication_history;

	public MedicalRecord(int record_id, int patient_id, String illness_history, String allergies, String surgeries,
			String medication_history) {
		super();
		this.record_id = record_id;
		this.patient_id = patient_id;
		this.illness_history = illness_history;
		this.allergies = allergies;
		this.surgeries = surgeries;
		this.medication_history = medication_history;
	}

	public int getRecord_id() {
		return record_id;
	}

	public void setRecord_id(int record_id) {
		this.record_id = record_id;
	}

	public int getPatient_id() {
		return patient_id;
	}

	public void setPatient_id(int patient_id) {
		this.patient_id = patient_id;
	}

	public String getIllness_history() {
		return illness_history;
	}

	public void setIllness_history(String illness_history) {
		this.illness_history = illness_history;
	}

	public String getAllergies() {
		return allergies;
	}

	public void setAllergies(String allergies) {
		this.allergies = allergies;
	}

	public String getSurgeries() {
		return surgeries;
	}

	public void setSurgeries(String surgeries) {
		this.surgeries = surgeries;
	}

	public String getMedication_history() {
		return medication_history;
	}

	public void setMedication_history(String medication_history) {
		this.medication_history = medication_history;
	}

	@Override
	public int compareTo(MedicalRecord o) {
		if(this.record_id > o.record_id )
			return 1;
		else if(this.record_id < o.record_id)
			return -1;
		return 0;
	}

}
