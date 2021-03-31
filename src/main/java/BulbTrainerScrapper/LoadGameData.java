package BulbTrainerScrapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class LoadGameData {
	private final static String WIKI = "https://bulbapedia.bulbagarden.net";
	private final static String WIKI_EXTENSION = "/wiki/";
	
	public static ArrayList<GameData> loadData(Game game, String sect) {
		if (game == null) {
			return loadAllData(getGamesData());
		}
		else {
			GameData gameData = loadGameWalkthrough(game);
			if (sect == null) {
				gameData = loadAllSections(gameData);
			}
			else {
				for (Section section : gameData.getSections()) {
					if (section.getName().contains(sect)) {
						try {
							gameData.addTrainers(loadSection(section, gameData));
						}
						catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
			ArrayList<GameData> gamesData = new ArrayList<GameData>();
			gamesData.add(gameData);
			return gamesData;
		}
	}
	
	private static GameData loadAllSections(GameData gameData) {
		for (Section section : gameData.getSections()) {
			try {
				gameData.addTrainers(loadSection(section, gameData));
			}
			catch (Exception e) {
				System.out.println("Section: " + section.getName());
				System.out.println("Game: " + gameData.getGame().getGame());
				e.printStackTrace();
			}
		}
		return gameData;
	}
	
	public static ArrayList<GameData> loadAllData(ArrayList<GameData> gamesData) {
		//Game sections are now all loaded up
		//Now to load the trainer data from each game
			for (GameData gameData : gamesData) {
				gameData = loadAllSections(gameData);
			}
		
		return gamesData;
	}
	
	
	private static GameData loadGameWalkthrough(Game game) {
		String source = "";
		try {
			source = loadPageHTML(WIKI+WIKI_EXTENSION+game.getWalkthroughLink());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new GameData(loadSections(source), game, null);
	}
		
	private static ArrayList<GameData> getGamesData() {
		ArrayList<GameData> gamesData = new ArrayList<GameData>();
		for (Game game : Game.values()) {
			gamesData.add(loadGameWalkthrough(game));
		}
		return gamesData;
	}

	private static ArrayList<Section> loadSections(String source) {
		ArrayList<Section> sections = new ArrayList<Section>();
		Document document = Jsoup.parse(source);
		Elements unorderedLists = document.select("h3 ~ ul");
		for (Element unorderedList : unorderedLists) {
			for (Element node : unorderedList.children()) {
				String sectionName = node.text();
				String sectionLink = node.getElementsByAttribute("href").first().attr("href");
				sections.add(new Section(sectionName, WIKI+sectionLink));
			}
	    }
		return sections;
	}
	
	private static ArrayList<Trainer> loadSection(Section section, GameData gameData) {
		String source = "";
		try {
			source = loadPageHTML(section.getLink());
			//Section page loaded now to actually operate on the page itself
			return getTrainers(source, gameData.getGame());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static ArrayList<Trainer> getTrainers(String source, Game game) {
		ArrayList<Trainer> trainers = new ArrayList<Trainer>();
		trainers.addAll(MajorTrainer.loadTrainers(source, game));
		trainers.addAll(MinorTrainer.loadTrainers(source, game));
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
