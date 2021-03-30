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
	
	public static ArrayList<GameData> loadData() {
		ArrayList<GameData> gamesData = getGamesData();
		String source = "";
		//Game sections are now all loaded up
		//Now to load the trainer data from each game
		
			for (GameData gameData : gamesData) {
				for (Section section : gameData.getSections()) {
					try {
						source = loadSection(section);
						//Section page loaded now to actually operate on the page itself
						gameData.setTrainers(LoadTrainers.getTrainers(source, gameData.getGame()).toArray(new Trainer[0]));
					}
					catch (Exception e) {
						System.out.println("Section: " + section);
						System.out.println("Game: " + gameData.getGame().getGame());
					}
				}
			}
		
		return gamesData;
	}
	
	
	private static ArrayList<GameData> getGamesData() {
		ArrayList<GameData> gamesData = new ArrayList<GameData>();
		String source = "";
		for (Game game : Game.values()) {
			try {
				source = loadPageHTML(WIKI+WIKI_EXTENSION+game.getWalkthroughLink());
			} catch (IOException e) {
				e.printStackTrace();
			}
			gamesData.add(new GameData(loadSections(source), game, null));
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
	
	private static String loadSection(Section section) {
		String source = "";
		try {
			source = loadPageHTML(section.getLink());
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
}
