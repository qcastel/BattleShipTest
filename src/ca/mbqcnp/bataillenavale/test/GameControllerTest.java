package ca.mbqcnp.bataillenavale.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import android.test.AndroidTestCase;

import ca.mbqcnp.bataillenavale.GameController;
import ca.mbqcnp.bataillenavale.entity.AttackResult;
import ca.mbqcnp.bataillenavale.entity.Direction;
import ca.mbqcnp.bataillenavale.entity.Map;
import ca.mbqcnp.bataillenavale.entity.Player;
import ca.mbqcnp.bataillenavale.entity.Ship;
import ca.mbqcnp.bataillenavale.entity.ShipPosition;
import ca.mbqcnp.bataillenavale.entity.Square;
import ca.mbqcnp.bataillenavale.ia.DeployementComputer;
import ca.mbqcnp.bataillenavale.test.entity.GameControllerBlackBox;
import ca.mbqcnp.bataillenavale.test.entity.GameControllerForTest;
import ca.mbqcnp.bataillenavale.test.tools.GameControllerBlackBoxMessage;

public class GameControllerTest  extends AndroidTestCase {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testGameController() {
		
		// initialize context
		GameControllerForTest gameController = new GameControllerForTest();
		
		// start test
		assertTrue("GameController map isn't instantiate", (gameController.getMap() != null));
		
		for(int i = 0; i < 10 ; i++){
			for(int j = 0; j < 10 ; j++){
				assertTrue("Square (" + i + ", " + j + ") isn't instantiate", (gameController.getMap().getSquare(i, j) != null));
				assertFalse("Square (" + i + ", " + j + ") has a ship." , gameController.getMap().getSquare(i, j).isShip());
			}
		}
		
		//WARNING Test dépendant d'une nombre de bateau définit dans l'armada
		int maxSize = 5;
		int nbShipsBySize[] = new int[maxSize+1];
		
		nbShipsBySize[0] = 0;
		nbShipsBySize[1] = 0;
		nbShipsBySize[2] = 1;
		nbShipsBySize[3] = 2;
		nbShipsBySize[4] = 1;
		nbShipsBySize[5] = 1;
		
		gameController.getArmada().addShip(2);
		gameController.getArmada().addShip(3);
		gameController.getArmada().addShip(3);
		gameController.getArmada().addShip(4);
		gameController.getArmada().addShip(5);
		
		assertTrue("The list of ship isn't initialize in Armada", (gameController.getArmada().getShipsList() != null));
		
		for(Ship ship : gameController.getArmada().getShipsList()){

			assertTrue("Size of ship should be between ]0," + maxSize + "].", ( 0 < ship.getSize() && ship.getSize() <= maxSize) );
				
			nbShipsBySize[ship.getSize()]--;
		}
		
		for(int i = 0; i <= maxSize ; i++){
			assertTrue( "Rules game aren't respected, there is '" + (-1 * nbShipsBySize[i]) + "' more ship of size " + i + ". Armada : " + gameController.getArmada() ,  ( nbShipsBySize[i] == 0));
		}
		
		
	}

