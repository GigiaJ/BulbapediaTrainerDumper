package BulbTrainerScrapper;

public class Pokemon {
	public static final String POKEMON_MOVE_INVALID = "None";
	public static int MAX_NUMBER_OF_MOVES = 4;
	public static int MAX_LEVEL = 100;
		
	int level = 0;
	String gender = null;
	String ability = null;
	String heldItem = null;
	String name = null;
	String[] moves = null;
	
	Pokemon() {
		moves = new String[4];
	}
	
	public void setLevel(int level) {
		this.level = level;
	}
	
	public void setGender(String gender) {
		if (gender == null) {
			this.gender = Gender.NONE.gender;
			return;
		}
		if (gender.equals("â™,")) {
			this.gender = Gender.FEMALE.gender;
		}
		if (gender.equals("â™€ "))
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
	
	public String getName() {
		return name;
	}
	
	public String getAbility() {
		return ability;
	}
	
	public String getHeldItem() {
		return heldItem;
	}
	
	public String getGender() {
		return gender;
	}
	
	public int getLevel() {
		return level;
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
		return "{ " + "name:" + name + " , " + "level:" + level + " , " + " ability:" + ability + " , " + " heldItem:" + heldItem + " , " + 
	"gender:" + gender + " , " + "moves:" + movesString + " } ";
	}
	
	
	enum Gender {
		MALE("Male"), FEMALE("Female"), NONE("None");
		
		String gender;
		
		Gender(String gender) {
			this.gender = gender;
		}
	}
	
}