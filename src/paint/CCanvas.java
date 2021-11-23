/*
*
* ===================================================
*
contents
* ===================================================
* 00- Rectangle
* 01- Square
* 02- Line
* 03-Triangle
* 04- Polygon
* 05-ELLIPSE
* 06-Line
*07-Triangle
08-FreeGon
*
* ----------------------------------------------------
*
Features
* ----------------------------------------------------
* - Live Drawing
* - While Release
___________________________________________________
*
* ===================================================
*/
/*
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paint;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import java.util.Stack;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.util.Pair;

/**
 * This class lets the user use important functions that allows   drawing shapes and using
 * methods which are not defined in the inherited class.
 *
 * @author sg
 */
public class CCanvas extends ECanvas {

    private Point2D imc; //initial mouse coordinates
    private Stack<Image> undoStack = new Stack();
    private Stack<Image> redoStack = new Stack();

    private Stack<Pair> freeNgonStack = new Stack(); //make this Point2D
    private Image drag_drop_image = null;
    private Rectangle r; //potentially replace this with a 'live' canvas, that gets cleared out and drawn on?
    private Line l;
    private Polygon p;
    private Ellipse ell;
    private ECanvas livecanvas = new ECanvas();
    private Color targetColor;
    public CCanvas() {
        super();

        this.setHeight(800);
        this.setWidth(1000);
        this.setOnMousePressed(e -> {
            this.imc = new Point2D(e.getX(), e.getY());

            if (Paint.getMode() == Paint.EDIT_MODE) {
                switch (Paint.edittoolbar.getDrawSelection()) {
                case EditToolBar.COLOR_GRAB:
                    Paint.colorpick.setValue(this.getImage().getPixelReader().getColor(
                                                 roundDouble(e.getX()),
                                                 roundDouble(e.getY())
                                             ));
                    break;
                case EditToolBar.RECTANGLE:
                case EditToolBar.SQUARE:
                    this.r = new Rectangle(imc.getX(), imc.getY(), 0, 0);
                    this.r.setFill(Paint.colorpick.getValue());
                    Paint.getCurrentTab().pane.getChildren().add(this.r);
                    break;
               
                case EditToolBar.LINE:
                    this.l = new Line(this.imc.getX(), this.imc.getY(), this.imc.getX(), this.imc.getY());
                    this.l.setStroke(Paint.colorpick.getValue());
                    Paint.getCurrentTab().pane.getChildren().add(this.l);
                    break;
                case EditToolBar.TRIANGLE:
                    this.p  = new Polygon();
                    this.p.getPoints().addAll(
                        this.convPairToArray(
                            super.getPolygonPoints(
                                3,
                                this.imc,
                                (int) e.getX()
                            )
                        )
                    );
                    this.p.setFill(Paint.colorpick.getValue());
                    Paint.getCurrentTab().pane.getChildren().add(this.p);
                    break;
                case EditToolBar.NGON:
                    this.p = new Polygon();
                    this.p.getPoints().addAll(
                        this.convPairToArray(
                            super.getPolygonPoints(
                                Integer.parseInt(
                                    Paint.edittoolbar.getOptionsField()
                                ),
                                this.imc,
                                (int) e.getX()
                            )
                        )
                    );
                    this.p.setFill(Paint.colorpick.getValue());
                    Paint.getCurrentTab().pane.getChildren().add(this.p);
                    break;
                case EditToolBar.ELLIPSE:
                case EditToolBar.CIRCLE:
                    this.ell = new Ellipse();
                    this.ell.setFill(Paint.colorpick.getValue());
                    Paint.getCurrentTab().pane.getChildren().add(this.ell);
                    break;
                case EditToolBar.FREENGON:
                    this.freeNgonStack.add(new Pair(e.getX(), e.getY()));
                    this.p = new Polygon();
                    break;
                
                default:
                    break;
                }
            }
        });

        this.setOnMouseReleased((MouseEvent e) -> {
            this.gc.setFill(Paint.colorpick.getValue());
            this.gc.setStroke(Paint.colorpick.getValue());
            this.gc.setLineWidth(Paint.brushSize);
            boolean fill=Paint.edittoolbar.getFill();
           

            if (Paint.getMode() == Paint.EDIT_MODE) {
                switch (Paint.edittoolbar.getDrawSelection()) {
                case EditToolBar.LINE:
                    super.drawLine(this.imc, e.getX(), e.getY());
                    postDraw();
                    break;
                case EditToolBar.CIRCLE:
                    super.drawCircle(this.imc, e.getX(), e.getY(), fill);
                    postDraw();
                    break;
                case EditToolBar.ELLIPSE:
                    super.drawEllipse(this.imc, e.getX(), e.getY(), fill);
                    postDraw();
                    break;
                case EditToolBar.RECTANGLE:
                    super.drawRectangle(this.imc, e.getX(), e.getY(), fill);
                    postDraw();
                    break;
                case EditToolBar.SQUARE:
                    super.drawSquare(this.imc, e.getX(), e.getY(), fill);
                    postDraw();
                    break;
                case EditToolBar.TEXTBOX:
                    this.gc.setFont(new Font(Paint.brushSize));
                    super.drawText(imc, Paint.edittoolbar.getOptionsField(), fill);
                    postDraw();
                    break;
                case EditToolBar.TRIANGLE:
                    super.drawTriangle(this.imc, e.getX(), fill);
                    postDraw();
                    break;
                   
                case EditToolBar.NGON:
                    int n = 0;
                    try {
                        n = Integer.parseInt(Paint.edittoolbar.getOptionsField());
                    } catch (NumberFormatException ex) {
                        System.out.println("Failed to parse options field: " + ex);
                        return; // to keep from drawing a shape
                    }
                    super.drawNGon(this.imc, e.getX(), n, fill);
                    postDraw();
                    break;

               
                case EditToolBar.DRAGDROP:
                    if (this.drag_drop_image == null) {
                        this.drag_drop_image = super.getRegionAsImage(this.imc, e.getX(), e.getY());
                        this.gc.clearRect(
                            roundDouble(this.imc.getX()),
                            roundDouble(this.imc.getY()),
                            roundDouble(e.getX() - this.imc.getX()),
                            roundDouble(e.getY() - this.imc.getY())
                        );

                        postDraw();
                        return;
                    }
                    this.gc.drawImage(
                        this.drag_drop_image,
                        e.getX(),
                        e.getY()
                    );
                    this.drag_drop_image = null;
                    Paint.getCurrentTab().imgHasBeenSaved = false;
                    break;
                    
                    case EditToolBar.CopyPaste:
                    if (this.drag_drop_image == null) {

                        this.drag_drop_image = super.getRegionAsImage(this.imc, e.getX(), e.getY());
                        postDraw();
                        return;
                    }
                    this.gc.drawImage(
                        this.drag_drop_image,
                        e.getX(),
                        e.getY()
                    );
                    this.drag_drop_image = null;
                    Paint.getCurrentTab().imgHasBeenSaved = false;
                    break;
              
                case EditToolBar.FREENGON:
                    if (this.freeNgonStack.size() == Integer.parseInt(Paint.edittoolbar.getOptionsField())) {
                        Pair freengon = convStackToPair(this.freeNgonStack);
                        this.gc.setFill(Paint.colorpick.getValue());
                        super.drawGon((double[]) freengon.getKey(),
                                      (double[]) freengon.getValue(),
                                      Integer.parseInt(Paint.edittoolbar.getOptionsField()));
                        this.freeNgonStack.clear();
                    }
                    break;
             
                default:
                    break;
                }
                this.imgToStack(this.getImage());
            }

        });

        this.setOnMouseDragged(e -> {

            double bsize = Paint.brushSize;
            double x = e.getX() - bsize / 2;
            double y = e.getY() - bsize / 2;

            //if in edit mode
            if (Paint.getMode() == Paint.EDIT_MODE) {
                switch (Paint.edittoolbar.getDrawSelection()) {
                case EditToolBar.ERASE:
                    this.gc.clearRect(x, y, bsize, bsize);
                    this.imgToStack(this.getImage());
                    postDraw();
                    break;
                case EditToolBar.PENCIL:
                    this.gc.setFill(Paint.colorpick.getValue());
                    this.gc.fillRect(x, y, bsize, bsize);
                    this.imgToStack(this.getImage());
                    postDraw();
                    break;
                
                case EditToolBar.SQUARE:
                    double s;
                    if (e.getX() >= e.getY()) {
                        s = e.getX() - this.imc.getX();
                    } else {
                        s = e.getY() - this.imc.getY();
                    }
                    this.r.setWidth(s);
                    this.r.setHeight(s);
                    break;
                case EditToolBar.LINE:
                    this.l.setEndX(e.getX());
                    this.l.setEndY(e.getY());
                    break;
                case EditToolBar.TRIANGLE:
                    this.p.getPoints().addAll(
                        this.convPairToArray(
                            super.getPolygonPoints(
                                3,
                                this.imc,
                                (int) e.getX()
                            )
                        )
                    );
                    break;
                case EditToolBar.NGON:
                    this.p.getPoints().addAll(
                        this.convPairToArray(
                            super.getPolygonPoints(
                                Integer.parseInt(
                                    Paint.edittoolbar.getOptionsField()
                                ),
                                this.imc,
                                (int) e.getX()
                            )
                        )
                    );
                    break;
                case EditToolBar.ELLIPSE:
                {
                    double cx = this.imc.getX() + (e.getX() - this.imc.getX()) / 2;
                    double cy = this.imc.getY() + (e.getY() - this.imc.getY()) / 2;
                    this.ell.setCenterX(cx);
                    this.ell.setCenterY(cy);
                    this.ell.setRadiusX((e.getX() - this.imc.getX()) / 2);
                    this.ell.setRadiusY((e.getY() - this.imc.getY()) / 2);
                    break;
                }
                case EditToolBar.CIRCLE:
                    double rad;
                    if (e.getX() >= e.getY()) {
                        rad = (e.getX() - this.imc.getX()) / 2;

                    } else {
                        rad = (e.getY() - this.imc.getY()) / 2;
                    }
                    this.ell.setCenterX(this.imc.getX() + rad);
                    this.ell.setCenterY(this.imc.getY() + rad);
                    this.ell.setRadiusX(rad);
                    this.ell.setRadiusY(rad);
                    break;
                default:
                    break;
                }
            }
        });

    }

