package board;



public class Ville extends Peuplement {

	
	public Ville(Localisation loc) 
	{
		setLocation(loc);
		setType(1);
	}

	
	public void giveResources(String resType) {
		getOwner().setNumberResourcesType(resType, getOwner().getNumberResourcesType(resType) + 2);
	}
}