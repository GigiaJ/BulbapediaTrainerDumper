package BulbTrainerScrapper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Main {

	
	public static void main(String[] args) {
		//ArrayList<Trainer> trainers = LoadTrainers.getAllTrainers();
		ArrayList<Trainer> trainers = LoadTrainers.getGameTrainers(Game.RB, 15);
		String allTrainers = "";
 		for (Trainer trainer : trainers) {
 			allTrainers += "\n" + trainer.name;
 			allTrainers += "\n" + trainer.getPokemon();
		}
		saveTo(allTrainers);
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