	@Test
	public void testIsPositionShipCorrect() {
		// initialize context
		GameControllerForTest gameController = new GameControllerForTest();
		gameController.setShip(new ShipPosition(1, 1, Direction.HORIZONTAL), 5);
		gameController.setShip(new ShipPosition(5, 1, Direction.VERTICAL), 3);
		gameController.setShip(new ShipPosition(5, 4, Direction.VERTICAL), 3);
		
		// start test
		
		//Ship of size 4 :
		
		this.testAPosition(gameController, true, 2, 1, Direction.HORIZONTAL, 4);
		
		this.testAPosition(gameController, false, 2, 1, Direction.VERTICAL, 4);
		
		this.testAPosition(gameController, false, 2, 4, Direction.VERTICAL, 4);
		
		this.testAPosition(gameController, true, 1, 6, Direction.HORIZONTAL, 4);
		
		this.testAPosition(gameController, false, 1, 5, Direction.HORIZONTAL, 4);
		
		this.testAPosition(gameController, false, 1, 7, Direction.HORIZONTAL, 4);
		
		this.testAPosition(gameController, true, 6, 3, Direction.VERTICAL, 4);
		
		this.testAPosition(gameController, false, 9, 3, Direction.VERTICAL, 4);
		
		this.testAPosition(gameController, false, 8, 3, Direction.VERTICAL, 4);
		
		this.testAPosition(gameController, false, 7, 3, Direction.VERTICAL, 4);		
		
		this.testAPosition(gameController, false, 9, 9, Direction.VERTICAL, 4);
		

		//Ship of size 2
		
		this.testAPosition(gameController, false,2, 9, Direction.HORIZONTAL, 2);
		
		this.testAPosition(gameController, false,9, 2, Direction.VERTICAL, 2);		
		
		this.testAPosition(gameController, true, 2, 8, Direction.VERTICAL, 2);		
		
		this.testAPosition(gameController, true, 8, 2, Direction.HORIZONTAL, 2);
		
		this.testAPosition(gameController, true, 5, 5, Direction.HORIZONTAL, 2);		
		
		this.testAPosition(gameController, true, 5, 2, Direction.HORIZONTAL, 2);
		
		this.testAPosition(gameController, true, 0, 0, Direction.HORIZONTAL, 2);		
		
		
	}
	
	private void testAPosition(GameControllerForTest gameController, boolean condition, int i, int j, Direction direction, int size){
		
		if(condition)
			assertTrue("Position (" + i + ", " + j + ", " + direction + ") should be valid for a ship of size " + size + " on map : \n" + gameController.getMap(), gameController.isPositionShipCorrect(new ShipPosition(i, j, direction), size));		
		else
			assertFalse("Position (" + i + ", " + j + ", " + direction + ") shouldn't be valid for a ship of size " + size + " on map : \n" + gameController.getMap(), gameController.isPositionShipCorrect(new ShipPosition(i, j, direction), size));		
			
	}

	// Sera plutot utile pour le debuging, en rajoutant cette fonction a la map, reste ici temporairement
	@Test 
	public void testIsMapCorrect(){
		// initialize context
		GameControllerForTest gameController = new GameControllerForTest();
		Map map = gameController.getMap();
		
		// start test
		int i; int j; Square square;
		for(Ship ship : gameController.getArmada().getShipsList()){
			i = ship.getShipPosition().getI();
			j = ship.getShipPosition().getJ();
			for(int k = 0; k < ship.getSize() ; k++){
				square = map.getSquare(i, j);
				assertTrue("Square  (" + i + ", " + j + ") should have a ship but is empty : " + ship + ".", square.isShip());
				assertTrue("Ship on square  (" + i + ", " + j + ") should be " + ship + " but there is an other one : " + square.getShip() + ".", square.getShip().equals(ship));
				if(ship.getShipPosition().getDirection() == Direction.HORIZONTAL)
					j++;
				else
					i++;
			}
		}
	}
	
