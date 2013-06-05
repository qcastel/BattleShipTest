package ca.mbqcnp.bataillenavale.test.entity;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import ca.mbqcnp.bataillenavale.entity.AttackResult;
import ca.mbqcnp.bataillenavale.test.tools.GameControllerBlackBoxMessage;

public class GameControllerBlackBox extends GameControllerForTest {


	private static final long serialVersionUID = 1L;

	private final boolean activeChangeTurn;
	private final boolean activeReady;
	private final boolean activeShootPosition;

	private List<GameControllerBlackBoxMessage> messages;

	public GameControllerBlackBox(){
		this(false, false, false);
	}


	public GameControllerBlackBox( boolean activeShootPosition,	boolean activeChangeTurn, boolean activeReady) {
		super();

		this.activeShootPosition = activeShootPosition;
		this.activeChangeTurn = activeChangeTurn;
		this.activeReady = activeReady;
		this.messages = new LinkedList<GameControllerBlackBoxMessage>();
	}



	@Override
	public void changeTurn() {
		if(this.activeChangeTurn){
			this.messages.add(new GameControllerBlackBoxMessage("changeTurn"));
		}
		changeTurn();
	}


	public List<GameControllerBlackBoxMessage> getMessages() {
		List<GameControllerBlackBoxMessage> tmp = this.messages;
		this.messages = new LinkedList<GameControllerBlackBoxMessage>();
		return tmp;
	}

	@Override
	public void ready() {
		if(this.activeReady) {
			this.messages.add(new GameControllerBlackBoxMessage("ready"));
		}
		super.ready();

	}


	public void setMessages(List<GameControllerBlackBoxMessage> messages) {
		this.messages = messages;
	}


	@Override
	public AttackResult shootPosition(int i, int j){
		if(this.activeShootPosition){
			List<Integer> params = new ArrayList<Integer>();
			params.add(0,i);
			params.add(1,j);
			this.messages.add(new GameControllerBlackBoxMessage("shootPosition", params));
		}
		return super.shootPosition(i, j);

	}


}
