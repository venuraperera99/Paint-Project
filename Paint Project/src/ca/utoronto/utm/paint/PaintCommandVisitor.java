package ca.utoronto.utm.paint;

import java.io.PrintWriter;
/**
 * A visitor class that is used on any of the visitable shape objects, the class is also used 
 * for any paint commands that are used in saving files.This class IS-A Visitor, HAS-A GraphicsContext,
 * and RESPONDS-TO visit(RectangleCommand/CircleCommand/SquiggleCommand)
 * @author Venura Perera
 *
 */
public class PaintCommandVisitor implements Visitor{

	private PaintModel paintModel;
	private PrintWriter writer;
	public PaintCommandVisitor(PrintWriter pw, PaintModel pm) {
		this.paintModel=pm; this.writer=pw;
	}

	@Override
	/**
	 * allows the class to be visited by a CircleCommand and save the circle to a save file
	 * @param circleCommand  a circleCommand that writes a circle to the save file
	 */
	public void visit(CircleCommand circleCommand) {
		writer.println("Circle");
		writer.print("\tcolor:"+(int)(circleCommand.getColor().getRed()*255)+","
		+(int)(circleCommand.getColor().getGreen()*255)+","
		+(int)(circleCommand.getColor().getBlue()*255)+"\n");
		writer.print("\tfilled:"+circleCommand.isFill()+"\n");
		writer.print("\tcenter:("+circleCommand.getCentre().x+","+circleCommand.getCentre().y+")\n");
		writer.print("\tradius:"+circleCommand.getRadius()+"\n");
		writer.println("End Circle");
	}

	@Override
	/**
	 * allows the class to be visited by a RectangleCommand and save the rectangle to a save file
	 * @param rectangleCommand  a rectangleCommand that writes a rectangle to the save file
	 */
	public void visit(RectangleCommand rectangleCommand) {
		writer.println("Rectangle");
		writer.print("\tcolor:"+(int)(rectangleCommand.getColor().getRed()*255)+","
		+(int)(rectangleCommand.getColor().getGreen()*255)+","
		+(int)(rectangleCommand.getColor().getBlue()*255)+"\n");
		writer.print("\tfilled:"+rectangleCommand.isFill()+"\n");
		writer.print("\tp1:("+rectangleCommand.getP1().x+","+rectangleCommand.getP1().y+")\n");
		writer.print("\tp2:("+rectangleCommand.getP2().x+","+rectangleCommand.getP2().y+")\n");
		writer.println("End Rectangle");
	}

	@Override
	/**
	 * allows the class to be visited by a SquiggleCommand and save the squiggle to a save file
	 * @param squiggleCommand  a squiggleCommand that writes a squiggle to the save file
	 */
	public void visit(SquiggleCommand squiggleCommand) {
		writer.println("Squiggle");
		writer.print("\tcolor:"+(int)(squiggleCommand.getColor().getRed()*255)+","
		+(int)(squiggleCommand.getColor().getGreen()*255)+","
		+(int)(squiggleCommand.getColor().getBlue()*255)+"\n");
		writer.print("\tfilled:"+squiggleCommand.isFill()+"\n");
		writer.println("\tpoints");
			for(Point p: squiggleCommand.getPoints()) {
				writer.print("\t\tpoint:("+p.x+","+p.y+")\n");
			}
			writer.println("\tend points");
		writer.println("End Squiggle");
	}

}
