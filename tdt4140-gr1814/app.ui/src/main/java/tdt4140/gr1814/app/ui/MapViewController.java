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
import javafx.event.EventHandler;
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
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import tdt4140.gr1814.app.core.datasaving.DataFetchController;
import tdt4140.gr1814.app.core.listeners.OnLocationChangedListener;
import tdt4140.gr1814.app.core.participants.Patient;
import tdt4140.gr1814.app.core.zones.Point;
import tdt4140.gr1814.app.core.zones.ZoneTailored;


//This is the controller class that controls the mapview window
public class MapViewController implements Initializable, MapComponentInitializedListener, OnLocationChangedListener,ControlledScreen{

	private ScreensController myController;
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
	Button deleteZone_btn;
	@FXML
	TableView<Patient> patient_list;
	@FXML
	TableColumn<Patient, String> list_names;
	@FXML
	TableColumn<Patient, CheckBox> list_view;
	@FXML
	TableColumn<Patient, CheckBox> list_zoneView;
	@FXML
	Button patientList_btn;
	@FXML
	ImageView view_img;
	@FXML
	ImageView zone_img;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		mapView.addMapInitializedListener(this); 
		PrepareTable();
	}
	
	//Sets the mapview center and settings
	@Override
	public void mapInitialized() {
		LatLong mapCenter = new LatLong(63.423000, 10.400000);		
		MapOptions mapOptions = new MapOptions();
		mapOptions.center(mapCenter)
				  .zoom(13).mapType(MapTypeIdEnum.ROADMAP)
				  .clickableIcons(false)
				  .streetViewControl(false)
				  .zoomControl(true)
				  .fullscreenControl(false);
		map = mapView.createMap(mapOptions);		
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
	//This method recieves a number of patient objects that will appear on the map, and adds them with their zone to the hashmap patientZoneOnMap.
	public void addAllViewablesPlygon(List<Patient> patients) {
		for (Patient p: patients) {
			if(patientZoneOnMap.get(p)==null) {
				if(p.getZone() != null) {
					Polygon pol = createPolygon(p, "yellow", false);
			        pol.setDraggable(false);
			        this.patientZoneOnMap.put(p, pol);
			        pol.setVisible(false);
			        map.addMapShape(pol); 
				}
				else {p.getViewZoneOnMap().setSelected(false);} //if there is no zone, the checkbox should not be checked
			}
		}
	}
	
	//This method recieves a patient objects that will appear on the map, and adds it and its zone to the hashmap patientZoneOnMap.
	public void addViewablesPolygon(Patient patient) {
		if(patient.getZone() != null) {
			Polygon pol = createPolygon(patient, "yellow", false);
		    pol.setDraggable(false);
		    pol.setVisible(false);
		    map.removeMapShape(patientZoneOnMap.get(patient));
		    patientZoneOnMap.replace(patient, pol);
		    map.addMapShape(pol);
		}
		else {patientZoneOnMap.replace(patient, null);}
	}
	
	public Polygon createPolygon(Patient patient, String color, boolean editable) {
		ArrayList<LatLong> latLongArrayList;
		latLongArrayList = new ArrayList<>();
		for (Point poi : patient.getZone().getPoints()) {
			latLongArrayList.add(new LatLong(poi.getLat(),poi.getLongt()));
		}
		LatLong[] latArr = new LatLong[latLongArrayList.size()];
		for (int i = 0; i < latLongArrayList.size(); i++) {
			latArr[i] = latLongArrayList.get(i);
		}
		MVCArray mvc = new MVCArray(latArr);
		PolygonOptions polyOpts = new PolygonOptions()
						        		.paths(mvc)
						        		.strokeColor("black")
						        		.fillColor(color)
						        		.editable(editable)
						        		.strokeWeight(1)
						        		.fillOpacity(0.4);
        Polygon pol = new Polygon(polyOpts);
        return pol;
	}
	
	//Creates eventhandler when clicking patient name(centering and setting zoom). 
	//Creates observableValue checkboxes in the tableview-columns for displaying markers and zones.
	public void PrepareTable() {
		patient_list.setFixedCellSize(25);
		patient_list.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
            		if(patient_list.getSelectionModel().getSelectedItem().getCurrentLocation() != null) {
               map.setCenter(patient_list.getSelectionModel().getSelectedItem().getCurrentLocation().getLatLong());
               if(map.getZoom() < 13) {map.setZoom(15);}}
            }
        });
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
                        if(patientZoneOnMap.get(user) != null) {displayZoneOnMap(user,new_val);}
                    }
                });
                return new SimpleObjectProperty<CheckBox>(checkBox);
            }
        });
		list_zoneView.setSortable(false);
	}
	
	//closes or displays the patient-list
	public void displayPatientList() {
		if(patient_list.isVisible()) {
			patientList_btn.setStyle("-fx-background-color: white;");
			patient_list.setVisible(false);
			view_img.setVisible(false);
			zone_img.setVisible(false);
			}
		else {
			patientList_btn.setStyle("-fx-background-color: invisible;");
			patient_list.setVisible(true);
			view_img.setVisible(true);
			zone_img.setVisible(true);
			}
	}
	//display function for zones
	public void displayZoneOnMap(Patient patient, Boolean b) {
		patientZoneOnMap.get(patient).setVisible(b);
	}
	
	//display function for markers
	public void displayOnMap(Patient patient, Boolean b) {
		patientsOnMap.get(patient).setVisible(b);
	}
	
	//when entering screen to view patient markers and zones this wil be executed. 
	//Here we hide all editing features, and show markers, zones and relevant buttons like patientList_btn.
	public void patientView() {
		menu_btn.setVisible(true);
		overview_btn.setVisible(false);
		saveZone_btn.setVisible(false);
		deleteZone_btn.setVisible(false);
		PrepareTable();
		//Patient list
		patient_list.setVisible(false);
		view_img.setVisible(false);
		zone_img.setVisible(false);
		patientList_btn.setVisible(true);
		patientList_btn.setStyle("-fx-background-color: white;");
		
		if (mapPolygon != null) {mapPolygon.getPath().clear();}
		 //display last location when opening map. solves problem of dissapearing markers when inputstream is over
		for (Patient p: patientsOnMap.keySet()) { //display last location when opening map. solves problem of dissapearing markers when inputstream is over
			if (!patient_Obslist.contains(p)) {patient_Obslist.add(p);}
			if (p.getCurrentLocation() != null) {
			onLocationChanged(p.getID(),p.getCurrentLocation());
			}
			if(p.getViewZoneOnMap().isSelected() && patientZoneOnMap.get(p)!=null) {displayZoneOnMap(p, true);;}
			Marker marker = this.patientsOnMap.get(p);
			if (marker != null) {
				displayOnMap(p,true);
			}
		}
		patient_list.setItems(patient_Obslist);
		//Setting size of tableview depending on number of patients. should be changed if we have deleted or added patients.
		patient_list.prefHeightProperty().bind(patient_list.fixedCellSizeProperty().multiply(Bindings.size(patient_list.getItems()).add(2.01)));
		patient_list.minHeightProperty().bind(patient_list.prefHeightProperty());
		patient_list.maxHeightProperty().bind(patient_list.prefHeightProperty());	
		
	}
	//when editing a patients zone from the patient-overview this wil be executed. Here we hide all zones,markers and features not related 
	//to editing a spesific patients zone, and show relevant buttons like delete_zone and save_sone.
	public void zoneView(Patient currentPatient) {
		this.currentPatient = currentPatient;
		if(currentPatient.getCurrentLocation() != null) {
			map.setCenter(currentPatient.getCurrentLocation().getLatLong());
		}
		menu_btn.setVisible(false);
		overview_btn.setVisible(true);
		saveZone_btn.setVisible(true);
		deleteZone_btn.setVisible(true);
		patient_list.setVisible(false);
		patientList_btn.setVisible(false);
		view_img.setVisible(false);
		zone_img.setVisible(false);
		for (Patient p: Patient.getAllPatients()) {
			if (patientZoneOnMap.get(p)!=null) {patientZoneOnMap.get(p).setVisible(false);}//hide zones when in zone-edit-view
			Marker marker = this.patientsOnMap.get(p);
			if (marker != null) {
				displayOnMap(p,false);
		}}
		if (currentPatient.getZone() == null) {
			displayNewZone(currentPatient);
		}else {
	        mapPolygon = createPolygon(currentPatient, "green", true);
	        mapPolygon.setDraggable(true);
	        map.addMapShape(mapPolygon);
		}
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
		//if(list_zoneView.getCellData(currentPatient).isSelected()) {patientZoneOnMap.get(currentPatient).setVisible(true);}
		zoneView(currentPatient);
		DataFetchController controller = new DataFetchController();
		controller.deleteZone(currentPatient);
		controller.insertZone(currentPatient);
		addViewablesPolygon(currentPatient);
		displayZoneOnMap(currentPatient, currentPatient.getViewZoneOnMap().isSelected());
	}
	
	public void deleteZone(){
		DataFetchController controller = new DataFetchController();
		controller.deleteZone(currentPatient);
		currentPatient.addZone(null);
		map.removeMapShape(mapPolygon);
		patientZoneOnMap.replace(currentPatient, null);
		displayNewZone(currentPatient);
	}
	
	//creates a new editable polygon on the map for creating a new zone to the current patient.
	public void displayNewZone(Patient currentPatient) {
		double lat;
		double longt;
		if (currentPatient.getCurrentLocation() != null) {
			lat = currentPatient.getCurrentLocation().getLat();
			longt = currentPatient.getCurrentLocation().getLongt();
		}else {
			lat = map.getCenter().getLatitude();
			longt = map.getCenter().getLongitude();
		}
		PolygonOptions polyOpts;
		LatLong[] latArr;
		String fillcolor = null;
		LatLong lat1 = new LatLong(lat+00.002508, longt-00.002743); //venstre hjørne topp
        LatLong lat2 = new LatLong(lat+00.002451, longt+00.002103); //høyre hjørne topp
        LatLong lat3 = new LatLong(lat-00.002663, longt+00.002103); //høyre hjørne bunn
        LatLong lat4 = new LatLong(lat-00.002414, longt-00.002529); //venstre bunn
        latArr = new LatLong[] {lat1,lat2,lat3,lat4};
        fillcolor = "red";
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
	
	//used when patient is deleted from patient-overview
	public void removePatientFromMap(Patient patient) {
		if (patientsOnMap.get(patient) != null)
			map.removeMarker(patientsOnMap.get(patient));
		if (patientZoneOnMap.get(patient) != null) {
			map.removeMapShape(patientZoneOnMap.get(patient));
		}
		patientsOnMap.remove(patient);
		patientZoneOnMap.remove(patient);
		patient_Obslist.remove(patient);
		System.out.println("obslist;");
		for (Patient p: patient_Obslist) {System.out.println(p.getFirstName());}
	}
	
	@Override
	public void setScreenParent(ScreensController screenParent) {
		myController = screenParent;	
	}
	
	@FXML
	public void goToHome(ActionEvent event) {
		for (Patient patient : patientsOnMap.keySet()) {
			if(patientZoneOnMap.get(patient) != null) {displayZoneOnMap(patient, false);}
		}
		patientView();
		myController.setScreen(ApplicationDemo.HomescreenID);
	}
	
	@FXML
	public void goToOverview() {
		patientView();
		myController.setScreen(ApplicationDemo.PatientOverviewID);
	}
	
	@Override
	public void showAlarm(Patient patient) {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "\t\tPatient is currently outside zone.\n\t\tShow in map?", ButtonType.CLOSE, ButtonType.OK);
		alert.setTitle("");
		alert.setHeaderText("\t\t\t     ALARM!");
		DialogPane dialogPane = alert.getDialogPane();
		dialogPane.setStyle("-fx-background-color: #f3f4f7;");
		Image image = new Image(ApplicationDemo.class.getResourceAsStream("pictures/mapWarning.png"));
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
	
	
}