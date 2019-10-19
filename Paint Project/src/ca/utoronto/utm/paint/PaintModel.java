package ca.utoronto.utm.paint;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javafx.scene.canvas.GraphicsContext;

public class PaintModel extends Observable implements Observer {

	public void save(PrintWriter writer) {
		PaintCommandVisitor paintCommandVisitor = new PaintCommandVisitor(writer, this);
		for (PaintCommand command: this.getCommands()) {
			if (command.getName() == "Circle") {
				CircleCommand circleCommand = (CircleCommand)command;
				circleCommand.accept(paintCommandVisitor);
			}
			else if (command.getName() == "Rectangle") {
				RectangleCommand rectangleCommand = (RectangleCommand)command;
				rectangleCommand.accept(paintCommandVisitor);
			}
			else if (command.getName() == "Squiggle") {
				SquiggleCommand squiggleCommand = (SquiggleCommand)command;
				squiggleCommand.accept(paintCommandVisitor);
			}
			
		}
	}
	public void reset(){
		for(PaintCommand c: this.commands){
			c.deleteObserver(this);
		}
		this.commands.clear();
		this.setChanged();
		this.notifyObservers();
	}
	
	public void addCommand(PaintCommand command){
		this.commands.add(command);
		command.addObserver(this);
		this.setChanged();
		this.notifyObservers();
	}
	
	private ArrayList<PaintCommand> commands = new ArrayList<PaintCommand>();
	
	public ArrayList<PaintCommand> getCommands() {
		return commands;
	}

	public void executeAll(GraphicsContext g) {
		PaintDrawVisitor pdv = new PaintDrawVisitor(g);
		for(PaintCommand c: this.commands) {
			if(c.getName() == "Circle") {
				CircleCommand circleCommand = (CircleCommand)c;
				circleCommand.accept(pdv);
			}
			if(c.getName() == "Rectangle") {
				RectangleCommand rectangleCommand = (RectangleCommand)c;
				rectangleCommand.accept(pdv);
			}
			if(c.getName() == "Squiggle") {
				SquiggleCommand squiggleCommand = (SquiggleCommand)c;
				squiggleCommand.accept(pdv);
			}
		}
	}
	
	/**
	 * We Observe our model components, the PaintCommands
	 */
	@Override
	public void update(Observable o, Object arg) {
		this.setChanged();
		this.notifyObservers();
	}
}
