package main;
public class Board {
	private final int SIZE=17; //board SIZE
	private final int LEVELS=15;
	private Cell[][][] board;
	private Enemies enemies[];
	private int nEnemies;	//nuber of the total of enemies and then the numbers of each type of enemies.
	private int nBalloons;
	private int nDrops;
	private int nBricks; //used on setBoard, on the while loop that set bricks
	private int nBombBonus; //number of each tipe of bonus
	private int nFireBonus;
	private int nFullFire;
	private int nRemoteBonus;
	private int nSkateBonus;
	private int nGeta;
	private int nDoor;
	private int levelRemoteBonus=(int)(Math.random()*15); //it is one bonus for each 10 levels. As we will only have one, we set it random
	//As levels are one dimension of our board array, they start from zero. So the first level will be the zero, and so one.
	//This means that the even levels will be odd in the code (level 2 is even, but it is the position 1 on the array, which is odd)

	//Constructor
	public Board(){
		setBoard();
	}
	
	//Method that builds up the board, creating walls and bricks and setting the bonuses beneath them
	public void setBoard() {
		board= new Cell[LEVELS][SIZE][SIZE];	//we define the board as an 3D array of cells, one that will get the level and the other the X and Y coordinate.
		for(int level=0;level<board.length;level++) {	//for loop changing the level creating (not initializing or adding the images)
			for(int ii=0;ii<board[level].length;ii++){	//X coordinates
				for(int jj=0;jj<board[level][ii].length;jj++){	//Y coordinates
					if((ii==0)||(ii==board[level].length -1)||(jj==0)||(jj==board[level][ii].length -1) || ((ii%2==0) && (jj%2==0))){
						board[level][jj][ii] = new Cell ("wall"); //this puts walls on the framework of the board and alternated on the regular cells
					}
					else {
						board[level][jj][ii]= new Cell("regular"); //this avoid nullpointers and set the regular cells
					}
				}
			}
			nBricks=50; //there will be 50 bricks on each level.
			while(nBricks>0) { //Setting the 50 bricks
				int x=(int)(Math.random()*17);   //coordinates chosen randomly                                                                         
				int y=(int)(Math.random()*17); //17 coordinates because we got from 0 to 16, in x and y
				if(!getCell(level,y, x).getType().equals("wall")&&!getCell(level,y, x).getType().equals("brick")&&!(x==1&&y==1)&&!(x==1&&y==2)&&!(x==2&&y==1)){  //we verify that the brick wont go to a cell which already has a wall or a brick, and that no bricks appear at the cell next to the player (upper right corner) so you dont get stucked.
					board[level][x][y]=new Cell ("brick");	//create a new cell of type brick at the coordinates that verified the conditions.
					nBricks--;	
				}			
			}

			for(int ii=0;ii<board[level].length;ii++){	//by defect every cell will have "regular" bonus (with no effect).
				for(int jj=0;jj<board[level][ii].length;jj++){
					setRegularBonus(level, ii, jj);
				}
			}
			setnBonus(level);	//in this method we choose the numbers of bonus of each type depending on the level we are.
			setBonus(level);	//here we set them at their correct position.

		}
	}
	public Cell getCell(int level, int jj, int ii){
		return board[level][ii][jj];	//we had to change the order of the coordinates to make them similar as the X and Y axis of the boardGUI work.
	}
	//This method sets (or resets) the number of bonus per level and their probabilities. Useful when changing the level
	public void setnBonus(int level) {
		nBombBonus=2;
		nFireBonus=1;
		nDoor=1;

		if(level==4 || level== 9 || level==14) {
			nFullFire=1;
		}
		if(level==levelRemoteBonus) {
			nRemoteBonus=1;
		}
		if(!(level%2==0)){ //50% chances in even levels, but in code-language they're odd (explained on levels)
			if((int)(Math.random()*1)==0) {
				nSkateBonus=1;
			}
		}
		if(0==(int)(Math.random()*5)){	//20% in every levels.
			nGeta=1;
		}
	}

