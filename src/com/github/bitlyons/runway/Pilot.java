package com.github.bitlyons.runway;

/**
 * Created by : Brendan Lyons (20066143)
 */
public class Pilot extends Employee{

    private double flightHours;


    public Pilot(String name, int age, double salary, double flightHours) {
        super(name, age, salary, "Pilot");
        this.flightHours = flightHours;
    }

    public double getFlightHours() {
        return flightHours;
    }

    public void setFlightHours(double flightHours) {
        this.flightHours = flightHours;
    }

    @Override
    public String toString() {
        return super.toString() +
                "Pilot{" +
                "flightHours=" + flightHours +
                '}';
    }
}
