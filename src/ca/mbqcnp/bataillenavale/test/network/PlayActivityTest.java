package ca.mbqcnp.bataillenavale.test.network;

import android.app.Instrumentation.ActivityMonitor;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.widget.Button;
import android.widget.TextView;
import ca.mbqcnp.bataillenavale.DeployementActivity;
import ca.mbqcnp.bataillenavale.PlayActivity;
import ca.mbqcnp.bataillenavale.R;
import ca.mbqcnp.bataillenavale.util.log.Log;

public class PlayActivityTest extends ActivityInstrumentationTestCase2<PlayActivity> {

	public PlayActivityTest() {
		super(PlayActivity.class);
	}


	public void testHostGame() {

		Log.i("Start Host game test");

		setActivityInitialTouchMode(false);
		PlayActivity activity = getActivity();

		// Add monitor to check for the second activity
		ActivityMonitor monitor = getInstrumentation().addMonitor(DeployementActivity.class.getName(), null, false);

		// Find button and click it
		Button buttonHostGame = (Button) activity.findViewById(R.id.button_host_game);
		TouchUtils.clickView(this, buttonHostGame);

		DeployementActivity deployementActivity = (DeployementActivity) monitor.waitForActivityWithTimeout(2000);
		assertNotNull("Deployment activity is unset", deployementActivity);

		TextView title = (TextView) deployementActivity.findViewById(R.id.title);

		String waitingMessage = deployementActivity.getResources().getString(R.string.wait_other_player);
		String currentMessage = title.getText().toString();
		assertTrue("Deployment message should be '" + waitingMessage + "', instead of '" + currentMessage + "'", currentMessage.contentEquals(waitingMessage));

		getInstrumentation().waitForIdleSync();

		Log.i("Done : Host game test");

	}

	public void testStartSinglePlayerGame() throws Exception {

		Log.i("Start Single game test");

		setActivityInitialTouchMode(false);
		PlayActivity activity = getActivity();

		// Add monitor to check for the second activity
		ActivityMonitor monitor = getInstrumentation().addMonitor(DeployementActivity.class.getName(), null, false);

		// Find button and click it
		Button buttonStartGame = (Button) activity.findViewById(R.id.button_start_game);
		TouchUtils.clickView(this, buttonStartGame);

		DeployementActivity deployementActivity = (DeployementActivity) monitor.waitForActivityWithTimeout(2000);
		assertNotNull("Deployment activity is unset", deployementActivity);

		Button buttonShuffleShip = (Button) deployementActivity.findViewById(R.id.button_ship);
		TouchUtils.clickView(this, buttonShuffleShip);

		assertTrue("shuffle ship doesn't work.", deployementActivity.getGameController().getArmada().isAllShipsSet());

		getInstrumentation().waitForIdleSync();
		Log.i("Done : Single game test");

	}
}
