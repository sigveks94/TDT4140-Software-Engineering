package tdt4140.gr1814.app.ui;

public class GitlabCISupport {
	
	
	/*
	 * This method allows Gitlab to run testing in headless mode,
	 * i.e. no keyboard or mouse present. It is also possible change
	 * other aspects of the continous integration on Gitlab, such as 
	 * the last line which alters the timout to 5 seconds.
	 */
    public static void headless() {
        System.setProperty("prism.verbose", "true"); // optional
        System.setProperty("java.awt.headless", "true");
        System.setProperty("testfx.robot", "glass");
        System.setProperty("testfx.headless", "true");
        System.setProperty("glass.platform", "Monocle");
        System.setProperty("monocle.platform", "Headless");
        System.setProperty("prism.order", "sw");
        System.setProperty("prism.text", "t2k");
        System.setProperty("testfx.setup.timeout", "5000");
    }
}