	@Test
	public void testSetShip() {
		// initialize context
		GameControllerForTest gameController = new GameControllerForTest();
		Map map = gameController.getMap();
		
		// start test
		Ship ship1, ship2, ship3;
		ship1 = this.testASetShip(gameController, true, 1, 1, Direction.HORIZONTAL, 5);
		ship2 = this.testASetShip(gameController, true, 5, 1, Direction.VERTICAL, 3);
		ship3 = this.testASetShip(gameController, true, 5, 4, Direction.VERTICAL, 3);
		
		assertTrue("Ships on (5,1) and (5,4) should be different.", (ship2 != ship3) );

		assertTrue("ShipPosition of the new ship should be different of null : " + ship1, ( ship1.getShipPosition() != null));
		assertTrue("ShipStatus of the new ship should be different of null : " + ship1, ( ship1.getShipStatus() != null));
		assertTrue("The ship should be on (1, 1) instead of : " + ship1.getShipPosition(),   ( (ship1.getShipPosition().getI() == 1) && (ship1.getShipPosition().getJ() == 1)));
		assertTrue("The ship should be horizontal instead of vertical : " + ship1, (ship1.getShipPosition().getDirection() == Direction.HORIZONTAL));
	
		
		for(int i = 0; i < 10; i++){
			for(int j = 0; j < 10; j++){
				if( !((i == 1 && 1 <= j && j <= 5) || (j == 1 && 5 <= i && i <= 7) || (j == 4 && 5 <= i && i <= 7)) ){
					assertFalse("There is a ship on square (" + i + "," + j + ") that shouldn't be here : map = \n" + map, map.getSquare(i, j).isShip());
				} else {
					assertTrue("There is not ship on square (" + i + "," + j + ") but it should be : map = \n" + map, map.getSquare(i, j).isShip());
				}
			}
		}
		
		for(int k = 0; k < 5 ; k++){
			assertTrue("Ship place on (1,1) isn't on square (1," + (1+k) + ").", map.getSquare(1, 1+k).isShip());
			assertTrue("Ship place on (1,1) isn't the same on square (1," + (1+k) + ").", ( ship1 == map.getSquare(1, 1+k).getShip()));
		}
		
		for(int k = 0; k < 3 ; k++){
			assertTrue("Ship place on (5,1) isn't on square (" + (5+k) + ", 1).", map.getSquare(5+k,1).isShip());
			assertTrue("Ship place on (5,1) isn't the same on square (" + (5+k) + ", 1).", ( ship2 == map.getSquare(5+k,1).getShip()));
		}
		
		for(int k = 0; k < 3 ; k++){
			assertTrue("Ship place on (5,4) isn't on square (" + (5+k) + ", 4).", map.getSquare(1, 1+k).isShip());
			assertTrue("Ship place on (5,4) isn't the same on square (" + (5+k) + ", 4).", ( ship3 == map.getSquare(5+k, 4).getShip()));
		}
		
		this.testASetShip(gameController, false, 1, 8, Direction.HORIZONTAL, 4);
		this.testASetShip(gameController, false, 5, 2, Direction.HORIZONTAL, 4);
		this.testASetShip(gameController, false, 1, 4, Direction.HORIZONTAL, 4);
		this.testASetShip(gameController, false, 0, 4, Direction.VERTICAL, 4);
		
		this.testASetShip(gameController, false, 1, 9, Direction.HORIZONTAL, 2);
		this.testASetShip(gameController, false, 9, 1, Direction.VERTICAL, 2);
		this.testASetShip(gameController, false, 1, 0, Direction.HORIZONTAL, 2);
		this.testASetShip(gameController, false, 0, 1, Direction.VERTICAL, 2);
		
		this.testASetShip(gameController, true, 8, 1, Direction.HORIZONTAL, 4);
		this.testASetShip(gameController, true, 5, 2, Direction.HORIZONTAL, 2);
		
		//test si le bateau existe déjà il ne peut pas en avoir un quatrième
		this.testASetShip(gameController, false, 1, 6, Direction.HORIZONTAL, 4);
		
		
	}
	
	private Ship testASetShip(GameControllerForTest gameController, boolean condition, int i, int j, Direction direction, int size){

		Ship ship = gameController.setShip(new ShipPosition(i, j, direction), size) ;		
		if(condition)
			assertTrue("Set a ship of size " + size + " on (" + i + ", " + j + ") should be valid \n" + gameController.getMap(), (ship != null));		
		else 
			assertFalse("Set a ship of size " + size + " on (" + i + ", " + j + ") shouldn't be valid \n" + gameController.getMap(), (ship != null));		
		return ship;
	}

	@Test
	public void testDeleteShip() {
		// initialize context
		GameControllerForTest gameController = new GameControllerForTest();		
		Ship ship1 = gameController.setShip(new ShipPosition(1, 1, Direction.HORIZONTAL), 5);
		Ship ship2 = gameController.setShip(new ShipPosition(5, 1, Direction.VERTICAL), 3);
		Ship ship3 = gameController.setShip(new ShipPosition(5, 4, Direction.VERTICAL), 3);
		Map map = gameController.getMap();
		
		//start test
		
		assertTrue("Delete ship1 failed", gameController.deleteShip(ship1));
		assertFalse("Ship has been release, it shouldn't be.", (ship1 == null));
		assertTrue("ShipPosition of the ship delete should be null", ( ship1.getShipPosition() == null));
		assertTrue("ShipStatus of the ship delete should be null", ( ship1.getShipStatus() == null));		
		
		for(int i = 0; i < 10; i++){
			for(int j = 0; j < 10; j++){
				if( !( (j == 1 && 5 <= i && i <= 7) || (j == 4 && 5 <= i && i <= 7)) ){
					assertFalse("There is a ship on square (" + i + "," + j + ") that shouldn't be here : map = \n" + map, map.getSquare(i, j).isShip());
				} else {
					assertTrue("There is not ship on square (" + i + "," + j + ") but it should be : map = \n" + map, map.getSquare(i, j).isShip());
					assertFalse("Ship on square (" + i + "," + j + ")  is the same that ship delete.", (map.getSquare(i, j).getShip() == ship1));
				}
			}
		}
		
		
		
	}

