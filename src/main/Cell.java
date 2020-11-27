package main;

public class Cell {
	private String type; //regular, brick or wall
	private String image;
	private boolean hasbonus; //boolean, true for the bricks with bonus
	private Bonus bonus; //field of Bonus type
	
	public Cell(String type) {
		switch(type){
		case "brick":	//types of cells (bricks, walls and regular cells)
			this.type=type;
			image="bricks.gif";
			
			break;
		case "regular":
			this.type=type;
			break;
		case "wall":
			this.type=type;
			image="wall.gif";
			break;	
		}
	}
	//normal setters and getters.
	public String getImage(){
		return image;
	}
	
	public String getType(){
		return type;
	}
	
	public void setType(String type) {
		this.type=type;
	}
	
	public void setBonus(String type) {
		bonus = new Bonus(type);
	}
	
	public void setHasBonus(boolean bonus) {
		this.hasbonus = bonus;
	}
	
	public boolean getHasBonus() {
		return this.hasbonus;
	}
	
	public Bonus getBonus() {
		return this.bonus;
	}
	

}