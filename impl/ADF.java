package impl;

import java.util.Arrays;
import java.util.List;

public class ADF {
	
	public ADF( String first_name, String last_name, String email,
			String phone, Name_Verifer nv,
			String city, String state, String zip_code, Address_Verifer Av,
			String timeframe, String prefered_purchase_type,
			String prefered_contact_type,
			String prefered_contact_time) {
		super();
		
		this.customer_data = new Customer_Data (first_name, last_name, email, phone, nv);
		this.customer_address = new Customer_Address(city, state, zip_code, Av );
		
		setTimeframe(timeframe);
		setPrefered_purchase_type(prefered_purchase_type);
		setPrefered_contact_type(prefered_contact_type);
		setPrefered_contact_time(prefered_contact_time);
	}

	private Customer_Data customer_data= null;
	private Customer_Address customer_address=null;
	private Time_Frame timeframe;
	private Purchase_Type prefered_purchase_type;
	private Contact_Type prefered_contact_type;
	private Contact_Time_Type prefered_contact_time;
	
	@Override
	public String toString() {
			String res = "";
			
			res += customer_data.toString()+" ";
			res += customer_address.toString()+" ";
			res += timeframe.toString()+" ";
			res += prefered_purchase_type.toString()+" ";
			res += prefered_contact_type.toString()+" ";
			res += prefered_contact_time.toString()+" ";
			
			return res;
	}
	
	public Customer_Data getCustomer_data() {
		return customer_data;
	}
	public void setCustomer_data(Customer_Data customer_data) {
		this.customer_data = customer_data;
	}
	public Customer_Address getAddress() {
		return customer_address;
	}
	public void setAddress(Customer_Address address) {
		this.customer_address = address;
	}
	public Time_Frame getTimeframe() {
		return timeframe;
	}
	public void setTimeframe(String timeframe_descr) {
		
		List<String> Descr  = Arrays.asList(timeframe_descr.split("\\s+"));
		
		//System.out.println("TIMEFRAME - "+timeframe_descr);
		
		if (Descr.size()<2){
			System.out.println("WTF????");
			System.out.println(timeframe_descr);
			for ( String s : Descr)
				System.out.println(s);
			this.timeframe = Time_Frame.OTHER;
			return;
		}			
		
		// general pattern: within <num> hour[s]|week[s]|month[s]
		
		int amount = Integer.parseInt(Descr.get(1));
		String Units = Descr.get(2).toLowerCase();
		
		if ( Units.contains("hour") ) {
			if ( amount <= 48  )
				this.timeframe = Time_Frame.WITHIN_48_HOURS;
			else
				if (amount > 48 && amount < 336 )
					this.timeframe = Time_Frame.WITHIN_2_WEEKS;
				else
					this.timeframe = Time_Frame.WITHIN_1_MONTH;
		}
		else
		if ( Units.contains("week") ) {
			if (amount >= 1 && amount <= 2 )
				this.timeframe = Time_Frame.WITHIN_2_WEEKS;
			else
				this.timeframe = Time_Frame.WITHIN_1_MONTH;
		}
		else
		if ( Units.contains("week") ) {
			if ( amount == 1 )
				this.timeframe = Time_Frame.WITHIN_1_MONTH;
		}
		else
			this.timeframe = Time_Frame.OTHER;
	}
	public Purchase_Type getPrefered_purchase_type() {
		return prefered_purchase_type;
	}
	public void setPrefered_purchase_type(String prefered_purchase_type) {
		
		if (prefered_purchase_type.toLowerCase().contains("cash"))
			this.prefered_purchase_type = Purchase_Type.CASH;
		else
		if (prefered_purchase_type.toLowerCase().contains("finance"))
			this.prefered_purchase_type = Purchase_Type.CASH;
		else
			this.prefered_purchase_type = Purchase_Type.OTHER;
		
	}
	public Contact_Type getPrefered_contact_type() {
		return prefered_contact_type;
	}
	public void setPrefered_contact_type(String prefered_contact_type) {
		if (prefered_contact_type.contains("phone"))
			this.prefered_contact_type = Contact_Type.PHONE;
		else 
		if (prefered_contact_type.contains("email"))
			this.prefered_contact_type = Contact_Type.EMAIL;
		else
			this.prefered_contact_type = Contact_Type.OTHER;
	}
	public Contact_Time_Type getPrefered_contact_time() {
		return prefered_contact_time;
	}
	public void setPrefered_contact_time(String prefered_contact_time) {
		
		prefered_contact_time = prefered_contact_time.toLowerCase();
		
		if (prefered_contact_time.contains("day"))
			this.prefered_contact_time = Contact_Time_Type.DAY;
		else
		if (prefered_contact_time.contains("afternoon"))
			this.prefered_contact_time = Contact_Time_Type.AFTERNOON;
		else
			this.prefered_contact_time = Contact_Time_Type.OTHER;
	}
	
	public float Calculate_Lead_Score(){
		
		float result = 0.0f;
		
		if ( customer_data.Is_First_Name_Correct())
			result += 0.025f * 10;
		else
			result += -0.025f * 10;
		
		if ( customer_data.Is_Last_Name_Correct())
			result += 0.05f * 10;
		else
			result += -0.025f * 10;
		
		if ( customer_data.Is_Email_Correct())
			result += 0.05f * 10;
		else
			result += -0.025f * 10;
		
		if ( customer_data.Is_Email_Correct())
			result += 0.05f * 10;
		else
			result += -0.025f * 10;		
		
		if ( customer_address.Is_Zip_Valid() )
			result += 0.1f * 10;
		else
			result += -0.05f * 10;

		if ( customer_address.Is_City_Valid())
			result += 0.1f * 10;
		else
			result += -0.05f * 10;

		if ( customer_address.Is_State_Valid())
			result += 0.1f * 10;
		else
			result += -0.05f * 10;
		
		if ( customer_data.Is_Phone_Correct())
			result += 0.1f * 10;
		else
			result += -0.05f * 10;
		
		if ( customer_address.Is_Zip_Corresponds_To_Address())
			result += 0.13f * 10;
		else
			result += -0.13f * 10;
		
		switch (this.timeframe){
			case WITHIN_48_HOURS: result += 0.1f*10;	break;
			case WITHIN_2_WEEKS: result += 0.05f*10;	break;
			case WITHIN_1_MONTH: result += 0.025f*10;	break;
			case OTHER: result -= 0.1f*10;	break;
		}
		
		switch (this.prefered_purchase_type){
			case CASH: result += 0.1f*10;	break;
			case FINANCE: result += 0.0625f*10;	break;
			case OTHER: result -= 0.05f*10;	break;
		}
		
		switch (this.prefered_contact_type){
			case PHONE: result += 0.07f*10;	break;
			case EMAIL: result -= 0.025f*10;	break;
			case OTHER: result -= 0.025f*10;	break;
		}
		
		switch (this.prefered_contact_time){
			case DAY: result += 0.075f*10;	break;
			case AFTERNOON: result += 0.035f*10;	break;
			case OTHER: result -= 0.025f*10;	break;
		}
		
		return result;
	}
}
