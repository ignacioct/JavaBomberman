/** 
 * @author Ignacio Talavera Cepeda, Luis Rodriguez Rubio
 * @since November 2017
 * @version 1.0	
 */
package main;
import java.util.Locale;
import edu.uc3m.game.GameBoardGUI;
public class Main {
	public static Board board = new Board();
	public static GameBoardGUI boardGUI = new GameBoardGUI(board.getSize(), board.getSize());
	public static Player player = new Player();
	public static void main(String[] args) throws InterruptedException {
		createInterface();
		int level;
		boolean newGame=false; //used for the new button to restart the game
		boolean levelEnd=false; //control variable for the while loop
		String lastAction = boardGUI.gb_getLastAction();
		//For loop with all the Main class inside. Used for keeping separated the levels
		//Each level is loaded when charged
		for(level=0;level<15 && player.getAlive();level++) {
			levelEnd=false; //control variable for the while loop
			if (newGame == true) { //if the newGame boolean is set on true with the new button the level will go back to zero, rebooting the game
				level=0;
				player.setCurrentPoints(0);
				createInterface();
			}
			//Variables to manage how much time do we spend in each level in order to give extra points or not (if we pass the level in less than 120 seconds we'll get extra points)
			long startTime = System.currentTimeMillis();
			long endTime=0;
			long totalTime =0;
			int initialHealth=player.getHealth();
			createBoard(level);			
			initializeSprites();
			//Declaration of the player initial positioning that has to refresh every new level
			player.setX(15);
			player.setY(20);
			//Infinite while loop for movement structure
			while (levelEnd==false && player.getAlive()) { 	//This loop will keep repeating until the player dies or we step onto the door in order to change the current level.
				lastAction = boardGUI.gb_getLastAction();	//we need to refresh the value of the lastAction variable to be accurated to what we want to do every moment.				
				boardGUI.gb_moveSpriteCoord(1, player.getX() - 6, player.getY() - 10);	//This tiny movement avoid overlapping of the bomb. It moves the player to the left and right very quickly, keeping his initial position but putting it before the bomb sprite
				boardGUI.gb_moveSpriteCoord(1, player.getX() - 5, player.getY() - 10);
				//New game button
				if(lastAction.length()>=8 && lastAction.substring(0,8).equals("new game")) {
					//when pressing the new button, we will receive one name for the interface,
					//we will reboot the game and create the interface again. 				
					if (lastAction.length()>8) { 
						//if the length is less than 8 the user wouldn´tt have added any name, so theres no point in changing int
						player.setName(lastAction.substring(9, (lastAction.length())));
					}
					newGame=true;
					levelEnd=true;
				}
				//Exit button
				if(lastAction.equals("exit game")){
					player.setHealth(0); //killing the player is the quickest way to end the game
				}
				//God command
				//This command will stop player's life to reach zero
				//if(lastAction.length()>7 && lastAction.substring(0,7).equals("command god")) {
				if(lastAction.equals("command god")) {
					player.setGodMode(true);
				}				
				//+1 level command
				//This command will hide us for the drops
				if(lastAction.equals("command avanti")) {
					levelEnd=true;
				}
				//Invisibility command
				//This command will hide us for the drops
				if(lastAction.equals("command cloak")) {
					player.setCloak(true);
				}				
				//Flying command
				//This command will make walking through walls and bricks possible
				if(lastAction.equals("command nimbus2000")) {
					player.setFly(true);
				}				
				//Supergod command
				//This command will give you fly, invisibility and invecibility
				if(lastAction.equals("command pickle rick")) {
					player.setFly(true);
					player.setCloak(true);
					player.setGodMode(true);
					player.setSuperCoolSecretThing(true);					
				}
				if(lastAction.equals("command stop")) {
					player.setFly(false);
					player.setCloak(false);
					player.setGodMode(false);
					player.setSuperCoolSecretThing(false);					
				}
				if (lastAction.equals("right")) {	//Right movement
					moveRight(level);
				}
				if (lastAction.equals("left")) {	//Left movement
					moveLeft(level);
				}
				if (lastAction.equals("up")) {	//Up movement
					moveUp(level);
				}
				if (lastAction.equals("down")) {	//Down movement
					moveDown(level);
				}
				if (lastAction.equals("tab") && player.getHasRemoteControl()) {
					detonateBombs();
				}
				//Space key used for bomb placing
				if (lastAction.equals("space")) {
					player.placeBomb();
				}
				//For loop that will check if a bomb is on the board and will control the explosions
				for (int i = 0; i < player.getBomb().length; i++) {
					//This if checks if a bomb is already placed.
					if (player.getBombActivated(i)) { // we check if a bomb is on the board and if there is still time remaning for the explosion.
						boardGUI.gb_setSquareImage(player.getBomb()[i].getBombX(), player.getBomb()[i].getBombY(), player.getBomb()[i].getSpriteBombs());	//alternate the bomb image
						player.activatedBomb(i);
					}
					//This if checks if a bomb is about to explode.
					if (player.getExplodingBomb(i)) {
						checkEntitiesMiddle(i);
						board.calculateRightRange(level,player, i);	//Calculate the range of the bomb for the right direction.						
						rightExplosion(level,i);						
						// Making bricks beneath (down) explode
						board.calculateRangeDown(level,player, i);
						downExplosion(level, i);
						//Making bricks to the left explode
						board.calculateRangeLeft(level,player, i);
						leftExplosion(level,i);
						// Making the upper bricks explode
						board.calculateRangeUp(level,player, i);
						upExplosion(level,i);						
						player.getBomb()[i].setBombsSpriteCounter((player.getBomb()[i].getBombsSpriteCounter() + 1) % 6);	//Changes the bomb sprite and decreases the time remaining
						player.getBomb()[i].setBombTiming(player.getBomb()[i].getBombTiming() - 1); 						// decreases the time remaining
					}
					//If the bomb exploded
					if (player.getBombExploded(i)) {
						player.setInterfaceBombs(player.getInterfaceBombs()+1);
						//4 for loops for every direction					
						for (int k = 0; k <= player.getBomb()[i].getRangeRight(); k++) {
							boardGUI.gb_setSquareImage(player.getBomb()[i].getBombX() + k,player.getBomb()[i].getBombY(), null); // if there is a bomb but the explosion timing is 0 it clear it from the board
							//if condition that will deploy the image of a bonus if it is beneath that brick after the explosion
							if(!(board.getCell(level,player.getBomb()[i].getBombY(),player.getBomb()[i].getBombX() + player.getBomb()[i].getRangeRight() ).getBonus().getType().equals("regular"))) {
								showBonusRight(level,i);
							}
						}
						for (int k = 0; k <= player.getBomb()[i].getRangeLeft(); k++) {
							boardGUI.gb_setSquareImage(player.getBomb()[i].getBombX() - k,player.getBomb()[i].getBombY(), null); // if there is a bomb but the explosion timing is 0 it clear it from the board
							//if condition that will deploy the image of a bonus if it is beneath that brick after the explosion
							if(!(board.getCell(level, player.getBomb()[i].getBombY(),player.getBomb()[i].getBombX() - player.getBomb()[i].getRangeLeft()).getBonus().getType().equals("regular"))) {
								showBonusLeft(level,i);
							}
						}
						for (int k = 0; k <= player.getBomb()[i].getRangeDown(); k++) {
							boardGUI.gb_setSquareImage(player.getBomb()[i].getBombX(),player.getBomb()[i].getBombY() + k, null); // if there is a bomb but the explosion timing is 0 it clear it from the board
							//if condition that will deploy the image of a bonus if it is beneath that brick after the explosion
							if(!(board.getCell(level,player.getBomb()[i].getBombY()+ player.getBomb()[i].getRangeDown(),player.getBomb()[i].getBombX()).getBonus().getType().equals("regular"))) {
								showBonusDown(level,i);
							}
						}
						for (int k = 0; k <= player.getBomb()[i].getRangeUp(); k++) {
							boardGUI.gb_setSquareImage(player.getBomb()[i].getBombX(),player.getBomb()[i].getBombY() - k, null); // if there is a bomb but the explosion timing is 0 it clear it from the board
							//if condition that will deploy the image of a bonus if it is beneath that brick after the explosion
							if(!(board.getCell(level,player.getBomb()[i].getBombY()- player.getBomb()[i].getRangeUp(),player.getBomb()[i].getBombX()).getBonus().getType().equals("regular"))) {
								showBonusUp(level,i);
							}
						}
						player.explodeBombs(i);	//refresh conditions in order to use that position of the bombs array again.						
					}
				}
				stepOnBonus(level,levelEnd);	//checks if the player steps on a bonus and gives a diferent thing depending on the type.
				balloonMovement(level);
				dropMovement(level);
				board.checkEnemiesCell(player); //if the player is on the same cell that an enemy, this method will damage the player				
				if(player.getHealth()<initialHealth) {	//we think it's bugged because it doesnt animate anything.
					boardGUI.gb_animateDamage();
				}
				refreshInterface(level);
				Thread.sleep(25);				
			}
			//here we check the time spent in each level
			endTime   = System.currentTimeMillis();
			totalTime = (endTime - startTime)/1000; 
			if(totalTime<=120 && newGame==false) { 	//if total time is less or equal than 2 minutes we will get the extra point. This won't happen if we are using the new game button				
				player.setCurrentPoints(player.getCurrentPoints()+2000);
			}
			for(int t=0;t<board.getSize();t++) {
				for(int y=0;y<board.getSize();y++) {
					boardGUI.gb_setSquareImage(t, y, null); //removing all previous levels images
				}
			}
			boardGUI.gb_setSpriteVisible(1, false); //making the player visible again
			for(int jj=0;jj<board.getnBalloons();jj++) {
				boardGUI.gb_setSpriteVisible(2+jj, false);
			}
			for(int jj=0;jj<board.getnDrops();jj++) {
				boardGUI.gb_setSpriteVisible(20+jj, false);
			}
		}
		//Fading colors for the "Game Over"
		for(int x=0;x<board.getSize();x++) {
			for(int y=0;y<board.getSize();y++) {
				boardGUI.gb_setSquareColor(x, y, 50+3*x, 0, 50+3*y);
			}
		}
		//Console message when the game is over
		boardGUI.gb_showMessageDialog("Game over");
	}	
	public static void createBoard(int level) {
		for (int ii = 0; ii < board.getSize(); ii++) {
			for (int jj = 0; jj < board.getSize(); jj++) {
				if (board.imageCondition(level,ii, jj)) {	//Check if that cell needs a setSquareImage or a setSquareColor.
					boardGUI.gb_setSquareImage(ii, jj, board.getCell(level,jj, ii).getImage());	//get the image depending of the type of cell
				} else {
					boardGUI.gb_setSquareColor(ii, jj, 204, 255, 153); 	// green for regular cells
				}
			}
		}
		board.setnEnemies(level);	//Calculate the number of enemies that are going to appear
		board.setEnemies(level);	//Create the number of enemies of each type we calculated.
	}
	public static void initializeSprites() {

		for(int ii=0;ii<board.getnDrops();ii++) {	//first we initialize the "drop" enemies
			boardGUI.gb_addSprite(20+ii, "enemy211.png", true);	//adding the sprite
			boardGUI.gb_moveSpriteCoord(20+ii, (board.getEnemies()[board.getnBalloons()+ii].getEnemyX()-5), (board.getEnemies()[board.getnBalloons()+ii].getEnemyY()-10));	//move just the drops to their position(we used the getnBalloons to avoid moving that type of enemies in this loop as they are at the beggining of the enemies[] array).
			boardGUI.gb_setSpriteVisible(20+ii, true);	//make drops visible.
		}
		for(int ii=0;ii<board.getnBalloons();ii++) {	//same for for the "balloons" enemies. 
			boardGUI.gb_addSprite(2+ii, "enemy121.png", true);
			boardGUI.gb_moveSpriteCoord(2+ii, (board.getEnemies()[ii].getEnemyX()-5), (board.getEnemies()[ii].getEnemyY()-10));	//As the balloons are at the beggining of the array we can just use the loop variable directly.
			boardGUI.gb_setSpriteVisible(2+ii, true);

		}
		boardGUI.gb_addSprite(1, "bomberman111.png", true);	//we initialize the player Sprite
		boardGUI.gb_moveSpriteCoord(1, player.getX() - 5, (player.getY()) - 10);	//we take it to his position (We had to do some operations so we could equalize the coordinates and the position of the image on the board).
		boardGUI.gb_setSpriteVisible(1, true);
	}
	public static void moveRight(int level) {
		boardGUI.gb_setSpriteImage(1, player.getSpriteRight()); 
		if (player.isFly()==false && board.getCell(level,(int) ((player.getY() - 1) * 0.1), (int) ((player.getX() + player.getSpeed()) * 0.1)).getType().equals("regular")) {	//check if the following cell is "regular" so you can move.
			player.moveRight(); 
		}
		else if(player.isFly() == true){	//if the fly command is activated, it will just move no matter the type of cell
			player.moveRight();
		}
	}
	public static void moveLeft(int level) {
		boardGUI.gb_setSpriteImage(1, player.getSpriteLeft());	//same as right but changes operations
		if (player.isFly()==false && board.getCell(level,(int) ((player.getY() - 1) * 0.1), (int) ((player.getX() - player.getSpeed()) * 0.1)).getType().equals("regular")) {	//check if the following cell is "regular" so you can move.
			player.moveLeft();
		}
		else if(player.isFly() == true){
			player.moveLeft();
		}
	}
	public static void moveUp(int level) {
		boardGUI.gb_setSpriteImage(1, player.getSpriteUp());	//like previous movements methods
		if (player.isFly()==false && board.getCell(level,(int) ((player.getY() - player.getSpeed() - 1) * 0.1), (int) ((player.getX()) * 0.1)).getType().equals("regular")) {	//check if the following cell is "regular" so you can move.
			player.moveUp();
		}
		else if(player.isFly() == true) {
			player.moveUp();
		}
	}
	public static void moveDown(int level) {	
		boardGUI.gb_setSpriteImage(1, player.getSpriteDown());	//like previous movements methods
		if (player.isFly()==false && board.getCell(level,(int) ((player.getY() + player.getSpeed() - 1) * 0.1), (int) ((player.getX()) * 0.1)).getType().equals("regular")) {	//check if the following cell is "regular" so you can move.
			player.moveDown();
		}
		else if(player.isFly() == true) {
			player.moveDown();
		}
	}
	public static void detonateBombs() {	
		for(int i=0;i<player.getBomb().length;i++) {	//for loop that pass over every bomb of the player.
			player.getBomb()[i].setBombTiming(20);		//set the timing of each bomb to 20, value in which the explosion starts to occur.
		}
	}
	public static void checkEntitiesMiddle(int i) {
		boardGUI.gb_setSquareImage(player.getBomb()[i].getBombX(), player.getBomb()[i].getBombY(),player.getBomb()[i].getSpriteExplosion()); //alternate the image of the middle of the explosion

		for(int jj=0;jj<board.getEnemies().length;jj++) {	//gets through every enemy
			if( (player.getBomb()[i].getBombX()) == (board.getEnemies()[jj].getEnemyX()/10) && player.getBomb()[i].getBombY() == (board.getEnemies()[jj].getEnemyY()/10) ) {	//check if any enemy is at the center of the explosion
				board.getEnemies()[jj].setEnemyHealth(board.getEnemies()[jj].getEnemyHealth()-5);	//if true, deals 5 damage per tick.
				killEnemies(jj);
			}
		}
		if( (player.getX()/10) == (player.getBomb()[i].getBombX() ) && ((player.getY()-1)/10) == (player.getBomb()[i].getBombY())) {	//check if the player is in the middle cell of the explosion
			player.setHealth(player.getHealth()-5);	//if true, deals 5 damage per tick.
		}
	}
	public static void killEnemies(int jj) {		
		if(board.getEnemies()[jj].getEnemyHealth()==0) {	//check the health of every enemie.
			player.setCurrentPoints(player.getCurrentPoints()+100);
			board.getEnemies()[jj].setEnemyAlive(false);	//this will make enemies stop hurting when the die
			if(jj<board.getnBalloons()) {					
				boardGUI.gb_setSpriteVisible(2+jj, false);	//if a balloon has no health it removes it from the board
			}
			else {
				boardGUI.gb_setSpriteVisible(20+jj-board.getnBalloons(), false);	//if a drop has no health it removes it from the board (We had to use the nBaloons again in order to make the loop only afect to the drops enemies.
			}
		}
	}
	public static void rightExplosion(int level, int i) {
		//Making bricks on the right explode. The for loop creates the images of the horizontal explosion and the if creates the image of the end of the range.
		for (int k = 1; k <= player.getBomb()[i].getRangeRight(); k++) {
			boardGUI.gb_setSquareColor(player.getBomb()[i].getBombX() + k, player.getBomb()[i].getBombY(), 204, 255, 153);	//change to regular the affected cells
			board.getCell(level,player.getBomb()[i].getBombY(), player.getBomb()[i].getBombX() + k).setType("regular");
			boardGUI.gb_setSquareImage((player.getBomb()[i].getBombX()) + k, player.getBomb()[i].getBombY(), player.getBomb()[i].getSpriteExplosionHorizontal());
			//Checking if enemies in range, lowering health and killing them if their health goes to 0
			for(int jj=0;jj<board.getEnemies().length;jj++) {								
				if( (player.getBomb()[i].getBombX() + k) == board.getEnemies()[jj].getEnemyX()/10 && player.getBomb()[i].getBombY() == board.getEnemies()[jj].getEnemyY()/10 ) {
					board.getEnemies()[jj].setEnemyHealth(board.getEnemies()[jj].getEnemyHealth()-5);
					killEnemies(jj);
				}
				player.checkPlayerExplosionRight(i, k); //These methods will check if the player is in the range of the explosion, for the four directions
			}
		}		
		if (player.getBomb()[i].getRangeRight() != 0) {
			boardGUI.gb_setSquareColor(player.getBomb()[i].getBombX() + player.getBomb()[i].getRangeRight(),player.getBomb()[i].getBombY(), 204, 255, 153);
			board.getCell(level,player.getBomb()[i].getBombY(),player.getBomb()[i].getBombX() + player.getBomb()[i].getRangeRight()).setType("regular");
			boardGUI.gb_setSquareImage((player.getBomb()[i].getBombX()) + player.getBomb()[i].getRangeRight(),player.getBomb()[i].getBombY(), player.getBomb()[i].getSpriteExplosionRight());
		}
	}
	public static void downExplosion(int level, int i) {
		for (int k = 1; k <= player.getBomb()[i].getRangeDown(); k++) {	//works as the other directions
			boardGUI.gb_setSquareColor(player.getBomb()[i].getBombX(), player.getBomb()[i].getBombY() + k,204, 255, 153);
			board.getCell(level,player.getBomb()[i].getBombY() + k, player.getBomb()[i].getBombX()).setType("regular");
			boardGUI.gb_setSquareImage((player.getBomb()[i].getBombX()), player.getBomb()[i].getBombY() + k,player.getBomb()[i].getSpriteExplosionVertical());
			for(int jj=0;jj<board.getEnemies().length;jj++) {								
				if( (player.getBomb()[i].getBombX()) == board.getEnemies()[jj].getEnemyX()/10 && player.getBomb()[i].getBombY() + k == board.getEnemies()[jj].getEnemyY()/10 ) {
					board.getEnemies()[jj].setEnemyHealth(board.getEnemies()[jj].getEnemyHealth()-5);
					killEnemies(jj);
				}
			}
			player.checkPlayerExplosionDown(i, k); //These methods will check if the player is in the range of the explosion, for the four directions
		}
		if (player.getBomb()[i].getRangeDown() != 0) {
			boardGUI.gb_setSquareColor(player.getBomb()[i].getBombX(),player.getBomb()[i].getBombY() + player.getBomb()[i].getRangeDown(), 204, 255, 153);
			board.getCell(level,player.getBomb()[i].getBombY() + player.getBomb()[i].getRangeDown(),player.getBomb()[i].getBombX()).setType("regular");
			boardGUI.gb_setSquareImage((player.getBomb()[i].getBombX()),player.getBomb()[i].getBombY() + player.getBomb()[i].getRangeDown(),player.getBomb()[i].getSpriteExplosionDown());
		}
	}
	public static void leftExplosion(int level, int i) {
		for (int k = 1; k <= player.getBomb()[i].getRangeLeft(); k++) {	//works as the other directions
			boardGUI.gb_setSquareColor(player.getBomb()[i].getBombX() - k, player.getBomb()[i].getBombY(),204, 255, 153);
			board.getCell(level,player.getBomb()[i].getBombY(), player.getBomb()[i].getBombX() - k).setType("regular");
			boardGUI.gb_setSquareImage((player.getBomb()[i].getBombX()) - k, player.getBomb()[i].getBombY(),player.getBomb()[i].getSpriteExplosionHorizontal());
			for(int jj=0;jj<board.getEnemies().length;jj++) {								
				if( (player.getBomb()[i].getBombX() - k) == board.getEnemies()[jj].getEnemyX()/10 && player.getBomb()[i].getBombY() == board.getEnemies()[jj].getEnemyY()/10 ) {
					board.getEnemies()[jj].setEnemyHealth(board.getEnemies()[jj].getEnemyHealth()-5);
					killEnemies(jj);
				}
			}
			player.checkPlayerExplosionLeft(i, k);//These methods will check if the player is in the range of the explosion, for the four directions
		}
		if (player.getBomb()[i].getRangeLeft() != 0) {
			boardGUI.gb_setSquareColor(player.getBomb()[i].getBombX() - player.getBomb()[i].getRangeLeft(),player.getBomb()[i].getBombY(), 204, 255, 153);
			board.getCell(level,player.getBomb()[i].getBombY(),player.getBomb()[i].getBombX() - player.getBomb()[i].getRangeLeft()).setType("regular");
			boardGUI.gb_setSquareImage((player.getBomb()[i].getBombX()) - player.getBomb()[i].getRangeLeft(),player.getBomb()[i].getBombY(), player.getBomb()[i].getSpriteExplosionLeft());
		}
	}
	public static void upExplosion(int level, int i) {
		for (int k = 1; k <= player.getBomb()[i].getRangeUp(); k++) {	//works as the other directions
			boardGUI.gb_setSquareColor(player.getBomb()[i].getBombX(), player.getBomb()[i].getBombY() - k,204, 255, 153);
			board.getCell(level,player.getBomb()[i].getBombY() - k, player.getBomb()[i].getBombX()).setType("regular");
			boardGUI.gb_setSquareImage((player.getBomb()[i].getBombX()), player.getBomb()[i].getBombY() - k,player.getBomb()[i].getSpriteExplosionVertical());
			for(int jj=0;jj<board.getEnemies().length;jj++) {								
				killEnemies(jj);
			}
			player.checkPlayerExplosionUp(i, k);//These methods will check if the player is in the range of the explosion, for the four directions
		}
		if (player.getBomb()[i].getRangeUp() != 0) {
			boardGUI.gb_setSquareColor(player.getBomb()[i].getBombX(),player.getBomb()[i].getBombY() - player.getBomb()[i].getRangeUp(), 204, 255, 153);
			board.getCell(level,player.getBomb()[i].getBombY() - player.getBomb()[i].getRangeUp(),player.getBomb()[i].getBombX()).setType("regular");
			boardGUI.gb_setSquareImage((player.getBomb()[i].getBombX()),player.getBomb()[i].getBombY() - player.getBomb()[i].getRangeUp(),player.getBomb()[i].getSpriteExplosionUp());
		}
	}
	public static void showBonusRight(int level, int i) {	//just simplifyes the expresion to show the image of a bonus at each direction of the bomb
		boardGUI.gb_setSquareImage(player.getBomb()[i].getBombX() + player.getBomb()[i].getRangeRight(),player.getBomb()[i].getBombY(), board.getCell(level,player.getBomb()[i].getBombY(),player.getBomb()[i].getBombX() + player.getBomb()[i].getRangeRight() ).getBonus().getImage());
	}
	public static void showBonusLeft(int level, int i) {
		boardGUI.gb_setSquareImage(player.getBomb()[i].getBombX() - player.getBomb()[i].getRangeLeft(),player.getBomb()[i].getBombY(), board.getCell(level,player.getBomb()[i].getBombY(),player.getBomb()[i].getBombX() - player.getBomb()[i].getRangeLeft() ).getBonus().getImage());
	}
	public static void showBonusDown(int level, int i) {
		boardGUI.gb_setSquareImage(player.getBomb()[i].getBombX(),player.getBomb()[i].getBombY() + player.getBomb()[i].getRangeDown(), board.getCell(level,player.getBomb()[i].getBombY() + player.getBomb()[i].getRangeDown(),player.getBomb()[i].getBombX() ).getBonus().getImage());
	}
	public static void showBonusUp(int level, int i) {
		boardGUI.gb_setSquareImage(player.getBomb()[i].getBombX(),player.getBomb()[i].getBombY() - player.getBomb()[i].getRangeUp(), board.getCell(level,player.getBomb()[i].getBombY() - player.getBomb()[i].getRangeUp(),player.getBomb()[i].getBombX()).getBonus().getImage());
	}		
	public static void balloonMovement(int level) {
		for(int jj=0;jj<board.getnBalloons();jj++) {
			if(board.getEnemies()[jj].getDirectionCounter()<=0) {	//we added a counter of moves a balloon does before changing direction so they can travel more distance.
				board.getEnemies()[jj].setBalloonDirection((int)(Math.random()*4));	//the direction is chosen randomly.
				board.getEnemies()[jj].setDirectionCounter(10);
			}
			else board.getEnemies()[jj].setDirectionCounter(board.getEnemies()[jj].getDirectionCounter()-1);					
			//each if checks the direction and if in that direction there is a brick, wall or a bomb.
			if(board.getEnemies()[jj].getBalloonDirection()==0 && board.checkBombsPosition(player, jj) && board.getCell(level, ((board.getEnemies()[jj].getEnemyY()+1)/10), (board.getEnemies()[jj].getEnemyX()/10)).getType().equals("regular")) {
				board.getEnemies()[jj].moveBalloonDown();	
				boardGUI.gb_setSpriteImage(2+jj, board.getEnemies()[jj].getSpriteBalloonLeft());
			}
			else if(board.getEnemies()[jj].getBalloonDirection()==1 && board.checkBombsPosition(player, jj) && board.getCell(level, (board.getEnemies()[jj].getEnemyY()-1)/10, board.getEnemies()[jj].getEnemyX()/10).getType().equals("regular")) {
				board.getEnemies()[jj].moveBalloonUp();
				boardGUI.gb_setSpriteImage(2+jj, board.getEnemies()[jj].getSpriteBalloonRight());
			}
			else if(board.getEnemies()[jj].getBalloonDirection()==2 && board.checkBombsPosition(player, jj) && board.getCell(level, (board.getEnemies()[jj].getEnemyY())/10, (board.getEnemies()[jj].getEnemyX()+1)/10).getType().equals("regular")) {
				board.getEnemies()[jj].moveBalloonRight();
				boardGUI.gb_setSpriteImage(2+jj, board.getEnemies()[jj].getSpriteBalloonRight());
			}
			else if(board.getEnemies()[jj].getBalloonDirection()==3 && board.checkBombsPosition(player, jj) && board.getCell(level, (board.getEnemies()[jj].getEnemyY())/10, (board.getEnemies()[jj].getEnemyX()-1)/10).getType().equals("regular")){
				board.getEnemies()[jj].moveBalloonLeft();
				boardGUI.gb_setSpriteImage(2+jj, board.getEnemies()[jj].getSpriteBalloonLeft());
			}
			boardGUI.gb_moveSpriteCoord(2+jj, board.getEnemies()[jj].getEnemyX()-5, board.getEnemies()[jj].getEnemyY()-10);
		}
	}
	public static void dropMovement(int level) {
		for(int jj=0;jj<board.getnDrops();jj++) {
			if (player.isCloak()==false) {	//this condition is for a command.
				//for the drop movement we substract the player X and Y coordinate with the ememies ones and depending of the sing (grater or smaller than 0) it will move towards one direction or the opposite.
				if(board.checkBombsPosition(player, board.getnBalloons()+jj) && player.getX()-board.getEnemies()[board.getnBalloons()+jj].getEnemyX()<0 && board.getCell(level, (board.getEnemies()[board.getnBalloons()+jj].getEnemyY()-1)/10, (board.getEnemies()[board.getnBalloons()+jj].getEnemyX()-1)/10).getType().equals("regular")) {
					board.getEnemies()[board.getnBalloons()+jj].setEnemyX(board.getEnemies()[board.getnBalloons()+jj].getEnemyX()-1);
					boardGUI.gb_setSpriteImage(20+jj, board.getEnemies()[board.getnBalloons()+jj].getSpriteDropLeft());
				}
				if(board.checkBombsPosition(player, board.getnBalloons()+jj) && player.getX()-board.getEnemies()[board.getnBalloons()+jj].getEnemyX()>0 && board.getCell(level, (board.getEnemies()[board.getnBalloons()+jj].getEnemyY()-1)/10, (board.getEnemies()[board.getnBalloons()+jj].getEnemyX()+1)/10).getType().equals("regular")) {
					board.getEnemies()[board.getnBalloons()+jj].setEnemyX(board.getEnemies()[board.getnBalloons()+jj].getEnemyX()+1);
					boardGUI.gb_setSpriteImage(20+jj, board.getEnemies()[board.getnBalloons()+jj].getSpriteDropRight());
				}
				if(board.checkBombsPosition(player, board.getnBalloons()+jj) && player.getY()-board.getEnemies()[board.getnBalloons()+jj].getEnemyY()<0 && board.getCell(level, (board.getEnemies()[board.getnBalloons()+jj].getEnemyY()-1-1)/10, board.getEnemies()[board.getnBalloons()+jj].getEnemyX()/10).getType().equals("regular")) {
					board.getEnemies()[board.getnBalloons()+jj].setEnemyY(board.getEnemies()[board.getnBalloons()+jj].getEnemyY()-1);

					boardGUI.gb_setSpriteImage(20+jj, board.getEnemies()[board.getnBalloons()+jj].getSpriteDropRight());
				}
				if(board.checkBombsPosition(player, board.getnBalloons()+jj) && player.getY()-board.getEnemies()[board.getnBalloons()+jj].getEnemyY()>0 && board.getCell(level, (board.getEnemies()[board.getnBalloons()+jj].getEnemyY()+1-1)/10, board.getEnemies()[board.getnBalloons()+jj].getEnemyX()/10).getType().equals("regular")) {
					board.getEnemies()[board.getnBalloons()+jj].setEnemyY(board.getEnemies()[board.getnBalloons()+jj].getEnemyY()+1);
					boardGUI.gb_setSpriteImage(20+jj, board.getEnemies()[board.getnBalloons()+jj].getSpriteDropLeft());
				}
				boardGUI.gb_moveSpriteCoord(20+jj, board.getEnemies()[board.getnBalloons()+jj].getEnemyX()-5, board.getEnemies()[board.getnBalloons()+jj].getEnemyY()-10);
				board.getEnemies()[board.getnBalloons()+jj].setEnemiesSpriteCounter((board.getEnemies()[board.getnBalloons()+jj].getEnemiesSpriteCounter()+1)%3);
			}
		}
	}
	public static void refreshInterface(int level) {
		boardGUI.gb_setValueAbility1(player.getBomb()[0].getRange()); //we will always, at least, have 1 bomb, and the range for all the bombs will be the same
		boardGUI.gb_setValueAbility2(player.getSpeed());	//each parameter has a variable that has to refresh on each loop of the while.
		boardGUI.gb_setValueLevel(level+1);
		boardGUI.gb_setValueHealthCurrent(player.getHealth());
		boardGUI.gb_setValuePointsDown(player.getInterfaceBombs());
		boardGUI.gb_setValuePointsUp(player.getCurrentPoints());
		boardGUI.gb_setTextPlayerName(player.getName());
		if (player.isSuperCoolSecretThing()==true) {
			boardGUI.gb_setPortraitPlayer("Rick_Sprite.png");
		}
		else {
			boardGUI.gb_setPortraitPlayer("White_Bomberman_R.png");
		}
	}
	public static void stepOnBonus(int level, boolean levelEnd) {
		//If the player steps into a bomb bonus
		if(board.getCell(level,((int)((player.getY()-1)*0.1)),((int)((player.getX())*0.1)) ).getBonus().getType().equals("bomb")){
			removeBonus(level);
			player.setNewBombs();
			player.setInterfaceBombs(player.getInterfaceBombs()+1);
			boardGUI.gb_println("Bomb bonus obtained!");
			boardGUI.gb_println("+1 bomb can be used");
		}
		if(board.getCell(level,((int)((player.getY()-1)*0.1)),((int)((player.getX())*0.1)) ).getBonus().getType().equals("fire")) {
			removeBonus(level);
			player.amplifyRange();
			boardGUI.gb_println("Fire bonus obtained!");
			boardGUI.gb_println("+1 range on your bombs");
		}
		if(board.getCell(level,((int)((player.getY()-1)*0.1)),((int)((player.getX())*0.1)) ).getBonus().getType().equals("fullFire")) {
			removeBonus(level);
			player.superAmplifyRange();
			boardGUI.gb_println("Full fire obtained!");
			boardGUI.gb_println("Bomb's range setted to max!");
		} 
		if(board.getCell(level,((int)((player.getY()-1)*0.1)),((int)((player.getX())*0.1)) ).getBonus().getType().equals("skate")) {
			removeBonus(level);
			player.amplifySpeed();
			boardGUI.gb_println("Skate bonus obtained!");
			boardGUI.gb_println("+1 player's speed obtained");
		} 
		if(board.getCell(level,((int)((player.getY()-1)*0.1)),((int)((player.getX())*0.1)) ).getBonus().getType().equals("geta")) {
			removeBonus(level);
			player.reduceSpeed();
			boardGUI.gb_println("Geta bonus obtained!");
			boardGUI.gb_println("-1 player's speed obtained");
		} 
		if(board.getCell(level,((int)((player.getY()-1)*0.1)),((int)((player.getX())*0.1)) ).getBonus().getType().equals("remoteControl")) {
			removeBonus(level);
			player.setHasRemoteControl(true);
			boardGUI.gb_println("Remote control bonus obtained!");
			boardGUI.gb_println("Now you control your bombs with tab");
		} 
		if(board.getCell(level,((int)((player.getY()-1)*0.1)),((int)((player.getX())*0.1)) ).getBonus().getType().equals("door")) {
			//If not all enemies are dead, the door won't be useful
			if(board.getAliveEnemies()!=0) {
				boardGUI.gb_println("You need to kill all enemies before going to the next level.");
			}
			else {
				removeBonus(level);
				player.setCurrentPoints(player.getCurrentPoints()+1000);
				levelEnd=true;
			}
		}
	}
	public static void removeBonus(int level) {
		boardGUI.gb_setSquareImage(((int)(player.getX()*0.1)), ((int)((player.getY()-1)*0.1)), null);	//remove any image in that cell and remove the bonus type it had stored.
		board.getCell(level,((int)((player.getY()-1)*0.1)),((int)(player.getX()*0.1)) ).getBonus().setType("regular");
	}
	public static void createInterface() {
		Locale.setDefault(new Locale("en")); // Main language -> english		
		boardGUI.setVisible(true);
		boardGUI.gb_setTextPointsUp("Points");		//Interface
		boardGUI.gb_setValuePointsUp(0);
		boardGUI.gb_setTextAbility1("Range");
		boardGUI.gb_setTextAbility2("Speed");
		boardGUI.gb_setTextPlayerName("Player");
		boardGUI.gb_setValueHealthMax(100);
		boardGUI.gb_setValueHealthCurrent(100); //it starts with full health	
		boardGUI.gb_setPortraitPlayer("White_Bomberman_R.png");
		boardGUI.gb_setTextPointsDown("Bombs");
		//Command reminder
		boardGUI.gb_println("List of useful commands:");
		boardGUI.gb_println("'avanti' for +1 level");
		boardGUI.gb_println("'god' for invencibility");
		boardGUI.gb_println("'cloak' for invisibility");
		boardGUI.gb_println("'nimbus2000' for flying");
		boardGUI.gb_println("'pickle rick' for super cool stuff");
		boardGUI.gb_println("'stop' for removing previous commands");
	}
}