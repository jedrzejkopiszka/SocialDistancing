package distancing.model;

public class Position {

    private double x;
    private double y;


    public Position(double x, double y){
        this.x = x;
        this.y =  y;
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
}