    /**
     *
     * This method runs only when the  Images are opened, or when the canvas is resized,
     * and sets the canvas width to be the proper size.
     *
     */
    public void updateDimensions() {
        //Also potential thought, I may have the image be a proportion of the current window size,
        //so that when the main window is resized, the image resizes with it.
        if (Paint.getCurrentTab().opened_image != null) {
            //setDimensions(Paint.getCurrentTab().opened_image.getWidth(), Paint.getCurrentTab().opened_image.getHeight());
            this.setHeight(Paint.getCurrentTab().opened_image.getHeight());
            this.setWidth(Paint.getCurrentTab().opened_image.getWidth());
        } else {
            this.setHeight(0);
            this.setWidth(0);
        }
    }

    /**
     * Update the canvas dimensions to be that of an image.
     *
     * @param i The image who's dimensions you want to set the canvas to
     */

    public void updateDimensions(Image i) {
        setDimensions((int) i.getWidth(), (int) i.getHeight());
    }

    //this is a really hackyway of doing this, I want to make this much cleaner
    //(ie refactor)
    public void updateDimensions(boolean inc_zoom) {
        if (inc_zoom) {
            this.setWidth(this.getWidth() * 2);
            this.setHeight(this.getHeight() * 2);
        } else {
            this.setWidth(this.getWidth() / 2);
            this.setHeight(this.getHeight() / 2);
        }
    }


