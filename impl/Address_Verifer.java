package impl;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class Address_Verifer {

	public class Pair<A, B> {
	    private A first;
	    private B second;

	    public Pair(A first, B second) {
	    	super();
	    	this.first = first;
	    	this.second = second;
	    }

	    public int hashCode() {
	    	int hashFirst = first != null ? first.hashCode() : 0;
	    	int hashSecond = second != null ? second.hashCode() : 0;

	    	return (hashFirst + hashSecond) * hashSecond + hashFirst;
	    }

	    public boolean equals(Object other) {
	    	if (other instanceof Pair) {
	    		Pair otherPair = (Pair) other;
	    		return 
	    		((  this.first == otherPair.first ||
	    			( this.first != null && otherPair.first != null &&
	    			  this.first.equals(otherPair.first))) &&
	    		 (	this.second == otherPair.second ||
	    			( this.second != null && otherPair.second != null &&
	    			  this.second.equals(otherPair.second))) );
	    	}

	    	return false;
	    }

	    public String toString()
	    { 
	           return "(" + first + ", " + second + ")"; 
	    }

	    public A getFirst() {
	    	return first;
	    }

	    public void setFirst(A first) {
	    	this.first = first;
	    }

	    public B getSecond() {
	    	return second;
	    }

	    public void setSecond(B second) {
	    	this.second = second;
	    }
	}
	
	// NOTE: http://opencsv.sourceforge.net/
	public Address_Verifer(String file_with_db) {
		super();

		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file_with_db));
			String line;
			Boolean first = true;
			while ((line = br.readLine()) != null) {
				if (first){
					first = false;
					continue;
				}
				
				if (line.trim().length()==0)
					continue;
				
				List<String> Addr_Details = Arrays.asList(line.split(","));
				
				String new_zip_code = Addr_Details.get(0).replaceAll("^\"|\"$", "");
				String new_city = Addr_Details.get(1).replaceAll("^\"|\"$", "");
				String new_state = Addr_Details.get(2).replaceAll("^\"|\"$", "");
				
				All_Cities.add(new_city);
				All_States.add(new_state);
				All_Zip_Codes.add(new_zip_code);
								
				// FIXME check it!
				Pair<String, String> new_entry = new Pair<String, String> (new_state, new_zip_code);
				ArrayList<Pair<String, String>> l = Address_Correspondence.get(new_city);
				if (l == null)
					l = new ArrayList<Pair<String, String>>();
				
				l.add(new_entry);

                Address_Correspondence.put(new_city, l);
			}
		
			br.close();
		}
		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}	
	
	public Boolean Is_Zip_Code_Valid ( String zip_code ){
		return All_Zip_Codes.contains(zip_code);
	}
	public Boolean Is_City_Valid(String city){
		return All_Cities.contains(city);
	}
	public Boolean Is_State_Valid(String state) {
		return All_States.contains(state);
	}
	
	public Boolean Is_Zip_Code_Corresponds_To_Address( String city, String state, String zip_code) {
		Boolean res = false;
		// FIXME check
		ArrayList<Pair<String, String>> l = Address_Correspondence.get(city);
		
		if (l == null)
			return res;
		
		for (Pair<String,String> cur_pair : l ) {
			if (cur_pair.second == zip_code) {
				res = true;
				break;
			}
		}
		
		return res;
	}

	// store only unique records
	protected HashSet<String> All_Cities = new HashSet<String>();
	protected HashSet<String> All_States = new HashSet<String>();	
	protected HashSet<String> All_Zip_Codes = new HashSet<String>();
		
	// Imitate multimap
	protected HashMap<String,ArrayList<Pair<String,String>>> Address_Correspondence = new HashMap<String,ArrayList<Pair<String,String>>>();
}
