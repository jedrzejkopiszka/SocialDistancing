package distancing.gui;

import distancing.model.Person;
import distancing.model.Simulation;
import distancing.model.State;
import javafx.animation.AnimationTimer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.util.EnumMap;


public class SocialSimController {

    @FXML
    Pane world;

    @FXML
    Button startButton;

    @FXML
    Button stopButton;

    @FXML
    Button stepButton;

    @FXML
    Slider sizeSlider;

    @FXML
    Slider recoverySlider;

    @FXML
    Slider distanceSlider;

    @FXML
    TextField tickText;

    @FXML
    Pane chart;

    @FXML
    Pane histogram;

    Simulation sim;
    EnumMap<State, Rectangle> hrects = new EnumMap<State, Rectangle>(State.class);
    private Movement clock;

    private class Movement extends AnimationTimer{

        private long FRAMES_PER_SEC = 50L;
        private long INTERVAL = 10000000L / FRAMES_PER_SEC;

        private long last = 0;
        private int ticks = 0;
        @Override
        public void handle(long now) {
            if (now - last > INTERVAL){
                step();
                last = now;
                tick();
            }
        }
        public int getTicks(){
            return ticks;
        }

        public void resetTicks() {
            ticks = 0;
        }

        public void tick() {
            ticks += 1;
        }
    }

    @FXML
    public void initialize(){
        sizeSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                setSize();
            }
        });
        recoverySlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                setRecovery();
            }
        });
        distanceSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                setDistance();
            }
        });
        clock = new Movement();
        world.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
    }

    private void setDistance() {
        Person.distance = (int)distanceSlider.getValue();
    }

    private void setRecovery() {
        Person.healtime = 50 * (int)recoverySlider.getValue();
    }

    private void setSize() {
        Person.radius = (int)sizeSlider.getValue();
        sim.draw();
    }

    @FXML
    public void reset(){
        stop();
        clock.resetTicks();
        tickText.setText("" + clock.getTicks());
        world.getChildren().clear();
        histogram.getChildren().clear();
        chart.getChildren().clear();
        sim = new Simulation(world, 100);
        setSize();
        setRecovery();
        setDistance();
        int offset = 0;
        for (State s: State.values()){
            Rectangle r = new Rectangle(60, 0, s.getColor());
            r.setTranslateX(offset);
            offset += 65;
            hrects.put(s, r);
            histogram.getChildren().add(r);
        }
        drawCharts();
    }


    @FXML
    public void start(){
        clock.start();
        disableButtons(true, false, true);
    }

    @FXML
    public void stop(){
        clock.stop();
        disableButtons(false, true, false);
    }

    @FXML
    public void step() {
        sim.move();
        sim.feelBetter();
        sim.resolveCollisions();
        sim.draw();
        clock.tick();
        tickText.setText("" + clock.getTicks());
        drawCharts();
    }

    public void disableButtons(boolean start, boolean stop, boolean step){
        startButton.setDisable(start);
        stopButton.setDisable(stop);
        stepButton.setDisable(step);
    }

    private void drawCharts() {
        EnumMap<State, Integer> currentPop = new EnumMap<State, Integer>(State.class);
        for (Person p: sim.getPeople()){
            if (!currentPop.containsKey(p.getState())){
                currentPop.put(p.getState(), 0);
            }
            currentPop.put(p.getState(), 1 + currentPop.get(p.getState()));
        }
        for (State state: hrects.keySet()){
            if (currentPop.containsKey(state)){
                hrects.get(state).setHeight(currentPop.get(state));
                hrects.get(state).setTranslateY(30 + 100 - currentPop.get(state));

                Circle c = new Circle(1, state.getColor());
                c.setTranslateX(clock.getTicks()/5.0);
                c.setTranslateY(130 - currentPop.get(state));
                chart.getChildren().add(c);
            }
        }
        if (!currentPop.containsKey(State.INFECTED)){
            stop();
        }
    }
}

