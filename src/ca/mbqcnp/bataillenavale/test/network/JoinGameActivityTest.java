package ca.mbqcnp.bataillenavale.test.network;

import android.app.Instrumentation.ActivityMonitor;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import ca.mbqcnp.bataillenavale.DeployementActivity;
import ca.mbqcnp.bataillenavale.JoinGameActivity;
import ca.mbqcnp.bataillenavale.PlayActivity;
import ca.mbqcnp.bataillenavale.R;
import ca.mbqcnp.bataillenavale.entity.Player;
import ca.mbqcnp.bataillenavale.ia.DeployementComputer;
import ca.mbqcnp.bataillenavale.test.entity.GameControllerForTest;
import ca.mbqcnp.bataillenavale.util.log.Log;
import ca.mbqcnp.bataillenavale.web.WebAccess;

public class JoinGameActivityTest extends ActivityInstrumentationTestCase2<PlayActivity> {

	protected JoinGameActivity joinGameActivity;

	public JoinGameActivityTest() {
		super(PlayActivity.class);
	}


	public void testJoinGame() throws Exception {

		Log.i("Start join game test");

		setActivityInitialTouchMode(false);
		PlayActivity activity = getActivity();
		// Add monitor to check for the second activity
		ActivityMonitor monitorJoinGameActivity = getInstrumentation().addMonitor(JoinGameActivity.class.getName(), null, false);

		// Find button and click it
		Button buttonJoinGame = (Button) activity.findViewById(R.id.button_join_game);
		TouchUtils.clickView(this, buttonJoinGame);

		JoinGameActivity joinGameActivity = (JoinGameActivity) monitorJoinGameActivity.waitForActivityWithTimeout(2000);
		assertNotNull("joinGame activity is unset", joinGameActivity);

		GameControllerForTest gameController = new GameControllerForTest();

		gameController.setDeployementIHMInterface(new DeployementComputer());
		gameController.setMyPlayer(new Player("Opponent Name", R.drawable.pirate1, 4, true, WebAccess.getIpAddress()));
		gameController.startHostGame();
		// Find button and click it
		final EditText buttonDirectIpInput = (EditText) joinGameActivity.findViewById(R.id.directIPInput);
		// set text
		getActivity().runOnUiThread(new Runnable() {

			@Override
			public void run() {
				buttonDirectIpInput.setText(WebAccess.getIpAddress());
			}
		});

		getInstrumentation().removeMonitor(monitorJoinGameActivity);
		ActivityMonitor monitorDeployementActivity = getInstrumentation().addMonitor(DeployementActivity.class.getName(), null, false);

		Button buttonEnterIp = (Button) joinGameActivity.findViewById(R.id.buttonEnterIp);
		TouchUtils.clickView(this, buttonEnterIp);


		DeployementActivity deployementActivity = (DeployementActivity) monitorDeployementActivity.waitForActivityWithTimeout(3000);
		assertNotNull("Deployment activity is unset", deployementActivity);
		gameController.getOpponentGameController().whoIAm(gameController.getMyPlayer());

		TextView textFirstPlayerName = (TextView) deployementActivity.findViewById(R.id.firstPlayerName);
		String opponentPlayerName = gameController.getMyPlayer().getName();
		String currentOpponentPlayerName = textFirstPlayerName.getText().toString();
		assertTrue("Opponent player name  should be '" + opponentPlayerName + "', instead of '" + currentOpponentPlayerName + "'", currentOpponentPlayerName.contentEquals(opponentPlayerName));

		getInstrumentation().waitForIdleSync();

		Log.i("Done : Join game test");

	}

}
