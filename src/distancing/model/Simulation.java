package distancing.model;

import javafx.scene.layout.Pane;

import java.util.ArrayList;

public class Simulation {
    private ArrayList<Person> people;

    public Simulation(Pane world, int popSize) {
        people = new ArrayList<Person>();

        for(int i=0; i < popSize; i++) {
            people.add(new Person(State.SUSCEPTIBLE, world));
        }
        people.add(new Person(State.INFECTED, world));
    }

    public ArrayList<Person> getPeople(){
        return people;
    }

    public void move(){
        for (Person p: people){
            p.move();
        }
    }

    public void draw(){
        for (Person p: people){
            p.draw();
        }
    }
}
