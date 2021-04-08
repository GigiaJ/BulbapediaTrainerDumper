package BulbTrainerScrapper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
	private static String[] jondHeader = new String[] {"Trainer/Name", "Level", "Ability", "Held Item", "Gender", "Moves", " ", "Class", "Coords", "Trainer Payout", "Skin", " ", "Opening Dialogue", "Losing Dialogue", " ", "Additional Items"};
 
	
	public static void main(String[] args) {
		ArrayList<GameData> gamesData = LoadGameData.loadData(Game.ORAS, null);
		ArrayList<String[]> output = new ArrayList<String[]>();
		output.add(jondHeader);
		for (GameData gameData : gamesData) {
			for (Trainer trainer : gameData.getTrainers()) {
				output.add(new String[] {trainer.getName()});
				output.add(trainer.getJondPokemon().split(", "));
			}			
		}
		saveTo(output, "testcsv.csv");
	}
	
	public static String convertToCSV(String[] data) {
	    return Stream.of(data)
	      .map(d -> escapeSpecialCharacters(d))
	      .collect(Collectors.joining(","));
	}
	
	public static String escapeSpecialCharacters(String data) {
	    String escapedData = data.replaceAll("\\R", "\n");
	    if (data.contains(",") || data.contains("\"") || data.contains("'")) {
	        data = data.replace("\"", "\"\"");
	        escapedData = data;
	    }
	    return escapedData;
	}
	
	/**
	 * Saves the string to a file
	 * 
	 * Only really needed as Eclipse will not show the entire string during debug
	 * 
	 * @param string
	 */
	private static void saveTo(ArrayList<String[]> string, String fileName) {

	    try {
	    	
	        File myObj = new File("C:\\Users\\gigia\\Downloads\\" + fileName);
	        if (myObj.createNewFile()) {
	          System.out.println("File created: " + myObj.getName());
	        } else {
	          System.out.println("File already exists.");
	        }
		    try (PrintWriter pw = new PrintWriter("C:\\Users\\gigia\\Downloads\\" + fileName)) {
		        string.stream()
		          .map(d->convertToCSV(d))
		          .forEach(pw::println);
		    }
	        System.out.println("Successfully wrote to the file.");
	      } catch (IOException e) {
	        System.out.println("An error occurred.");
	        e.printStackTrace();
	      }
	}
}
