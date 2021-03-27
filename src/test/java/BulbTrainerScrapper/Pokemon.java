package BulbTrainerScrapper;

public class Pokemon {
	static int MAX_NUMBER_OF_MOVES = 4;
	static int MAX_LEVEL = 100;
	
	
	int level;
	String gender;
	String ability;
	String heldItem;
	String name;
	String[] moves;
	
	Pokemon() {
		moves = new String[4];
	}
	
	public void setLevel(int level) {
		this.level = level;
	}
	
	public void setGender(String gender) {
		this.gender = gender;
	}
	
	public void setAbility(String ability) {
		this.ability = ability;
	}
	
	public void setHeldItem(String heldItem) {
		this.heldItem = heldItem;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void addMove(String moveToAdd) {
		if (moveToAdd != null) {
			for (int i = 0; i < MAX_NUMBER_OF_MOVES; i++) {
				if (moves[i] == null) {
					moves[i] = moveToAdd;
					return;
				}
			}
		}
	}
	
	public String toString() {
		String movesString = "[";
		for (int i = 0; i < MAX_NUMBER_OF_MOVES; i++) {
			if (moves[i] != null) {
				if (i != MAX_NUMBER_OF_MOVES - 1) {
					if (i+1 < MAX_NUMBER_OF_MOVES && moves[i+1] != null)
						movesString+=moves[i]+",";
					else 
						movesString+=moves[i];
				}
				else {
					movesString+=moves[i];
				}
			}
		}
		movesString+="]";
		return "{ " + "name:" + name + " , " + " ability:" + ability + " , " + " heldItem:" + heldItem + " , " + 
	"gender:" + gender + " , " + "moves:" + movesString + " } ";
	}
	
}