package main;
public class Player {

	private String name = "Player"; //name of the player for the interface
	private int x;
	private int y;
	private int health;
	private int speed;
	private boolean alive;
	private String [] spriteRight; //array of images of the movement to the right
	private String [] spriteLeft;
	private String [] spriteUp;
	private String [] spriteDown;
	private int spriteCounter ; //this counter will make possible the change between movement sprites into the arrays
	private Bombs [] bomb;	//array of bombs that the player will have
	private int currentBombs = 1;	//default number of bombs
	private int interfaceBombs=1; 	//this field will count the number of available bombs we have at each moment (for the interface)
	private boolean hasRemoteControl =false; //variable that makes possible the Remote control mechanic. True if we get that bonus
	private int currentPoints;	
	private boolean godMode=false; 	//field used for the invencibility command
	private boolean cloak = false; 	//field used for the invisibility command
	private boolean fly = false; 	//field used for the flying command
	private boolean superCoolSecretThing=false;
	//normal setters and getters
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setX (int x) {
		this.x = x;
	}
	public void setY (int y) {
		this.y = y;
	}
	public void setSpeed (int speed) {
		this.speed = speed;
	}
	public void setSpriteCounter (int spriteCounter) {
		this.spriteCounter = spriteCounter;
	}
	public void setHasRemoteControl(boolean control) {
		this.hasRemoteControl=control;
	}	
	
	public void setCurrentPoints(int currentPoints) {
		this.currentPoints = currentPoints;
	}
	
