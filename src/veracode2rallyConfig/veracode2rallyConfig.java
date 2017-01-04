package veracode2rallyConfig;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

public class veracode2rallyConfig extends Application {

	public enum buttontype {
		NEW, EDIT
	};

	private static Stage primaryStage = new Stage();

	public TableView<Mapping> tableView = new TableView<>();

	public Tab veracodeCredentialsTab = new Tab("Veracode Credentials");
	public Tab rallyCredentialsTab = new Tab("Rally Credentials");
	public Tab customFieldsTab = new Tab("Rally Custom Fields");

	public ObservableList<Mapping> mappings = FXCollections.observableArrayList();
	public ObservableList<Veracode> veracodecreds = FXCollections.observableArrayList();
	public ObservableList<Rally> rallycreds = FXCollections.observableArrayList();
	public ObservableList<Customfield> customfields = FXCollections.observableArrayList();

	public TextField txtVeracodeAppName = new TextField();
	public TextField txtVeracodeAppId = new TextField();
	public TextField txtRallyProjectName = new TextField();
	public TextField txtRallyProjectId = new TextField();

	public TextField txtVeracodeUserName = new TextField();
	public TextField txtVeracodePassword = new PasswordField();
	public TextField txtVeracodeApiId = new TextField();
	public TextField txtVeracodeApiKey = new PasswordField();
	public CheckBox  chxVeracodeEncrypt = new CheckBox();
	
	
	public TextField txtRallyUserName = new TextField();
	public TextField txtRallyPassword = new PasswordField();
	public TextField txtRallyApiKeyDescription = new TextField();
	public TextField txtRallyApiKey = new PasswordField();
	public CheckBox  chxRallyEncrypt = new CheckBox();

	public TextField txtMitigationAction = new TextField();
	public TextField txtMitigationComment = new TextField();
	public TextField txtMitigationHistory = new TextField();
	public TextField txtUniqueId = new TextField();

	public Button editButton = new Button("Edit...");
	public Button deleteButton = new Button("Delete...");

	public ObservableList<String> options = FXCollections.observableArrayList("Unmitigated flaws affecting policy",
			"Flaws affecting policy", "All unmitigated flaws", "All flaws");
	public ComboBox<String> cbxImport = new ComboBox<String>(options);

	public ObservableList<String> veracodeLogin = FXCollections.observableArrayList("Username/Password", "API ID/Key");
	public ComboBox<String> cbxVeracodeLogin = new ComboBox<String>(veracodeLogin);

	public ObservableList<String> rallyLogin = FXCollections.observableArrayList("Username/Password", "API Key");
	public ComboBox<String> cbxRallyLogin = new ComboBox<String>(rallyLogin);

	private Stage dialogStage = new Stage();

	private static File CurrentFile = null;

	public MenuItem menuSave = new MenuItem("Save");

	public static void main(String[] args) {
		launch(args);
	}