	@Test
	public void testShootPosition() {
		GameControllerForTest gameController = new GameControllerForTest();		
		GameControllerBlackBox gameControllerBlackBox = new GameControllerBlackBox(true, false, false);

		gameController.setOpponentGameController(gameControllerBlackBox);
		gameController.setDeployementIHMInterface(new DeployementComputer());
		gameControllerBlackBox.setOpponentGameController(gameController);
		gameControllerBlackBox.setDeployementIHMInterface(new DeployementComputer());
		gameController.setMyPlayer(new Player("Test", 1, 1));
		gameController.setMyTurn(true);
		gameControllerBlackBox.setMyPlayer(new Player("BlackBox",2,2));
		gameControllerBlackBox.setMyTurn(false);

		gameController.setOpponentPlayer(gameControllerBlackBox.getMyPlayer());
		gameControllerBlackBox.setOpponentPlayer(gameController.getMyPlayer());
		
		Ship ship1 = gameController.setShip(new ShipPosition(1, 1, Direction.HORIZONTAL), 5);
		Ship ship2 = gameController.setShip(new ShipPosition(5, 1, Direction.VERTICAL), 3);
		Ship ship3 = gameController.setShip(new ShipPosition(5, 4, Direction.VERTICAL), 3);
		Ship ship4 = gameController.setShip(new ShipPosition(9, 8, Direction.HORIZONTAL), 2);
		Ship ship5 = gameController.setShip(new ShipPosition(4, 7, Direction.VERTICAL), 4);

		gameControllerBlackBox.setShip(new ShipPosition(1, 1, Direction.HORIZONTAL), 5);
		gameControllerBlackBox.setShip(new ShipPosition(5, 1, Direction.VERTICAL), 3);
		gameControllerBlackBox.setShip(new ShipPosition(5, 4, Direction.VERTICAL), 3);
		gameControllerBlackBox.setShip(new ShipPosition(9, 8, Direction.HORIZONTAL), 2);
		gameControllerBlackBox.setShip(new ShipPosition(4, 7, Direction.VERTICAL), 4);

		gameController.buttonReadyEvent();
		
		gameControllerBlackBox.buttonReadyEvent();
		Map map = gameController.getMap();

		//start test
		
			for(int i = 0; i < 10; i++){
				for(int j = 0; j < 10; j++){
					if( i == 9 && j == 9)
						testShootOnePosition(gameController,gameControllerBlackBox, i, j, true, true);
					else if( !((i == 1 && 1 <= j && j <= 5) || (j == 1 && 5 <= i && i <= 7) || (j == 4 && 5 <= i && i <= 7) || (i == 9  && 8 <= j && j <= 9) || (j == 7 && 4 <= i && i <= 7) ) ){
						testShootOnePosition(gameController,gameControllerBlackBox, i, j, false, false);
					} else {
						testShootOnePosition(gameController,gameControllerBlackBox, i, j, true, false);
					}
				}
			}

		
	}
	
	
	private void testShootOnePosition(GameControllerForTest gameController,GameControllerBlackBox gameControllerBlackBox , int i,int j, boolean isAShipHit, boolean isGameOver) {
		
		AttackResult attackResult = gameController.shootPosition(i, j);
		try {
			Thread.sleep(100);
		}  catch (InterruptedException e) {
			e.printStackTrace();
		}
		List params = new LinkedList<GameControllerBlackBoxMessage>();
		params.add(0,i);
		params.add(1,j);
		this.checkGameControllerBlackBoxReceiveMessage(gameControllerBlackBox, "shootPosition", params);
		
		Map map = gameController.getMap();		
		if(!isGameOver)
			assertFalse("The game shouldn't be over: Last shot = (" + i + ", " + j + ") AttackResult =  " + attackResult + " \n and map : " + map, attackResult.isGameOver());
		else 
			assertTrue("The game should be over: Last shot = (" + i + ", " + j + ") AttackResult =  " + attackResult + " \n and map : " + map, attackResult.isGameOver());
		
		if( i == 9 && j == 9)
			System.out.println(isAShipHit);
		
		if(!isAShipHit)
			assertFalse("No ship shouldn't be hit on (" + i + ", " + j + ") : AttackResult =  " + attackResult + " \n and map : " + map, attackResult.isAShipHit());
		else
			assertTrue("A ship should be hit on (" + i + ", " + j + ") : AttackResult =  " + attackResult + " \n and map : " + map, attackResult.isAShipHit());
		
		gameControllerBlackBox.shootPosition(i, j);
	}

