package BulbTrainerScrapper;

public class Section {
	private String name;
	private String link;
	/*
	 * Possibly add more later
	 */
	
	
	Section(){
	}
	
	Section(String name, String link) {
		this.name = name;
		this.link = link;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}
	

}
