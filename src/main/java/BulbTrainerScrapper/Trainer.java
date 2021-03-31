package BulbTrainerScrapper;

public class Trainer {
	final static String NO_NAME = "No name";
	final int MAX_POKEMON = 6;
	private Pokemon[] pokemon;
	private String name;
	private String location;
	private String role;
	private Game game;

	Trainer() {
		pokemon = new Pokemon[MAX_POKEMON];
		name = null;
		location = null;
		game = null;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setLocation(String location) {
		this.location = location;
	}
	
	public void setGame(Game game) {
		this.game = game;
	}
	
	public void setRole(String role) {
		this.role = role;
	}
	
	public void addPokemon(Pokemon monToAdd) {
		if (monToAdd != null) {
			for (int i = 0; i < MAX_POKEMON; i++) {
				if (pokemon[i] == null) {
					pokemon[i] = monToAdd;
					return;
				}
			}
		}
	}
	
	public void setPokemon(Pokemon[] pokemon) {
		this.pokemon = pokemon;
	}
	
	public String getPokemon() {
		String s = "";
		for (Pokemon mon : pokemon) {
			if (mon != null)
				s += mon.toString() + "\n";
		}
		return s;
	}
	
	public int getMAX_POKEMON() {
		return MAX_POKEMON;
	}

	public String getName() {
		return name;
	}


	public String getLocation() {
		return location;
	}
	
	public Game getGame() {
		return game;
	}
	
	public String getRole() {
		return role;
	}



}
