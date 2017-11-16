package seng202.group6.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Optional;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.TextFieldTreeCell;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.converter.DefaultStringConverter;
import seng202.group6.DataContainer;
import seng202.group6.model.AirlineList;
import seng202.group6.model.AirportList;
import seng202.group6.model.FlightList;
import seng202.group6.model.RouteList;
import seng202.group6.persistence.FileFormatException;
import seng202.group6.persistence.PersistenceManager;
import seng202.group6.utils.RecordType;

/**
 * Controls the main gui window Has functions used by buttons, combo boxes and all other objects in
 * the main GUI window.
 */
public class MainWindowController implements Observer {
    // Included Elements and their Controllers:
    @FXML
    @SuppressWarnings("unused")
    private VBox includedFilterView;
    @FXML
    @SuppressWarnings("unused")
    private BasicFilterController includedFilterViewController;

    @FXML
    @SuppressWarnings("unused")
    private VBox includedRawDataView;
    @FXML
    @SuppressWarnings("unused")
    private RawDataViewerController includedRawDataViewController;
    @FXML
    @SuppressWarnings("unused")
    private AnchorPane anchorRDV;

    @FXML
    @SuppressWarnings("unused")
    private AnchorPane includedMapView;
    @FXML
    @SuppressWarnings("unused")
    private MapController includedMapViewController;

    @FXML
    @SuppressWarnings("unused")
    private AnchorPane includedGraphsView;
    @FXML
    @SuppressWarnings("unused")
    private GraphsController includedGraphsViewController;

    // Main tab pane
    @FXML
    @SuppressWarnings("unused")
    private TabPane tabPaneMain;
    @FXML
    @SuppressWarnings("unused")
    private Tab tabMap;
    @FXML
    @SuppressWarnings("unused")
    private Tab tabRawData;
    @FXML
    @SuppressWarnings("unused")
    private Tab tabGraphs;

    // List selector
    @FXML
    @SuppressWarnings("unused")
    private TreeView<String> listTreeView;
    private TreeItem<String> airlineTree;
    private TreeItem<String> airportTree;
    private TreeItem<String> routeTree;
    private TreeItem<String> flightTree;

    // For list selection tree view cells
    private final MenuItem newAirlineList = new MenuItem("New Airline List");
    private final MenuItem newAirportList = new MenuItem("New Airport List");
    private final MenuItem newFlightList = new MenuItem("New Flight List");
    private final MenuItem newRouteList = new MenuItem("New Route List");

    /**
     * The data container whose contents are displayed by this controller's view.
     */
    private DataContainer dataContainer;
    private PersistenceManager persistenceManager;
    /**
     * The currently opened store file.
     */
    private File lastFile = null;


    /**
     * Checks whether the given tab is the currently active tab.
     *
     * @param tab The queried tab.
     * @return true if the tab is the currently selected tab, false otherwise.
     */
    private boolean isMainTab(Tab tab) {
        return tabPaneMain.getSelectionModel().getSelectedItem().equals(tab);
    }


    /**
     * Checks if the map tab is currently selected. Used by external classes checking.
     *
     * @return true if the map tab is selected, false otherwise.
     */
    boolean isMapTabSelected() {
        return isMainTab(tabMap);
    }

    boolean isGraphTabSelected() {
        return isMainTab(tabGraphs);
    }


