package com;

public class PhysicsController {

    int state = 0; //states: 0 means no wheels contacting floor. 1 means left wheel contacting floor. 2 means right wheel contacting floor. 3 means both wheels contacting floor.
    double time = 0;
    final double timestep = 0.00001;
    final double k = 1; // spring constant
    final double l = 100; // half of wheelbase length
    final double leq = 5; //equilibrium length for the springs.
    final double m = 1; // mass
    final double g = 9.81; // gravity.
    double y = 0;
    double theta = 0; // in radians.
    double x = 0;
    double yvel = 0;
    double xvel = 0;
    double thetavel = 0;
    
    public PhysicsController (double y, double x, double theta, double yvel, double xvel, double thetavel) {
        this.y = y;
        this.x = x;
        this.theta = theta;
        this.yvel = yvel;
        this.xvel = xvel;
        this.thetavel = thetavel;
    }

    public void update() {
        state = currentState();

        time += timestep;
        x += xvel * timestep;
        y += yvel * timestep;
        theta += thetavel * timestep;
        xvel += 0;
        yvel += calculateYA();
        thetavel += calculateTA();

    }

    public String toString() {
        return state +"\n" + y + ", " + theta + "\n" + yvel + ", " +thetavel + "\n" + calculateYA() + ", " + calculateTA();
    }

    public int x1() { // left vertex
        double val = x - l*Math.cos(theta);
        return (int)val;
    }

    public int y1() { // left vertex
        double val = y - l*Math.sin(theta);
        return (int) val;
    }

    public int x2() { // right vertex
        double val = x + l*Math.cos(theta);
        return (int) val;
    }

    public int y2() { // right vertex  
        double val = y + l*Math.sin(theta);
        return (int) val;
    }

    private double calculateTA() {
        if(distFrom2PI() < 0.01 && state == 3) {
            theta = 0;
            System.out.println("Unstable theta values reached.");
        }
        System.out.println("entered with state " + state);
        double TA = 0;
        switch(state) {
            case 0:
                TA = 0;
                break;
            case 1:
                TA = 12 * k * ((leq - y/Math.cos(theta) - l*Math.tan(theta)) * (-y/Math.cos(theta) * Math.tan(theta) - l*Math.pow(1/Math.cos(theta), 2))) 
                            /  (m*Math.pow(2*l, 2));
                break;

            case 2:
                TA = 12 * k * ((leq - y/Math.cos(theta) + l*Math.tan(theta)) * (-y/Math.cos(theta) * Math.tan(theta) + l*Math.pow(1/Math.cos(theta), 2)))
                            /  (m*Math.pow(2*l, 2));
                break;

            case 3: 
                TA = 12 * k * ((leq - y/Math.cos(theta) - l*Math.tan(theta)) * (-y/Math.cos(theta) * Math.tan(theta) - l*Math.pow(1/Math.cos(theta), 2)) 
                            +  (leq - y/Math.cos(theta) + l*Math.tan(theta)) * (-y/Math.cos(theta) * Math.tan(theta) + l*Math.pow(1/Math.cos(theta), 2)))
                            /  (m*Math.pow(2*l, 2));
                break;
        }

        return TA;
    }

    private double calculateYA() {
        double YA = 0;
        System.out.println("entered with state " + state);
        switch(state) {
            case 0:
                YA = -g;
                break;
            case 1:
                YA = -g + k*(leq - y/Math.cos(theta) - l*Math.tan(theta)) / Math.cos(theta);
                break;
            case 2:
                YA = -g + k*(leq - y/Math.cos(theta) + l*Math.tan(theta)) / Math.cos(theta);
                break;
            case 3:
                YA = -g + 2*k / Math.cos(theta) * (leq - y/Math.cos(theta));
                break;
        }

        return YA;
    }

    private double distFrom2PI() {
        return Math.abs(Math.abs(theta % (2 * Math.PI)) - Math.PI/2);
    }

    private double leftWheelDistance() {
        if (distFrom2PI() < 0.01) {
            return 1000000;
        }
        return (leq - y/Math.cos(theta) - l*Math.tan(theta));
    }

    private double rightWheelDistance() {
        if (distFrom2PI() < 0.01) {
            return 1000000;
        }
        return (leq - y/Math.cos(theta) + l*Math.tan(theta));
    }

    private boolean isOriented() { //checks proper orientation: meaning theta is between pi/2 and -pi/2.
        return (theta %(2 * Math.PI) < Math.PI/2 && theta % (2*Math.PI) > -Math.PI/2);
    }

    private int currentState() {
        boolean left = false;
        boolean right = false;
        //left wheel collision calculation:
        if (isOriented() && leftWheelDistance() < leq) { 
            left = true;
        }

        //right wheel collision calculation:
        if(isOriented() && rightWheelDistance() < leq) {
            right = true;
        }

        if(left && right) {
            return 3;
        }

        if (left) {
            return 1;
        }

        if(right) {
            return 2;
        }

        return 0;
    }

}