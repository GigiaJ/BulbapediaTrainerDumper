package BulbTrainerScrapper;

import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class MajorTrainer {
	
	protected static ArrayList<Trainer> loadTrainers(String source, Game game) {
		ArrayList<Trainer> trainers = new ArrayList<Trainer>();
		Document document = Jsoup.parse(source);
		Elements tables = document.select("table.expandable");
		
		for (Element table : tables) {
			Trainer trainer = new Trainer();
			for (Element node : table.select("tbody > tr > td > table > tbody > tr > td > table > tbody > tr > td")) {
					if (trainer.getName() == null) {
						Elements nodes = node.select("b > big");
						if (!nodes.isEmpty()) {
							trainer.setName(nodes.first().text());
						}
					}
					if (trainer.getLocationOrRole() == null) {
						Elements nodes = node.select("b  > a > span");
						if (trainer.getLocationOrRole() == null && trainer.getName() != null) {
							if (!nodes.isEmpty()) {
								trainer.setLocationOrRole(nodes.first().text());
							}
						}
						trainer.setGame(game);
					}

				}
				
				for (Element pokeSearch : table.select("tbody > tr > td > table > tbody > tr > td > table > tbody")) {
					Pokemon pokemon = new Pokemon();
					Elements nodes = pokeSearch.select("td.roundy [title*=mon)]");
					if (!nodes.isEmpty()) {
						Elements parentNodesChildren = nodes.parents().first().children();
						for (Element node : parentNodesChildren) {
								if (pokemon.getName() == null) {
									pokemon.setName(node.text());
								}
								else if (pokemon.getGender() == null) {
									if (node.text().equals("Lv.")) {
										pokemon.setGender(null);
										pokemon.setLevel(Integer.valueOf(node.parent().ownText()));
									}
									else {
										pokemon.setGender(node.text());
									}
								}
								else if (pokemon.getLevel() == 0) {
									pokemon.setLevel(Integer.valueOf(node.parent().ownText()));
								}
						}
					}
					if (pokemon.getName() != null) {
						nodes = pokeSearch.select("td [title*=(Ability)]");
						if (!nodes.isEmpty()) {
							pokemon.setAbility(nodes.first().text());
						}
						nodes = pokeSearch.select("tr [style=\"text-align: center;\"]");
						if (!nodes.isEmpty()) {
							for (Element node : nodes) {
								if (pokemon.getHeldItem() == null) {
									if (!pokemon.getAbility().equals(node.text())) {
										pokemon.setHeldItem(node.text());
									}
								}
								else {
									//Covers the moves but we can't check if they are valid here
								}
							}
						}
					}
					nodes = pokeSearch.select("td.roundy > table.roundy");
					if (!nodes.isEmpty()) {
						/*
						 * 9 results from this. The first isn't a move
						 * The second is the first move
						 * the third is the filler value that is only displayed if the move is invalid
						 */
						for (int i = 1; i < 9; i=i+2) {
							if (nodes.get(i).attr("style").contains("display:none")) {
								pokemon.addMove(Pokemon.POKEMON_MOVE_INVALID);
							}
							else {
								if (pokemon.getName() != null) {
									String checkForMove = nodes.get(i).select("[title*=move]").first().text();
									if (checkForMove != null) {
										pokemon.addMove(checkForMove);
									}
								}
							}
						}
					}
					if (pokemon.getName() != null) {
						trainer.addPokemon(pokemon);
					}
				}
				if (trainer.getName() != null) {
					trainers.add(trainer);
				}
			}
		return trainers;
	}
}
