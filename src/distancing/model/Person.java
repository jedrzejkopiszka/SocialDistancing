package distancing.model;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Person {
    public static int radius = 5;
    public static int healtime = 5 * 50;
    public static int distance = 200;

    private State state;
    private Position loc;
    private Position origin;
    private Heading heading;
    private Circle c;
    private Pane world;
    private int sickTime = 0;

    public Person(State state, Pane world) {
        this.state = state;
        this.world = world;
        this.heading = new Heading();
        this.loc = new Position(world);
        this.origin = new Position(loc.getX(), loc.getY());
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
        loc.move(heading, world, origin);
    }

    public void draw(){
        c.setRadius(radius);
        c.setTranslateX(loc.getX());
        c.setTranslateY(loc.getY());
    }

    public void collisionCheck(Person other){
        if (loc.collision(other.loc)){
            if (other.getState() == State.INFECTED && state == State.SUSCEPTIBLE){
                setState(State.INFECTED);
            }
        }
    }

    public void feelBetter(){
        if (state == State.INFECTED){
            sickTime++;
            if (sickTime> healtime){
                setState(State.RECOVERED);
            }
        }

    }
}
