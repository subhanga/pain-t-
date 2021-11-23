
package paint;

import java.io.File;
import java.io.FileInputStream;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 *
 * @author sg
 */
public class Img {

    /**
     * This method is responsible for launching the 'Create New Image' dialog,
     * and when the image dimensions are confirmed, it opens the new image in
     * a new tab.
     */
    public static void launchCreateNewImage() {
        Stage createnewimgStage = new Stage();
        createnewimgStage.initOwner(Paint.window);
        GridPane gp = new GridPane();
        Text heightlbl = new Text("Height: ");
        TextField heightfld = new TextField();
        heightfld.setText("500");

        Text widthlbl = new Text("Width: ");
        TextField widthfld = new TextField();
        widthfld.setText("500");

        Button createbtn = new Button();
        createbtn.setText("Create");
        createbtn.setOnAction((ActionEvent event) -> {
            WritableImage wi = new WritableImage(
                Integer.parseInt(widthfld.getText()),
                Integer.parseInt(heightfld.getText())
            );
            ImageView iv = new ImageView(wi);

            String rn = "Untitled";
            Paint.addTab(rn, wi);
            createnewimgStage.close();
        });

        Button cancelbtn = new Button();
        cancelbtn.setText("Cancel");
        cancelbtn.setOnAction((ActionEvent event) -> {
            createnewimgStage.close();
        });


        gp.add(heightlbl, 0, 0);
        gp.add(heightfld, 1, 0);

        gp.add(widthlbl, 0, 1);
        gp.add(widthfld, 1, 1);

        gp.add(cancelbtn, 0, 2);
        gp.add(createbtn, 1, 2);

        Scene createnewimgScene = new Scene(gp, 300, 300);
        createnewimgStage.setScene(createnewimgScene);
        createnewimgStage.setTitle("Create New Image");
        createnewimgStage.show();
    }

    /**
     * This method is responsible for creating, and showing the about window
     * for the program, pretty straight forward.
     */
    public static void launchAboutWindow() {
        //Here is the dialog box related to the about
        //page. I am going to add in some basic info here
        //about the project.
        Stage aboutStage = new Stage();
        aboutStage.initOwner(Paint.window);
        GridPane gp = new GridPane();
        Text nametxt = new Text(Paint.PROGRAM_NAME );
                                
        Text copytxt = new Text("SG CS250");
        Text licetxt = new Text("Cs250");

        Button closebtn = new Button();
        closebtn.setText("Ok");
        closebtn.setOnAction((ActionEvent aboutevent) -> {
            aboutStage.close();
        });

        Hyperlink rel_notes = new Hyperlink();
        rel_notes.setWrapText(true);

        gp.add(nametxt, 0, 0);
        gp.add(copytxt, 0, 1);
        gp.add(licetxt, 0, 2);
        try {
            //System.out.println(CustomFileHandler.openFile(Paint.window).toString());
            gp.add(new ImageView(new Image(new FileInputStream(new File("gplv3.png")))), 1, 2);
        } catch (Exception e) {
            System.out.println("Popup.java; Failed to add ImageView:" + e);
        }
        gp.add(new Text("Release Notes:"), 0, 3);
        gp.add(rel_notes, 1, 3);

        gp.add(closebtn, 0, 5);

        Scene aboutScene = new Scene(gp, 300, 200);
        aboutStage.setScene(aboutScene);
        aboutStage.setTitle("About");
        aboutStage.show();
    }

    public static void saveWarning() {
        Stage saveWarnStage = new Stage();
        saveWarnStage.initOwner(Paint.window);
        VBox vb = new VBox();
        Text warning = new Text("Warning, You saved an image different data.");
        Button okbtn = new Button("Ok");
        okbtn.setOnAction(e -> {
            saveWarnStage.close();
        });
        vb.getChildren().addAll(warning, okbtn);
        Scene savewarn = new Scene(vb, 700, 100);
        saveWarnStage.setScene(savewarn);
        saveWarnStage.show();
    }

    public static void closeConfirmation() {
        //maybe make this a boolean method?
        Stage closeConfirmStage = new Stage();
        closeConfirmStage.initOwner(Paint.window);
        VBox vb = new VBox();
        Text warning = new Text("Warning, You could lose unsaved moidifcations! Save?");
        Button yesbtn = new Button("Yes");
        yesbtn.setOnAction(e -> {
            FileHandel.saveFile();
            closeConfirmStage.close();
        });
        Button nobtn = new Button("No");
        nobtn.setOnAction(e -> {
            closeConfirmStage.close();
        });

        vb.getChildren().addAll(warning, yesbtn, nobtn);

        Scene closeConfirmScene = new Scene(vb, 400, 100);
        closeConfirmStage.setScene(closeConfirmScene);
        closeConfirmStage.show();
    }
    /**
     *
     * Make a Popup window with whatever the provided Image is; meant to help in
     * debugging.
     *
     * @param i The image to show.
     */
    public static void showImage(Image i) {
        Stage showImage = new Stage();
        showImage.initOwner(Paint.window);
        ImageView iv = new ImageView(i);
        VBox vb = new VBox();
        vb.getChildren().addAll(iv);
        Scene showImageScene = new Scene(vb, i.getWidth(), i.getHeight());
        showImage.setScene(showImageScene);
        showImage.show();
    }

}