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
		String source = loadSection(WIKI+HGSS, 15);
		getTrainers(source);
		for (Trainer trainer : trainers) {
			System.out.println(trainer.name);
			System.out.println(trainer.getPokemon());
		}
		
	}
	
	public static void getTrainers(String source) {
		while(MajorTrainer.hasNextTrainer(source)) {
			Pair<Trainer, String> trainerPass = MajorTrainer.getTrainer(source);
			trainers.add(trainerPass.getFirstObject());
			source = trainerPass.getSecondObject();
		}
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
