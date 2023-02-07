package board;



public class CarteD {

	private final String TypeCarte;
			private final String SousT;
	
	public CarteD(String cT) {
		TypeCarte = cT;
		SousT = null;
	}
	
	
	public CarteD(String cT, String sT) {
		TypeCarte = cT;
		SousT = sT;
	}
	
	
	public String getType() {
		return TypeCarte;
	}
	
	
	public String getSousT() {
		return SousT;
	}
}
