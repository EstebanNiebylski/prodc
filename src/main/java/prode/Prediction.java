package prode;
import org.javalite.activejdbc.Model;
import java.util.ArrayList;

public class Prediction{

	private ArrayList<String> predict;
	private Integer idPred;

	public Prediction(Integer id) {
		predict=null;
		idPred=id;
	}

	public void setPrediction(ArrayList<String> npred) {
		predict= npred;
	}
}