	public void setInterfaceBombs(int interfaceBombs) {
		this.interfaceBombs = interfaceBombs;
	}
	public boolean isGodMode() {
		return godMode;
	}
	public void setGodMode(boolean godMode) {
		this.godMode = godMode;
	}	
	public boolean isCloak() {
		return cloak;
	}
	public void setCloak(boolean cloak) {
		this.cloak = cloak;
	}	
	public boolean isFly() {
		return fly;
	}
	public void setFly(boolean fly) {
		this.fly = fly;
	}
	public boolean isSuperCoolSecretThing() {
		return superCoolSecretThing;
	}
	public void setSuperCoolSecretThing(boolean superCoolSecretThing) {
		this.superCoolSecretThing = superCoolSecretThing;
	}
	public int getX () {
		return this.x;
	}
	public int getY() {
		return this.y;
	}
	public int getHealth() {
		return this.health;
	}
	public int getSpeed() {
		return this.speed;
	}
	public boolean getAlive () {
		if (this.health <=0) {
			return false;
		}
		else {
			return alive;
		}
	}
	public int getCurrentPoints() {
		return currentPoints;
	}
	public boolean getHasRemoteControl() {
		return this.hasRemoteControl;
	}
	public int getSpriteCounter() {
		return this.spriteCounter;
	}
	public int getInterfaceBombs() {
		return interfaceBombs;
	}
	public int getCurrentBombs() {
		return currentBombs;
	}
	public void setCurrentBombs(int currentBombs) {
		this.currentBombs = currentBombs;
	}
	public String getSpriteRight() {
		return this.spriteRight [this.spriteCounter]; 
	}
	public String getSpriteLeft() {
		return this.spriteLeft[this.spriteCounter]; 
	}
	public String getSpriteUp() {
		return this.spriteUp [this.spriteCounter]; 
	}
	public String getSpriteDown() {
		return this.spriteDown [this.spriteCounter]; 
	}
	public Bombs[] getBomb() {
		return this.bomb;
	}
	public boolean getBombAvailable(int ii) {
		return (getBomb()[ii].getBombAvailable()==true);
	}
//	This set methods makes the arrays of images that makes the animation possible
	public void setSpriteRight () {
		this.spriteRight = new String [5];
		this.spriteRight [0] = "bomberman131.png";
		this.spriteRight [1] = "bomberman132.png";
		this.spriteRight [2] = "bomberman133.png";
		this.spriteRight [3] = "bomberman134.png";
		this.spriteRight [4] = "bomberman135.png";
	}
	public void setSpriteLeft () {
		this.spriteLeft = new String [5];
		this.spriteLeft [0] = "bomberman121.png";
		this.spriteLeft [1] = "bomberman122.png";
		this.spriteLeft [2] = "bomberman123.png";
		this.spriteLeft [3] = "bomberman124.png";
		this.spriteLeft [4] = "bomberman125.png";
	}
	public void setSpriteUp () {
		this.spriteUp = new String [5];
		this.spriteUp [0] = "bomberman101.png";
		this.spriteUp [1] = "bomberman102.png";
		this.spriteUp [2] = "bomberman103.png";
		this.spriteUp [3] = "bomberman104.png";
		this.spriteUp [4] = "bomberman105.png";
	}
	public void setSpriteDown () {
		this.spriteDown = new String [5];
		this.spriteDown [0] = "bomberman111.png";
		this.spriteDown [1] = "bomberman112.png";
		this.spriteDown [2] = "bomberman113.png";
		this.spriteDown [3] = "bomberman114.png";
		this.spriteDown [4] = "bomberman115.png";
	}
	//Method that sets all that's necessary for the bombs
	public void setBombs() {
		this.bomb= new Bombs [31];
		for(int i=0;i<this.bomb.length;i++) {
			this.bomb[i]=new Bombs();

		}
		this.bomb[0].setBombAvailable(true); //setting the first bomb as we'll always have one
	}	
	public void setHealth (int health) {
		if (godMode==false){ //if god command is activated, health wont go down
			this.health = health;
		}
	}
	public void setNewBombs () {
		this.bomb[this.currentBombs].setBombAvailable(true); //currentBombs starts from 1 and they wont reach 32 because of game design (15 levels, 2 bomb bonus per level, +1 bomb we have by default)
		this.currentBombs +=1;
	}
	//this method will sum 1 in the range. Designed for the fire bonus.
	public void amplifyRange() {
		for (int ii=0; ii<getBomb().length; ii++) {
			this.bomb[ii].setRange(((this.bomb[ii]).getRange()+1));
		}
	}
	//this method will sum 1 point of speed
	public void amplifySpeed() {
		if(this.speed<10) {
			this.speed +=1;
		}
		//even if because of level design speed wont ever be 10, we should limit it on the code
	}
	//this method will reduce the speed of the player to its minimum
	public void reduceSpeed() {
		this.speed=1;
	}
	public void superAmplifyRange() {
		for (int ii=0; ii<getBomb().length; ii++) {
			this.bomb[ii].setRange(((this.bomb[ii]).getRange()+5)); //the setRange method will make that the value wont overpass 5
		}
	}
	public void setBommbXCoord(int i) {
		this.bomb[i].setBombX( (int)(getX()*0.1) );
	}
	public void setBommbYCoord(int i) {
		this.bomb[i].setBombY((int)((getY()-1)*0.1) );
	}
	
	//method used to simplify bomb's placing on the main class
	public void placeBomb() {
		boolean control = false;
		for (int i = 0; i < getBomb().length && control == false; i++) {

			if (getBombAvailable(i)) { // check the number of bombs you can put simultaneusly			

				getBomb()[i].setBombVisible(true);				//change the variable we will use to set bombs visible
				setBommbXCoord(i);
				setBommbYCoord(i);
				getBomb()[i].setBombAvailable(false);			//decreases the number of bombs we have left
				getBomb()[i].setBombTiming(100);				//Start the countdown for the bomb to explode
				getBomb()[i].setRangeRight(getBomb()[i].getRange());	//calculate the range that the bomb will have for each direction
				getBomb()[i].setRangeLeft(getBomb()[i].getRange());
				getBomb()[i].setRangeDown(getBomb()[i].getRange());
				getBomb()[i].setRangeUp(getBomb()[i].getRange());				
				interfaceBombs-=1;								//decreases by one the interface number of bombs we can use at this moment.
				control = true;									//this control variable helps us stopping the loop as soon as it finds one bomb available.
			}
		}	
	}
	public boolean getBombActivated(int i) {
		return (getBomb()[i].getBombTiming()>20 && getBomb()[i].getBombVisible()==true);	//conditions that check if the bomb has reached its time to explode.
	}
	public void activatedBomb(int i) {			//Method that animates the bomb spite and decrease the time remaining
		getBomb()[i].setBombsSpriteCounter((getBomb()[i].getBombsSpriteCounter()+1)%2);		 //moving between the arrays of images
		if(getHasRemoteControl()==false) {		//if we have the remote the countdown wont decrease and it will explode just if we press "tabulator".
			getBomb()[i].setBombTiming(getBomb()[i].getBombTiming()-1);				//decreases the time remaning
		}
	}
	
