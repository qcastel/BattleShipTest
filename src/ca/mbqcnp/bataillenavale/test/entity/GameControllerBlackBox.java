package ca.mbqcnp.bataillenavale.test.entity;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import ca.mbqcnp.bataillenavale.entity.AttackResult;
import ca.mbqcnp.bataillenavale.test.tools.GameControllerBlackBoxMessage;

public class GameControllerBlackBox extends GameControllerForTest {


	private static final long serialVersionUID = 1L;

	private boolean activeShootPosition; 
	private boolean activeChangeTurn;
	private boolean activeReady;
	
	private List messages;
	
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


	
	public AttackResult shootPosition(int i, int j){
		if(activeShootPosition){
			List params = new ArrayList();
			params.add(0,i);
			params.add(1,j);	
			messages.add(new GameControllerBlackBoxMessage("shootPosition", params));
		} 
		return super.shootPosition(i, j);
		
	}
	

	public void changeTurn() {
		if(activeChangeTurn){
			messages.add(new GameControllerBlackBoxMessage("changeTurn"));
		} 
		changeTurn();
	}
	
	public void ready() {
		if(activeReady) {
			messages.add(new GameControllerBlackBoxMessage("ready"));
		} 
		super.ready();
		
	}


	public List getMessages() {
		List tmp = messages;
		messages = new LinkedList<GameControllerBlackBoxMessage>();
		return tmp;
	}


	public void setMessages(List messages) {
		this.messages = messages;
	}

	
}
