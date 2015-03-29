package impl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Customer_Data {
	
	public static final Pattern VALID_EMAIL_ADDRESS_REGEX = 
			Pattern.compile("^[A-Z]{2,}[A-Z0-9._%+-]*@[A-Z]{3,}[A-Z0-9.-]*\\.[A-Z]{2,}$", Pattern.CASE_INSENSITIVE);
		    // NOTE the simpler version which formally correspond to requirements below
			// but it do not accept email like name.last_name@domain123.com
			//Pattern.compile("^[A-Z]{2,}@[A-Z]{3,}\\.[A-Z]{2,}$", Pattern.CASE_INSENSITIVE);
	
	public static final Pattern VALID_PHONE_REGEX = 
			Pattern.compile("^\\(?[0-9]{3}\\)?-?\\(?[0-9]{3}\\)?-?[0-9]{4}$", Pattern.CASE_INSENSITIVE);
	
	public Customer_Data(String first_name, String last_name, String email,
			String phone, Name_Verifer nv) {
		super();
		
		setFirst_name(first_name,nv);
		setLast_name (last_name,nv);
		setEmail(email);
		setPhone(phone);
	}
	
	@Override
	public String toString() {
		return first_name+ " "+last_name+" "+email+" "+phone;		
	}
	
	// personal data
	private String first_name;
	private String last_name;
	private String email;	// ?
	private String phone;	// ? Reg-exp
	
	// calculated fields
	private Boolean is_first_name_correct;
	private Boolean is_last_name_correct;
	private Boolean is_email_correct;
	private Boolean is_phone_correct;
	
	Boolean Is_First_Name_Correct(){
		return is_first_name_correct;
	}
	
	Boolean Is_Last_Name_Correct(){
		return is_last_name_correct;
	}
	
	Boolean Is_Email_Correct(){
		return is_email_correct;
	}
	
	Boolean Is_Phone_Correct(){
		return is_phone_correct;
	}	
	
	public String getFirst_name() {
		return first_name;
	}
	public void setFirst_name(String first_name,Name_Verifer nv) {
		this.first_name = first_name;
		this.is_first_name_correct = nv.Is_First_Name_Correct(this.first_name);
	}
	public String getLast_name() {
		return last_name;
	}
	public void setLast_name(String last_name,Name_Verifer nv) {
		this.last_name = last_name;
		this.is_last_name_correct= nv.Is_Last_Name_Correct(this.last_name);	
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
		Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(this.email);
		this.is_email_correct = matcher.find();
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
		Matcher matcher = VALID_PHONE_REGEX.matcher(this.phone);
		this.is_phone_correct = matcher.find();	
	}

}
