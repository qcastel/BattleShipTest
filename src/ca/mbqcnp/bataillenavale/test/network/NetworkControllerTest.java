package ca.mbqcnp.bataillenavale.test.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.Semaphore;

import android.test.AndroidTestCase;
import ca.mbqcnp.bataillenavale.network.NetworkController;
import ca.mbqcnp.bataillenavale.network.NetworkInterface;
import ca.mbqcnp.bataillenavale.util.log.Log;

public class NetworkControllerTest extends AndroidTestCase {

	public static class HosterClassTest implements Runnable {

		protected Semaphore closeConnexion = new Semaphore(0);
		protected PingClassTest pingClassTest;
		protected IPingClassTest pingOpponent;

		public void close() {
			this.closeConnexion.release();
		}

		@Override
		public void run() {
			try {
				this.pingClassTest = new PingClassTest();
				ServerSocket serverSocket = new ServerSocket(SERVERPORT);
				Log.v("Waiting for client at " + serverSocket.getInetAddress().getHostAddress() + ":" + serverSocket.getLocalPort());
				Socket socket = serverSocket.accept();
				Log.v("Client connected.");

				NetworkController networkControllerV2 = new NetworkController(this.pingClassTest, IPingClassTest.class,  socket.getInputStream(), socket.getOutputStream());
				this.pingOpponent = (IPingClassTest) NetworkController.newInstance(networkControllerV2, IPingClassTest.class);
				this.closeConnexion.acquire();
				Log.v("Close.");
			} catch (IOException e) {
				Log.e("Socket error", e);
				;
			} catch (InterruptedException e) {
				Log.e("Semaphore error", e);
				;
			}

		}

		public boolean wasPingMethodeCalled() {
			return this.pingClassTest.pingMethodeCalled;
		}

	}

	public static interface IPingClassTest {
		public String ping(String message);
	}

	public static class JoinnerClassTest implements Runnable {

		protected Semaphore closeConnexion = new Semaphore(0);
		protected Semaphore connexionOpen = new Semaphore(0);
		protected PingClassTest pingClassTest;
		protected IPingClassTest pingOpponent;

		public void close() {
			this.closeConnexion.release();
		}

		public String ping(String message) throws InterruptedException {
			this.connexionOpen.acquire();
			return this.pingOpponent.ping(message);
		}
		@Override
		public void run() {
			try {
				Log.v("JoinnerClassTest", "Start connexion.");
				Socket socket = new Socket("127.0.0.1", SERVERPORT);
				Log.v("JoinnerClassTest", "connected.");
				NetworkController networkControllerV2 = new NetworkController(this.pingClassTest, IPingClassTest.class, socket.getInputStream(), socket.getOutputStream());
				this.pingOpponent = (IPingClassTest) NetworkController.newInstance(networkControllerV2, IPingClassTest.class);
				this.connexionOpen.release();

				this.closeConnexion.acquire();
				Log.v("Close.");

			} catch (IOException e) {
				Log.e("Socket error", e);
				;
			} catch (InterruptedException e) {
				Log.e("Semaphore error", e);
				;

			}

		}

	}

	public static class PingClassTest implements IPingClassTest, NetworkInterface {

		public boolean pingMethodeCalled = false;

		@Override
		public void connectionClose() {
		}

		@Override
		public void connectionClose(boolean b) {
		}

		@Override
		public String ping(String message) {
			this.pingMethodeCalled = true;
			return "Pong :" + message;
		}

	}

	public static final int SERVERPORT = 8182;


	public void testCreateInstance() throws UnknownHostException, IOException, InterruptedException {

		Log.v("Initialize");

		HosterClassTest hosterClassTest = new HosterClassTest();
		Thread hosterThread = new Thread(hosterClassTest);
		hosterThread.setName("Hoster test");

		hosterThread.start();

		JoinnerClassTest joinnerClassTest = new JoinnerClassTest();
		Thread joinnerThread = new Thread(joinnerClassTest);
		joinnerThread.setName("Joinner test");
		joinnerThread.start();

		Log.v("Do story");

		String message = "Hello !";
		String answer = joinnerClassTest.ping(message);
		String expectedMessage = new PingClassTest().ping(message);

		Log.v("Check result");

		assertTrue("Hoster doesn't received a ping message", hosterClassTest.wasPingMethodeCalled());
		assertTrue("Ping answer isn't equals to expected message : ", expectedMessage.contentEquals(answer));

		hosterClassTest.close();
		joinnerClassTest.close();

		Log.v("Test done");
	}

}
