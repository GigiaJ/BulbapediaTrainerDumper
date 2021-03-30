package BulbTrainerScrapper;

public enum Game {
	HGSS("HeartGold_and_SoulSilver"), FRLG("FireRed_and_LeafGreen"),
	DP("Diamond_and_Pearl"), P("Platinum"),
	XY("X_and_Y"),
	ORAS("Omega_Ruby_and_Alpha_Sapphire"), BW("Black_and_White"),
	BW2("Black_2_and_White_2"), RB("Red_and_Blue"),
	Y("Yellow"), GS("Gold_and_Silver"),
	C("Crystal"), E("Emerald"),
	RS("Ruby_and_Sapphire");

	private final String APPENDIX = "Appendix:";
	private final String WALKTHROUGH = "_walkthrough";
	private String game;
	
	Game(String string) {
		this.game = string;
	}

	public String getGame() {
		return game;
	}
	
	public String getWalkthroughLink() {
		return APPENDIX + game + WALKTHROUGH;
	}
}