	@Test
	public void testReady() {
	// Initialize context
		GameControllerForTest gameController = new GameControllerForTest();		
		GameControllerBlackBox gameControllerBlackBox = new GameControllerBlackBox(false, false, true);


		gameController.setOpponentGameController(gameControllerBlackBox);
		Ship ship1 = gameController.setShip(new ShipPosition(1, 1, Direction.HORIZONTAL), 5);
		Ship ship2 = gameController.setShip(new ShipPosition(5, 1, Direction.VERTICAL), 3);
		Ship ship3 = gameController.setShip(new ShipPosition(5, 4, Direction.VERTICAL), 3);
		Ship ship4 = gameController.setShip(new ShipPosition(9, 8, Direction.HORIZONTAL), 2);
		Ship ship5 = gameController.setShip(new ShipPosition(4, 7, Direction.VERTICAL), 4);

		gameControllerBlackBox.setShip(new ShipPosition(1, 1, Direction.HORIZONTAL), 5);
		gameControllerBlackBox.setShip(new ShipPosition(5, 1, Direction.VERTICAL), 3);
		gameControllerBlackBox.setShip(new ShipPosition(5, 4, Direction.VERTICAL), 3);
		gameControllerBlackBox.setShip(new ShipPosition(9, 8, Direction.HORIZONTAL), 2);
		gameControllerBlackBox.setShip(new ShipPosition(4, 7, Direction.VERTICAL), 4);

		/* Tester que l'on recup�re bien l'exception de Ready*/
		gameController.buttonReadyEvent();
		try {
			Thread.sleep(4000);
		}  catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		this.checkGameControllerBlackBoxReceiveMessage(gameControllerBlackBox, "ready");
		
		assertFalse("GameController should be waiting his opponent before start", (gameController.getGameStatus() == GameController.GameStatus.WAITING_OPPONENT_DEPLOYMENT));
		/*Tester qu'il attend bien la r�ponse de son adversaire*/
		gameControllerBlackBox.buttonReadyEvent();
		try {
			Thread.sleep(4000);
		}  catch (InterruptedException e) {

			e.printStackTrace();
		}
		/* Tester que le jeux est bien lanc� et que c'est bien le joueur hosteur qui joue */
		assertFalse("GameController should be on Attack mode", (gameController.getGameStatus() == GameController.GameStatus.ATTACK));
		assertFalse("GameController should be on Defense mode", (gameControllerBlackBox.getGameStatus() == GameController.GameStatus.DEFENSE));

		fail("Not yet implemented");
	}
	


