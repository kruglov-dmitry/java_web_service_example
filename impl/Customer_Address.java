package impl;

public class Customer_Address {
	private String city;	
	private String state;
	private String zip_code;
	
	// calculated fields
	private Boolean is_zip_code_valid;
	private Boolean is_city_valid;
	private Boolean is_state_valid;
	private Boolean is_zip_code_corresponds_to_address;
	
	@Override
	public String toString() {
		return city + " " + state + " "+ zip_code ;		
	}
	
	public Customer_Address(String city, String state, String zip_code, Address_Verifer Av ) {
		super();
		setCity(city, Av);
		setState(state, Av);
		setZip_code(zip_code, Av);
		
		this.is_zip_code_corresponds_to_address = Av.Is_Zip_Code_Corresponds_To_Address( this.city, this.state, this.zip_code);
	}
	
	public Boolean Is_Zip_Valid(){
		return is_zip_code_valid;
	}
	
	public Boolean Is_City_Valid(){
		return is_city_valid;
	}
	
	public Boolean Is_State_Valid(){
		return is_state_valid;
	}
	
	public Boolean Is_Zip_Corresponds_To_Address(){
		return is_zip_code_corresponds_to_address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city,Address_Verifer Av ) {
		this.city = city;
		this.is_city_valid = Av.Is_City_Valid(this.city);
		this.is_zip_code_corresponds_to_address = Av.Is_Zip_Code_Corresponds_To_Address( this.city, this.state, this.zip_code);
	}

	public String getState() {
		return state;
	}

	public void setState(String state,Address_Verifer Av ) {
		this.state = state;
		this.is_state_valid = Av.Is_State_Valid(this.state);
		this.is_zip_code_corresponds_to_address = Av.Is_Zip_Code_Corresponds_To_Address( this.city, this.state, this.zip_code);
	}

	public String getZip_code() {
		return zip_code;
	}

	public void setZip_code(String zip_code,Address_Verifer Av ) {
		this.zip_code = zip_code;
		this.is_zip_code_valid = Av.Is_Zip_Code_Valid(this.zip_code);
		this.is_zip_code_corresponds_to_address = Av.Is_Zip_Code_Corresponds_To_Address( this.city, this.state, this.zip_code);
	}

	public Boolean getIs_zip_code_valid() {
		return is_zip_code_valid;
	}

}
