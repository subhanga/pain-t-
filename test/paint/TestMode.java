package paint;

import javafx.scene.paint.Color;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class TestMode{
    
    public TestMode(){
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }
    
    /**
     * Test of setFillShape method, of class DCanvas.
     */
    @Test
    public void testSetMode(){
        int Edit_mode = 1;
        System.out.println("setMode");
        ModeMenu instance = new ModeMenu();
        instance.setMode(Edit_mode);
        assertEquals(1,instance.getMode());//if getMode returns 1 its all good
    }

    
  
}