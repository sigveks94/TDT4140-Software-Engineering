package tdt4140.gr1814.app.ui;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import com.lynden.gmapsfx.GetCoordinatesFromMap;
import com.lynden.gmapsfx.GoogleMapView;
import com.lynden.gmapsfx.MapComponentInitializedListener;
import com.lynden.gmapsfx.javascript.object.GoogleMap;
import com.lynden.gmapsfx.javascript.object.LatLong;
import com.lynden.gmapsfx.javascript.object.MVCArray;
import com.lynden.gmapsfx.javascript.object.MapOptions;
import com.lynden.gmapsfx.javascript.object.MapTypeIdEnum;
import com.lynden.gmapsfx.javascript.object.Marker;
import com.lynden.gmapsfx.javascript.object.MarkerOptions;
import com.lynden.gmapsfx.shapes.Polygon;
import com.lynden.gmapsfx.shapes.PolygonOptions;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;
import tdt4140.gr1814.app.core.datasaving.DataFetchController;
import tdt4140.gr1814.app.core.listeners.OnLocationChangedListener;
import tdt4140.gr1814.app.core.participants.Patient;
import tdt4140.gr1814.app.core.zones.Point;
import tdt4140.gr1814.app.core.zones.ZoneTailored;


//This is the controller class that controls the mapview window
public class MapViewController implements Initializable, MapComponentInitializedListener, OnLocationChangedListener,ControlledScreen{

	private ScreensController myController;
	private boolean newZoneMap;//screen when adding new zone to a patient.
	private Polygon mapPolygon;
	private Patient currentPatient;
	
	//The hashmap holds track of all the patients that are currently being displayed in the map. Each patient has a marker associated with it, and by passing the patient as key the associated marker is return as the value
	private Map<Patient, Marker> patientsOnMap;
	private Map<Patient, Polygon> patientZoneOnMap;
	private ObservableList<Patient> patient_Obslist = FXCollections.observableArrayList();
	
	public MapViewController() {
		this.patientsOnMap = new HashMap<Patient, Marker>();
		this.patientZoneOnMap = new HashMap<Patient, Polygon>();
	}
	
	@FXML
	GoogleMapView mapView;
	@FXML
	GoogleMap map;
	@FXML
	Button overview_btn;
	@FXML
	Button menu_btn;
	@FXML
	Button saveZone_btn;
	@FXML
	TableView<Patient> patient_list;
	@FXML
	TableColumn<Patient, String> list_names;
	@FXML
	TableColumn<Patient, CheckBox> list_view;
	@FXML
	TableColumn<Patient, CheckBox> list_zoneView;

	//This method recieves a number of patient objects that will appear on the map. Aswell as adding the patient to the hashmap this mapview controller adds itself as a listener to the patient object. Whenever
	//a patient gets it location updated this controller object will be notified in order to update the marker on the map
	public void addViewables(Patient... patients) {
		for(Patient p : patients) {
			this.patientsOnMap.put(p, null);
			p.registerListener(this);	
		}
	}
	
	//Does the same as the method above, argument is List instead of varargs
	public void addAllViewables(List<Patient> patients) {
		for(Patient p : patients) {
			this.patientsOnMap.put(p, null);
			p.registerListener(this);
		}
	}
	
