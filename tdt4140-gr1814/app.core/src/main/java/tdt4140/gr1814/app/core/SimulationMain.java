package tdt4140.gr1814.app.core;

import java.io.IOException;

public class SimulationMain {

	
public static void main(String[] args) throws IOException {
	
String str = 
		"hgfdsa:35.33868 16.54459 \r\n" + 
		"kjhgff:58.20727 87.45917 \r\n" + 
		"kjhvgfyijn:54.02726 63.24921 \r\n" + 
		"ppoiuytr:20.06281 36.31115 \r\n" + 
		"lkjhgfd:80.07951 13.46279 \r\n" + 
		"ahahahaha:31.57807 88.27476 \r\n" + 
		"kjhgfd:78.56328 39.31201 \r\n" + 
		"djdjdjdj:26.04727 70.13356 \r\n" + 
		"kdjkdkdkd:89.52058 88.57303 \r\n" + 
		"lslslslsls:56.51977 58.97881 \r\n" + 
		"pspspsps:67.98291 51.49189 \r\n" + 
		"ahahahaha:56.48398 33.60740 \r\n" + 
		"ahahahaha:76.35572 06.45651 \r\n" + 
		"papapapa:62.05932 40.98163 \r\n" + 
		"mamamama:11.18858 22.55524 \r\n" + 
		"ahahahaha:25.27939 85.20797 \r\n" + 
		"LALALALA:86.41801 70.07546"; /* \r\n" + 
		"58.37229 10.85163 \r\n" + 
		"20.80940 01.43364 \r\n" + 
		"89.08138 87.39767 \r\n" + 
		"35.52267 35.40681 \r\n" + 
		"42.34427 68.96031 \r\n" + 
		"41.77038 85.76075 \r\n" + 
		"28.56378 23.36101 \r\n" + 
		"60.60187 14.19896 \r\n" + 
		"32.29910 94.42275 \r\n" + 
		"59.34826 35.07703 \r\n" + 
		"50.69082 21.53414 \r\n" + 
		"74.40653 35.39493 \r\n" + 
		"19.16429 57.36424 \r\n" + 
		"41.32875 89.10187 \r\n" + 
		"75.41634 80.70958 \r\n" + 
		"26.33976 42.64613 \r\n" + 
		"83.30168 13.67779 \r\n" + 
		"58.29573 10.08781 \r\n" + 
		"63.80622 74.38624 \r\n" + 
		"36.83390 34.12003 \r\n" + 
		"84.23633 51.69481 \r\n" + 
		"86.15325 46.22770"; */

	InputController c = new InputController();
	Point centreOfAttention = new Point("ahahahaha", 50.00001, 30.00001);
	Patient harald = new Patient("Harald", "Aarskog", 'M', 1234567890l, 98765432, "pappanteharaldsinmail");
	CareTaker morteharald = new CareTaker("morteharaldusername", "pwerhemmelig");
	CareTaker farteharald = new CareTaker("farteharalduser", "pwerhemmelig");
	harald.addListeners(morteharald, farteharald);
	
	c.addPatientInList(harald);
	harald.addZone(centreOfAttention, 5000000.0);
	c.metamorphise(str);
	
}
}
