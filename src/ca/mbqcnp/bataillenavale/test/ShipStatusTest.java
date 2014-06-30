package ca.mbqcnp.bataillenavale.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import android.test.AndroidTestCase;

import ca.mbqcnp.bataillenavale.entity.ShipStatus;
import ca.mbqcnp.bataillenavale.entity.Square;

public class ShipStatusTest  extends AndroidTestCase {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void  testIsSunk(){
		System.out.println("testShipShunk");
		int numTest = 1;
		//initialise a ship shunk
		Square squares[] = new Square[5];
		for(int i = 0; i < 5; i++){
			squares[i] = new Square(1,1);
			squares[i].attack();
		}
		ShipStatus shipStatus = new ShipStatus(squares);
		
		// start test
		assertTrue("test " + numTest++ + " : ship should be sunk", shipStatus.isSunk());
	
		squares[3].unAttack();
		assertFalse("test " + numTest++ + " : ship should be unsunk", shipStatus.isSunk() );
		
		squares[3].attack();
		assertTrue("test " + numTest++ + " : ship should be unsunk", shipStatus.isSunk() );
		
		for(int i = 0; i < 5; i++){
			squares[i].unAttack();
		}
		assertFalse("test " + numTest++ + " : ship should be unsunk", shipStatus.isSunk() );
	}

}
