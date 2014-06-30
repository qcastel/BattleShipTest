package ca.mbqcnp.bataillenavale.test.entity;

import ca.mbqcnp.bataillenavale.DeployementIHMInterface;
import ca.mbqcnp.bataillenavale.entity.Player;

public class DeploymentFake implements DeployementIHMInterface{

	@Override
	public void readyPlayer(Player player) {		
	}

	@Override
	public void printPopup(String message) {		
	}

	@Override
	public void setOpponentPlayer(Player secondPlayer) {		
	}

	@Override
	public void closePopup() {		
	}

	@Override
	public void unblockReadyButton() {		
	}

	@Override
	public void blockReadyButton() {		
	}

	@Override
	public void switchViewToWarzone() {		
	}

	@Override
	public void opponentDisconnected(boolean opponentAskingDeconnection) {		
	}

}
