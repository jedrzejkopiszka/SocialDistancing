package distancing.gui;

import distancing.model.Person;
import distancing.model.Simulation;
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


    Simulation sim;
    private Movement clock;

    private class Movement extends AnimationTimer{

        private long FRAMES_PER_SEC = 50L;
        private long INTERVAL = 10000000L / FRAMES_PER_SEC;

        private long last = 0;

        @Override
        public void handle(long now) {
            if (now - last > INTERVAL){
                step();
                last = now;
            }
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
        Person.radius = (int)distanceSlider.getValue();
        sim.draw();
    }

    private void setRecovery() {
        Person.healtime = (int)recoverySlider.getValue();
        sim.draw();
    }

    private void setSize() {
        Person.radius = (int)sizeSlider.getValue();
        sim.draw();
    }

    @FXML
    public void reset(){
        stop();
        world.getChildren().clear();
        sim = new Simulation(world, 100);
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
    }

    public void disableButtons(boolean start, boolean stop, boolean step){
        startButton.setDisable(start);
        stopButton.setDisable(stop);
        stepButton.setDisable(step);
    }
}

