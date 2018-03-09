package tdt4140.gr1814.app.core;

import org.junit.Before;
import org.junit.Test;
import org.junit.After;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;


public class Hyperlink_BrowserTest {
	
	Hyperlink_Browser browser;
	String failURL = "www.error..";
	String successURL = "https://www.datatilsynet.no/rettigheter-og-plikter/overvaking-og-sporing/lokalisering/";
	
	@Before
	public void setUp() {
		browser = new Hyperlink_Browser();
	}
	/*
	@Test
	public void hyperlinkfailTest() {
		assertFalse(browser.browse(failURL));
	}
	*/
	/*
	@Test
	public void hyperlinksuccessTest() {
		assertTrue(browser.browse(successURL));
	}
	*/

}
