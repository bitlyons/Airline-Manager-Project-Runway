package com.github.bitlyons.runway;

import java.io.Serializable;

/**
 * Created by : Brendan Lyons (20066143)
 */
public class Employee implements Serializable{
    private String name;
    private int age, daysWithCompany;
    private double salary;
    private String role;

    public Employee(String name, int age, double salary, String role){
        this.name = name;
        this.age = age;
        this.salary = salary;
        this.daysWithCompany = 0; //initial value
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getDaysWithCompany() {
        return daysWithCompany;
    }

    public void setDaysWithCompany(int daysWithCompany) {
        this.daysWithCompany = daysWithCompany;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }


    public boolean isChiefPurser() {
        return false;
    }

    public boolean isPurser() {
        return false;
    }

    public double getFlightHours() {
        return 0.0;
    }

    @Override
    public String toString() {
        return "name :'" + name + '\'' +
                ", age :" + age +
                ", daysWithCompany :" + daysWithCompany +
                ", salary :" + salary;
    }
}
