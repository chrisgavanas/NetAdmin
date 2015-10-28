package application;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

import org.apache.commons.math3.ml.clustering.Cluster;
import org.apache.commons.math3.ml.clustering.DBSCANClusterer;

import records.BaseStationRecords;
import records.BatteryRecords;
import records.GpsRecords;
import records.WifiRecords;
import Interfaces.BaseStationWrapper;
import Interfaces.BatteryWrapper;
import Interfaces.GpsWrapper;
import Interfaces.WifiWrapper;
import ap_elaboration.ApLocator;

import com.lynden.gmapsfx.GoogleMapView;
import com.lynden.gmapsfx.MapComponentInitializedListener;
import com.lynden.gmapsfx.javascript.object.GoogleMap;
import com.lynden.gmapsfx.javascript.object.LatLong;
import com.lynden.gmapsfx.javascript.object.MVCArray;
import com.lynden.gmapsfx.javascript.object.MapOptions;
import com.lynden.gmapsfx.javascript.object.MapTypeIdEnum;
import com.lynden.gmapsfx.javascript.object.Marker;
import com.lynden.gmapsfx.javascript.object.MarkerOptions;
import com.lynden.gmapsfx.shapes.Polyline;
import com.lynden.gmapsfx.shapes.PolylineOptions;

import data_analyzing.DataAnalyzer;
import data_analyzing.StayPoint;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

