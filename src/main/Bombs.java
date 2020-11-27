package main;

public class Bombs {

	//Variable declaring. All will be used on the methods below
	private int bombTiming=0;
	private boolean bombVisible=false;
	private boolean bombAvailable=false;
	private int rangeRight;
	private int rangeLeft;
	private int rangeUp;
	private int rangeDown;
	private String [] spriteBombs;//array of images for the bomb
	private int bombSpriteCounter=0;////this counter will make possible the change between bomb sprites into the arrays
	private int bombX;
	private int bombY;
	private String [] spriteExplosion;
	private String [] spriteExplosionRight;
	private String [] spriteExplosionLeft;
	private String [] spriteExplosionVertical;
	private String [] spriteExplosionHorizontal;
	private String [] spriteExplosionUp;
	private String [] spriteExplosionDown;
	private int range=1;
	//Non-argument constructor
	public Bombs () {
		setSpriteBombs();	//initialized the sprite of images for each bomb and the explosion for the 4 directions.
		setSpriteExplosion();
		setSpriteExplosionRight();
		setSpriteExplosionLeft();
		setSpriteExplosionUp();
		setSpriteExplosionDown();
		setSpriteExplosionVertical();
		setSpriteExplosionHorizontal();


	}
	public int getRangeRight() {
		return rangeRight;
	}
	public void setRangeRight(int rangeRight) {
		this.rangeRight = rangeRight;
	}
	public int getRangeLeft() {
		return rangeLeft;
	}
	public void setRangeLeft(int rangeLeft) {
		this.rangeLeft = rangeLeft;
	}
	public int getRangeUp() {
		return rangeUp;
	}
	public void setRangeUp(int rangeUp) {
		this.rangeUp = rangeUp;
	}
	public int getRangeDown() {
		return rangeDown;
	}
	public void setRangeDown(int rangeDown) {
		this.rangeDown = rangeDown;
	}	
	public void setBombTiming(int bombTiming) {
		this.bombTiming=bombTiming;
	}
	public void setBombVisible(boolean bombVisible) {
		this.bombVisible=bombVisible;
	}
	public void setBombAvailable(boolean bombAvailable) {
		this.bombAvailable=bombAvailable;
	}	
	public void setBombsSpriteCounter(int bombsCounter) {
		this.bombSpriteCounter=bombsCounter;
	}
	public void setBombX(int bombX) {
		this.bombX=bombX;
	}
	public void setBombY(int bombY){
		this.bombY=bombY;
	}
	public int getBombTiming() {
		return this.bombTiming;
	}
	public boolean getBombVisible() {
		return this.bombVisible;
	}
	public boolean getBombAvailable() {
		return this.bombAvailable;
	}
	public int getBombsSpriteCounter() {
		return this.bombSpriteCounter;
	}
	public int getBombX() {
		return bombX;
	}
	public int getBombY() {
		return bombY;
	}
	public int getRange() {
		return this.range;
	}
	//Arrays for the explosion animation on the central position and the directions
	public void setSpriteExplosion() {
		this.spriteExplosion=new String[6];	
		this.spriteExplosion[0]="explosion_C3.gif";
		this.spriteExplosion[1]="explosion_C3.gif";
		this.spriteExplosion[2]="explosion_C1.gif";
		this.spriteExplosion[3]="explosion_C1.gif";
		this.spriteExplosion[4]="explosion_C2.gif";
		this.spriteExplosion[5]="explosion_C2.gif";
	}
	public void setSpriteExplosionRight() {
		this.spriteExplosionRight=new String[6];	
		this.spriteExplosionRight[0]="explosion_E3.gif";
		this.spriteExplosionRight[1]="explosion_E3.gif";
		this.spriteExplosionRight[2]="explosion_E1.gif";
		this.spriteExplosionRight[3]="explosion_E1.gif";
		this.spriteExplosionRight[4]="explosion_E2.gif";
		this.spriteExplosionRight[5]="explosion_E2.gif";
	}
	public void setSpriteExplosionLeft() {
		this.spriteExplosionLeft=new String[6];	
		this.spriteExplosionLeft[0]="explosion_W3.gif";
		this.spriteExplosionLeft[1]="explosion_W3.gif";
		this.spriteExplosionLeft[2]="explosion_W1.gif";
		this.spriteExplosionLeft[3]="explosion_W1.gif";
		this.spriteExplosionLeft[4]="explosion_W2.gif";
		this.spriteExplosionLeft[5]="explosion_W2.gif";
	}
	public void setSpriteExplosionUp() {
		this.spriteExplosionUp=new String[6];	
		this.spriteExplosionUp[0]="explosion_N3.gif";
		this.spriteExplosionUp[1]="explosion_N3.gif";
		this.spriteExplosionUp[2]="explosion_N1.gif";
		this.spriteExplosionUp[3]="explosion_N1.gif";
		this.spriteExplosionUp[4]="explosion_N2.gif";
		this.spriteExplosionUp[5]="explosion_N2.gif";
	}
	public void setSpriteExplosionDown() {
		this.spriteExplosionDown=new String[6];	
		this.spriteExplosionDown[0]="explosion_S3.gif";
		this.spriteExplosionDown[1]="explosion_S3.gif";
		this.spriteExplosionDown[2]="explosion_S1.gif";
		this.spriteExplosionDown[3]="explosion_S1.gif";
		this.spriteExplosionDown[4]="explosion_S2.gif";
		this.spriteExplosionDown[5]="explosion_S2.gif";
	}
	public void setSpriteExplosionVertical() {
		this.spriteExplosionVertical=new String[6];	
		this.spriteExplosionVertical[0]="explosion_V3.gif";
		this.spriteExplosionVertical[1]="explosion_V3.gif";
		this.spriteExplosionVertical[2]="explosion_V1.gif";
		this.spriteExplosionVertical[3]="explosion_V1.gif";
		this.spriteExplosionVertical[4]="explosion_V2.gif";
		this.spriteExplosionVertical[5]="explosion_V2.gif";
	}
	public void setSpriteExplosionHorizontal() {
		this.spriteExplosionHorizontal=new String[6];	
		this.spriteExplosionHorizontal[0]="explosion_H3.gif";
		this.spriteExplosionHorizontal[1]="explosion_H3.gif";
		this.spriteExplosionHorizontal[2]="explosion_H1.gif";
		this.spriteExplosionHorizontal[3]="explosion_H1.gif";
		this.spriteExplosionHorizontal[4]="explosion_H2.gif";
		this.spriteExplosionHorizontal[5]="explosion_H2.gif";
	}
	//Sets the array for the Bombs sprite animation
	public void setSpriteBombs() {
		this.spriteBombs=new String[2];
		this.spriteBombs[0]="bomb1.gif";
		this.spriteBombs[1]="bomb2.gif";
	}
	public void setRange(int range) {
		if (range<=5) {
			this.range=range;
		}
		else {
			this.range=5; //the max value of the range
		}
	}	
	//getters of the different sprites that will automatically take the position of the array that the counter points.
	public String getSpriteExplosion() {
		return this.spriteExplosion[this.bombSpriteCounter];
	}
	public String getSpriteExplosionRight() {
		return this.spriteExplosionRight[this.bombSpriteCounter];
	}
	public String getSpriteExplosionLeft() {
		return this.spriteExplosionLeft[this.bombSpriteCounter];
	}
	public String getSpriteExplosionUp() {
		return this.spriteExplosionUp[this.bombSpriteCounter];
	}
	public String getSpriteExplosionDown() {
		return this.spriteExplosionDown[this.bombSpriteCounter];
	}
	public String getSpriteExplosionVertical() {
		return this.spriteExplosionVertical[this.bombSpriteCounter];
	}
	public String getSpriteExplosionHorizontal() {
		return this.spriteExplosionHorizontal[this.bombSpriteCounter];
	}
	public String getSpriteBombs() {
		return this.spriteBombs[this.bombSpriteCounter];		
	}
}
