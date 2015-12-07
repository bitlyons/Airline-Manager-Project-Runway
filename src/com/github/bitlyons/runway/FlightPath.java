package com.github.bitlyons.runway;

import java.io.Serializable;

/**
 * Created by : Brendan Lyons (20066143)
 */
public class FlightPath implements Serializable {
    private String airportA, airportB;
    private double flightTime;

    public FlightPath(String airportA, String airportB, double flightTime) {
        this.airportA = airportA;
        this.airportB = airportB;
        this.flightTime = flightTime;
    }

    public String getAirportA() {
        return airportA;
    }

    public void setAirportA(String airportA) {
        this.airportA = airportA;
    }

    public String getAirportB() {
        return airportB;
    }

    public void setAirportB(String airportB) {
        this.airportB = airportB;
    }

    public double getFlightTime() {
        return flightTime;
    }

    public void setFlightTime(double flightTime) {
        this.flightTime = flightTime;
    }
}
