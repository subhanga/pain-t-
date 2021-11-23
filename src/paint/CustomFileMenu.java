/*
* ===================================================
*
contents
newimage, open, save, saveas, clear, quit
* ===================================================
* 00- NewImage
* 01- Open
* 02- Save
* 03-Save as
* 04- Clear 
* 05-qit
*
* ----------------------------------------------------
*
Features
* ----------------------------------------------------
* - open image anytype
* - save as
* - Open Image && save Image
*
* ____________________________________________________
*
* ===================================================
*/

package paint;

import java.io.File;
import java.io.FileNotFoundException;
import javafx.event.ActionEvent;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Menu;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

/**
 *
 * @author sg
 */
public class CustomFileMenu extends Menu {
    //This file will modify
    public CustomFileMenu() {
        super();
        this.setText("File");

        MenuItem saveas = new MenuItem("Save As");
        saveas.setOnAction((ActionEvent event) -> {
            FileHandel.saveAsFile(Paint.window);
        });
        saveas.setAccelerator(new KeyCodeCombination(KeyCode.S,
                              KeyCombination.SHIFT_DOWN,
                              KeyCombination.CONTROL_DOWN)
                             );

        MenuItem save = new MenuItem("Save");
        save.setOnAction((ActionEvent event) -> {
            FileHandel.saveFile();
        });
        save.setAccelerator(new KeyCodeCombination(KeyCode.S,
                            KeyCombination.CONTROL_DOWN)
                           );


        MenuItem open = new MenuItem("Open");
        open.setOnAction((ActionEvent event) -> {
            try {
                File f = FileHandel.openFile(Paint.window);
                Paint.addTab(f);
            } catch (FileNotFoundException ex) {
                System.out.println("CustomFileMenu; File was not found:" + ex);
            }
        });
        open.setAccelerator(new KeyCodeCombination(KeyCode.O,
                            KeyCombination.CONTROL_DOWN)
                           );

        MenuItem quit = new MenuItem("Quit");
        quit.setOnAction((ActionEvent event) -> {
            Paint.close();
        });
        quit.setAccelerator(new KeyCodeCombination(KeyCode.Q,
                            KeyCombination.SHIFT_DOWN,
                            KeyCombination.CONTROL_DOWN)
                           );

        MenuItem clear = new MenuItem("Clear");
        clear.setOnAction((ActionEvent event) -> {
            Paint.getCurrentTab().clearImage();
        });

        MenuItem newimage = new MenuItem("New");
        newimage.setOnAction((ActionEvent event) -> {
            //Show the create new Image dialog
            Img.launchCreateNewImage();
        });
        newimage.setAccelerator(new KeyCodeCombination(KeyCode.N,
                                KeyCombination.CONTROL_DOWN)
                               );

        //add all of the menu items to the file menu
        this.getItems().addAll(newimage, open, save, saveas, clear, quit);
    }
}