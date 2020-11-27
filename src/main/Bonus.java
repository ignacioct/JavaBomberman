package main;

public class Bonus {
	private String type;
	private String image;

	public Bonus(String type) {
		switch(type){
		
		case "regular":	//we needed to add a regular type bonus that have nothing stored in order to avoid null pointers and make easier comparisons.
			this.type=type;
			break;
		case "bomb":	//here we added every bonus with its corresponding image.
			this.type=type;
			image="Bombupsprite.png";
			break;
		case "fire":
			this.type=type;
			image="Fireupsprite.png";
			break;
		case "fullFire":
			this.type=type;
			image="Fullfiresprite.png";
			break;
		case "skate":
			this.type=type;
			image="Skatesprite.png";
			break;
		case "geta":
			this.type=type;
			image="Getasprite.png";
			break;
		case "door":
			this.type=type;
			image="DoorClosed.png";
			break;
		case "remoteControl":
			this.type=type;
			image="Remote_Control_2.png";
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

}
