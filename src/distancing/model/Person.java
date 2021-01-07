package distancing.model;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Person {
    public static int radius = 5;
    private State state;
    private Position loc;
    private Heading heading;
    private Circle c;
    private Pane world;

    public Person(State state, Pane world) {
        this.state = state;
        this.world = world;
        this.heading = new Heading();
        this.loc = new Position(world, radius);
        this.c = new Circle(radius, state.getColor());
        c.setStroke(Color.BLACK);
        world.getChildren().add(c);
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
        c.setFill(state.getColor());
    }

    public void move(){
        loc.move(heading);
    }

    public void draw(){
        c.setRadius(radius);
        c.setTranslateX(loc.getX());
        c.setTranslateY(loc.getY());
    }
}
