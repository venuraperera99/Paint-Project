package ca.utoronto.utm.paint;
import javafx.scene.canvas.GraphicsContext;
import java.util.ArrayList;

public class SquiggleCommand extends PaintCommand implements Visitable{
	
	public SquiggleCommand() {
		super("Squiggle");
	}

	private ArrayList<Point> points=new ArrayList<Point>();
	
	
	public void add(Point p){ 
		
		this.points.add(p); 
		this.setChanged();
		this.notifyObservers();
	}
	public ArrayList<Point> getPoints(){ return this.points; }
	
	@Override
	/**
	 * Accepts this class as an visitor, and allows it to use visit methods in other visitor classes
	 */
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}
}