	@Test
	public void testChangeTurn() {
		GameControllerForTest gameController = new GameControllerForTest();		
		GameControllerBlackBox gameControllerBlackBox = new GameControllerBlackBox(false, true, false);



		gameController.setOpponentGameController(gameControllerBlackBox);
		Ship ship1 = gameController.setShip(new ShipPosition(1, 1, Direction.HORIZONTAL), 5);
		Ship ship2 = gameController.setShip(new ShipPosition(5, 1, Direction.VERTICAL), 3);
		Ship ship3 = gameController.setShip(new ShipPosition(5, 4, Direction.VERTICAL), 3);
		Ship ship4 = gameController.setShip(new ShipPosition(9, 8, Direction.HORIZONTAL), 2);
		Ship ship5 = gameController.setShip(new ShipPosition(4, 7, Direction.VERTICAL), 4);

		gameControllerBlackBox.setShip(new ShipPosition(1, 1, Direction.HORIZONTAL), 5);
		gameControllerBlackBox.setShip(new ShipPosition(5, 1, Direction.VERTICAL), 3);
		gameControllerBlackBox.setShip(new ShipPosition(5, 4, Direction.VERTICAL), 3);
		gameControllerBlackBox.setShip(new ShipPosition(9, 8, Direction.HORIZONTAL), 2);
		gameControllerBlackBox.setShip(new ShipPosition(4, 7, Direction.VERTICAL), 4);

		gameController.buttonReadyEvent();
		gameControllerBlackBox.buttonReadyEvent();


		
		try {
			gameController.askShootPosition(1, 1);
			Thread.sleep(4000);
			assertFalse("GameController should be on Defense mode", (gameController.getGameStatus() == GameController.GameStatus.DEFENSE));
			assertFalse("GameController should be on Attack mode", (gameControllerBlackBox.getGameStatus() == GameController.GameStatus.ATTACK));
			this.checkGameControllerBlackBoxReceiveMessage(gameControllerBlackBox, "changeTurn");
			
			gameControllerBlackBox.askShootPosition(1, 1);
			Thread.sleep(4000);
			assertFalse("GameController should be on Attack mode", (gameController.getGameStatus() == GameController.GameStatus.ATTACK));
			assertFalse("GameController should be on Defense mode", (gameControllerBlackBox.getGameStatus() == GameController.GameStatus.DEFENSE));
			
			
			gameController.askShootPosition(2, 2);
			Thread.sleep(4000);
			assertFalse("GameController should be on Defense mode", (gameController.getGameStatus() == GameController.GameStatus.DEFENSE));
			assertFalse("GameController should be on Attack mode", (gameControllerBlackBox.getGameStatus() == GameController.GameStatus.ATTACK));
			this.checkGameControllerBlackBoxReceiveMessage(gameControllerBlackBox, "changeTurn");
			
			gameControllerBlackBox.askShootPosition(2, 2);
			Thread.sleep(4000);
			assertFalse("GameController should be on Attack mode", (gameController.getGameStatus() == GameController.GameStatus.ATTACK));
			assertFalse("GameController should be on Defense mode", (gameControllerBlackBox.getGameStatus() == GameController.GameStatus.DEFENSE));
			
			
			gameController.askShootPosition(3, 3);
			Thread.sleep(4000);
			assertFalse("GameController should be on Defense mode", (gameController.getGameStatus() == GameController.GameStatus.DEFENSE));
			assertFalse("GameController should be on Attack mode", (gameControllerBlackBox.getGameStatus() == GameController.GameStatus.ATTACK));
			this.checkGameControllerBlackBoxReceiveMessage(gameControllerBlackBox, "changeTurn");
			
			gameControllerBlackBox.askShootPosition(3, 3);
			Thread.sleep(4000);
			assertFalse("GameController should be on Attack mode", (gameController.getGameStatus() == GameController.GameStatus.ATTACK));
			assertFalse("GameController should be on Defense mode", (gameControllerBlackBox.getGameStatus() == GameController.GameStatus.DEFENSE));
			
			
		}  catch (InterruptedException e) {

			e.printStackTrace();
		}
	}

	
	private void checkGameControllerBlackBoxReceiveMessage(GameControllerBlackBox gameControllerBlackBox, String function){
		this.checkGameControllerBlackBoxReceiveMessage(gameControllerBlackBox, function, new LinkedList());
	}
	
	private void checkGameControllerBlackBoxReceiveMessage(GameControllerBlackBox gameControllerBlackBox, String function, List params){
		List<GameControllerBlackBoxMessage> messagesBlackBox = gameControllerBlackBox.getMessages();
		assertFalse("Opponent didn't receive a '" + function + "' message.", messagesBlackBox.isEmpty());
		boolean b = false;
		for(GameControllerBlackBoxMessage m : messagesBlackBox){
			b |= ( m.getFunctionCall().contains(function) && (params == null || m.getParams() == params));
		}
		assertFalse("GameController should have send a message '" + function + "' to his opponent with params : " + params,b);
	}


}
