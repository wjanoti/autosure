package no.uio.ifi.autosure.models;

import java.io.Serializable;

public class Customer implements Serializable {

    private String name;
    private String address;
    private String dateOfBirth;
    private int fiscalNumber;
    private int policyNumber;

    public Customer(String name, String address, String dateOfBirth, int fiscalNumber, int policyNumber) {
        this.name = name;
        this.address = address;
        this.dateOfBirth = dateOfBirth;
        this.fiscalNumber = fiscalNumber;
        this.policyNumber = policyNumber;
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

}
