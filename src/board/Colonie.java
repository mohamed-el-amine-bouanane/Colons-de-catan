package board;


public class Colonie extends Peuplement {

	
	public Colonie(int x, int y) {
		setLocation(new Localisation(x, y));
	}
	
	public void giveResources(String resType) {
		getOwner().setNumberResourcesType(resType, getOwner().getNumberResourcesType(resType) + 1);
	}
	
}