public class NetHandler extends Application implements
		MapComponentInitializedListener {
	// ------------------------------------------------------------------------------------------------------------------------------------//
	private GoogleMapView mapView;
	private GoogleMap map;
	private DatePicker FromDatePicker;
	private DatePicker ToDatePicker;
	private VBox vbox;
	private WifiRecords wifi;
	private GpsRecords gps;
	private BaseStationRecords bs;
	private String Selected = null;
	private boolean clicked = false;
	private ComboBox<String> listView;
	private CheckBox cbAP;
	private BatteryRecords bat;
	private CheckBox cbC;
	private CheckBox cbGPS;
	private CheckBox cbSP;
	private MenuBar menuBar;
	private Menu statistics;
	private Button PoI;
	private BarChart<String, Number> bc;
	private GridPane gridpanemain;
	private OptionsWrapper UserStayOptions;
	private OptionsWrapper StayOptions;
	private String AP;
	private String SP;
	private String CELL;
	private String CAP;
	// ------------------------------------------------------------------------------------------------------------------------------------//

	// ------------------------------------------------------------------------------------------------------------------------------------//
	@Override
	public void start(final Stage stage) throws Exception {
		// --Vbox&Gridmain Initialization--//
		String wifiFile = "", baseStationFile = "", gpsFile = "", batteryFile = "";
		Properties prop = new Properties();
		try {
            InputStream propsStream = new FileInputStream("project.properties");
            prop.load(propsStream);
            String tmp = prop.getProperty("access_point");
            if (tmp != null)
                AP = new String(tmp);
            else
            	throw new FileNotFoundException();
            tmp = prop.getProperty("stay_point");
            if (tmp != null)
                SP = new String(tmp);
            else
            	throw new FileNotFoundException();
            tmp = prop.getProperty("cell");
            if (tmp != null)
                CELL = new String(tmp);
            else
            	throw new FileNotFoundException();
            tmp = prop.getProperty("confirmed_access_point");
            if (tmp != null)
                CAP = new String(tmp);
            else
            	throw new FileNotFoundException();
            tmp = prop.getProperty("bs_path");
            if (tmp != null)
            	baseStationFile = new String(tmp);
            else
            	throw new FileNotFoundException();
            tmp = prop.getProperty("wifi_path");
            if (tmp != null)
            	wifiFile = new String(tmp);
            else
            	throw new FileNotFoundException();
            tmp = prop.getProperty("battery_path");
            if (tmp != null)
            	batteryFile = new String(tmp);
            else
            	throw new FileNotFoundException();
            tmp = prop.getProperty("gps_path");
            if (tmp != null)
            	gpsFile = new String(tmp);
            else
            	throw new FileNotFoundException();
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
		vbox = new VBox();
		vbox.setTranslateX(0);
		vbox.setTranslateY(0);
		vbox.setTranslateZ(0);
		vbox.autosize();
        menuBar = new MenuBar();
        menuBar.setStyle("-fx-background-color: #999999");
        statistics = new Menu("Statistics");
        MenuItem item1 = new MenuItem("BatteryPerDay");
        item1.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
            	pop_battery_statisticsPerDay(stage);
            }
        });
        MenuItem item2 = new MenuItem("Companies");
        item2.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
            	pop_companies_statistics(stage);
            }
        });
        MenuItem item3 = new MenuItem("Battery");
        item3.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
            	pop_battery_statistics(stage);
            }
        });
        statistics.getItems().addAll(item1,item2,item3);
        menuBar.getMenus().add(statistics);
		vbox.getChildren().add(menuBar);
		vbox.setStyle("-fx-background-color: black");
		gridpanemain = new GridPane();
		gridpanemain.setStyle("-fx-background-color: #0B0B3B;-fx-border-color: black;");

		// --Vbox&Gridmain Initialization--//

		// --Files Initialization--//
		wifi = new WifiRecords(wifiFile);
		wifi.createIndex("bssid", true);
		wifi.createIndex("userId", true);
		gps = new GpsRecords(gpsFile);
		gps.createIndex("userId", true);
		bs = new BaseStationRecords(baseStationFile);
		bs.createIndex("userId", true);
		bat = new BatteryRecords(batteryFile);
		bat.createIndex("userId", true);
		// --Files Initialization--//

		// --Find AccessPoint's Locations--//
		ApLocator ap = new ApLocator(wifi);
		ap.averageLoc();
		// --Find AccessPoint's Locations--//

		// --Add the options menu--//
		add_option_menu();
		// --Add the options menu--//

		// --Initialize and add the map--//
		mapView = new GoogleMapView();
		mapView.addMapInializedListener(this);
		gridpanemain.add(mapView, 0, 1);
		// --Initialize and add the map--//

		// --Add the battery chart--//
		add_battery_chart();
		// --Add the battery chart--//

		// --Add the gridpane to the main layout--//
		vbox.getChildren().add(gridpanemain);
		// --Add the gridpane to the main layout--//

		// --Create the Scence Initialize it and show the stage--//
		Scene scene = new Scene(new Group(vbox));
		scene.setFill(Color.ROYALBLUE);
		stage.setScene(scene);
		stage.setTitle("NetHandler");
		stage.sizeToScene();
		stage.show();
		// --Create the Scence Initialize it and show the stage--//

	}

	// ------------------------------------------------------------------------------------------------------------------------------------//

	// ------------------------------------------------------------------------------------------------------------------------------------//
	public static void main(String[] args) {
		launch(args);
	}

	// ------------------------------------------------------------------------------------------------------------------------------------//

	// ------------------------------------------------------------------------------------------------------------------------------------//
	@Override
	public void mapInitialized() {
		// --Initialize the map with its options--//
		MapOptions mapOptions = new MapOptions();
		try {
			mapOptions
					.center(new LatLong(37.96771842694788, 23.76956294938037))
					.mapType(MapTypeIdEnum.ROADMAP).overviewMapControl(false)
					.panControl(false).rotateControl(false).scaleControl(false)
					.streetViewControl(true).zoomControl(true).zoom(12);
		} catch (netscape.javascript.JSException e) {
			System.out.println("Check your internet connection :\n"
					+ e.getMessage());
			System.exit(0);
		}
		map = mapView.createMap(mapOptions);
		// --Initialize the map with its options--//

		// --Add to the map the selected data and only them--//
		if (cbAP.isSelected())
			add_access_points();
		if (cbGPS.isSelected())
			add_path();
		if (cbC.isSelected())
			add_base_stations();
		if (cbSP.isSelected())
			add_stay_points();
		if (clicked)
			add_points_of_intrest();
		// --Add to the map the selected data and only them--//
	}

	// ------------------------------------------------------------------------------------------------------------------------------------//

	// ------------------------------------------------------------------------------------------------------------------------------------//
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void add_battery_chart()
	{
		//--Initialize battery chart--//
		CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
		bc=new BarChart<String,Number>(xAxis,yAxis);
        bc.setTitle("Battery Summary");
        xAxis.setLabel("Date/Time");       
        yAxis.setLabel("Battery Level");
        XYChart.Series series1 = new XYChart.Series();      
        bc.setLegendVisible(false);
        //--Initialize battery chart--//
        
        //--Add the data between the selected dates with color code(red=unpugged,blue=usb,green=plugged)--//
        if (Selected!=null)
	    {
			if(bat.getIndex("userId").getBatHm().get(Selected)!=null)
			{
				boolean oversized=false;
				int count=0;
				if( (bat.getIndex("userId").getBatHm().get(Selected).size())>=5000)
				{
					oversized=true;
				}
				for(BatteryWrapper elem : bat.getIndex("userId").getBatHm().get(Selected))
				{
					if (oversized&& count%10!=0) {count++;continue;}
					if ((elem.getDate().compareTo(Date.from(FromDatePicker.getValue().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()))>0)
					    	&&(elem.getDate().compareTo(Date.from(ToDatePicker.getValue().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()))<0))
					{
						 XYChart.Data<String, Number> data = new XYChart.Data<String, Number>(elem.getDate().toString(), elem.getBatteryLevel());
						 data.nodeProperty().addListener(new ChangeListener<Node>() {
						   @Override public void changed(ObservableValue<? extends Node> ov, Node oldNode, Node newNode) 
						   {
						     if (newNode != null) 
						     {
						       if (elem.isPlugged()==0 ) 
						       {
						         newNode.setStyle("-fx-bar-fill: red;");
						       }
						       else if (elem.isPlugged()==1 ) 
						       {
						         newNode.setStyle("-fx-bar-fill: green;");
						       }  
						       else if (elem.isPlugged()==2 ) 
						       {
						         newNode.setStyle("-fx-bar-fill: blue;");
						       }  
						     }
						   }
						 });
						series1.getData().add(data);
					}
					count++;
				}
				bc.getData().add(series1);
			}
	    }     
        //--Add the data between the selected dates with color code(red=unpugged,blue=usb,green=plugged)--//
        
        //--Reset the gridpane--//
        gridpanemain.getChildren().clear();
        gridpanemain.add(bc,1,1);
        gridpanemain.add(mapView,0,1);
        //--Reset the gridpane--//
	}

	// ------------------------------------------------------------------------------------------------------------------------------------//

	// ------------------------------------------------------------------------------------------------------------------------------------//
	public void add_access_points() {
		// --Add user access points between the selected date--//
		if (Selected != null) {
			if (wifi.getIndex("userId").getWifHm().get(Selected) != null) {
				HashMap<String, WifiWrapper> hm = new HashMap<String, WifiWrapper>();
				for (WifiWrapper elem : wifi.getIndex("userId").getWifHm()
						.get(Selected)) {
					if ((elem.getTimeRegistered().compareTo(Date.from(FromDatePicker.getValue()
											.atStartOfDay()
											.atZone(ZoneId.systemDefault())
											.toInstant())) > 0)
							&& (elem.getTimeRegistered().compareTo(Date.from(ToDatePicker.getValue()
											.atStartOfDay()
											.atZone(ZoneId.systemDefault())
											.toInstant())) < 0)) {
						if (hm.get(elem.getBssid()) != null) {
							WifiWrapper nelem = new WifiWrapper(
									elem.getUserId(), elem.getSsid(),
									elem.getBssid(),
									((hm.get(elem.getBssid()).getLevel() + elem
											.getLevel()) / 2),
									elem.getFrequency(), elem.getLatitude(),
									elem.getLongitude(),
									elem.getTimeRegistered());
							hm.put(nelem.getBssid(), nelem);
						} else {
							hm.put(elem.getBssid(), elem);
						}
					}
				}
				for (String key : hm.keySet()) {
					WifiWrapper elem = hm.get(key);
					MarkerOptions markerOptions = new MarkerOptions();
					markerOptions
							.position(
									new LatLong(elem.getLatitude(), elem
											.getLongitude()))
							.visible(Boolean.TRUE)
							.title("Ssid:" + elem.getSsid() + "\nBssid:"
									+ elem.getBssid() + "\nFrequency:"
									+ elem.getFrequency() + "\nLevel:"
									+ elem.getLevel() + "\nCoords::"
									+ elem.getLatitude() + ", " + elem.getLongitude() + "\nDate:"
									+ elem.getTimeRegistered().toString())
							.icon(AP);
					Marker marker = new Marker(markerOptions);
					map.addMarker(marker);
				}
			}
		}
		// --Add user access points between the selected date--//

	}

	// ------------------------------------------------------------------------------------------------------------------------------------//

	// ------------------------------------------------------------------------------------------------------------------------------------//
	public void add_base_stations() {
		// --Add user base stations between the selected date--//
		if (Selected != null) {
			if (bs.getIndex("userId").getBsHm().get(Selected) != null) {
				for (BaseStationWrapper elem : bs.getIndex("userId").getBsHm()
						.get(Selected)) {
					if ((elem.getTimeRegistered()
							.compareTo(
									Date.from(FromDatePicker.getValue()
											.atStartOfDay()
											.atZone(ZoneId.systemDefault())
											.toInstant())) > 0)
							&& (elem.getTimeRegistered().compareTo(
									Date.from(ToDatePicker.getValue()
											.atStartOfDay()
											.atZone(ZoneId.systemDefault())
											.toInstant())) < 0)) {
						if (elem.getLatitude() != 666
								&& elem.getLongitude() != 666) {
							MarkerOptions markerOptions = new MarkerOptions();
							markerOptions
									.position(
											new LatLong(elem.getLatitude(),
													elem.getLongitude()))
									.visible(Boolean.TRUE)
									.title("Operator:" + elem.getOperator()
											+ "\nMcc:" + elem.getMcc()
											+ "\nMnc:" + elem.getMnc()
											+ "\nMnc:" + elem.getCid()
											+ "\nLac:" + elem.getLac())
									.icon(CELL);
							Marker marker = new Marker(markerOptions);
							map.addMarker(marker);
						}
					}
				}
			}
		}
		// --Add user base stations between the selected date--//

	}

	// ------------------------------------------------------------------------------------------------------------------------------------//

	public void add_path() {
		// --Add user path between the selected date--//
		ArrayList<LatLong> path = new ArrayList<LatLong>();
		int number = 1;
		
		if (Selected != null) {
			if (gps.getIndex("userId").getGpsHm().get(Selected) != null) {
				for (GpsWrapper elem : gps.getIndex("userId").getGpsHm()
						.get(Selected)) {
					if ((elem.getDate()
							.compareTo(
									Date.from(FromDatePicker.getValue()
											.atStartOfDay()
											.atZone(ZoneId.systemDefault())
											.toInstant())) > 0)
							&& (elem.getDate().compareTo(
									Date.from(ToDatePicker.getValue()
											.atStartOfDay()
											.atZone(ZoneId.systemDefault())
											.toInstant())) < 0)) {
						path.add(new LatLong(elem.getLatitude(), elem
								.getLongitude()));
					}
					
					//Adding best APs on map for battery saving
					List<WifiWrapper> list = wifi.getIndex("userId").getWifHm().get(Selected);
					double Latitude = 0, Longitude = 0;
					String ssid = null, bssid = null;
					int level = -1000;
					Date date = null;

					for (WifiWrapper wifi : list)
						if (Math.abs(elem.getDate().getTime() - wifi.getTimeRegistered().getTime()) <= 5 * 1000) {
							if (wifi.getBssid() != bssid) {
								if ((bssid == null && Math.abs(DataAnalyzer.calculateDifference(wifi.getLatitude(), wifi.getLongitude(), elem.getLatitude(), elem.getLongitude())) < 40.0)
										|| (bssid != null && !bssid.equals(wifi.getBssid()) && level < wifi.getLevel() && Math.abs(DataAnalyzer.calculateDifference(wifi.getLatitude(), wifi.getLongitude(), elem.getLatitude(), elem.getLongitude())) < 40.0)) {
									bssid = wifi.getBssid();
									ssid = wifi.getSsid();
									level = wifi.getLevel();
									Latitude = wifi.getLatitude();
									Longitude = wifi.getLongitude();
									date = wifi.getTimeRegistered();
								}
							}
						}
					if (bssid != null) {
						MarkerOptions markerOptions = new MarkerOptions();
						markerOptions
							.position(new LatLong(Latitude, Longitude))
							.visible(Boolean.TRUE)
							.title(String.valueOf(number++) + " " + ssid + "\nDate: " + date.toString())
							.icon(CAP);
						Marker marker = new Marker(markerOptions);
						map.addMarker(marker);
					}
				}
				
				LatLong[] path2 = new LatLong[path.size()];
				path2 = path.toArray(path2);
				Polyline line;
				PolylineOptions line_opt;
				line_opt = new PolylineOptions();
				line_opt.path(new MVCArray(path2)).clickable(false)
						.draggable(false).editable(false)
						.strokeColor("#0000FF").strokeWeight(4).visible(true);
				line = new Polyline(line_opt);
				map.addMapShape(line);
			}
		}
		// --Add user path between the selected date--//
	}

	
	//Adding stay points on map
	public void add_stay_points() {
		if (Selected != null) {
			List<GpsWrapper> gpsUserList;

			if ((gpsUserList = gps.getIndex("userId").getGpsHm().get(Selected)) != null) {
				DataAnalyzer analyzer = new DataAnalyzer(UserStayOptions.Tmin,
						UserStayOptions.Tmax, UserStayOptions.Dmax);
				List<StayPoint> staypoints = analyzer
						.AnalyzeStayPoints(gpsUserList);

				for (StayPoint sp : staypoints) {
					MarkerOptions markerOptions = new MarkerOptions();

					LatLong point = new LatLong(sp.getLatitude(),
							sp.getLongitude());
					markerOptions
							.position(point)
							.visible(Boolean.TRUE)
							.title("(" + sp.getLatitude() + ","
									+ sp.getLongitude() + ")")
							.icon(SP);
					Marker marker = new Marker(markerOptions);
					map.addMarker(marker);
					analyzer = null;
					staypoints = null;
				}
			}
		}
	}

	// ------------------------------------------------------------------------------------------------------------------------------------//

	// ------------------------------------------------------------------------------------------------------------------------------------//
	public void add_option_menu() {
		// --Initialize the calendars--//
		FromDatePicker = new DatePicker();
		ToDatePicker = new DatePicker();
		FromDatePicker.setValue(LocalDate.now());
		final Callback<DatePicker, DateCell> dayCellFactory = new Callback<DatePicker, DateCell>() {
			@Override
			public DateCell call(final DatePicker datePicker) {
				return new DateCell() {
					@Override
					public void updateItem(LocalDate item, boolean empty) {
						super.updateItem(item, empty);
						if (item.isBefore(FromDatePicker.getValue().plusDays(1))) {
							setDisable(true);
							setStyle("-fx-background-color: #ffc0cb;");
						}
						long p = ChronoUnit.DAYS.between(
								FromDatePicker.getValue(), item);
						setTooltip(new Tooltip("Searching results for " + p
								+ " days"));
					}
				};
			}
		};
		ToDatePicker.setDayCellFactory(dayCellFactory);
		ToDatePicker.setValue(FromDatePicker.getValue().plusDays(1));
		// --Initialize the calendars--//

		// --Option menu construction--//
		GridPane gridPane = new GridPane();
		gridPane.setHgap(20);
		gridPane.setVgap(20);
		Label checkInlabel = new Label("Starting date:");
		gridPane.add(checkInlabel, 0, 0);
		GridPane.setHalignment(checkInlabel, HPos.LEFT);
		gridPane.add(FromDatePicker, 0, 1);

		Label checkOutlabel = new Label("Ending date:");
		gridPane.add(checkOutlabel, 1, 0);
		GridPane.setHalignment(checkOutlabel, HPos.LEFT);
		gridPane.add(ToDatePicker, 1, 1);

		ObservableList<String> names = FXCollections.observableArrayList();
		HashSet<String> hs = new HashSet<String>();
		for (String elem : wifi.getIndex("userId").getWifHm().keySet())
			hs.add(elem);
		for (String elem : gps.getIndex("userId").getGpsHm().keySet())
			hs.add(elem);
		for (String elem : bs.getIndex("userId").getBsHm().keySet())
			hs.add(elem);
		names.setAll(hs);
		names.sort(new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				o1 = o1.replace("user", "");
				o2 = o2.replace("user", "");

				int a = Integer.valueOf(o1), b = Integer.valueOf(o2);
				
				return ((Integer)a).compareTo((Integer)b);
			}			
		});
		
		listView = new ComboBox<String>(names);
		Label label3 = new Label("Select a user:");
		gridPane.add(label3, 2, 0);
		GridPane.setHalignment(label3, HPos.LEFT);
		gridPane.add(listView, 2, 1);

		Button btn = new Button();
		btn.setText("Generate the User Data");
		btn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Selected = listView.getSelectionModel().getSelectedItem();
				mapInitialized();
				add_battery_chart();
			}
		});

		cbAP = new CheckBox("Access Points");
		cbC = new CheckBox("Cells");
		cbGPS = new CheckBox("Gps Route Tracking");
		cbSP = new CheckBox("Stay Points");
		cbSP.selectedProperty().addListener(new ChangeListener<Boolean>() {
			public void changed(ObservableValue<? extends Boolean> ov,
					Boolean old_val, Boolean new_val) {
				if (old_val == false && new_val == true) {
					Dialog<String> dialog = new Dialog<>();

					GridPane grid = new GridPane();
					grid.setHgap(10);
					grid.setVgap(10);
					grid.setPadding(new Insets(20, 150, 10, 10));

					Text title1 = new Text("Tmin");
					title1.setFill(Color.DARKRED);
					TextField hours1 = new TextField();
					hours1.setPromptText("0");
					TextField minutes1 = new TextField();
					minutes1.setPromptText("0");
					TextField seconds1 = new TextField();
					seconds1.setPromptText("0");
					Text title2 = new Text("Tmax");
					title2.setFill(Color.DARKRED);
					TextField hours2 = new TextField();
					hours2.setPromptText("0");
					TextField minutes2 = new TextField();
					minutes2.setPromptText("0");
					TextField seconds2 = new TextField();
					seconds2.setPromptText("0");
					Text title3 = new Text("Dmax");
					title3.setFill(Color.DARKRED);
					TextField distance = new TextField();
					distance.setPromptText("0.0");

					grid.add(title1, 3, 0);
					grid.add(new Label("Hours"), 0, 1);
					grid.add(hours1, 1, 1);
					grid.add(new Label("Minutes"), 2, 1);
					grid.add(minutes1, 3, 1);
					grid.add(new Label("Seconds"), 4, 1);
					grid.add(seconds1, 5, 1);
					grid.add(title2, 3, 3);
					grid.add(new Label("Hours"), 0, 4);
					grid.add(hours2, 1, 4);
					grid.add(new Label("Minutes"), 2, 4);
					grid.add(minutes2, 3, 4);
					grid.add(new Label("Seconds"), 4, 4);
					grid.add(seconds2, 5, 4);
					grid.add(title3, 3, 6);
					grid.add(new Label("Distance"), 2, 7);
					grid.add(distance, 3, 7);

					ButtonType confirmButton = new ButtonType("OK",
							ButtonData.OK_DONE);
					dialog.getDialogPane().getButtonTypes().add(confirmButton);

					dialog.getDialogPane().setContent(grid);
					dialog.setResultConverter(dialogButton -> {
						if (dialogButton == confirmButton) {
							return new String("OK");
						}
						return null;
					});

					Optional<String> result = dialog.showAndWait();
					if (!result.isPresent())
						cbSP.setSelected(false);
					
					result.ifPresent(OkButton -> {
						try {
							long Tmin = 0, Tmax = 0;
							double Dmax = 0;

							if (hours1.getText().startsWith("-")
									|| hours2.getText().startsWith("-")
									|| minutes1.getText().startsWith("-")
									|| minutes2.getText().startsWith("-")
									|| seconds1.getText().startsWith("-")
									|| seconds2.getText().startsWith("-")
									|| distance.getText().startsWith("-"))
								throw new NumberFormatException();

							if (hours1.getText().length() > 0)
								Tmin += Long.parseLong(hours1.getText()) * 3600000;
							if (minutes1.getText().length() > 0)
								Tmin += Long.parseLong(minutes1.getText()) * 60000;
							if (seconds1.getText().length() > 0)
								Tmin += Long.parseLong(seconds1.getText()) * 1000;
							if (hours2.getText().length() > 0)
								Tmax += Long.parseLong(hours2.getText()) * 3600000;
							if (minutes2.getText().length() > 0)
								Tmax += Long.parseLong(minutes2.getText()) * 60000;
							if (seconds2.getText().length() > 0)
								Tmax += Long.parseLong(seconds2.getText()) * 1000;
							if (distance.getText().length() > 0)
								Dmax += Double.parseDouble(distance.getText());

							UserStayOptions = new OptionsWrapper(Tmin, Tmax,
									Dmax);
						} catch (NumberFormatException e) {
							Alert alert = new Alert(AlertType.ERROR);
							alert.setTitle("Error Dialog");
							alert.setContentText("Invalid Input.");
							alert.showAndWait();
							cbSP.setSelected(false);
							UserStayOptions = null;
						}
					});
				} else
					UserStayOptions = null;
			}
		});
		PoI = new Button();
		PoI.setText("Points Of Intrest");
		PoI.setStyle("-fx-base: salmon;");
		PoI.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				Dialog<String> dialog = new Dialog<>();

				GridPane grid = new GridPane();
				grid.setHgap(10);
				grid.setVgap(10);
				grid.setPadding(new Insets(20, 150, 10, 10));

				Text title1 = new Text("Tmin");
				title1.setFill(Color.DARKRED);
				TextField hours1 = new TextField();
				hours1.setPromptText("0");
				TextField minutes1 = new TextField();
				minutes1.setPromptText("0");
				TextField seconds1 = new TextField();
				seconds1.setPromptText("0");
				Text title2 = new Text("Tmax");
				title2.setFill(Color.DARKRED);
				TextField hours2 = new TextField();
				hours2.setPromptText("0");
				TextField minutes2 = new TextField();
				minutes2.setPromptText("0");
				TextField seconds2 = new TextField();
				seconds2.setPromptText("0");
				Text title3 = new Text("Dmax");
				title3.setFill(Color.DARKRED);
				TextField distance = new TextField();
				distance.setPromptText("0.0");

				grid.add(title1, 3, 0);
				grid.add(new Label("Hours"), 0, 1);
				grid.add(hours1, 1, 1);
				grid.add(new Label("Minutes"), 2, 1);
				grid.add(minutes1, 3, 1);
				grid.add(new Label("Seconds"), 4, 1);
				grid.add(seconds1, 5, 1);
				grid.add(title2, 3, 3);
				grid.add(new Label("Hours"), 0, 4);
				grid.add(hours2, 1, 4);
				grid.add(new Label("Minutes"), 2, 4);
				grid.add(minutes2, 3, 4);
				grid.add(new Label("Seconds"), 4, 4);
				grid.add(seconds2, 5, 4);
				grid.add(title3, 3, 6);
				grid.add(new Label("Distance"), 2, 7);
				grid.add(distance, 3, 7);

				ButtonType confirmButton = new ButtonType("OK",
						ButtonData.OK_DONE);
				dialog.getDialogPane().getButtonTypes().add(confirmButton);

				dialog.getDialogPane().setContent(grid);
				dialog.setResultConverter(dialogButton -> {
					if (dialogButton == confirmButton) {
						return new String("OK");
					}
					return null;
				});

				Optional<String> result = dialog.showAndWait();

				result.ifPresent(OkButton -> {
					try {
						long Tmin = 0, Tmax = 0;
						double Dmax = 0;

						if (hours1.getText().startsWith("-")
								|| hours2.getText().startsWith("-")
								|| minutes1.getText().startsWith("-")
								|| minutes2.getText().startsWith("-")
								|| seconds1.getText().startsWith("-")
								|| seconds2.getText().startsWith("-")
								|| distance.getText().startsWith("-"))
							throw new NumberFormatException();

						if (hours1.getText().length() > 0)
							Tmin += Long.parseLong(hours1.getText()) * 3600000;
						if (minutes1.getText().length() > 0)
							Tmin += Long.parseLong(minutes1.getText()) * 60000;
						if (seconds1.getText().length() > 0)
							Tmin += Long.parseLong(seconds1.getText()) * 1000;
						if (hours2.getText().length() > 0)
							Tmax += Long.parseLong(hours2.getText()) * 3600000;
						if (minutes2.getText().length() > 0)
							Tmax += Long.parseLong(minutes2.getText()) * 60000;
						if (seconds2.getText().length() > 0)
							Tmax += Long.parseLong(seconds2.getText()) * 1000;
						if (distance.getText().length() > 0)
							Dmax += Double.parseDouble(distance.getText());

						StayOptions = new OptionsWrapper(Tmin, Tmax, Dmax);
						
						Dialog<String> dialog2 = new Dialog<>();

						GridPane grid2 = new GridPane();
						grid2.setHgap(10);
						grid2.setVgap(10);
						grid2.setPadding(new Insets(20, 150, 10, 10));

						TextField epsilon = new TextField();
						epsilon.setPromptText("0.0");
						TextField minPts = new TextField();
						minPts.setPromptText("0");

						grid2.add(new Label("Epsilon"), 0, 0);
						grid2.add(epsilon, 1, 0);
						grid2.add(new Label("MinPts"), 0, 2);
						grid2.add(minPts, 1, 2);

						ButtonType confirmButton2 = new ButtonType("OK", ButtonData.OK_DONE);
						dialog2.getDialogPane().getButtonTypes().add(confirmButton2);

						dialog2.getDialogPane().setContent(grid2);
						dialog2.setResultConverter(dialogButton2 -> {
							if (dialogButton2 == confirmButton2) {
								return new String("OK");
							}
							return null;
						});

						Optional<String> result2 = dialog2.showAndWait();

						result2.ifPresent(OkButton2 -> {
							if (epsilon.getText().startsWith("-") || minPts.getText().startsWith("-")) {
								throw new NumberFormatException();
							}

							StayOptions.epsilon = 0;
							StayOptions.minPts = 0;
							
							if (epsilon.getText().length() > 0)
								StayOptions.epsilon += Double.parseDouble(epsilon.getText());
							if (minPts.getText().length() > 0)
								StayOptions.minPts += Integer.parseInt(minPts.getText());
							
							clicked = true;
							Selected = listView.getSelectionModel().getSelectedItem();
							mapInitialized();
							add_battery_chart();
						});
					} catch (NumberFormatException ex) {
						Alert alert = new Alert(AlertType.ERROR);
						alert.setTitle("Error Dialog");
						alert.setContentText("Invalid Input.");
						alert.showAndWait();
						clicked = false;
						StayOptions = null;
					}
				});
			}
		});
		SplitPane sp = new SplitPane();
		sp.getItems().addAll(cbAP, cbC, cbGPS, cbSP, PoI);
		Label label4 = new Label("Choose the data you want to see:");
		gridPane.add(label4, 3, 0);
		GridPane.setHalignment(label4, HPos.LEFT);
		gridPane.add(sp, 3, 1);

		Label label5 = new Label("Generate them:");
		gridPane.add(label5, 13, 0);
		GridPane.setHalignment(label5, HPos.LEFT);
		gridPane.add(btn, 13, 1);

		gridPane.setStyle("-fx-background-color: #088A85;-fx-border-color: black;");
		// --Option menu construction--//

		// --Add it to the main layout--//
		vbox.getChildren().add(gridPane);
		// --Add it to the main layout--//
	}

	// ------------------------------------------------------------------------------------------------------------------------------------//
	public void add_points_of_intrest() {
		if (clicked) {
			DataAnalyzer analyzer = new DataAnalyzer(StayOptions.Tmin,
					StayOptions.Tmax, StayOptions.Dmax);
			List<StayPoint> sp = new ArrayList<StayPoint>();

			for (Map.Entry<String, ArrayList<GpsWrapper>> entry : gps.getIndex("userId").getGpsHm().entrySet()) {
				List<StayPoint> userSP = analyzer.AnalyzeStayPoints(entry.getValue());
				sp.addAll(userSP);
			}

			
			DBSCANClusterer<StayPoint> clusterer = new DBSCANClusterer<StayPoint>(StayOptions.epsilon, StayOptions.minPts);
			List<Cluster<StayPoint>> clusteredPoints = clusterer.cluster(sp);
			
			for (Cluster<StayPoint> iter : clusteredPoints) {
				List<StayPoint> points = iter.getPoints();
				double minLat, minLon, maxLat, maxLon;
				
				minLat = maxLat = points.get(0).getLatitude();
				minLon = maxLon = points.get(0).getLongitude();
				for (StayPoint iter2 : points) {
					if (iter2.getLatitude() < minLat)
						minLat = iter2.getLatitude();
					if (iter2.getLatitude() > maxLat)
						maxLat = iter2.getLatitude();
					if (iter2.getLongitude() < minLon)
						minLon = iter2.getLongitude();
					if (iter2.getLongitude() > maxLon)
						maxLon = iter2.getLongitude();
				}
				List<LatLong> path = new ArrayList<LatLong>();
				path.add(new LatLong(minLat, minLon));
				path.add(new LatLong(maxLat, minLon));
				path.add(new LatLong(maxLat, maxLon));
				path.add(new LatLong(minLat, maxLon));
				path.add(new LatLong(minLat, minLon));
				LatLong[] path2 = new LatLong[path.size()];
				path2 = path.toArray(path2);	
				Polyline line;
				PolylineOptions line_opt;
				line_opt = new PolylineOptions();
				line_opt.path(new MVCArray(path2)).clickable(false)
						.draggable(false).editable(false)
						.strokeColor("#FF0000").strokeWeight(4).visible(true);
				line = new Polyline(line_opt);
				map.addMapShape(line);			
			}
		}

		StayOptions = null;
		clicked = false;
	}
	
	//Chart for battery statistics
	public void pop_battery_statisticsPerDay(final Stage primaryStage) {
		final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(primaryStage);
        VBox dialogVbox = new VBox(20);
        dialogVbox.setTranslateX(0);
        dialogVbox.setTranslateY(0);
        dialogVbox.setTranslateZ(0);
		
		CategoryAxis xAxis = new CategoryAxis();
		NumberAxis yAxis = new NumberAxis();
    	BarChart<String, Number> bc2 = new BarChart<String, Number>(xAxis, yAxis);
    	DatePicker calendar = new DatePicker(); 
    	calendar.valueProperty().addListener(new ChangeListener<LocalDate>() {
			@Override
			public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
				int year1 = newValue.getYear(), month1 = newValue.getMonthValue(), day1 = newValue.getDayOfMonth();
				
				bc2.getData().clear();
				XYChart.Series<String, Number> series1 = new XYChart.Series<String, Number>();
				
				int[] perHour = new int[24];
				for (int i = 0; i < 24; i++) {
					Map<String, ArrayList<BatteryWrapper>> index = bat.getIndex("userId").getBatHm();
					
					for (Map.Entry<String, ArrayList<BatteryWrapper>> entry : index.entrySet()) {
						List<BatteryWrapper> list = entry.getValue();
						for (BatteryWrapper bat : list)
							if (bat.getBatteryLevel() < 15.0) {
								Calendar c = Calendar.getInstance();
								c.setTime(bat.getDate());

								int year2 = c.get(Calendar.YEAR), month2 = c.get(Calendar.MONTH) + 1, day2 = c.get(Calendar.DAY_OF_MONTH), time = c.get(Calendar.HOUR_OF_DAY);

								if (year1 == year2 && month1 == month2 && day1 == day2 && time == i) {
									perHour[c.get(Calendar.HOUR_OF_DAY)]++;
									break;
								}
							}
					}
				}
				for (int j = 0; j < perHour.length; j++) {
					XYChart.Data<String, Number> data = new XYChart.Data<String, Number>(j + ":00", perHour[j]);
					series1.getData().add(data);
				}
				
				bc2.getData().add(series1);
			}
    	});
    	
    	GridPane gridpanemain2 = new GridPane();
    	gridpanemain2.setStyle("-fx-background-color: #0B0B3B;-fx-border-color: black;");
    	gridpanemain2.add(bc2, 1, 0);
    	gridpanemain2.add(calendar, 2, 0);
    	
        dialogVbox.getChildren().add(gridpanemain2);

        Scene dialogScene = new Scene(dialogVbox, 620, 400);
        dialog.setScene(dialogScene);
        dialog.show();
	}

	//Chart for companies statistics
	public void pop_companies_statistics(final Stage primaryStage) {
		final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(primaryStage);
        VBox dialogVbox = new VBox(20);
        dialogVbox.setTranslateX(0);
        dialogVbox.setTranslateY(0);
        dialogVbox.setTranslateZ(0);
        dialogVbox.setStyle("-fx-background-color: #0B0B3B;-fx-border-color: black;");
		
		CategoryAxis xAxis = new CategoryAxis();
		NumberAxis yAxis = new NumberAxis();
    	BarChart<String, Number> bc2 = new BarChart<String, Number>(xAxis, yAxis);

    	Map<String,Integer> companyUsers = new HashMap<String,Integer>();
    	Map<String, ArrayList<BaseStationWrapper>> index = bs.getIndex("userId").getBsHm();
		List<String> owner = new ArrayList<String>();
    	
    	for (Map.Entry<String, ArrayList<BaseStationWrapper>> entry : index.entrySet()) {
    		List<BaseStationWrapper> list = entry.getValue();
    		
    		for (BaseStationWrapper company : list) {
    			String companyName = company.getOperator();
    			
    			companyName = companyName.toLowerCase();
    			if (companyName.contains("vodaf") || companyName.contains("dafone") || companyName.contains("cu"))
    				companyName = "VODAFONE GR";
    			else if (companyName.contains("cosm") || companyName.contains("mote") || companyName.contains("coms"))
    				companyName = "COSMOTE GR";
    			else if (companyName.contains("win") || companyName.contains("ind"))
    				companyName = "WIND GR";

    			companyName = companyName.toUpperCase();
    			if (!owner.contains(companyName))
    				owner.add(companyName);
    		}

    		for (String company : owner)
    			if (companyUsers.containsKey(company))
    				companyUsers.put(company, companyUsers.get(company) + 1);
    			else
    				companyUsers.put(company, new Integer(1));
    		
    		owner.clear();
    	}
    	
		XYChart.Series<String, Number> series1 = new XYChart.Series<String, Number>();

    	for (Map.Entry<String, Integer> entry : companyUsers.entrySet()) {
    		XYChart.Data<String, Number> data = new XYChart.Data<String, Number>(entry.getKey(), entry.getValue());
			series1.getData().add(data);    		
    	}

		bc2.getData().add(series1);
		dialogVbox.getChildren().add(bc2);
        Scene dialogScene = new Scene(dialogVbox, 620, 400);
        dialog.setScene(dialogScene);
        dialog.show();
	}
	
	public void pop_battery_statistics(final Stage primaryStage) {
		final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(primaryStage);
        VBox dialogVbox = new VBox(20);
        dialogVbox.setTranslateX(0);
        dialogVbox.setTranslateY(0);
        dialogVbox.setTranslateZ(0);
        dialogVbox.setStyle("-fx-background-color: #0B0B3B;-fx-border-color: black;");
		
		CategoryAxis xAxis = new CategoryAxis();
		NumberAxis yAxis = new NumberAxis();
    	BarChart<String, Number> bc2 = new BarChart<String, Number>(xAxis, yAxis);
    	int[] perHour = new int[24];
    	
    	bc2.getData().clear();
		XYChart.Series<String, Number> series1 = new XYChart.Series<String, Number>();
		
    	for (int i = 0; i < 24; i++) {
			Map<String, ArrayList<BatteryWrapper>> index = bat.getIndex("userId").getBatHm();
			
			for (Map.Entry<String, ArrayList<BatteryWrapper>> entry : index.entrySet()) {
				List<BatteryWrapper> list = entry.getValue();
				for (BatteryWrapper bat : list) {
					Calendar c = Calendar.getInstance();
					c.setTime(bat.getDate());
					
					if (bat.getBatteryLevel() < 15.0 && c.get(Calendar.HOUR_OF_DAY) == i) {
						perHour[i]++;
						break;
					}
				}
			}
		}
    	
    	for (int j = 0; j < perHour.length; j++) {
			XYChart.Data<String, Number> data = new XYChart.Data<String, Number>(j + ":00", perHour[j]);
			series1.getData().add(data);
		}
		
		bc2.getData().add(series1);	
		dialogVbox.getChildren().add(bc2);
        Scene dialogScene = new Scene(dialogVbox, 620, 400);
        dialog.setScene(dialogScene);
        dialog.show();
	}
	
	private class OptionsWrapper {
		public long Tmin;
		public long Tmax;
		public double Dmax;
		public double epsilon;
		public int minPts;

		OptionsWrapper(long Tmin, long Tmax, double Dmax) {
			this.Tmin = Tmin;
			this.Tmax = Tmax;
			this.Dmax = Dmax;
			this.epsilon = -1;
			this.minPts = -1;
		}
	}
}
