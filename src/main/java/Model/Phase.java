package Model;

import java.sql.Date;
import org.javalite.activejdbc.Model;
import java.util.List;

public class Phase extends Model {
	
	static {
        dateFormat("dd/MM/yyyy", "expires");
    }  
	
	public Phase(){}
	
	public String getId(){ return getString("id"); } 
	public String getName(){ return getString("name"); }
	public String getExpires(){ return getString("expires"); }
	
	public static List<Phase> listPhase(){
		return findAll();
	}
	
	public static Phase getPhaseId(int id){
		return findById(id);
	}

	public void saveExpires(String fecha){
	  setDate("expires",fecha);
      saveIt();
	}
}
