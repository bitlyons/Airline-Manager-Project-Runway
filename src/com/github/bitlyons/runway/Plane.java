package com.github.bitlyons.runway;

import java.io.Serializable;

/**
 * Created by : Brendan Lyons (20066143)
 */
public class Plane implements Serializable {

    private String name, manufacturer;
    private int passengerNo, yearsInService;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public int getPassengerNo() {
        return passengerNo;
    }

    public void setPassengerNo(int passengerNo) {
        this.passengerNo = passengerNo;
    }

    public int getYearsInService() {
        return yearsInService;
    }

    public void setYearsInService(int yearsInService) {
        this.yearsInService = yearsInService;
    }

    public Plane(String name, String manufacturer, int passangerNo, int yearsInService) {
        this.name = name;

        this.manufacturer = manufacturer;
        this.passengerNo = passangerNo;
        this.yearsInService = yearsInService;
    }
}
