package no.uio.ifi.autosure.models;

import java.io.Serializable;

public class Customer implements Serializable {

    private String username;
    private String name;
    private String address;
    private String dateOfBirth;
    private int fiscalNumber;
    private int policyNumber;

    public Customer(String username, String name, String address, String dateOfBirth, int fiscalNumber, int policyNumber) {
        this.username = username;
        this.name = name;
        this.address = address;
        this.dateOfBirth = dateOfBirth;
        this.fiscalNumber = fiscalNumber;
        this.policyNumber = policyNumber;
    }

    public String getUsername() {
        return username;
    }

    public int getPolicyNumber() {
        return policyNumber;
    }

    public String getName() {
        return name;
    }

    public int getFiscalNumber() {
        return fiscalNumber;
    }

    public String getAddress() {
        return address;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String toString() {
        return " [ Name : " + this.getName() + "| User name :" + this.getUsername() + "| Address: " + this.getAddress() + "| Date of birth: " + this.getDateOfBirth() + " |Fiscal Number : " + Integer.toString(this.getFiscalNumber()) + " |Policy Number : " + Integer.toString(this.getPolicyNumber()) + "]";
    }
}
