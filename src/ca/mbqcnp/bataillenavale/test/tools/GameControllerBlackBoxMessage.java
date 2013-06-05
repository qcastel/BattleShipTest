package ca.mbqcnp.bataillenavale.test.tools;

import java.util.List;

/**
 * Message used by the blackbox, to transmit method call and params
 */
public class GameControllerBlackBoxMessage {


	protected String functionCall;

	protected List<?> params;

	public GameControllerBlackBoxMessage(String function){
		this(function, null);

	}

	public GameControllerBlackBoxMessage(String function, List<?> params) {
		this.functionCall = function;
		this.params = params;
	}


	public String getFunctionCall() {
		return this.functionCall;
	}

	public List<?> getParams() {
		return this.params;
	}



}