    /**
     * Initialization method.
     */
    @FXML
    @SuppressWarnings("unused")
    private void initialize() {
        AnchorPane.setTopAnchor(includedRawDataView, 0.0);
        AnchorPane.setLeftAnchor(includedRawDataView, 0.0);
        AnchorPane.setRightAnchor(includedRawDataView, 0.0);
        AnchorPane.setBottomAnchor(includedRawDataView, 0.0);

        AnchorPane.setTopAnchor(includedGraphsView, 0.0);
        AnchorPane.setLeftAnchor(includedGraphsView, 0.0);
        AnchorPane.setRightAnchor(includedGraphsView, 0.0);
        AnchorPane.setBottomAnchor(includedGraphsView, 0.0);

        AnchorPane.setTopAnchor(includedMapView, 0.0);
        AnchorPane.setLeftAnchor(includedMapView, 0.0);
        AnchorPane.setRightAnchor(includedMapView, 0.0);
        AnchorPane.setBottomAnchor(includedMapView, 0.0);

        tabPaneMain.getSelectionModel().selectedItemProperty().addListener((observable, oldTab, newTab) -> {
            if (newTab.equals(tabMap)) {
                includedMapViewController.reloadPage();
            } else if (newTab.equals(tabGraphs)) {
                includedGraphsViewController.redrawAllGraphs();
            }
        });

        newAirlineList.setOnAction(event -> createRecordList(RecordType.AIRLINE));
        newAirportList.setOnAction(event -> createRecordList(RecordType.AIRPORT));
        newFlightList.setOnAction(event -> createRecordList(RecordType.FLIGHT));
        newRouteList.setOnAction(event -> createRecordList(RecordType.ROUTE));
    }


    /**
     * Sets up the controller with the data container used by the program and persistence manager
     * that is used for saving and loading data. <p> Initializes elements dependent on data
     * container and cascades data container assignment to included controllers.
     *
     * @param dataContainer      the data container being used by the program
     * @param persistenceManager the persistence manager of the program
     */
    public void setUp(DataContainer dataContainer, PersistenceManager persistenceManager) {
        String mapURL = MainWindowController.class.getResource("/map.html").toExternalForm();

        this.dataContainer = dataContainer;
        dataContainer.addObserver(this);

        this.persistenceManager = persistenceManager;

        initialiseTreeView();

        includedFilterViewController.setUp(dataContainer);
        includedRawDataViewController.setUp(dataContainer);
        includedMapViewController.setUp(this, dataContainer, mapURL);
        includedGraphsViewController.setUp(dataContainer, this);
    }


    /**
     * Exits the program.
     */
    public void closeProgram() {
        if (askToSave()) {
            // askToSave returned true, indicating approved (or ignored)
            System.exit(0);
        }
    }


    @Override
    public void update(Observable o, Object arg) {
        if (o == dataContainer && arg instanceof DataContainer.Notification) {
            switch ((DataContainer.Notification) arg) {
                case LISTS_CHANGED:
                    updateTreeViewLists();
                    break;
            }
        }
    }


    /**
     * Checks whether any changes have been made to the data and if so, asks the user if they wish
     * to save them.
     *
     * @return false if the user cancelled, otherwise return true as the user saved, declined to
     * save, or no changes were made.
     */
    private boolean askToSave() {
        if (!dataContainer.getModified()) {
            // If the data container has not been modified don't need to bother asking
            return true;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Save Changes?");
        ((Stage) alert.getDialogPane().getScene().getWindow()).getIcons()
                .add(new Image(getClass().getClassLoader().getResourceAsStream("icon.png")));
        alert.setHeaderText(null);
        alert.setContentText("Do you want to save your changes?");

        ButtonType buttonTypeSave = new ButtonType("Save", ButtonBar.ButtonData.YES);
        ButtonType buttonTypeProceed = new ButtonType("Don't Save", ButtonBar.ButtonData.NO);
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeSave, buttonTypeProceed, buttonTypeCancel);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent()) {
            if (result.get() == buttonTypeSave) {
                savePersistent();
                return true;
            } else if (result.get() == buttonTypeProceed) {
                return true;
            }
        }

