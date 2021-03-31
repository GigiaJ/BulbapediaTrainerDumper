package BulbTrainerScrapper;

import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class MinorTrainer extends Trainer {

	protected static ArrayList<Trainer> loadTrainers(String source, Game game) {
		ArrayList<Trainer> trainers = new ArrayList<Trainer>();
		Document document = Jsoup.parse(source);
		Elements tables = document.select("table.expandable");
		
		for (Element table : tables) {
			//The trainer label is in this particular tag structure
			Elements trainerCheck = table.select("tbody > tr > th");
			//If it isn't the correct table then pass over it
			if (!trainerCheck.isEmpty()) {
				if (!trainerCheck.text().contains("Trainers")) {
					continue;
				}
			}
			
			Trainer trainer = new Trainer();
			for (Element node : table.select("tbody > tr > td > table.roundy > tbody > tr")) {
				if (node.hasAttr("align")) {
					Elements nodeTrainerNameCheck = node.select("td > b");
					if (nodeTrainerNameCheck.first() != null && !nodeTrainerNameCheck.first().children().isEmpty()) {
						Elements fromOuterTag = nodeTrainerNameCheck.first().children();
						if (!nodeTrainerNameCheck.isEmpty()) {
							Element roleCheck = fromOuterTag.first();
							trainer.setRole(roleCheck.ownText());
							if (nodeTrainerNameCheck.first().ownText() != null && !nodeTrainerNameCheck.first().ownText().equals("")) {
								trainer.setName(nodeTrainerNameCheck.first().ownText());
							}
							else {
								if (roleCheck != null) {
									//Gets the second item as it is a minor trainer with a link in the name
									if (fromOuterTag.size() > 1) {
										Element nameCheck = fromOuterTag.get(1);
										trainer.setName(nameCheck.ownText());
									}
									else {
										trainer.setName(Trainer.NO_NAME);
									}
								}
							}
						}
					}
					Elements pokemonCheck = node.select("tr > td > table > tbody");
					Pokemon pokemon = new Pokemon();
					for (Element check : pokemonCheck) {
						Elements titleCheck = check.select("a[title*=mon)]");
						if (!titleCheck.isEmpty()) {
							pokemon.setName(titleCheck.first().text());
						}
						if (pokemon.getName() != null) {
							Elements parentsChildren = titleCheck.first().parent().children();
							if (parentsChildren.size() > 1) {
								pokemon.setGender(parentsChildren.get(1).ownText());
							}
						}
						if (pokemon.getName() != null) {
							//The parent of the parent is just outside our next entries location 
							//And our next entry is always in the third child
							Elements parentsChildren = titleCheck.first().parent().parent().children();
							if (!parentsChildren.isEmpty()) {
								//Only one child in here
								String level = parentsChildren.get(2).child(0).ownText();
								if (level.equals("/")) {
									//Usually the third in that last children list entry is the rematch level
									//The first is the word Lv and the second is the initial match level
									level = parentsChildren.get(2).child(0).children().get(1).ownText();
									pokemon.setLevel(Integer.valueOf(level));
								}
								else {
									pokemon.setLevel(Integer.valueOf(level));
								}
							}
						}
						if (pokemon.getLevel() != 0) {
							Elements parentsChildren = titleCheck.first().parent().parent().parent().children();
							if (!parentsChildren.isEmpty()) {
								//Completely outside and then back in there is only one line of text here
								pokemon.setHeldItem(parentsChildren.get(1).text());
							}
						}
					}
					if (pokemon.getHeldItem() != null) {
						trainer.addPokemon(pokemon);
					}
				}
				else {
					if (trainer.getName() != null) {
						trainers.add(trainer);
						trainer = new Trainer();
					}	
				}
			}
		}
		
		return trainers;
	}	
}
