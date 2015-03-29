package impl;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;

public class Name_Verifer {
	protected HashSet<String> All_Names = new HashSet<String>();
	
	public Name_Verifer(String file_with_db) {
		super();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file_with_db));
			String line;
			while ((line = br.readLine()) != null) {
				String new_name = line.trim();
				All_Names.add(new_name);
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
	
	public Boolean Is_First_Name_Correct(String first_name){
		return All_Names.contains(first_name);
	}
	public Boolean Is_Last_Name_Correct(String last_name){
		return All_Names.contains(last_name);
	}

}
