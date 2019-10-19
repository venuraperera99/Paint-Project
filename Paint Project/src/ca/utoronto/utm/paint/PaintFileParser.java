package ca.utoronto.utm.paint;

import javafx.scene.paint.Color;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * Parse a file in Version 1.0 PaintSaveFile format. An instance of this class
 * understands the paint save file format, storing information about
 * its effort to parse a file. After a successful parse, an instance
 * will have an ArrayList of PaintCommand suitable for rendering.
 * If there is an error in the parse, the instance stores information
 * about the error. For more on the format of Version 1.0 of the paint 
 * save file format, see the associated documentation.
 * 
 * @author 
 *
 */
public class PaintFileParser {
	private int lineNumber = 0; // the current line being parsed
	private String errorMessage =""; // error encountered during parse
	private PaintModel paintModel; 
	
	/**
	 * Below are Patterns used in parsing 
	 */
	private Pattern pFileStart=Pattern.compile("^PaintSaveFileVersion1.0$");
	private Pattern pFileEnd=Pattern.compile("^EndPaintSaveFile$");

	private Pattern pCircleStart=Pattern.compile("^Circle$");
	private Pattern pCircleEnd=Pattern.compile("^EndCircle$");
	// ADD MORE!!
	private Pattern pRectangleStart=Pattern.compile("^Rectangle$");
	private Pattern pRectangleEnd=Pattern.compile("^EndRectangle$");
	
	private Pattern pSquiggleStart=Pattern.compile("^Squiggle$");
	private Pattern pPointsStart=Pattern.compile("^points$");
	private Pattern pPointsEnd=Pattern.compile("^endpoints$");
	private Pattern pSquiggleEnd=Pattern.compile("^EndSquiggle$");
	
	private Pattern pColor=Pattern.compile("^color:([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5]),"
			+ "([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5]),([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$");
	private Pattern pFilled=Pattern.compile("^filled:(true|false)$");
	private Pattern pCenter=Pattern.compile("^center:[(]([0-9]|[1-9][0-9]|[0-9]{3}),([0-9]|[1-9][0-9]|[0-9]{3})[)]$");
	private Pattern pRadius=Pattern.compile("^radius:([0-9]|[1-9][0-9]|[0-9]{3})$"); 
	private Pattern pP1=Pattern.compile("^p1:[(]([0-9]|[1-9][0-9]|[0-9]{3}),([0-9]|[1-9][0-9]|[0-9]{3})[)]$");
	private Pattern pP2=Pattern.compile("^p2:[(]([0-9]|[1-9][0-9]|[0-9]{3}),([0-9]|[1-9][0-9]|[0-9]{3})[)]$");
	private Pattern pPoint=Pattern.compile("^point:[(]([0-9]|[1-9][0-9]|[0-9]{3}),([0-9]|[1-9][0-9]|[0-9]{3})[)]$");
	
	/**
	 * Store an appropriate error message in this, including 
	 * lineNumber where the error occurred.
	 * @param mesg
	 */
	private void error(String mesg){
		this.errorMessage = "Error in line "+lineNumber+" "+mesg;
	}
	
	/**
	 * 
	 * @return the error message resulting from an unsuccessful parse
	 */
	public String getErrorMessage(){
		return this.errorMessage;
	}
	
