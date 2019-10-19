package ca.utoronto.utm.paint;
/**
 * An interface that allows classes to be visited in the Visitor design pattern. Which allows
 * classes to be visited and operations to be executed with ease.
 * @author Venura Perera
 *
 */
public interface Visitable {

	public void accept(Visitor visitor);
}