	public void setnEnemies(int level) {
		nBalloons=(int)((Math.random()*10)+1);	//random number of balloons between 1 and 10.
		if(level==1 || level==5 || level==9 || level== 13) {	//drops will just be added every 4 levels, first one at level 2.
			nDrops+=1;
		}
		nEnemies=nBalloons+nDrops;
	}


	public void setRegularBonus(int level,int ii, int jj) {
		getCell(level, ii, jj).setBonus("regular");	//we set a "regular" bonus for the cell with the coordinates it recieves.
	}

	//This method deploys all bonuses, using the variables obtained in setnBonus()
	public void setBonus(int level) {
		//Bomb bonus
		System.out.println((level+1)); //Checker removable
		while (this.nBombBonus>0) { //2 Bomb bonuses per level
			int x=(int)(Math.random()*17);   //coordinates chosen randomly
			int y=(int)(int)(Math.random()*17); //17 coordinates because we got from 0 to 16, in x and y
			if(getCell(level,y, x).getType().equals("brick") && getCell(level,y, x).getBonus().getType().equals("regular") ){  //bonuses will only be on bricks, but not on other bonuses
				getCell(level,y, x).setBonus("bomb");
				this.nBombBonus-=1;
				System.out.println("Bombs are: " +x + ":" + y); //Checker removable
			}
		}
		//Fire bonus
		while (this.nFireBonus>0) { //1 fire per round
			int x=(int)(Math.random()*17);   //coordinates chosen randomly
			int y=(int)(int)(Math.random()*17); //17 coordinates because we got from 0 to 16, in x and y
			if(getCell(level,y, x).getType().equals("brick") && getCell(level,y, x).getBonus().getType().equals("regular") ){  //bonuses will only be on bricks, but not on other bonuses
				getCell(level,y, x).setBonus("fire");
				this.nFireBonus-=1;
				System.out.println("Fires are: " +x + ":" + y); //Checker removable		
			}
		}
		//Special fire
		while (this.nFullFire>0) { //1 fire per 5 levels
			int x=(int)(Math.random()*17);   //coordinates chosen randomly
			int y=(int)(int)(Math.random()*17); //17 coordinates because we got from 0 to 16, in x and y
			if(getCell(level,y, x).getType().equals("brick") && getCell(level,y, x).getBonus().getType().equals("regular") ){  //bonuses will only be on bricks, but not on other bonuses
				getCell(level,y, x).setBonus("fullFire");
				this.nFullFire-=1;
				System.out.println("Full fires are: " +x + ":" + y); //Checker removable		
			}
		}
		//Roller skate
		while (this.nSkateBonus>0) { //1 every 10 levels.
			int x=(int)(Math.random()*17);   //coordinates chosen randomly
			int y=(int)(int)(Math.random()*17); //17 coordinates because we got from 0 to 16, in x and y
			if(getCell(level,y, x).getType().equals("brick") && getCell(level,y, x).getBonus().getType().equals("regular") ){  //bonuses will only be on bricks, but not on other bonuses
				getCell(level,y, x).setBonus("skate");
				this.nSkateBonus-=1;
				System.out.println("Roller skates are: " +x + ":" + y); //Checker removable		
			}
		}
		//Geta 
		while (this.nGeta>0) { //1 every 10 levels.
			int x=(int)(Math.random()*17);   //coordinates chosen randomly
			int y=(int)(int)(Math.random()*17); //17 coordinates because we got from 0 to 16, in x and y
			if(getCell(level,y, x).getType().equals("brick") && getCell(level,y, x).getBonus().getType().equals("regular") ){  //bonuses will only be on bricks, but not on other bonuses
				getCell(level,y, x).setBonus("geta");
				this.nGeta-=1;
				System.out.println("Getas are: " +x + ":" + y); //Checker removable		
			}
		}
		//Remote control
		while (this.nRemoteBonus>0) { //1 every 10 levels.
			int x=(int)(Math.random()*17);   //coordinates chosen randomly
			int y=(int)(int)(Math.random()*17); //17 coordinates because we got from 0 to 16, in x and y
			if(getCell(level,y, x).getType().equals("brick") && getCell(level,y, x).getBonus().getType().equals("regular")){  //bonuses will only be on bricks, but not on other bonuses
				getCell(level,y, x).setBonus("remoteControl");
				this.nRemoteBonus-=1;
				System.out.println("Remotes are: " +x + ":" + y); //Checker removable		
			}
		}
		//Door
		while (this.nDoor>0) { //1 every 10 levels.
			int x=(int)(Math.random()*17);   //coordinates chosen randomly
			int y=(int)(int)(Math.random()*17); //17 coordinates because we got from 0 to 16, in x and y
			if(getCell(level,y, x).getType().equals("brick") && getCell(level,y, x).getBonus().getType().equals("regular") ){  //bonuses will only be on bricks, but not on other bonuses
				getCell(level,y, x).setBonus("door");
				this.nDoor-=1;
				System.out.println("Door is: " +x + ":" + y); //Checker removable		
			}
		}
	}