	public void start(Stage primaryStage) {
		try {
			
			veracode2rallyConfig.primaryStage = primaryStage;

			dialogStage.setTitle("Edit Record");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);

			VBox topBox = new VBox(createMenus());

			BorderPane borderPane = new BorderPane();
			borderPane.setCenter(createTabs(primaryStage));
			borderPane.setTop(topBox);
			Scene scene = new Scene(borderPane, 800, 400);

			primaryStage.setScene(scene);
			primaryStage.setTitle("Veracode2Rally Configuration - New");
			primaryStage.show();

			validateButtons();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private MenuBar createMenus() {

		MenuItem menuNew = new MenuItem("New");
		menuNew.setAccelerator(KeyCombination.keyCombination("Ctrl+N"));
		menuNew.setOnAction(e -> menuNewClicked());

		// MenuItem that will open XML files
		MenuItem menuOpen = new MenuItem("Open");
		menuOpen.setOnAction(e -> {
			try {
				menuOpenClicked();
			} catch (IOException e3) {
				e3.printStackTrace();
			}
		});

		// MenuItem that will save XML files
		menuSave.setOnAction(e -> {
			try {
				menuSaveClicked();
			} catch (IOException e2) {
				e2.printStackTrace();
			}
		});

		MenuItem menuSaveAs = new MenuItem("Save As...");
		menuSaveAs.setOnAction(e -> {
			try {
				menuSaveAsClicked();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});

		MenuItem menuExit = new MenuItem("Exit");
		menuExit.setOnAction(e -> menuExitClicked());

		Menu menuFile = new Menu("File");
		menuFile.getItems().addAll(menuNew, menuOpen, menuSave, menuSaveAs, menuExit);
		MenuBar menuBar = new MenuBar();
		menuBar.getMenus().addAll(menuFile);

		return menuBar;
	} 

	private TabPane createTabs(Window primaryStage) {

		HBox hbox = new HBox();
		hbox.setStyle("-fx-background-color: #c1c1c1;");
		hbox.setPrefSize(100, 85);

		// Create the ButtonBar instance
		ButtonBar buttonBar = new ButtonBar();

		// Create the buttons to go into the ButtonBar
		Button newButton = new Button("New...");
		newButton.setOnAction(e -> newButtonClicked());
		editButton.setOnAction(e -> editButtonClicked());
		deleteButton.setOnAction(e -> deleteButtonClicked());

		// Add buttons to the ButtonBar
		buttonBar.getButtons().addAll(newButton, editButton, deleteButton);

		hbox.getChildren().addAll(buttonBar);

		BorderPane insidePane = new BorderPane();
		insidePane.setCenter(createTable());
		insidePane.setBottom(hbox);

		Tab tableTab = new Tab("Project Mapping");
		tableTab.setContent(insidePane);
		tableTab.setClosable(false);
		TabPane tabPane = new TabPane();

		createVeracodeTab();
		createRallyTab();
		createCustomFieldsTab();

		tabPane.getTabs().addAll(tableTab, veracodeCredentialsTab, rallyCredentialsTab, customFieldsTab);

		return tabPane;

	} // private TabPane createTabs() {

	private Node createTable() {

		// veracodename column
		TableColumn<Mapping, String> veracodenameColumn = new TableColumn<>("Veracode App Name");
		veracodenameColumn.setMinWidth(150);
		veracodenameColumn.setCellValueFactory(new PropertyValueFactory<>("veracodename"));

		// veracodeid column
		TableColumn<Mapping, String> veracodeidColumn = new TableColumn<>("Veracode App ID");
		veracodeidColumn.setMinWidth(125);
		veracodeidColumn.setCellValueFactory(new PropertyValueFactory<>("veracodeid"));

		// rallyname column
		TableColumn<Mapping, String> rallynameColumn = new TableColumn<>("Rally Project Name");
		rallynameColumn.setMinWidth(125);
		rallynameColumn.setCellValueFactory(new PropertyValueFactory<>("rallyname"));

		// rallyid column
		TableColumn<Mapping, String> rallyidColumn = new TableColumn<>("Rally Project ID");
		rallyidColumn.setMinWidth(125);
		rallyidColumn.setCellValueFactory(new PropertyValueFactory<>("rallyid"));

		// importfilter column
		TableColumn<Mapping, String> importfilterColumn = new TableColumn<>("Import");
		importfilterColumn.setMinWidth(200);
		importfilterColumn.setCellValueFactory(new PropertyValueFactory<>("importfilter"));

		tableView = new TableView<>();
		tableView.setItems(mappings);
		
		tableView.getColumns().add(veracodenameColumn);
		tableView.getColumns().add(veracodeidColumn);
		tableView.getColumns().add(rallynameColumn);
		tableView.getColumns().add(rallyidColumn);
		tableView.getColumns().add(importfilterColumn);
		
		tableView.getSelectionModel().selectFirst();

		// Handle tableViewselection changes.
		tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			validateButtons();
		});

		
		tableView.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent click) {
				if (click.getClickCount() == 2 && tableView.getItems().size() != 0) {
					launchEditDialog(primaryStage, "edit");
				}
			}
		});
		
		return tableView;
	};

	
	public void launchEditDialog(Window primaryStage, String buttontype) {

		// layout components in a gridpane
		GridPane dialogPane = new GridPane();
		dialogPane.setHgap(10);
		dialogPane.setVgap(10);
		dialogPane.setPadding(new Insets(20, 150, 10, 10));
		dialogPane.setStyle("-fx-background-color: #c1c1c1;");

		dialogPane.add(new Label("Veracode App Name:"), 0, 0);
		dialogPane.add(txtVeracodeAppName, 1, 0);

		dialogPane.add(new Label("Veracode App ID:"), 0, 1);
		dialogPane.add(txtVeracodeAppId, 1, 1);

		dialogPane.add(new Label("Rally Project Name:"), 0, 2);
		dialogPane.add(txtRallyProjectName, 1, 2);

		dialogPane.add(new Label("Rally Project ID:"), 0, 3);
		dialogPane.add(txtRallyProjectId, 1, 3);

		dialogPane.add(new Label("Import:"), 0, 4);
		dialogPane.add(cbxImport, 1, 4);

		ButtonBar dialogButtonBar = new ButtonBar();
		Button okButton = new Button();
		okButton.setOnAction(e -> okButtonClicked(buttontype));

		okButton.setText("OK");
		okButton.setPrefWidth(60);

		switch (buttontype) {

		case "new":
			txtVeracodeAppName.setText("");
			txtVeracodeAppId.setText("");
			txtRallyProjectName.setText("");
			txtRallyProjectId.setText("");
			cbxImport.getSelectionModel().selectLast();
			break;

		case "edit":
			txtVeracodeAppName.setText(tableView.getSelectionModel().getSelectedItem().getVeracodename());
			txtVeracodeAppId.setText(tableView.getSelectionModel().getSelectedItem().getVeracodeid());
			txtRallyProjectName.setText(tableView.getSelectionModel().getSelectedItem().getRallyname());
			txtRallyProjectId.setText(tableView.getSelectionModel().getSelectedItem().getRallyid());
			cbxImport.getSelectionModel().select(tableView.getSelectionModel().getSelectedItem().getImportfilter());
		}

		// okButton will add record

		Button cancelButton = new Button();
		cancelButton.setOnAction(e -> cancelButtonClicked());
		cancelButton.setText("Cancel");
		cancelButton.setPrefWidth(60);

		// Add buttons to the ButtonBar
		dialogButtonBar.getButtons().addAll(okButton, cancelButton);

		dialogPane.add(dialogButtonBar, 1, 8);

		Scene scene = new Scene(dialogPane, 700, 400);
		dialogStage.setScene(scene);

		// Show the dialog and wait until the user closes it
		dialogStage.showAndWait();

	} // private void launchEditDialog(Window primaryStage) {

	// New button clicked
	public void newButtonClicked() {
		launchEditDialog(primaryStage, "new");
	}

	// Edit button clicked
	public void editButtonClicked() {
		launchEditDialog(primaryStage, "edit");
	}

	// Delete button clicked
	public void deleteButtonClicked() {

		ObservableList<Mapping> recordsSelected, RecordData;
		RecordData = tableView.getItems();
		recordsSelected = tableView.getSelectionModel().getSelectedItems();
		recordsSelected.forEach(RecordData::remove);

	}

	// OK button clicked
	public void okButtonClicked(String buttontype) {

		switch (buttontype) {

		case "new":

			mappings.add(new Mapping(txtVeracodeAppName.getText(), txtVeracodeAppId.getText(),
					txtRallyProjectName.getText(), txtRallyProjectId.getText(),
					cbxImport.getSelectionModel().selectedItemProperty().getValue().toString()));

			tableView.getSelectionModel().selectLast();
			break;

		case "edit":

			mappings.get(tableView.getSelectionModel().getSelectedIndex())
					.setVeracodename(txtVeracodeAppName.getText());

			mappings.get(tableView.getSelectionModel().getSelectedIndex()).setVeracodeid(txtVeracodeAppId.getText());

			mappings.get(tableView.getSelectionModel().getSelectedIndex()).setRallyname(txtRallyProjectName.getText());

			mappings.get(tableView.getSelectionModel().getSelectedIndex()).setRallyid(txtRallyProjectId.getText());

			mappings.get(tableView.getSelectionModel().getSelectedIndex())
					.setImportfilter(cbxImport.getSelectionModel().getSelectedItem());

			tableView.refresh();
			break;
		}

		dialogStage.close();
		}

	// Cancel button clicked
	public void cancelButtonClicked() {

		dialogStage.close();
	}

	// New menu clicked
	public void menuNewClicked() {
		mappings.clear();
		validateButtons();
		CurrentFile = null;
		veracode2rallyConfig.primaryStage.setTitle("Veracode2Rally Configuration - New");

	}

	// Exit menu clicked
	public void menuExitClicked() {

		primaryStage.close();

	}

	// Open menu clicked
	public void menuOpenClicked() throws IOException {

		FileChooser fileChooser = createFileChooser();
		File file = fileChooser.showOpenDialog(primaryStage);

		if (file != null) {
			loadFromFile(file);
		}
	}

	// Save menu clicked
	public void menuSaveClicked() throws IOException {

		if (CurrentFile == null) {
			
			FileChooser fileChooser = createFileChooser();
			fileChooser.showSaveDialog(primaryStage);
		}

		else {
			saveToFile(CurrentFile);
		}
	}

	public void menuSaveAsClicked() throws IOException {

		
		FileChooser fileChooser = createFileChooser();
		File file = fileChooser.showSaveDialog(primaryStage);

		if (file != null) {
			saveToFile(file);
		}
	}

	public void validateButtons() {

		if (tableView.getItems().size() == 0) {
			editButton.setDisable(true);
			deleteButton.setDisable(true);
		}

		else {
			editButton.setDisable(false);
			deleteButton.setDisable(false);
		}
	}


	public void saveToFile(File file) {
		try {

			JAXBContext contextObj = JAXBContext.newInstance(Project.class);
			Marshaller marshallerObj = contextObj.createMarshaller();
			marshallerObj.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

			ArrayList<Veracode> veracodecreds = new ArrayList<Veracode>();
			if (chxVeracodeEncrypt.isSelected()) {
					Veracode veracoderecord = new Veracode(txtVeracodeUserName.getText(), Encrypt(txtVeracodePassword.getText()),
					txtVeracodeApiId.getText(), Encrypt(txtVeracodeApiKey.getText()),
					cbxVeracodeLogin.getSelectionModel().getSelectedItem(), chxVeracodeEncrypt.isSelected());
					veracodecreds.add(veracoderecord);
			}
			else  {
				
					Veracode veracoderecord = new Veracode(txtVeracodeUserName.getText(), txtVeracodePassword.getText(),
					txtVeracodeApiId.getText(), txtVeracodeApiKey.getText(),
					cbxVeracodeLogin.getSelectionModel().getSelectedItem(), chxVeracodeEncrypt.isSelected());	
					veracodecreds.add(veracoderecord);	
			}
			
			
			ArrayList<Rally> rallycreds = new ArrayList<Rally>();	
			if (chxRallyEncrypt.isSelected()) {
					Rally rallyrecord = new Rally(txtRallyUserName.getText(), Encrypt(txtRallyPassword.getText()), Encrypt(txtRallyApiKey.getText()),
					cbxRallyLogin.getSelectionModel().getSelectedItem(), chxRallyEncrypt.isSelected());
					rallycreds.add(rallyrecord);
			}
			else  {
					
					Rally rallyrecord = new Rally(txtRallyUserName.getText(), txtRallyPassword.getText(), txtRallyApiKey.getText(),
					cbxRallyLogin.getSelectionModel().getSelectedItem(), chxRallyEncrypt.isSelected());
					rallycreds.add(rallyrecord);
	
				}
	
			
			Customfield custfield = new Customfield(txtMitigationAction.getText(), txtMitigationComment.getText(),
			txtMitigationHistory.getText(), txtUniqueId.getText());

			ArrayList<Customfield> customfields = new ArrayList<Customfield>();
			customfields.add(custfield);

			Project que = new Project(mappings, rallycreds, veracodecreds, customfields);
			marshallerObj.marshal(que, file);
			veracode2rallyConfig.primaryStage.setTitle("Veracode2Rally Configuration - " + file.getAbsolutePath());
			CurrentFile = file;
			validateButtons();

		} catch (Exception e) { // catches ANY exception
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Could not save data");
			alert.setContentText("Could not save data to file:\n" + file.getPath());

			alert.showAndWait();

		}

	}

	public void loadFromFile(File file) {

		try {
			
			JAXBContext context = JAXBContext.newInstance(Project.class);
			Unmarshaller um = context.createUnmarshaller();

			// Reading XML from the file and unmarshalling.
			Project wrapper = (Project) um.unmarshal(file);
			
			mappings.clear();
			mappings.addAll(wrapper.getMappings());
			
			veracode2rallyConfig.primaryStage.setTitle("Veracode2Rally Configuration - " + file.getAbsolutePath());
			veracodecreds.clear();
			veracodecreds.addAll(wrapper.getVeracode());
	
			txtVeracodeUserName.setText(veracodecreds.get(0).getUsername());
			txtVeracodeApiId.setText(veracodecreds.get(0).getApi_id());
			cbxVeracodeLogin.getSelectionModel().select(veracodecreds.get(0).getLogin_credential());
			chxVeracodeEncrypt.setSelected(veracodecreds.get(0).getEncyrpt());
			chxVeracodeEncrypt.setSelected(veracodecreds.get(0).getEncyrpt());
			
			
			
			if (veracodecreds.get(0).getEncyrpt()) {
				txtVeracodePassword.setText(Decrypt(veracodecreds.get(0).getPassword()));
				txtVeracodeApiKey.setText(Decrypt(veracodecreds.get(0).getApi_key()));
			}
			else {
				txtVeracodePassword.setText(veracodecreds.get(0).getPassword());
				txtVeracodeApiKey.setText(veracodecreds.get(0).getApi_key());	
			}
			

			rallycreds.clear();
			rallycreds.addAll(wrapper.getRally());
			txtRallyUserName.setText(rallycreds.get(0).getUsername());
			cbxRallyLogin.getSelectionModel().select(rallycreds.get(0).getLogin_credential());
			chxRallyEncrypt.setSelected(rallycreds.get(0).getEncyrpt());

			if (chxRallyEncrypt.isSelected()) {
				txtRallyPassword.setText(Decrypt(rallycreds.get(0).getPassword()));
				txtRallyApiKey.setText(Decrypt(rallycreds.get(0).getApi_key()));
				}
				else {
					txtRallyPassword.setText(rallycreds.get(0).getPassword());
					txtRallyApiKey.setText(rallycreds.get(0).getApi_key());	
				}
							
			customfields.clear();
			customfields.addAll(wrapper.getCustomfield());
			txtMitigationAction.setText(customfields.get(0).getMitigation_action());
			txtMitigationComment.setText(customfields.get(0).getMitigation_comment());
			txtMitigationHistory.setText(customfields.get(0).getMitigation_history());
			txtUniqueId.setText(customfields.get(0).getUnique_id());

			CurrentFile = file;
			validateButtons();

		} catch (Exception e) { // catches ANY exception
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Could not load data");
			alert.setContentText("Could not load data from file:\n" + file.getPath());

			alert.showAndWait();
		}

	}

	private Tab createVeracodeTab() {

		veracodeCredentialsTab.setClosable(false);

		GridPane veracodePane = new GridPane();
		veracodePane.setHgap(10);
		veracodePane.setVgap(10);
		veracodePane.setPadding(new Insets(20, 150, 10, 10));
		veracodePane.setStyle("-fx-background-color: #c1c1c1;");

		veracodePane.add(new Label("Veracode Username:"), 0, 0);
		veracodePane.add(txtVeracodeUserName, 1, 0);

		veracodePane.add(new Label("Veracode Password:"), 0, 1);
		veracodePane.add(txtVeracodePassword, 1, 1);
	
		veracodePane.add(new Label("Veracode API ID:"), 0, 2);
		veracodePane.add(txtVeracodeApiId, 1, 2);
		
		veracodePane.add(new Label("Veracode API Key:"), 0, 3);
		veracodePane.add(txtVeracodeApiKey, 1, 3);

		veracodePane.add(new Label("Login with:"), 0, 4);
		veracodePane.add(cbxVeracodeLogin, 1, 4);

		veracodePane.add(new Label("Encrypt Password and Key:"), 0, 5);
		veracodePane.add(chxVeracodeEncrypt, 1, 5);
		
		
		cbxVeracodeLogin.getSelectionModel().selectFirst();
		veracodeCredentialsTab.setContent(veracodePane);

		return veracodeCredentialsTab;
	} 
	
	private Tab createRallyTab() {

		rallyCredentialsTab.setClosable(false);

		GridPane rallyPane = new GridPane();
		rallyPane.setHgap(10);
		rallyPane.setVgap(10);
		rallyPane.setPadding(new Insets(20, 150, 10, 10));
		rallyPane.setStyle("-fx-background-color: #c1c1c1;");

		rallyPane.add(new Label("Rally Username:"), 0, 0);
		rallyPane.add(txtRallyUserName, 1, 0);

		rallyPane.add(new Label("Rally Password:"), 0, 1);
		rallyPane.add(txtRallyPassword, 1, 1);

		rallyPane.add(new Label("Rally API Key:"), 0, 2);
		rallyPane.add(txtRallyApiKey, 1, 2);

		rallyPane.add(new Label("Login with:"), 0, 3);
		rallyPane.add(cbxRallyLogin, 1, 3);

		rallyPane.add(new Label("Encrypt Password and Key:"), 0, 4);
		rallyPane.add(chxRallyEncrypt, 1, 4);
		
		cbxRallyLogin.getSelectionModel().selectFirst();
		rallyCredentialsTab.setContent(rallyPane);

		return rallyCredentialsTab;
	} 
	
	private Tab createCustomFieldsTab() {

		customFieldsTab.setClosable(false);

		GridPane customFieldsPane = new GridPane();
		customFieldsPane.setHgap(10);
		customFieldsPane.setVgap(10);
		customFieldsPane.setPadding(new Insets(20, 150, 10, 10));
		customFieldsPane.setStyle("-fx-background-color: #c1c1c1;");

		customFieldsPane.add(new Label("Mitigation Action:"), 0, 0);
		customFieldsPane.add(txtMitigationAction, 1, 0);

		customFieldsPane.add(new Label("Mitigation Comment:"), 0, 1);
		customFieldsPane.add(txtMitigationComment, 1, 1);

		customFieldsPane.add(new Label("Mitigation History:"), 0, 2);
		customFieldsPane.add(txtMitigationHistory, 1, 2);

		customFieldsPane.add(new Label("Unique ID:"), 0, 3);
		customFieldsPane.add(txtUniqueId, 1, 3);

		customFieldsTab.setContent(customFieldsPane);

		return customFieldsTab;
	} 

	
	public String Encrypt(String text) {

	StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
		 encryptor.setPassword(";?kpL={vfAr=q$[%"); 	
		 String strEncryptedText = encryptor.encrypt(text);
         return strEncryptedText;
	}

	
	public String Decrypt(String text) {
		StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
		 encryptor.setPassword(";?kpL={vfAr=q$[%"); // could be got from web, env variable...		
		 String strDecryptedText = encryptor.decrypt(text);
		 return strDecryptedText;
			
	}
	
	
	  public static FileChooser createFileChooser() throws IOException {

		  FileChooser chooser = new FileChooser();
			// Set extension filter
			FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
			chooser.getExtensionFilters().add(extFilter);
			
	      // initialize chooser	with default path	
			String currentDir = new File(".").getCanonicalPath();
		
			if (new File(currentDir + "\\resources").exists()){
				chooser.setInitialDirectory(new File(currentDir + "\\resources")); 
			}		
			else{
				chooser.setInitialDirectory(new File(currentDir)); 	
			}
			
			chooser.setInitialFileName("veracode2rallyConfig.xml");
            return chooser ;
	   }
	
}