    //need to preserve the image modifications
    public void zoomIn() {
        this.updateDimensions(true); // zoom in
        this.gc.drawImage(this.getImage(), 0, 0, this.getWidth(), this.getHeight());
        Paint.getCurrentTab().setScrollPrefSize(this.getWidth(), this.getHeight());
        this.imgToStack(this.getImage());

    }
    public void zoomOut() {
        this.updateDimensions(false); // zoom out
        this.gc.drawImage(this.getImage(), 0, 0, this.getWidth(), this.getHeight());
        Paint.getCurrentTab().setScrollPrefSize(this.getWidth(), this.getHeight());
        this.imgToStack(this.getImage());

    }
    /**
     * Add an Image to the undo Stack
     *
     * @param i The image to add to the stack
     */
    private void imgToStack(Image i) {
        this.undoStack.push(i);
        this.redoStack.clear(); // reset the redo stack (should only be able to redo what you've undone
        System.out.println("CustomCanvas.java; Added Image to undo Stack");
    }

    /**
     * Rounds Any Double values to integers, may be removed in favor of type casting.
     *
     * @param d
     * @return An Integer rounded via the Math Library.
     */
    private int roundDouble(double d) {
        return (int) Math.round(d);
    }

    /**
     *
     * This Method is responsible for undoing actions that are taken by the
     * user, by pop-ing them off of the undo stack, and setting the canvas
     * to be the next image in line, so to speak.
     *
     */
    public void undo() {
        if (! this.undoStack.empty()) { //if the image stack is not empty
            Image i = this.undoStack.pop();
            try {
                this.redoStack.add(i);
            } catch (Exception e) {
                System.out.println("CustomCanvas.java; Failed to add i to redoStack:" + e);
            }
            try {
                Paint.getCurrentTab().setImage(i);
            } catch (Exception e) {
                System.out.println("CustomCanvas.java; Failed to execute setImage():" + e);
            }
        } else {
            System.out.println("CustomCanvas.java; undoStack is empty");
        }
    }
    /**
     * This Method is allows the redo actions ,by adding image to last image 
     */
    public void redo() {
        if (! redoStack.empty()) {
            Image lastimg = redoStack.pop(); //get the last image
            Paint.getCurrentTab().setImage(lastimg);
            undoStack.add(lastimg);
        }
    }
    /**
     * This method drawn in an image  to the canvas.
     */
    public void postDraw() {
        Paint.getCurrentTab().pane.getChildren().remove(this.r);
        this.r = null;
        Paint.getCurrentTab().pane.getChildren().remove(this.l);
        this.l = null;
        Paint.getCurrentTab().pane.getChildren().remove(this.p);
        this.p = null;
        Paint.getCurrentTab().pane.getChildren().remove(this.ell);
        this.ell = null;
        this.targetColor = null;
        Paint.getCurrentTab().imgHasBeenSaved = false;
    }
    /**
     * Changes pair of double arrays;  convert the output polygon to use javafx polyon.
     * @param pts A pair of the X and Y points, both in the proper order
     * @return A Double[] of the X and Y points
     */
    public Double[] convPairToArray(Pair pts) {
        double[] xp = (double[]) pts.getKey();
        double[] yp = (double[]) pts.getValue();
        Double[] rp = new Double[xp.length + yp.length];
        for (int i = 0; i < xp.length; i++) {
            rp[i * 2] = xp[i];
            rp[i * 2 + 1] = yp[i];
        }
        return rp;
    }
    /**
     * Converts a stack object full of pairs, into a singular pair object,
     * @param s A stack of pairs.
     * @return
     */
    public Pair<double[],double[]> convStackToPair(Stack s) {
        int sSize = s.size();
        double[] xp = new double[sSize];
        double[] yp = new double[sSize];
        Pair pt;
        for (int i = 0; i < sSize; i++) {
            pt = (Pair) s.pop();
            xp[i] = (double) pt.getKey();
            yp[i] = (double) pt.getValue();
        }
        return new Pair(xp, yp);
    }

}