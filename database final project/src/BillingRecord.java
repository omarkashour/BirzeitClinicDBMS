public class BillingRecord implements Comparable<BillingRecord> {
	private int record_id;
	private int patient_id;
	private String billing_method;
	private double total_amount;
	private double amount_paid;
	private double amount_left;
	private String date_of_billing;
	private String details;
	private String payment_status;

	public BillingRecord(int record_id, int patient_id, String billing_method, double total_amount, double amount_paid,
			double amount_left, String date_of_billing, String details, String payment_status) {
		super();
		this.record_id = record_id;
		this.patient_id = patient_id;
		this.billing_method = billing_method;
		this.total_amount = total_amount;
		this.amount_paid = amount_paid;
		this.amount_left = amount_left;
		this.date_of_billing = date_of_billing;
		this.details = details;
		this.payment_status = payment_status;
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

	public String getBilling_method() {
		return billing_method;
	}

	public void setBilling_method(String billing_method) {
		this.billing_method = billing_method;
	}

	public double getTotal_amount() {
		return total_amount;
	}

	public void setTotal_amount(double total_amount) {
		this.total_amount = total_amount;
	}

	public double getAmount_paid() {
		return amount_paid;
	}

	public void setAmount_paid(double amount_paid) {
		this.amount_paid = amount_paid;
	}

	public double getAmount_left() {
		return amount_left;
	}

	public void setAmount_left(double amount_left) {
		this.amount_left = amount_left;
	}

	public String getDate_of_billing() {
		return date_of_billing;
	}

	public void setDate_of_billing(String date_of_billing) {
		this.date_of_billing = date_of_billing;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public String getPayment_status() {
		return payment_status;
	}

	public void setPayment_status(String payment_status) {
		this.payment_status = payment_status;
	}

	@Override
	public int compareTo(BillingRecord o) {
		if(this.record_id > o.record_id)
			return 1;
		else if(this.record_id < o.record_id)
			return -1;
		return 0;
	}

}
