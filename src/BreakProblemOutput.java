/**
 * This class will used to create objects work as return type 
 * to store two different type of data 
 * 
 * @author Mahran
 *
 */
public class BreakProblemOutput {
	
	/** Array 2D of Dynamic Programming*/
	private boolean [][] segmented ;
	
	/** A flag that tell us if it can be segmented or not*/
	private boolean isSegmented ; 
	
	
	public BreakProblemOutput(boolean [][] segmented , boolean isSegmented) {
		
		this.segmented = segmented;
		this.isSegmented = isSegmented;
	}

	
	public boolean[][] getSegmented() {
		return segmented;
	}

	public void setSegmented(boolean[][] segmented) {
		this.segmented = segmented;
	}

	public boolean isSegmented() {
		return isSegmented;
	}

	public void setSegmented(boolean isSegmented) {
		this.isSegmented = isSegmented;
	}
	
	

}
