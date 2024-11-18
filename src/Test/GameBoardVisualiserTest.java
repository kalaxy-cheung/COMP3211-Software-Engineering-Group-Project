package Test;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import static org.junit.Assert.assertEquals;

public class GameBoardVisualiserTest {
    private File tempXmlFile;

    @Before
    public void setUp() throws IOException {
        // Create a temporary XML file for testing
        tempXmlFile = File.createTempFile("testGameBoard", ".xml");
        try (PrintWriter out = new PrintWriter(new FileWriter(tempXmlFile))) {
            out.println("<squares>");
            out.println("    <square position=\"1\">");
            out.println("        <name>Go</name>");
            out.println("        <price>0</price>");
            out.println("    </square>");
            out.println("    <square position=\"2\">");
            out.println("        <name>Income Tax</name>");
            out.println("        <price>200</price>");
            out.println("    </square>");
            out.println("    <square position=\"3\">");
            out.println("        <name>Jail</name>");
            out.println("        <price>0</price>");
            out.println("    </square>");
            out.println("    <square position=\"4\">");
            out.println("        <name>Free Parking</name>");
            out.println("        <price>0</price>");
            out.println("    </square>");
            out.println("    <square position=\"5\">");
            out.println("        <name>Go Jail</name>");
            out.println("        <price>0</price>");
            out.println("    </square>");
            out.println("</squares>");
        }
    }

    @After
    public void tearDown() {
        // Delete the temporary file after tests are done
        if (tempXmlFile != null && tempXmlFile.exists()) {
            tempXmlFile.delete();
        }
    }

    @Test
    public void testLoadGridFromXML() {
        EdgeGridDisplayFromXML gridDisplay = new EdgeGridDisplayFromXML() {
            @Override
            protected String[][] loadGridFromXML(String filePath) {
                return super.loadGridFromXML(tempXmlFile.getAbsolutePath());
            }
        };

        String[][] grid = gridDisplay.loadGridFromXML(tempXmlFile.getAbsolutePath());

        // Check the expected values in the grid
        assertEquals("Go", grid[5][5]); // Position 1
        assertEquals("Income Tax ($200)", grid[5][4]); // Position 2
        assertEquals("Jail", grid[5][3]); // Position 3
        assertEquals("Free Parking", grid[5][2]); // Position 4
        assertEquals("Go Jail", grid[5][1]); // Position 5

        // Ensure non-edge positions are empty
        assertEquals("", grid[1][1]);
        assertEquals("", grid[2][2]);
    }
}