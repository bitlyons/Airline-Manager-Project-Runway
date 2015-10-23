package com.github.bitlyons.runway;

/**
 * Created by : Brendan Lyons (20066143)
 */
public class Attendant extends Employee{
    private boolean isChiefPurser, isPurser;

    public Attendant(String name, int age, double salary, boolean isChiefPurser, boolean isPurser) {
        super(name, age, salary, "Attendant");
        this.isChiefPurser = isChiefPurser;
        this.isPurser = isPurser;
    }

    @Override
    public String toString() {
        return  super.toString()+
                " Attendant{" +
                "isChiefPurser=" + isChiefPurser +
                ", isPurser=" + isPurser +
                "} " ;
    }
}