	public boolean getExplodingBomb(int i) {//we use this methods to simplify Ifs conditions.
		return (getBomb()[i].getBombVisible()==true && getBomb()[i].getBombTiming()>0 && getBomb()[i].getBombTiming()<=20);	//check if a bomb is exploding and that the timer isn't 0 (time to end the explosion).
	}
	public boolean getBombExploded(int i) {//we use this methods to simplify Ifs conditions.
		return (getBomb()[i].getBombVisible()==true && getBomb()[i].getBombTiming()==0);	//check if the timer of a bomb is 0 so it has ended the explosion animation.
	}
	public void explodeBombs(int i) {
		getBomb()[i].setBombAvailable(true);	//as this bomb is not in the board anymore it increases our number of bombs by 1; 
		getBomb()[i].setBombVisible(false);		
		getBomb()[i].setBombsSpriteCounter(0); //reboot the counter for the bomb sprite animation
		setBommbXCoord(0);
		setBommbYCoord(0);
	}
	public void moveRight() {
		setX( getX() + getSpeed() );	//changes the coordinates of the coordiates depending of the direction and the current speed of the player.
		setSpriteCounter(( getSpriteCounter() + 1)%5);	//alternate the counter for the sprite images that will animate the movement of the player.
	}
	public void moveLeft() {
		setX(getX() - getSpeed() );
		setSpriteCounter(((getSpriteCounter()+1))%5);
	}
	public void moveUp() {
		setY( getY() - getSpeed() );
		setSpriteCounter(( getSpriteCounter() + 1)%5);
	}
	public void moveDown() {
		setY( getY() + getSpeed() );
		setSpriteCounter(( getSpriteCounter() + 1)%5);
	}
	//These methods will check if the player is in the range of the explosion, for the four directions
	public void checkPlayerExplosionRight (int i, int k) {
		if( (getX()/10) == (getBomb()[i].getBombX() + k) && ((getY()-1)/10) == (getBomb()[i].getBombY())) {
			setHealth(getHealth()-5);
		}
	}
	public void checkPlayerExplosionLeft (int i, int k) {
		if( (getX()/10) == (getBomb()[i].getBombX() - k) && ((getY()-1)/10) == (getBomb()[i].getBombY())) {
			setHealth(getHealth()-5);
		}
	}
	public void checkPlayerExplosionUp (int i, int k) {
		if( (getX()/10) == (getBomb()[i].getBombX() ) && ((getY()-1)/10) == (getBomb()[i].getBombY()-k)) {
			setHealth(getHealth()-5);
		}
	}
	public void checkPlayerExplosionDown (int i, int k) {
		if( (getX()/10) == (getBomb()[i].getBombX() ) && ((getY()-1)/10) == (getBomb()[i].getBombY()+k)) {
			setHealth(getHealth()-5);
		}
	}
	//Full-constructor
	public Player () {
		setSpriteRight();
		setSpriteDown();
		setSpriteLeft();
		setSpriteUp();
		setBombs();		
		this.health = 100;
		this.speed = 1; //1 is default, it won't reach 10 because of level design
		this.alive = true;
		this.currentBombs=1;
	}
}