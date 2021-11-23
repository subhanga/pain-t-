
package paint;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 *
 * @author sg
 */
public class Paint extends Application {

    //Constants
    final static String PROGRAM_NAME = "Nepali Paint";
    final static String RELEASE_STR = "Best in the world";
    final static int DEFAULT_MODE = 0;
    final static int EDIT_MODE = 1;
    /**
     * Represents the interval (in seconds) that Paint should try to perform an autosave
     */
    final static int AUTOSAVE_INTERVAL = 300;
    /**
     * Represents the interval (in seconds) that Paint should print a log message to the console
     */
    final static int LOGGER_INTERVAL = 10;
    final static Logger LOG = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    //pointers
    public static Stage window; // primaryStage
    public static BorderPane border;
    public static TabPane tab;
    public static CustomMenuBar menub;
    public static EditToolBar edittoolbar;
    public static Timeline autosave;
    public static Timeline logger;

    //Variables
    public static int mode = DEFAULT_MODE; //default to default...
    public static boolean AUTOSAVEON = false; //have autosave be off by default
    public static ColorPicker colorpick = new ColorPicker();
    public static double brushSize = 5;

    @Override
    public void start(Stage primaryStage) {
        Paint.window = primaryStage; //have window refer to primaryStage
        Paint.edittoolbar = new EditToolBar();
        Paint.edittoolbar.setVisible(false);
        Paint.menub = new CustomMenuBar();
        border = new BorderPane();
        //tab pane
        Paint.tab = new TabPane();
        Paint.tab.getTabs().add(
            new CustomTab("Tab 1")
        );

        //color picker
        Paint.colorpick.setValue(Color.BLACK);

        //root
        VBox root = new VBox(); //set up how the windows will laid out
        root.getChildren().addAll(menub);
        
        border.setTop(root);
        border.setRight(edittoolbar);
        border.setCenter(tab);

        //scene setup
        Scene scene = new Scene(border, 1000, 500); //create the scene
        //single key keyboard shortcuts for paint
        scene.setOnKeyPressed(e -> {
            if (Paint.getMode() == Paint.EDIT_MODE) {
                switch (e.getCode()) {
                case E:
                    Paint.edittoolbar.setTool(EditToolBar.ERASE);
                    break;
                case S:
                    Paint.edittoolbar.setTool(EditToolBar.SQUARE);
                    break;
                case P:
                    Paint.edittoolbar.setTool(EditToolBar.COLOR_GRAB);
                    break;
                
                case SPACE:
                    Paint.edittoolbar.setTool(EditToolBar.NONE);
                    break;
                case F:
                    Paint.edittoolbar.setTool(EditToolBar.LINE);
                    break;
                default:
                    break;
                }
            }
        });
        //themeing code down here??
        //scene.getRoot().setStyle("-fx-base:black");
        //scene.getStylesheets().add("dark-theme.css");
        //scene.getStylesheets().add(getClass().getResource("dark-theme-2.css").toString());

        //setup the main window
        primaryStage.setTitle(PROGRAM_NAME);
        primaryStage.setScene(scene);
        primaryStage.show();

        //start up the autosave timer
        Paint.autosave = new Timeline(
            new KeyFrame(Duration.seconds(Paint.AUTOSAVE_INTERVAL),
        ev -> {
            if (Paint.AUTOSAVEON) {
                if (Paint.getCurrentTab().opened_file != null) {
                    if (!Paint.getCurrentTab().imgHasBeenSaved) {
                        FileHandel.saveFile();
                    }
                }
            }
        }
                        )
        );
        // Make sure the 'thread' runs forever, and start it up
        Paint.autosave.setCycleCount(Animation.INDEFINITE);
        Paint.autosave.play();
        //edittoolbar.setVisible(true);

        //setup the logger
        Paint.logger = new Timeline(
            new KeyFrame(Duration.seconds(Paint.LOGGER_INTERVAL),
        ev -> {
            Paint.LOG.log(Level.INFO,
                          "Selected Tool: "
                          + Paint.edittoolbar.getDrawSelection()
                          + " | Saved: "
                          + Paint.getCurrentTab().imgHasBeenSaved
                          + " | Opened File: "
                          + Paint.getCurrentTab().opened_file
                          + " | AutoSave Enabled: "
                          + Paint.AUTOSAVEON
                         );
        })
        );

        Paint.logger.setCycleCount(Animation.INDEFINITE);
        Paint.logger.play();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Closes Paint nicely.
     */
    public static void close() {
        Paint.window.close(); //close the main window/stage
        Paint.autosave.stop();
        Paint.logger.stop();
        System.exit(0); //Have a successful exit code.
    }
    /**
     *
     * @return The "mode" of Paint
     */
    public static int getMode() {
        return Paint.mode;
    }
    /**
     * Sets Paint's mode, with idiot proofing built in!
     * @param i
     */
    public static void setMode(int i) {
        if (i == Paint.EDIT_MODE) {
            Paint.mode = i;
        } else { //if 0, or something else, return to default.
            Paint.mode = Paint.DEFAULT_MODE;
        }
    }

    /**
     * This method shows the EditToolBar, so that when Paint is in EDIT_MODE,
     * you can access all of the features of EDIT_MODE.
     */
    public static void showEditToolBar() {
        Paint.edittoolbar.setVisible(true);
    }

    /**
     * This method hides the EditToolBar, so that when Paint is not in EDIT_MODE
     * there is no confusion.
     */
    public static void hideEditToolBar() {
        Paint.edittoolbar.setVisible(false);
    }

    /**
     * This method is intended to be a general purpose update script, currently
     * it only handles showing and hiding the edit tool bar, but it could be expanded
     * to do anything else that warrants updating the window.
     */
    public static void update() {
        if (getMode() == Paint.EDIT_MODE) {
            showEditToolBar();
        } else {
            hideEditToolBar();
        }
    }

    /**
     *
     * Add a new CustomTab to Paint.tab (the TabPane).
     *
     * @param lbl A string of whatever you want the label to be
     * @param i The image you want to be shown on the tab
     */
    public static void addTab(String lbl, Image i) {

        CustomTab t = new CustomTab(lbl);
        Paint.tab.getTabs().add(
            t
        );
        Paint.tab.getSelectionModel().select(t);
        t.setImage(i);
    }

    /**
     * Add a new CustomTab to Paint.tab (the TabPane). The tab title will be
     * whatever the file name is.
     *
     * @param f A file that you want to have a tab for.
     * @throws FileNotFoundException
     */
    public static void addTab(File f) throws FileNotFoundException {
        CustomTab t = new CustomTab(f.getName());
        t.opened_file = f;
        Paint.tab.getTabs().add(
            t
        );
        Paint.tab.getSelectionModel().select(t);
        t.setImage(new Image(new FileInputStream(f)));
    }

    /**
     *
     * @return The current tab that is selected by Paint.
     */
    public static CustomTab getCurrentTab() {
        return (CustomTab) Paint.tab.getSelectionModel().getSelectedItem();
    }

}