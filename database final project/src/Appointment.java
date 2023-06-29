import java.text.SimpleDateFormat;
import java.util.Date;

public class Appointment implements Comparable<Appointment> {
	private int ap_id;
	private int patient_id;
	private int phys_id;
	private String ap_reason;
	private String ap_date;
	private String ap_time;
	private String status;
	private double cost;
	private String duration;

	public Appointment(int ap_id, int patient_id, int phys_id, String ap_reason, String ap_date, String ap_time,
			String status, double cost, String duration) {
		super();
		this.ap_id = ap_id;
		this.patient_id = patient_id;
		this.phys_id = phys_id;
		this.ap_reason = ap_reason;
		this.ap_date = ap_date;
		this.ap_time = ap_time;
		this.status = status;
		this.cost = cost;
		this.duration = duration;
	}

	public int getAp_id() {
		return ap_id;
	}

	public void setAp_id(int ap_id) {
		this.ap_id = ap_id;
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

	public String getAp_reason() {
		return ap_reason;
	}

	public void setAp_reason(String ap_reason) {
		this.ap_reason = ap_reason;
	}

	public String getAp_date() {
		return ap_date;
	}

	public void setAp_date(String ap_date) {
		this.ap_date = ap_date;
	}

	public String getAp_time() {
		return ap_time;
	}

	public void setAp_time(String ap_time) {
		this.ap_time = ap_time;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	@Override
	public int compareTo(Appointment o) {
		   SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		    try {
		        Date thisDate = dateFormat.parse(this.ap_date);
		        Date otherDate = dateFormat.parse(o.ap_date);
		        return thisDate.compareTo(otherDate);
		    } catch (Exception e) {
		        e.printStackTrace();
		    }
		    return 0;
	}
	
	

}
