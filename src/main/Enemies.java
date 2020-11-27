package main;

public class Enemies {
	private String type;
	private String image; 
	private int enemyX;	//X coordinate for each enemy
	private int enemyY;	//Y coordinate for each enemy
	private int enemyHealth=10;	//enemies have lower health in order to make easier to kill them with bombs.
	private boolean alive = true; //boolean that wil make the enemies stop hurting when they die
	private String spriteBalloonRight[];	//array of the different images for each direction of the enemies
	private String spriteBalloonLeft[];
	private String spriteDropRight[];
	private String spriteDropLeft[];
	private int enemiesSpriteCounter;	//counter that will help us do the animations changing the position of the array of images
	private int directionCounter;	//how many coordinates will it take to change direction of the balloon
	private int balloonDirection;	//will alternate between 0 and 3, each value point to a different direction (right, left, up, down).

	public Enemies (String type) {
		switch(type){
		case "balloon"://two types of enemies
			this.type=type;
			image="enemy121.png";	//a default image
			break;
		case "drop":
			this.type=type;
			image="enemy211.png";
			break;
		}
		setSpriteBalloon();	//we added this methods in orther to create the sprites just after defining the enemy.
		setSpriteDrop();
	}

	public void setSpriteDrop() {
		spriteDropRight = new String [3];	//two sprites one for right and upwards direction and the other for left and downwards.
		spriteDropRight[0]="enemy221.png";
		spriteDropRight[1]="enemy222.png";
		spriteDropRight[2]="enemy223.png";

		spriteDropLeft = new String [3];
		spriteDropLeft[0]="enemy211.png";
		spriteDropLeft[1]="enemy212.png";
		spriteDropLeft[2]="enemy213.png";
	}

	public void setSpriteBalloon() {
		spriteBalloonRight = new String [3];	//two sprites one for right and upwards direction and the other for left and downwards.
		spriteBalloonRight[0]="enemy121.png";
		spriteBalloonRight[1]="enemy122.png";
		spriteBalloonRight[2]="enemy123.png";

		spriteBalloonLeft = new String [3];
		spriteBalloonLeft[0]="enemy111.png";
		spriteBalloonLeft[1]="enemy112.png";
		spriteBalloonLeft[2]="enemy113.png";
	}

	public void moveBalloonDown() {
		setEnemyY(getEnemyY()+1);	//in order to move the ballon downward on the table we need to increase the value of his Y coordinate.
		setEnemiesSpriteCounter((getEnemiesSpriteCounter()+1)%3);	//each time an enemy moves increases by one the spriteCounter that will choose the image (when it reach 3 it will automaticly go down to 0 again).
	}

	public void moveBalloonUp() {	//baloon movement works similar for every direction.
		setEnemyY(getEnemyY()-1);
		setEnemiesSpriteCounter((getEnemiesSpriteCounter()+1)%3);
	}

	public void moveBalloonRight() {
		setEnemyX(getEnemyX()+1);
		setEnemiesSpriteCounter((getEnemiesSpriteCounter()+1)%3);
	}

	public void moveBalloonLeft() {
		setEnemyX(getEnemyX()-1);
		setEnemiesSpriteCounter((getEnemiesSpriteCounter()+1)%3);
	}

	public int getDirectionCounter() {
		return directionCounter;	//we created this variable in order to make the movenet of the ballons more natural but still randomly. it will count the number of moves a ballon does before changin direction.
	}

	public String getSpriteBalloonRight() {
		return spriteBalloonRight[enemiesSpriteCounter];	//when we need the image of an enemy it will automatically get the one pointed by the spriteCounter.
	}

	public String getSpriteDropLeft() {
		return spriteDropLeft[enemiesSpriteCounter];
	}
	public String getSpriteDropRight() {
		return spriteDropRight[enemiesSpriteCounter];
	}

	public String getSpriteBalloonLeft() {
		return spriteBalloonLeft[enemiesSpriteCounter];
	}
	//normal setters and getters
	public void setDirectionCounter(int directionCounter) {	
		this.directionCounter = directionCounter;	
	}

	public int getEnemiesSpriteCounter() {
		return enemiesSpriteCounter;
	}

	public void setEnemiesSpriteCounter(int enemiesSpriteCounter) {
		this.enemiesSpriteCounter = enemiesSpriteCounter;
	}	

	public int getBalloonDirection() {
		return balloonDirection;
	}

	public void setBalloonDirection(int balloonDirection) {
		this.balloonDirection = balloonDirection;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public int getEnemyX() {
		return enemyX;
	}

	public void setEnemyX(int enemyX) {
		this.enemyX = enemyX;
	}

	public int getEnemyY() {
		return enemyY;
	}

	public void setEnemyY(int enemyY) {
		this.enemyY = enemyY;
	}

	public int getEnemyHealth() {
		return enemyHealth;
	}

	public void setEnemyHealth(int enemyHealth) {
		this.enemyHealth = enemyHealth;
	}

	public void setEnemyAlive(boolean alive) {
		this.alive=alive;
	}
	public boolean getEnemyAlive() {
		return this.alive;
	}

}
