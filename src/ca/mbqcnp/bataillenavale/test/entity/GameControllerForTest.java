package ca.mbqcnp.bataillenavale.test.entity;


import ca.mbqcnp.bataillenavale.GameController;
import ca.mbqcnp.bataillenavale.GameControllerInterface;
import ca.mbqcnp.bataillenavale.entity.Armada;
import ca.mbqcnp.bataillenavale.entity.Map;
import ca.mbqcnp.bataillenavale.entity.Player;

public class GameControllerForTest extends GameController {


	public GameControllerForTest() {
		super(new Player("Black Box", 0, 0), false);
	}

	private static final long serialVersionUID = 1L;


	@Override
	public Armada getArmada() {
		return this.armada;
	}

	public GameStatus getGameStatus() {
		return this.gameStatus;
	}

	@Override
	public Map getMap() {
		return this.map;
	}

	@Override
	public GameControllerInterface getOpponentGameController() {
		return this.opponentGameController;
	}

	public void setArmada(Armada armada) {
		this.armada = armada;
	}

	public void setGameStatus(GameStatus gameStatus) {
		this.gameStatus = gameStatus;
	}

	public void setMap(Map map) {
		this.map = map;
	}

	public void setOpponentGameController(GameController opponentGameController) {
		this.opponentGameController = opponentGameController;
	}
}
