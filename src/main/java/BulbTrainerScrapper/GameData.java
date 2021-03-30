package BulbTrainerScrapper;

import java.util.ArrayList;

public class GameData {
	private Section[] sections;
	private Game game;
	private Trainer[] trainers;
	
	public GameData() {	
	}
	
	public GameData(Section[] sections, Game game, Trainer[] trainers) {
		this.sections = sections;
		this.game = game;
		this.trainers = trainers;
	}
	
	public GameData(ArrayList<Section> sections, Game game, ArrayList<Trainer> trainers) {
		this.sections = sections.toArray(new Section[0]);
		this.game = game;
		if (trainers != null) 
			this.trainers = trainers.toArray(new Trainer[0]);
		this.trainers = null;
	}

	public Section[] getSections() {
		return sections;
	}

	public void setSections(Section[] sections) {
		this.sections = sections;
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public Trainer[] getTrainers() {
		return trainers;
	}

	public void setTrainers(Trainer[] trainers) {
		this.trainers = trainers;
	}
	
	
}
