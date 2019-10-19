package ca.utoronto.utm.paint;
import javafx.scene.canvas.GraphicsContext;

public class CircleCommand extends PaintCommand implements Visitable{
	private Point centre;
	private int radius;
	
	public CircleCommand(Point centre, int radius){
		super("Circle");
		this.centre = centre;
		this.radius = radius;
	}
	
	public Point getCentre() { return centre; }
	public void setCentre(Point centre) { 
		this.centre = centre; 
		this.setChanged();
		this.notifyObservers();
	}
	public int getRadius() { return radius; }
	public void setRadius(int radius) { 
		this.radius = radius; 
		this.setChanged();
		this.notifyObservers();
	}

	@Override
	/**
	 * Accepts this class as an visitor, and allows it to use visit methods in other visitor classes
	 */
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}

}
