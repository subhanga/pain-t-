/*
* ===================================================
*
contents
* ===================================================
* 00- FileMenu
* 01- Viewmenu
* 02- Modemenu
* 03-HelpMenu
* 04- Exit
* 
*
* ----------------------------------------------------
*
*
* ===================================================
*/

package paint;

import javafx.event.ActionEvent;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

/**
 *
 * @author sg
 */
public class CustomMenuBar extends MenuBar {

    public CustomMenuBar() {
        super();

        //File menu code
        Menu filemenu = new CustomFileMenu();


        //mode menu (move to separate file (needs more complicated logic))
        ModeMenu modemenu = new ModeMenu();
        modemenu.setText("Home");
        modemenu.setMode(1);

        //Help menu code
        Menu helpmenu = new Menu("Help");
          MenuItem log = new MenuItem("Mode");
          log.setOnAction((ActionEvent event) -> {
        });
          

        MenuItem about = new MenuItem("Info");
        about.setOnAction((ActionEvent event) -> {
            //Img.launchAboutWindow();
        });
        MenuItem autosaveToggle = new MenuItem("AutoSave Off");
        autosaveToggle.setOnAction((ActionEvent event) -> {
            if (Paint.AUTOSAVEON) {
                Paint.AUTOSAVEON = false;
                autosaveToggle.setText("AutoSave Off");
            } else {
                Paint.AUTOSAVEON = true;
                autosaveToggle.setText("AutoSave On");
            }
        });

        helpmenu.getItems().addAll(about, autosaveToggle,log);


        //View menu
        Menu viewmenu = new Menu("View");

        MenuItem zoomin = new MenuItem("Zoom In");
        zoomin.setOnAction((ActionEvent event) -> {
            Paint.getCurrentTab().imgcanvas.zoomIn();
        });
        zoomin.setAccelerator(new KeyCodeCombination(KeyCode.I,
                              KeyCombination.CONTROL_DOWN));


        MenuItem zoomout = new MenuItem("Zoom Out");
        zoomout.setOnAction((ActionEvent event) -> {
            Paint.getCurrentTab().imgcanvas.zoomOut();

        });
        zoomout.setAccelerator(new KeyCodeCombination(KeyCode.D,
                               KeyCombination.CONTROL_DOWN));

        MenuItem resetview = new MenuItem("Reset");
        resetview.setOnAction((ActionEvent event) -> {
            Paint.getCurrentTab().setImage(Paint.getCurrentTab().opened_image);
        });

        viewmenu.getItems().addAll( zoomout,zoomin);
        //Add all of the menus to the MenuBar
        this.getMenus().addAll(filemenu, viewmenu,modemenu, helpmenu);


    }

}