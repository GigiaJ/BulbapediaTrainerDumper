package BulbTrainerScrapper;

public class Trainer {
	final int MAX_POKEMON = 6;
	Pokemon[] pokemon;
	String name;
	
	Trainer() {
		pokemon = new Pokemon[MAX_POKEMON];
	}
	
	public void setName(String name) {
		this.name = name;
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
	
	public String getPokemon() {
		String s = "";
		for (Pokemon mon : pokemon) {
			if (mon != null)
				s += mon.toString() + "\n";
		}
		return s;
	}
	
}