	/**
	 * Parse the inputStream as a Paint Save File Format file.
	 * The result of the parse is stored as an ArrayList of Paint command.
	 * If the parse was not successful, this.errorMessage is appropriately
	 * set, with a useful error message.
	 * 
	 * @param inputStream the open file to parse
	 * @param paintModel the paint model to add the commands to
	 * @return whether the complete file was successfully parsed
	 */
	public boolean parse(BufferedReader inputStream, PaintModel paintModel) {
		this.paintModel = paintModel;
		this.errorMessage="";
		
		// During the parse, we will be building one of the 
		// following commands. As we parse the file, we modify 
		// the appropriate command.
		
		CircleCommand circleCommand = new CircleCommand(new Point(0,0), 0); 
		RectangleCommand rectangleCommand = new RectangleCommand(new Point(0,0), new Point(0,0));
		SquiggleCommand squiggleCommand = new SquiggleCommand();
	
		try {	
			int state=0; Matcher m; String l; Matcher m2; Matcher m3; Matcher m4; String line;
			
			this.lineNumber=0;
			while ((l = inputStream.readLine()) != null) {
				this.lineNumber++;
				line = l.replaceAll("\\s", "");
				System.out.println(lineNumber+" "+l+" "+state);
				switch(state){
					case 0:
						m=pFileStart.matcher(line);
						if(m.matches()){
							state=1;
							break;
						}
						error("Expected StartofPaintSaveFile");
						return false;
					case 1: // Looking for the start of a new object or end of the save file
						m=pCircleStart.matcher(line);
						m2=pRectangleStart.matcher(line);
						m3=pSquiggleStart.matcher(line);
						m4=pFileEnd.matcher(line);
						if(m.matches()){ // If its a Circle
							state=2; 
							break;
						}
						else if(m2.matches()) { // If its a Rectangle
							state=7;
							break;
						}
						else if(m3.matches()) { // If its a Squiggle
							state=12;
							break;
						}
						else if(m4.matches()) { // If its End Paint Save File
							state=17;
							break;
						}
						else if(line.equals("")) {
							break;
						}
						error("Expected an Object or End Paint Save File");
						return false;
					case 2: // Looking for color for Circle
						m=pColor.matcher(line);
						if(m.matches()) {
							int r = Integer.parseInt(m.group(1)); int b = Integer.parseInt(m.group(3)); 
							int g = Integer.parseInt(m.group(2));
							circleCommand.setColor(Color.rgb(r, g, b));
							state = 3;
							break;
						}
						error("Expected a color value");
						return false;
					case 3: // Looking for filled of Circle
						m=pFilled.matcher(line);
						if(m.matches()) {
							boolean f = Boolean.parseBoolean(m.group(1));
							circleCommand.setFill(f);
							state = 4;
							break;
						}
						error("Expected a boolean value for filled");
						return false;
					case 4: // Looking for center of Circle
						m=pCenter.matcher(line);
						if(m.matches()) {
							Point p = new Point(Integer.parseInt(m.group(1)),Integer.parseInt(m.group(2)));
							circleCommand.setCentre(p);
							state = 5;
							break;
						}
						error("Expected a point for center of Circle");
						return false;
					case 5: // Looking for radius for Circle
						m=pRadius.matcher(line);
						if(m.matches()) {
							int r = Integer.parseInt(m.group(1));
							circleCommand.setRadius(r);
							state = 6;
							break;
						}
						error("Expected an integer for radius of circle");
						return false;
					case 6: // Looking for EndCircle
						m=pCircleEnd.matcher(line);
						if(m.matches()) {
							paintModel.addCommand(circleCommand);
							state = 1;
							circleCommand = new CircleCommand(new Point(0,0), 0);
							break;
						}
						error("Expecting EndCircle");
						return false;
					case 7: // Looking for Color for Rectangle
						m=pColor.matcher(line.trim());
						if(m.matches()) {
							int r = Integer.parseInt(m.group(1)); int b = Integer.parseInt(m.group(3)); 
							int g = Integer.parseInt(m.group(2));
							rectangleCommand.setColor(Color.rgb(r, g, b));
							state = 8;
							break;
						}
						error("Expected a color value");
						return false;
					case 8: // Looking for Filled for Rectangle
						m=pFilled.matcher(line);
						if(m.matches()) {
							boolean f = Boolean.parseBoolean(m.group(1));
							rectangleCommand.setFill(f);
							state = 9;
							break;
						}
						error("Expected a boolean value for filled");
						return false;
					case 9: // Looking for P1 for Rectangle
						m=pP1.matcher(line);
						if(m.matches()) {
							Point p1 = new Point(Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2)));
							rectangleCommand.setP1(p1);
							state = 10;
							break;
						}
						error("Expected a point 'p1'");
						return false;
					case 10: // Looking for P2 for Rectangle
						m=pP2.matcher(line);
						if(m.matches()) {
							Point p2 = new Point(Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2)));
							rectangleCommand.setP2(p2);
							state = 11;
							break;
						}
						error("Expected a point 'p2'");
						return false;
					case 11: // Looking for EndRectangle
						m=pRectangleEnd.matcher(line);
						if(m.matches()) {
							paintModel.addCommand(rectangleCommand);
							state = 1;
							rectangleCommand = new RectangleCommand(new Point(0,0), new Point(0,0));
							break;
						}
						error("Expected EndRectangle");
						return false;
					case 12: // Looking for Color for Squiggle
						m=pColor.matcher(line);
						if(m.matches()) {
							int r = Integer.parseInt(m.group(1)); int b = Integer.parseInt(m.group(3)); 
							int g = Integer.parseInt(m.group(2));
							squiggleCommand.setColor(Color.rgb(r, g, b));
							state = 13;
							break;
						}
						error("Expected a color value");
						return false;
					case 13: // Looking for Filled for Squiggle
						m=pFilled.matcher(line);
						if(m.matches()) {
							boolean f = Boolean.parseBoolean(m.group(1));
							squiggleCommand.setFill(f);
							state = 14;
							break;
						}
						error("Expected a boolean value for filled");
						return false;
					case 14: // Looking for 'points' string for Squiggle
						m=pPointsStart.matcher(line);
						if(m.matches()) {
							state = 15;
							break;
						}
						error("Expected the string 'points' for Squiggle");
						return false;
					case 15: // Looking for all the points and the 'endpoints' string for Squiggle 
						m=pPoint.matcher(line);
						m2=pPointsEnd.matcher(line);
						if(m.matches()) {
							squiggleCommand.add(new Point(Integer.parseInt(m.group(1)),Integer.parseInt(m.group(2))));
							break;
						}
						else if(m2.matches()) {
							state = 16;
							break;
						}
						error("Expected 0 or more points");
						return false;
					case 16: // Looking for EndSquiggle
						m=pSquiggleEnd.matcher(line);
						if(m.matches()) {
							paintModel.addCommand(squiggleCommand);
							state = 1;
							squiggleCommand = new SquiggleCommand();
							break;
						}
						error("Expected EndSquiggle");
						return false;
					case 17: // Looking for EndPaintSaveFile
						m=pFileEnd.matcher(line);
						if(m.matches()) {
							break;
						}
						error("Expected 'EndPaintSaveFile' at the end of the file as the last line");
						return false;
				}
			}
		}  catch (Exception e){
			error("Didn't parse");
		}
		return true;
	}
}
