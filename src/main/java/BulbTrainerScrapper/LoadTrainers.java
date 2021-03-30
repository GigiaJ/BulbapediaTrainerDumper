package BulbTrainerScrapper;

import java.util.ArrayList;

public class LoadTrainers {
	
	public static ArrayList<Trainer> getTrainers(String source, Game game) {
		ArrayList<Trainer> trainers = new ArrayList<Trainer>();
		trainers.addAll(MajorTrainer.loadTrainers(source, game));
		return trainers;
	}
}
