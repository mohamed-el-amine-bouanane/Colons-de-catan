package board;


public class Loca_Chemin extends Localisation {

	private int orientation; // 0 = top , 1 =right,
	
	
	public Loca_Chemin(int x, int y, int o) {
		super(x, y);
		orientation = o;
	}
	
	
	public int getOrientation() {
		return orientation;
	}
}
