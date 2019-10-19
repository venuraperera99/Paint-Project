package ca.utoronto.utm.paint;

import java.util.ArrayList;

import javafx.scene.canvas.GraphicsContext;
/**
 * A visitor class that is used whenever any of the 3 visitable classes are visited, Whenever the class is visited
 * It draws the shape of the class that is being visited. This class IS-A Visitor, HAS-A GraphicsContext,
 * and RESPONDS-TO visit(RectangleCommand/CircleCommand/SquiggleCommand)
 * @author Venura Perera
 *
 */
public class PaintDrawVisitor implements Visitor{

	private GraphicsContext g;
	public PaintDrawVisitor(GraphicsContext graphic) {
		this.g=graphic; 
	}

	@Override
	/**
	 * allows the class to be visited by a CircleCommand and execute the circleCommand
	 * @param circleCommand  a circleCommand that draws a circle
	 */
	public void visit(CircleCommand circleCommand) {
		int x = circleCommand.getCentre().x;
		int y = circleCommand.getCentre().y;
		int radius = circleCommand.getRadius();
		if(circleCommand.isFill()){
			g.setFill(circleCommand.getColor());
			g.fillOval(x-radius, y-radius, 2*radius, 2*radius);
		} else {
			g.setStroke(circleCommand.getColor());
			g.strokeOval(x-radius, y-radius, 2*radius, 2*radius);
		}
		
	}

	@Override
	/**
	 * allows the class to be visited by a RectangleCommand and execute the rectangleCommand
	 * @param rectangleCommand  a rectangleCommand that draws a circle
	 */
	public void visit(RectangleCommand rectangleCommand) {
		Point topLeft = rectangleCommand.getTopLeft();
		Point dimensions = rectangleCommand.getDimensions();
		if(rectangleCommand.isFill()){
			g.setFill(rectangleCommand.getColor());
			g.fillRect(topLeft.x, topLeft.y, dimensions.x, dimensions.y);
		} else {
			g.setStroke(rectangleCommand.getColor());
			g.strokeRect(topLeft.x, topLeft.y, dimensions.x, dimensions.y);
		}
		
	}

	@Override
	/**
	 * allows the class to be visited by a SquiggleCommand and execute the squiggleCommand
	 * @param squiggleCommand  a squiggleCommand that draws a squiggle
	 */
	public void visit(SquiggleCommand squiggleCommand) {
		ArrayList<Point> points = squiggleCommand.getPoints();
		g.setStroke(squiggleCommand.getColor());
		for(int i=0;i<points.size()-1;i++){
			Point p1 = points.get(i);
			Point p2 = points.get(i+1);
			g.strokeLine(p1.x, p1.y, p2.x, p2.y);
		}
		
	}

}
