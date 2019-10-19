package ca.utoronto.utm.paint;
/**
 * An interface that allows said 'Visitable' classes to visit and execute the command
 * @author Venura Perera
 *
 */
public interface Visitor {

	public void visit(CircleCommand circleCommand);
	public void visit(RectangleCommand rectangleCommand);
	public void visit(SquiggleCommand squiggleCommand);
}
