package distancing.model;

import javafx.scene.layout.Pane;

public class Position {

    private double x;
    private double y;


    public Position(double x, double y){
        this.x = x;
        this.y =  y;
    }

    public Position(Pane world, int radius) {
        this(radius + Math.random() * (world.getWidth() - 2 * radius),
                radius + Math.random() * (world.getHeight() - 2*radius));
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double distance(Position other){
        return Math.sqrt(Math.pow(this.x - other.x, 2) + Math.pow(this.y - other.y, 2));
    }

    public void move(Heading heading){
        x += heading.getDx();
        y += heading.getDy();
    }
}