	public void addAllViewablesPlygon(List<Patient> patients) {
		ArrayList<LatLong> latLongArrayList;
		for (Patient p: patients) {
			if(patientZoneOnMap.get(p)==null) {
			latLongArrayList = new ArrayList<>();
			if(p.getZone() != null) {
			for (Point poi : p.getZone().getPoints()) {
				latLongArrayList.add(new LatLong(poi.getLat(),poi.getLongt()));//fail
			}
			LatLong[] latArr = new LatLong[latLongArrayList.size()];
			for (int i = 0; i < latLongArrayList.size(); i++) {
				latArr[i] = latLongArrayList.get(i);
			}
			MVCArray mvc = new MVCArray(latArr);
			PolygonOptions polyOpts = new PolygonOptions()
							        		.paths(mvc)
							        		.strokeColor("black")
							        		.fillColor("yellow")
							        		.editable(false)
							        		.strokeWeight(1)
							        		.fillOpacity(0.4);
	        Polygon pol = new Polygon(polyOpts);
	        pol.setDraggable(false);
	        this.patientZoneOnMap.put(p, pol);
			}
			else {p.getViewableOnMap().setSelected(false);} //if there is no zone, the checkbox should not be checked
		}}
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		mapView.addMapInitializedListener(this); 
		patient_list.setFixedCellSize(25);
		list_names.setCellValueFactory(new PropertyValueFactory<>("FirstName"));
		list_view.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Patient, CheckBox>, ObservableValue<CheckBox>>() {
            @Override
            public ObservableValue<CheckBox> call(
                    TableColumn.CellDataFeatures<Patient, CheckBox> arg0) {
                		Patient user = arg0.getValue();

                		CheckBox checkBox = new CheckBox();

                		checkBox.selectedProperty().setValue(user.getViewableOnMap().isSelected());

                		checkBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
                		public void changed(ObservableValue<? extends Boolean> ov, Boolean old_val, Boolean new_val) {

                        user.getViewableOnMap().setSelected(new_val);
                        displayOnMap(user,new_val);
                    }
                });
                return new SimpleObjectProperty<CheckBox>(checkBox);
            }
        });
		list_view.setSortable(false);
		list_zoneView.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Patient, CheckBox>, ObservableValue<CheckBox>>() {
            @Override
            public ObservableValue<CheckBox> call(
                    TableColumn.CellDataFeatures<Patient, CheckBox> arg0) {
                		Patient user = arg0.getValue();

                		CheckBox checkBox = new CheckBox();

                		checkBox.selectedProperty().setValue(user.getViewZoneOnMap().isSelected());

                		checkBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
                		public void changed(ObservableValue<? extends Boolean> ov, Boolean old_val, Boolean new_val) {

                        user.getViewZoneOnMap().setSelected(new_val);
                        displayZoneOnMap(user,new_val);
                    }
                });
                return new SimpleObjectProperty<CheckBox>(checkBox);
            }
        });
		list_zoneView.setSortable(false);
		
		
	}

	
	@Override
	public void mapInitialized() {
		//Sets the mapview center
		LatLong mapCenter = new LatLong(63.423000, 10.400000);		
		
		//Sets the mapview type, denies clickable icons like markers marking shops and other facilities, disables streetview and enables zoomcontrol.
		MapOptions mapOptions = new MapOptions();
		mapOptions.center(mapCenter)
				  .zoom(13).mapType(MapTypeIdEnum.ROADMAP)
				  .clickableIcons(false)
				  .streetViewControl(false)
				  .zoomControl(true)
				  .fullscreenControl(false);
		
		map = mapView.createMap(mapOptions);

		//Implementing tailored zone
		/*
		//Adds the zone for each patient to the map so its visible for the user
		/*
		for(Patient p : this.patientsOnMap.keySet()) {
			if(p.getZone() == null) {
				continue;
			}
			Circle zone = new Circle();
			zone.setCenter(p.getZone().getCentre().getLatLong());
			zone.setRadius(p.getZone().getRadius());
			map.addMapShape(zone);
		}
		*/
		
	}

	//This is the method inherited from the "OnLocationChangedListener" interface. Whenever a patient gets it location changed it will notify its listener. This map will be one of its listeners. When the location changes
	//The old marker associated with the patient is removed and a new is placed on the map with the new location. The hashmap is also updated.
	@Override
	public void onLocationChanged(String deviceId, Point newLocation) {
		Patient patient = Patient.getPatient(deviceId);
		LatLong latlong = new LatLong(newLocation.getLat(), newLocation.getLongt());
		Marker marker = this.patientsOnMap.get(patient);
		if(marker != null) {
			marker.setOptions(new MarkerOptions().position(latlong).label(patient.getFullName()));
		}
		else {
			marker = new Marker(new MarkerOptions().position(latlong).visible(true).label(patient.getFullName()));
			this.patientsOnMap.replace(patient, marker);
			map.addMarker(marker);
		}
	}
	
	public void displayZoneOnMap(Patient patient, Boolean b) {
		if(b){
			if (patientsOnMap.get(patient).getVisible()) {
				map.addMapShape(patientZoneOnMap.get(patient));
			}
		}else {map.removeMapShape(patientZoneOnMap.get(patient));}
	}
	
	public void displayOnMap(Patient patient, Boolean b) {
		patientsOnMap.get(patient).setVisible(b);
	}

	
	@Override
	public void setScreenParent(ScreensController screenParent) {
		myController = screenParent;	
	}
	
	@FXML
	public void goToHome(ActionEvent event) {
		for (Patient patient : patientsOnMap.keySet()) {
			map.removeMapShape(patientZoneOnMap.get(patient));
			patient.getViewZoneOnMap().setSelected(false);
			list_zoneView.getCellData(patient).setSelected(false);
		}
		patientView();
		myController.setScreen(ApplicationDemo.HomescreenID);
	}
	
	@FXML
	public void goToOverview() {
		patientView();
		myController.setScreen(ApplicationDemo.PatientOverviewID);
	}
	public void zoneView(Patient currentPatient) {
		this.currentPatient = currentPatient;
		menu_btn.setVisible(false);
		overview_btn.setVisible(true);
		saveZone_btn.setVisible(true);
		newZoneMap = true;
		for (Patient p: Patient.patients) {
			Marker marker = this.patientsOnMap.get(p);
			if (marker != null) {
				displayOnMap(p,false);
		}}
		PolygonOptions polyOpts;
		LatLong[] latArr;
		String fillcolor = null;
		if (currentPatient.getZone() == null) {
	        LatLong lat1 = new LatLong(63.426508,10.394743);
	        LatLong lat2 = new LatLong(63.426451,10.397103);
	        LatLong lat3 = new LatLong(63.425663,10.397103);
	        LatLong lat4 = new LatLong(63.425414,10.394529);
	        
	        latArr = new LatLong[] {lat1,lat2,lat3,lat4};
	        fillcolor = "red";
	        
		} else {
			ArrayList<LatLong> latLongArrayList = new ArrayList<>();
			for (Point poi : currentPatient.getZone().getPoints()) {
				latLongArrayList.add(new LatLong(poi.getLat(),poi.getLongt()));
			}
			latArr = new LatLong[latLongArrayList.size()];
			for (int i = 0; i < latLongArrayList.size(); i++) {
				latArr[i] = latLongArrayList.get(i);
			}
			fillcolor = "green";
		}
		MVCArray mvc = new MVCArray(latArr);
        polyOpts = new PolygonOptions()
        		.paths(mvc)
        		.strokeColor("black")
        		.fillColor(fillcolor)
        		.editable(true)
        		.strokeWeight(1)
        		.fillOpacity(0.4);
        mapPolygon = new Polygon(polyOpts);
        mapPolygon.setDraggable(true);
        map.addMapShape(mapPolygon);
	}
	
	public void patientView() {
		menu_btn.setVisible(true);
		overview_btn.setVisible(false);
		saveZone_btn.setVisible(false);
		newZoneMap = false;
		if (mapPolygon != null) {mapPolygon.getPath().clear();}
		 //display last location when opening map. solves problem of dissapearing markers when inputstream is over
		for (Patient p: patientsOnMap.keySet()) { //display last location when opening map. solves problem of dissapearing markers when inputstream is over
			if (!patient_Obslist.contains(p)) {patient_Obslist.add(p);}
			if (p.getCurrentLocation() != null) {
			onLocationChanged(p.getID(),p.getCurrentLocation());
			}
		}
		patient_list.setItems(patient_Obslist);
		//Setting size of tableview depending on number of patients
		patient_list.prefHeightProperty().bind(patient_list.fixedCellSizeProperty().multiply(Bindings.size(patient_list.getItems()).add(2.01)));
		patient_list.minHeightProperty().bind(patient_list.prefHeightProperty());
		patient_list.maxHeightProperty().bind(patient_list.prefHeightProperty());

		
		
	}
	
	public void saveZone() throws SQLException {
		GetCoordinatesFromMap getArray = new GetCoordinatesFromMap(mapPolygon.getPath());
		getArray.calculate();
		ArrayList<double[]> makePoints = getArray.getLatLongSave();
		ArrayList<Point> pointList = new ArrayList<Point>();
		for (double[] latLong : makePoints) {
			pointList.add(new Point(currentPatient.getID(),latLong[0],latLong[1]));
		}
		currentPatient.addZone(new ZoneTailored(pointList));
		System.out.println("SAVING...");
		map.removeMapShape(mapPolygon);
		zoneView(currentPatient);

		DataFetchController controller = new DataFetchController();
		controller.deleteZone(currentPatient);
		controller.insertZone(currentPatient);
	}

	@Override
	public void showAlarm(Patient patient) {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "\t\tPatient is currently outside zone.\n\t\tShow in map?", ButtonType.CLOSE, ButtonType.OK);
		alert.setTitle("");
		alert.setHeaderText("\t\t\t     ALARM!");
		DialogPane dialogPane = alert.getDialogPane();
		dialogPane.setStyle("-fx-background-color: #f3f4f7;");
		Image image = new Image(ApplicationDemo.class.getResourceAsStream("mapWarning.png"));
		ImageView imageView = new ImageView(image);
		alert.setGraphic(imageView);
		alert.showAndWait();
		if (alert.getResult() == ButtonType.OK) {
			map.setCenter(patient.getCurrentLocation().getLatLong());
			myController.getMapViewController().map.setZoom(15);
			patientView();
			}
		if (alert.getResult() == ButtonType.CLOSE) {alert.close();;}
		
	}
	
	public void removePatientFromMap(Patient patient) {
		Marker marker =patientsOnMap.get(patient);
		map.removeMarker(marker);
	}
	
	
}