package com.example.myclinic;

public class HospitalLocation {

    private String hospitalName;
    private String hospitalAddress;
    private String city;
    private String availableTime;

    HospitalLocation(){}

    HospitalLocation(String hospitalName, String hospitalAddress, String city, String availableTime) {
        this.hospitalName = hospitalName;
        this.hospitalAddress = hospitalAddress;
        this.city = city;
        this.availableTime = availableTime;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public String getHospitalAddress() {
        return hospitalAddress;
    }

    public void setHospitalAddress(String hospitalAddress) {
        this.hospitalAddress = hospitalAddress;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAvailableTime() {
        return availableTime;
    }

    public void setAvailableTime(String availableTime) {
        this.availableTime = availableTime;
    }
}
