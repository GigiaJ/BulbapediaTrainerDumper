package BulbTrainerScrapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class LoadTrainers {
	private final static String WIKI = "https://bulbapedia.bulbagarden.net/wiki/";

	protected static ArrayList<Trainer> getAllTrainers() {
		ArrayList<Trainer> trainers = new ArrayList<Trainer>();
		for (Game game : Game.values()) {
			trainers.addAll(getGameTrainers(game));
		}
		return trainers;
	}
	
	protected static ArrayList<Trainer> getGameTrainers(Game game) {
		return getGameTrainers(game, 0);
	}
	
	protected static ArrayList<Trainer> getGameTrainers(Game game, int section) {
		final String SECTION_START = "<li> <a href=\"/wiki/" + game.getGame();
		ArrayList<Trainer> gameTrainers = new ArrayList<Trainer>();
		String source = "";
		try {
			source = loadPageHTML(WIKI+game.getGame());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String sectionNumberString;
		int sectionNumberIndex;
		do {
			sectionNumberIndex = source.indexOf(SECTION_START);
			source = source.substring(sectionNumberIndex + SECTION_START.length());
			sectionNumberString = source.substring(9, source.indexOf("\"")); //9 skips over the /Section_ part of the line
			int sectionNumber;
			try {
				sectionNumber = Integer.valueOf(sectionNumberString);
			} catch(NumberFormatException e){
				continue;
			}
			String sectionSource;
			if (section == 0)
				sectionSource = loadSection(WIKI+game.getGame(), sectionNumber);
			else 
				sectionSource = loadSection(WIKI+game.getGame(), section);
			try {
				gameTrainers.addAll(getTrainers(sectionSource));
			} catch(Exception e) {
				System.out.println("Game: " + game);
				System.out.println("Section: " + sectionNumber);
				e.printStackTrace();
			}
		} while (gameHasSectionsRemaining(source, game));
		return gameTrainers;
	}
	
	private static boolean gameHasSectionsRemaining(String source, Game game) {
		final String SECTION_START = "<li> <a href=\"/wiki/" + game.getGame();
		return source.contains(SECTION_START);
	}
	
	private static String loadSection(String url, int number) {
		String source = "";
		try {
			source = loadPageHTML(url + "/Section_" + number);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return source;
	}
	
	private static ArrayList<Trainer> getTrainers(String source) {
		ArrayList<Trainer> trainers = new ArrayList<Trainer>();
	
		while(MajorTrainer.hasNextTrainer(source)) {
			Pair<Trainer, String> trainerPass = MajorTrainer.getTrainer(source);
			trainers.add(trainerPass.getFirstObject());
			source = trainerPass.getSecondObject();
		}
		
		return trainers;
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
}
