package BulbTrainerScrapper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.jsoup.Jsoup;

public class Main {
	private final static String WIKI = "https://bulbapedia.bulbagarden.net/wiki/";
	private static String HGSS;
	private static String FRLG;
	private static String[] GAMES = {
			HGSS = "Appendix:HeartGold_and_SoulSilver_walkthrough/",
			FRLG = "Appendix:FireRed_and_LeafGreen_walkthrough/"
	};
	static ArrayList<Trainer> trainers = new ArrayList<Trainer>();
	
	public static void main(String[] args) {
		String source = loadSection(WIKI+HGSS, 29);
		getTrainers(source);
		for (Trainer trainer : trainers) {
			System.out.println(trainer.name);
			System.out.println(trainer.pokemon[0]);
		}
		
	}
	
	public static void getTrainers(String source) {
		while(hasNextTrainer(source)) {
			Pair<Trainer, String> trainerPass = getTrainer(source);
			trainers.add(trainerPass.getFirstObject());
			source = trainerPass.getSecondObject();
			

		}
	}
	
	public static Pair<Trainer, String> getTrainer(String source) {
		final String LOCATE_TRAINER = "border-radius: 20px; -moz-border-radius: 20px; -webkit-border-radius: 20px; -khtml-border-radius: 20px; -icab-border-radius: 20px; -o-border-radius: 20px; background:";
		final String BEGIN_TRAINER = "expandable\" ";
		Trainer trainer = new Trainer();
		String findTrainer = source.substring(0, source.indexOf(LOCATE_TRAINER));
		source = source.substring(findTrainer.lastIndexOf(BEGIN_TRAINER));
		
		trainer.setName(getTrainerName(source));
		
		do  {
			Pair<Pokemon, String> pokemonPass = getPokemon(source);
			source = pokemonPass.getSecondObject();
			trainer.addPokemon(pokemonPass.getFirstObject());
		}
		while (hasNextPokemon(source));
		
		return new Pair(trainer, source);
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
		
		Pokemon pokemon = new Pokemon();
		pokemon.setAbility(getAbility(source));
		
		source = source.substring(source.indexOf(PASS_ABILITY) + PASS_ABILITY.length());
		
		pokemon.setHeldItem(getHeldItem(source));
		
		source = source.substring(source.indexOf(PASS_HELD_ITEM) + PASS_HELD_ITEM.length());
		
		pokemon.setName(getPokemonName(source));
		
		source = source.substring(source.indexOf(PASS_NAME) + PASS_NAME.length());
		
		pokemon.setGender(getGender(source));
		
		pokemon.setLevel(Integer.valueOf(getLevel(source)));
		
		for (int i = 0; i < Pokemon.MAX_NUMBER_OF_MOVES; i++)
		{
			Pair<String, String> movePass = getMove(source);
			source = movePass.getSecondObject();
			pokemon.addMove(movePass.getFirstObject());
		}
		
		return new Pair(pokemon, source);
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
		final String IMAGE_TAG_START = "<img ";
		final String A_TAG_END = "</a>";
		String heldItem = source.substring(source.indexOf(HELD_ITEM_BEGIN_INDEX) + HELD_ITEM_BEGIN_INDEX.length(),
				source.indexOf(HELD_ITEM_END_INDEX));
		if (heldItem.contains(IMAGE_TAG_START)) {
			heldItem = heldItem.substring(heldItem.indexOf(IMAGE_TAG_START)).substring(heldItem.indexOf(A_TAG_END));
		}
		return Jsoup.parse(heldItem).text();
	}
	
	private static String getPokemonName(String source) {
		final String NAME_BEGIN_INDEX = "<span style=\"color:#000;\">";
		final String NAME_END_INDEX = "</span></a>";
		String name = source.substring(source.indexOf(NAME_BEGIN_INDEX) + NAME_BEGIN_INDEX.length(),
				source.indexOf(NAME_END_INDEX));
		return name;
	}
	
	private static String getGender(String source) {
		final String GENDER_BEGIN_INDEX = ";\">";
		final String GENDER_END_INDEX = "</span>";
		String gender = source.substring(source.indexOf(GENDER_BEGIN_INDEX) + GENDER_BEGIN_INDEX.length(),
				source.indexOf(GENDER_END_INDEX));
		return gender;
	}
	
	private static String getLevel(String source) {
		final String LEVEL_BEGIN_INDEX = "</small>";
		final String LEVEL_END_INDEX = "\n</td></tr>";
		String level = source.substring(source.indexOf(LEVEL_BEGIN_INDEX) + LEVEL_BEGIN_INDEX.length(),
				source.indexOf(LEVEL_END_INDEX));
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
				return new Pair(null, source);
			}
		}
		return new Pair(move, source);
	}
	
	private static boolean hasNextPokemon(String source) {
		final String LOCATE_TRAINER = "border-radius: 20px; -moz-border-radius: 20px; -webkit-border-radius: 20px; -khtml-border-radius: 20px; -icab-border-radius: 20px; -o-border-radius: 20px; background:";
		final String ABILITY_BEGIN_INDEX = "<span style=\"color:#000;\">";
		if (source.contains(LOCATE_TRAINER)) {
			if (source.indexOf(LOCATE_TRAINER) > source.indexOf(ABILITY_BEGIN_INDEX)) { //
				return true;
			}
		}
		
		return false;
	}
	
	private static boolean hasNextTrainer(String source) {
		final String LOCATE_TRAINER = "border-radius: 20px; -moz-border-radius: 20px; -webkit-border-radius: 20px; -khtml-border-radius: 20px; -icab-border-radius: 20px; -o-border-radius: 20px; background:";
		if (source.contains(LOCATE_TRAINER)) {
			return true;
		}
		return false;
	}
	
	private static String loadSection(String url, int number) {
		String source = "";
		try {
			source = loadPageHTML(url + "Section_" + number);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return source;
	}
	
	/**
	 * Grabs the source code of the given url
	 * @param url	the url to get the page source of
	 * @return		the page source code
	 * @throws IOException 
	 */
	private static String loadPageHTML(String url) throws IOException {
		String source = "";
		URL ur = new URL(url);
	    HttpURLConnection yc =(HttpURLConnection) ur.openConnection();
	    BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
	    String inputLine;
	    while ((inputLine = in.readLine()) != null) {
	    	source+=inputLine+"\n";
	    }
	    
	    in.close();
	    return source;
	}
	
	/**
	 * Saves the string to a file
	 * 
	 * Only really needed as Eclipse will not show the entire string during debug
	 * 
	 * @param string
	 */
	private static void saveTo(String string) {
	    try {
	        File myObj = new File("C:\\Users\\Jaggar\\Downloads\\output.txt");
	        if (myObj.createNewFile()) {
	          System.out.println("File created: " + myObj.getName());
	        } else {
	          System.out.println("File already exists.");
	        }
	        
	        FileWriter myWriter = new FileWriter("C:\\Users\\Jaggar\\Downloads\\output.txt");
	        myWriter.write(string);
	        myWriter.close();
	        System.out.println("Successfully wrote to the file.");
	      } catch (IOException e) {
	        System.out.println("An error occurred.");
	        e.printStackTrace();
	      }
	}
}
