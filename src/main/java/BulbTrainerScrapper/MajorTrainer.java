package BulbTrainerScrapper;

import org.jsoup.Jsoup;

public class MajorTrainer {
	private final static String LOCATE_TRAINER = "border-radius: 20px; -moz-border-radius: 20px; -webkit-border-radius: 20px; -khtml-border-radius: 20px; -icab-border-radius: 20px; -o-border-radius: 20px; background:";

	protected static Pair<Trainer, String> getTrainer(String source) {
		Trainer trainer = new Trainer();
		source = source.substring(source.indexOf(LOCATE_TRAINER) + LOCATE_TRAINER.length());
		trainer.setName(getTrainerName(source));
		
		do  {
			Pair<Pokemon, String> pokemonPass = getPokemon(source);
			source = pokemonPass.getSecondObject();
			trainer.addPokemon(pokemonPass.getFirstObject());
		}
		while (hasNextPokemon(source));
		
		return new Pair<Trainer, String>(trainer, source);
	}
	
	private static String getTrainerName(String source) {
		final String NAME_BEGIN_INDEX = "<b><big>";
		final String NAME_END_INDEX = "</big></b>";
		String name = source.substring(source.indexOf(NAME_BEGIN_INDEX) + NAME_BEGIN_INDEX.length(), source.indexOf(NAME_END_INDEX));
		return Jsoup.parse(name).text();
	}
	
	private static Pair<Pokemon, String> getPokemon(String source) {
		//will allow moving the string past the area containing the given string
		final String PASS_ABILITY = "<small>Held item:</small>";
		final String PASS_HELD_ITEM = "</td></tr></table>";
		final String PASS_NAME = "</span></a>";
		final String ALT_PASS_NAME = "mon)\"><span style=\"color:#000;\">";
		Pokemon pokemon = new Pokemon();
		pokemon.setAbility(getAbility(source));
		
		if (pokemon.getAbility().length() < 50) {
			source = source.substring(source.indexOf(PASS_ABILITY) + PASS_ABILITY.length());
			
			pokemon.setHeldItem(getHeldItem(source));
			
			source = source.substring(source.indexOf(PASS_HELD_ITEM) + PASS_HELD_ITEM.length());
			
			pokemon.setName(getPokemonName(source));
			
			source = source.substring(source.indexOf(PASS_NAME) + PASS_NAME.length());
			
			pokemon.setGender(getGender(source));
			
			pokemon.setLevel(Integer.valueOf(getLevel(source)));
		} else {
			/*Early games lacked multiple bits of info*/
			pokemon.setAbility("None");
			pokemon.setHeldItem("None");
			pokemon.setName(getPokemonName(source));
			source = source.substring(source.indexOf(ALT_PASS_NAME) + ALT_PASS_NAME.length());
			pokemon.setGender("None");
			pokemon.setLevel(Integer.valueOf(getLevel(source)));
		}
		
		for (int i = 0; i < Pokemon.MAX_NUMBER_OF_MOVES; i++)
		{
			Pair<String, String> movePass = getMove(source);
			source = movePass.getSecondObject();
			pokemon.addMove(movePass.getFirstObject());
		}
		
		return new Pair<Pokemon, String>(pokemon, source);
	}
	
	private static String getAbility(String source) {
		final String ABILITY_BEGIN_INDEX = "(Ability)\"><span style=\"color:#000;\">";
		final String ABILITY_END_INDEX = "</span></a>";
		source = source.substring(source.indexOf(ABILITY_BEGIN_INDEX) + ABILITY_BEGIN_INDEX.length());
		String ability = source.substring(0,
				source.indexOf(ABILITY_END_INDEX));
		return ability;
	}
	
	private static String getHeldItem(String source) {
		final String HELD_ITEM_BEGIN_INDEX = "<td style=\"text-align: center;\"> ";
		final String HELD_ITEM_END_INDEX = "\n</td></tr></table>";
		String heldItem = source.substring(source.indexOf(HELD_ITEM_BEGIN_INDEX) + HELD_ITEM_BEGIN_INDEX.length(),
				source.indexOf(HELD_ITEM_END_INDEX));
		return Jsoup.parse(heldItem).text();
	}
	
	private static String getPokemonName(String source) {
		final String NAME_BEGIN_INDEX = "mon)\"><span style=\"color:#000;\">";
		final String NAME_END_INDEX = "</span></a>";
		source = source.substring(source.indexOf(NAME_BEGIN_INDEX));
		String name = source.substring(NAME_BEGIN_INDEX.length(),
				source.indexOf(NAME_END_INDEX));
		return name;
	}
	
	private static String getGender(String source) {
		final String GENDER_BEGIN_INDEX = ";\">";
		final String GENDER_END_INDEX = "</span>";
		String gender = "";
		try {
		 gender = source.substring(source.indexOf(GENDER_BEGIN_INDEX) + GENDER_BEGIN_INDEX.length(),
				source.indexOf(GENDER_END_INDEX));
		} catch (StringIndexOutOfBoundsException e) {
			gender = "None";
			return gender;
		}
		if (gender.length() > 10) {
			gender = "None";
		}
		return gender;
	}
	
	private static String getLevel(String source) {
		final String LEVEL_BEGIN_INDEX = "</small>";
		final String LEVEL_END_INDEX = "\n</td></tr>";
		String level = source.substring(source.indexOf(LEVEL_BEGIN_INDEX) + LEVEL_BEGIN_INDEX.length(),
				source.indexOf(LEVEL_END_INDEX));
		if (level.length() > 10) {
			level = Jsoup.parse(level).text();
			if (level.contains(",")) {
				level = level.split(", ")[0];
			}
		}
		return level;
	}
	
	private static Pair<String, String> getMove(String source) {
		final String MOVE_BEGIN_INDEX = "(move)\"><span style=\"color:#000;\">";
		final String MOVE_END_INDEX = "</span></a>";
		source = source.substring(source.indexOf(MOVE_BEGIN_INDEX) + MOVE_BEGIN_INDEX.length());
		String move = source.substring(0, source.indexOf(MOVE_END_INDEX));
		if (move.equals("Curse")) {
			final String CURSE_TYPE = "</span></a>\n" + 
					"</td></tr>\n" + 
					"<tr>\n" + 
					"<td class=\"roundybl\" width=\"50%\" style=\"text-align: center; background:#68A090; line-height:12px;\"> <small><a href=\"/wiki/Unknown_(type)\" class=\"mw-redirect\" title=\"Unknown (type)";
			
			if (source.indexOf(CURSE_TYPE) == source.indexOf(MOVE_END_INDEX)) {
				return new Pair<String, String>(null, source);
			}
		}
		return new Pair<String, String>(move, source);
	}
	
	private static boolean hasNextPokemon(String source) {
		final String MAJOR_POKE_INDEX = "<td class=\"roundy\" style=\"margin:auto; text-align: center; background: ";
		int levelIdx = source.indexOf(MAJOR_POKE_INDEX);
		int trainerIdx = source.indexOf(LOCATE_TRAINER);
		if (source.contains(LOCATE_TRAINER)) {
			if (trainerIdx > levelIdx) { //
				return true;
			}
		}
		else {
			if (source.contains(MAJOR_POKE_INDEX)) {
				return true;
			}
			
		}
		
		return false;
	}
	
	static boolean hasNextTrainer(String source) {
		if (source.contains(LOCATE_TRAINER)) {
			return true;
		}
		return false;
	}
	
	
}