        return false;
    }


    /**
     * Prepares the file chooser dialogue for persistent storage.
     *
     * @return the dialogue
     */
    private FileChooser buildFileChooser() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("SkySurf store file", "*.sky"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("All files", "*"));

        if (lastFile != null && lastFile.getParentFile() != null && lastFile.getParentFile().exists()) {
            fileChooser.setInitialDirectory(lastFile.getParentFile());
        }

        return fileChooser;
    }


    /**
     * Starts dialogue to load persistent dat file.
     */
    @FXML
    @SuppressWarnings("unused")
    private void loadPersistent() {
        if (!askToSave()) {
            // ask to save returned false - indicating 'Cancel' was chosen, so close the operation
            return;
        }
        Stage chooserStage = new Stage();
        FileChooser fileChooser = buildFileChooser();
        fileChooser.setTitle("Open File");
        chooserStage.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream("icon.png")));

        File inFile = fileChooser.showOpenDialog(chooserStage);
        if (inFile != null) {
            try {
                persistenceManager.loadFromFile(inFile);
                lastFile = inFile;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (FileFormatException e) {
                e.printStackTrace();

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Could not load file!");
                ((Stage) alert.getDialogPane().getScene().getWindow()).getIcons()
                        .add(new Image(getClass().getClassLoader().getResourceAsStream("icon.png")));
                alert.setHeaderText("Unable to load File!\nPlease check file type and format.");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
        }
    }


    /**
     * Saves data to a file that can be re-loaded later.
     */
    @FXML
    @SuppressWarnings("unused")
    private void saveAsPersistent() {
        Stage chooserStage = new Stage();
        FileChooser fileChooser = buildFileChooser();
        fileChooser.setInitialFileName("data_store.sky");
        fileChooser.setTitle("Save File");
        File outFile = fileChooser.showSaveDialog(chooserStage);
        if (outFile != null) {
            try {
                persistenceManager.saveToFile(outFile);
                lastFile = outFile;
            } catch (IOException e) {
                e.printStackTrace();

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Unable to save!");
                ((Stage) alert.getDialogPane().getScene().getWindow()).getIcons()
                        .add(new Image(getClass().getClassLoader().getResourceAsStream("icon.png")));
                alert.setHeaderText(null);
                alert.setContentText("Unable to save to file.");
                alert.showAndWait();
            }
        }
    }


    /**
     * Saves the data to the last loaded/saved file. If the last file does not exist or is
     * inaccessible, calls save as.
     */
    @FXML
    @SuppressWarnings("unused")
    private void savePersistent() {
        if (lastFile == null) {
            saveAsPersistent();
        } else {
            try {
                if (lastFile.exists() && lastFile.canWrite()) {
                    persistenceManager.saveToFile(lastFile);
                } else {
                    lastFile = null;
                    saveAsPersistent();
                }
            } catch (IOException e) {
                e.printStackTrace();

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Unable to save!");
                ((Stage) alert.getDialogPane().getScene().getWindow()).getIcons()
                        .add(new Image(getClass().getClassLoader().getResourceAsStream("icon.png")));
                alert.setHeaderText("Unable to save to file.");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
        }
    }


    /**
     * Opens the distance calculation window.
     */
    @FXML
    @SuppressWarnings("unused")
    private void openDistanceCalculator() {
        try {
            FXMLLoader distLoader = new FXMLLoader(getClass().getClassLoader().getResource("Distance.fxml"));
            Parent distRoot = distLoader.load();
            DistanceController distanceController = distLoader.getController();
            distanceController.setUp(dataContainer.getActiveAirportList());

            Stage distStage = new Stage();
            distStage.initModality(Modality.APPLICATION_MODAL);
            distStage.setResizable(false);
            distStage.setTitle("Distance Calculator");
            distStage.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream("icon.png")));
            distStage.setScene(new Scene(distRoot, 600, 400));
            distStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Opens the importer window.
     */
    @FXML
    @SuppressWarnings("unused")
    private void openImporter() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Importer.fxml"));
            Parent root = loader.load();
            ImporterController importer = loader.getController();
            importer.setUp(dataContainer);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.setTitle("Import File");
            stage.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream("icon.png")));
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Opens the settings window.
     */
    @FXML
    @SuppressWarnings("unused")
    private void openSettings() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Settings.fxml"));
            Parent root = loader.load();
            SettingsController settings = loader.getController();
            settings.setUp(this);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.setTitle("Settings");
            stage.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream("icon.png")));
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Deletes a list.
     *
     * @param type  To identify which type of list is being deleted.
     * @param index Index of the list to be removed.
     */
    private void deleteList(TreeItem type, int index) {
        if (type == airlineTree) {
            dataContainer.deleteAirlineList(index);
        } else if (type == airportTree) {
            dataContainer.deleteAirportList(index);
        } else if (type == routeTree) {
            dataContainer.deleteRouteList(index);
        } else if (type == flightTree) {
            dataContainer.deleteFlightList(index);
        }
    }


    /**
     * Refreshes the list display to show the lists currently held in the data container.
     */
    private void updateTreeViewLists() {
        airlineTree.getChildren().clear();
        for (AirlineList airlineList : dataContainer.getAirlineLists()) {
            airlineTree.getChildren().add(new TreeItem<>(airlineList.getName()));
        }
        airportTree.getChildren().clear();
        for (AirportList airportList : dataContainer.getAirportLists()) {
            airportTree.getChildren().add(new TreeItem<>(airportList.getName()));
        }
        flightTree.getChildren().clear();
        for (FlightList flightList : dataContainer.getFlightLists()) {
            flightTree.getChildren().add(new TreeItem<>(flightList.getName()));
        }
        routeTree.getChildren().clear();
        for (RouteList routeList : dataContainer.getRouteLists()) {
            routeTree.getChildren().add(new TreeItem<>(routeList.getName()));
        }
    }


    /**
     * Sets up the list views.
     */
    @SuppressWarnings("unchecked")
    private void initialiseTreeView() {
        TreeItem<String> root = new TreeItem<>("Lists");
        airlineTree = new TreeItem<>(RecordType.AIRLINE + " Lists");
        airportTree = new TreeItem<>(RecordType.AIRPORT + " Lists");
        routeTree = new TreeItem<>(RecordType.ROUTE + " Lists");
        flightTree = new TreeItem<>(RecordType.FLIGHT + " Lists");

        airlineTree.setExpanded(true);
        airportTree.setExpanded(true);
        routeTree.setExpanded(true);
        flightTree.setExpanded(true);
        root.getChildren().addAll(airportTree, airlineTree, flightTree, routeTree);
        listTreeView.setEditable(false);
        listTreeView.setRoot(root);
        listTreeView.setShowRoot(false);

        listTreeView.setCellFactory(a -> new ListTreeCell());
    }


    /**
     * Handles mouse click event on tree view. Used to select active list.
     *
     * @param mouseEvent Mouse click event.
     */
    public void treeClicked(MouseEvent mouseEvent) {
        if (mouseEvent.getButton() == MouseButton.PRIMARY) {
            TreeItem<String> selected = listTreeView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                List children = selected.getParent().getChildren();
                if (children.size() != 0) {
                    int index = children.indexOf(selected);
                    if (selected.getParent().equals(airlineTree) &&
                            !dataContainer.isActiveAirlineList(index)) {
                        dataContainer.setActiveAirlineList(index);
                    } else if (selected.getParent().equals(airportTree) &&
                            !dataContainer.isActiveAirportList(index)) {
                        dataContainer.setActiveAirportList(index);
                    } else if (selected.getParent().equals(routeTree) &&
                            !dataContainer.isActiveRouteList(index)) {
                        dataContainer.setActiveRouteList(index);
                    } else if (selected.getParent().equals(flightTree) &&
                            !dataContainer.isActiveFlightList(index)) {
                        dataContainer.setActiveFlightList(index);
                    }
                }
            }
        }
    }


    /**
     * Wrapper for {@code createRecordList} called from GUI
     */
    @FXML
    @SuppressWarnings("unused")
    private void createRecordList() {
        createRecordList(null);
    }


    /**
     * Opens the create list window with the given type preselected.
     *
     * @param type the type of list to preselect, can be null
     */
    private void createRecordList(RecordType type) {
        try {
            FXMLLoader createListLoader = new FXMLLoader(getClass().getClassLoader().getResource("CreateList.fxml"));
            Parent root = createListLoader.load();
            CreateListController createListController = createListLoader.getController();

            createListController.setUp(dataContainer, type);

            Stage createListStage = new Stage();
            createListStage.initModality(Modality.APPLICATION_MODAL);
            createListStage.setResizable(false);
            createListStage.setTitle("Create New List");
            createListStage.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream("icon.png")));
            createListStage.setScene(new Scene(root));
            createListStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Sets the upper limit for the number of items the map will show
     *
     * @param mapLimit the max number of items that the map will show
     */
    void setMapLimit(Integer mapLimit) {
        includedMapViewController.setMaxItems(mapLimit);
    }


    /**
     * Gets the maximum number of items the map will display.
     *
     * @return the maximum number of items the map will display
     */
    Integer getMapLimit() {
        return includedMapViewController.getMaxItems();
    }


    /**
     * Gets the maximum number of items the graphs will display.
     *
     * @return the maximum number of items the map will display
     */
    Integer getGraphLimit() {
        return includedGraphsViewController.getMaxItems();
    }


    /**
     * Sets the upper limit for the number of items the graph will show
     *
     * @param graphLimit the max number of items that the graph will show
     */
    void setGraphLimit(Integer graphLimit) {
        includedGraphsViewController.setMaxItems(graphLimit);
    }


    /**
     * Custom tree cell to specify renaming behaviour and context menu for list selection tree
     * view.
     */
    private final class ListTreeCell extends TextFieldTreeCell<String> {
        private final MenuItem rename;
        private final MenuItem remove;

        public ListTreeCell() {
            super(new DefaultStringConverter());
            rename = new MenuItem("Rename");
            rename.setOnAction(arg0 -> {
                listTreeView.setEditable(true);
                startEdit();
            });

            remove = new MenuItem("Delete");
            remove.setOnAction(event -> {
                int index = getTreeItem().getParent().getChildren().indexOf(getTreeItem());
                deleteList(getTreeItem().getParent(), index);
            });
        }

        @Override
        public void commitEdit(String value) {
            super.commitEdit(value);
            TreeItem<String> treeItem = getTreeItem();
            int index = treeItem.getParent().getChildren().indexOf(treeItem);
            try {
                if (treeItem.getParent().equals(airlineTree)) {
                    dataContainer.getAirlineLists().get(index).setName(value);
                } else if (treeItem.getParent().equals(airportTree)) {
                    dataContainer.getAirportLists().get(index).setName(value);
                } else if (treeItem.getParent().equals(routeTree)) {
                    dataContainer.getRouteLists().get(index).setName(value);
                } else if (treeItem.getParent().equals(flightTree)) {
                    dataContainer.getFlightLists().get(index).setName(value);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            listTreeView.setEditable(false);
        }

        @Override
        public void cancelEdit() {
            super.cancelEdit();
            setText(getItem());
            setGraphic(getTreeItem().getGraphic());
            listTreeView.setEditable(false);
        }

        @Override
        public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            TreeItem tree = getTreeItem();

            if (!isEditing() && tree != null) {
                ContextMenu newMenu = new ContextMenu();
                TreeItem parent = tree.getParent();

                if (parent == airlineTree || parent == airportTree ||
                        parent == flightTree || parent == routeTree) {
                    newMenu.getItems().add(rename);
                    newMenu.getItems().add(remove);
                }

                if (tree == airlineTree || parent == airlineTree) {
                    newMenu.getItems().add(newAirlineList);

                } else if (tree == airportTree || parent == airportTree) {
                    newMenu.getItems().add(newAirportList);

                } else if (tree == flightTree || parent == flightTree) {
                    newMenu.getItems().add(newFlightList);

                } else if (tree == routeTree || parent == routeTree) {
                    newMenu.getItems().add(newRouteList);
                }

                setContextMenu(newMenu);
            }
            if (empty || item == null) {
                setText(null);
                setGraphic(null);
            }
        }
    }
}
