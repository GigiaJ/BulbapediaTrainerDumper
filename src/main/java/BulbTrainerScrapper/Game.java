package BulbTrainerScrapper;

public enum Game {
	HGSS("Appendix:HeartGold_and_SoulSilver_walkthrough"), FRLG("Appendix:FireRed_and_LeafGreen_walkthrough"),
	DP("Appendix:Diamond_and_Pearl_walkthrough"), P("Appendix:Platinum_walkthrough"),
	XY("Appendix:X_and_Y_walkthrough"),
	ORAS("Appendix:Omega_Ruby_and_Alpha_Sapphire_walkthrough"), BW("Appendix:Black_and_White_walkthrough"),
	BW2("Appendix:Black_2_and_White_2_walkthrough"), RB("Appendix:Red_and_Blue_walkthrough"),
	Y("Appendix:Yellow_walkthrough"), GS("Appendix:Gold_and_Silver_walkthrough"),
	C("Appendix:Crystal_walkthrough"), E("Appendix:Emerald_walkthrough"),
	RS("Appendix:Ruby_and_Sapphire_walkthrough");

	private String game;
	
	Game(String string) {
		this.game = string;
	}

	public String getGame() {
		return game;
	}
}
