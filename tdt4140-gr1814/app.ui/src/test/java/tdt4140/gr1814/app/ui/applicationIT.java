package tdt4140.gr1814.app.ui;

import java.io.IOException;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;

public class applicationIT {
	
	ApplicationDemo demo = new ApplicationDemo();
	
	
	
	@Before
	public void setUp() {
		try {
			demo.main(null);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testLogin() {
		
	}
	
}