	//This method will be used in the main class to distinguish between those cells which needs an image and those which not
	public boolean imageCondition(int level, int ii,int jj) {
		return ((getCell(level,jj,ii).getType().equals("wall")) || getCell(level,jj,ii).getType().equals("brick") );
	}

	//Creating enemies
	public void setEnemies(int level) {
		if(level<=14) {
			int control=0;
			int x;
			int y;
			enemies  = new Enemies[nEnemies];
			while(control<nBalloons) {
				x=(int)(Math.random()*170); 	//coordinates chosen randomly                                                                         
				y=(int)(Math.random()*170); 	//17 coordinates because we got from 0 to 16, in x and y
				if(getCell(level,y/10, x/10).getType().equals("regular") && x>20 && y>20){  //we verify that the brick wont go to a cell which already has a wall or a brick, and we also deny overlapping
					enemies[control]=new Enemies ("balloon");
					enemies[control].setEnemyX(x);	//we set the coordinates that verifies the conditions above.
					enemies[control].setEnemyY(y);
					control+=1;
					
				}

			}
			control=0;
			while(control<nDrops) {//new
				x=(int)(Math.random()*170); 	//coordinates chosen randomly                                                                         
				y=(int)(Math.random()*170); 	//17 coordinates because we got from 0 to 16, in x and y
				if(getCell(level,y/10, x/10).getType().equals("regular") && x>20 && y>20){  //we verify that the brick wont go to a cell which already has a wall or a brick, and we also deny overlapping
					enemies[nBalloons+control]=new Enemies ("drop");
					enemies[nBalloons+control].setEnemyX(x);	//we set the coordinates that verifies the conditions above.
					enemies[nBalloons+control].setEnemyY(y);
					control+=1;
				}			
				
			}
			
		}
	}

	//getters and setters for Enemies

	public Enemies[] getEnemies() {
		return enemies;
	}

	public void setEnemies(Enemies[] enemies) {
		this.enemies = enemies;
	}

	public int getnBalloons() {
		return nBalloons;
	}
	
	public int getSize() {
		return SIZE;
	}

	public void setnBalloons(int nBalloons) {
		this.nBalloons = nBalloons;
	}
	public int getnEnemies() {
		return nEnemies;
	}
	public int getAliveEnemies() {
		int control=0;
		for(int ii=0;ii<enemies.length;ii++) {
			if(enemies[ii].getEnemyAlive()==true) {
				control+=1;
			}
		}
		return control;
	}


	public int getnDrops() {
		return nDrops;
	}

	public void setnDrops(int nDrops) {
		this.nDrops = nDrops;
	}

