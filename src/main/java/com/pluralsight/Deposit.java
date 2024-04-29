package com.pluralsight;

public class Deposit extends Transaction{
    private String date;
    private String time;
    private String driscription;
    private String vendor;
    private double amount;

    public Deposit(String date, String time, String discription, String vendor, double amount) {
        super(date, time, discription, vendor, amount);
    }



}
