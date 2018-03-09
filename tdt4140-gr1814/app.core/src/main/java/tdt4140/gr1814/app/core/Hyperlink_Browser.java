package tdt4140.gr1814.app.core;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

// Class with function for opening given url on default internet-browser through eclipse
public class Hyperlink_Browser {
	
	
    public static boolean browse(String url) {
        if(Desktop.isDesktopSupported()){
            Desktop desktop = Desktop.getDesktop();
            try {
                desktop.browse(new URI(url));
                return true;
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
                return false;
            }
        }else{
            Runtime runtime = Runtime.getRuntime();
            try {
                runtime.exec("xdg-open " + url);
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
    }
}