	//This methods compute the range of the explosion on each direction
	public void calculateRightRange(int level,Player player, int i) {
		for(int k=0;k<=player.getBomb()[i].getRange();k++) {	//for loop that will go 1 by one checking the cells til it get to the player current range.
			if(player.getBomb()[i].getBombX()+k <17) {	//we had to put a limit in orther to avoid index out of bounds with the board dimensions.
				if( getCell(level,player.getBomb()[i].getBombY(),player.getBomb()[i].getBombX()+k ).getType().equals("wall")&&player.getBomb()[i].getRange()==player.getBomb()[i].getRangeRight()) {
					player.getBomb()[i].setRangeRight(k-1);	//if we find a wall, the range wont cover that cell, just the previous.
				}
				else if(getCell(level,player.getBomb()[i].getBombY(),player.getBomb()[i].getBombX()+k ).getType().equals("brick")&&player.getBomb()[i].getRange()==player.getBomb()[i].getRangeRight()) {
					player.getBomb()[i].setRangeRight(k);	//otherwise if we find a brick, the maximum range for this direction will be till the brick cell included, but not others behid it.
				}
			}
		}
	}
	public void calculateRangeDown(int level, Player player, int i) {
		for(int k=0;k<=player.getBomb()[i].getRange();k++) {//sames as previous
			if(player.getBomb()[i].getBombY()+k <17) {			
				if( getCell(level,player.getBomb()[i].getBombY()+k,player.getBomb()[i].getBombX() ).getType().equals("wall")&&player.getBomb()[i].getRange()==player.getBomb()[i].getRangeDown()) {
					player.getBomb()[i].setRangeDown(k-1);				
				}
				else if(getCell(level,player.getBomb()[i].getBombY()+k,player.getBomb()[i].getBombX() ).getType().equals("brick")&&player.getBomb()[i].getRange()==player.getBomb()[i].getRangeDown()) {
					player.getBomb()[i].setRangeDown(k);				
				}
			}
		}
	}
	public void calculateRangeLeft(int level, Player player, int i) {
		for(int k=0;k<=player.getBomb()[i].getRange();k++) {//sames as previous
			if(player.getBomb()[i].getBombX()-k >=0) {			
				if( getCell(level,player.getBomb()[i].getBombY(),player.getBomb()[i].getBombX()-k ).getType().equals("wall")&&player.getBomb()[i].getRange()==player.getBomb()[i].getRangeLeft()) {
					player.getBomb()[i].setRangeLeft(k-1);				
				}
				else if(getCell(level,player.getBomb()[i].getBombY(),player.getBomb()[i].getBombX()-k ).getType().equals("brick")&&player.getBomb()[i].getRange()==player.getBomb()[i].getRangeLeft()) {			
					player.getBomb()[i].setRangeLeft(k);				
				}			
			}
		}
	}
	public void calculateRangeUp(int level, Player player, int i) {
		for(int k=0;k<=player.getBomb()[i].getRange();k++) {//sames as previous
			if(player.getBomb()[i].getBombY()-k >=0) {
				if(getCell(level,player.getBomb()[i].getBombY()-k,player.getBomb()[i].getBombX() ).getType().equals("wall")&&player.getBomb()[i].getRange()==player.getBomb()[i].getRangeUp()) {
					player.getBomb()[i].setRangeUp(k-1);
				}
				else if(getCell(level,player.getBomb()[i].getBombY()-k,player.getBomb()[i].getBombX() ).getType().equals("brick")&&player.getBomb()[i].getRange()==player.getBomb()[i].getRangeUp()) {
					player.getBomb()[i].setRangeUp(k);
				}
			}
		}
	}

	public void checkEnemiesCell(Player player) {	//check if any enemy is at the same cell as the player, if true, it makes damage to the player
		for(int i=0; i < getEnemies().length; i++) {
			if(player.getX()/10 == enemies[i].getEnemyX()/10 && (player.getY()-1)/10 == (enemies[i].getEnemyY())/10 && enemies[i].getEnemyAlive()==true) {
				player.setHealth(player.getHealth()-5);
			}
		}

	}

	public boolean checkBombsPosition(Player player, int jj) {	//check if any enemy is at the same cell of any bomb

		for(int ii=0; ii< player.getBomb().length; ii++) {	
			if(((player.getBomb()[ii].getBombY()==((getEnemies()[jj].getEnemyY())/10)) && (player.getBomb()[ii].getBombX()==(getEnemies()[jj].getEnemyX()/10)))) {
				return false;
			}
		}
		return true;	
	}
	public boolean balloonMoveConditions(int level, Player player, int jj) {
		return (checkBombsPosition(player, jj) && getCell(level, ((getEnemies()[jj].getEnemyY()+1-1)/10), (getEnemies()[jj].getEnemyX()/10)).getType().equals("regular"));
	}
}