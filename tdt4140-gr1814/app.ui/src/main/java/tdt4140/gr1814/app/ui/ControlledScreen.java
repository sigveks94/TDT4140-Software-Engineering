package tdt4140.gr1814.app.ui;

import tdt4140.gr1814.app.core.participants.Patient;

public interface ControlledScreen {
    
    //This method will allow the injection of the Parent ScreenPane. All fxml-controllers implement this interface.
    public void setScreenParent(ScreensController screenPage);
    
    public void showAlarm(Patient patient);
}