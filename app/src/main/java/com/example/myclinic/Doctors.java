package com.example.myclinic;

public class Doctors {

    private String doctorID;
    private String doctorName;
    private String specialization;
    private String experience;
    private String rating;
    private String qualification;
    private String certifications;
    private String location;
    private String fee;

    public Doctors() {
    }


    public Doctors(String doctorID, String doctorName, String specialization, String experience,  String qualification, String certifications, String location, String fee) {
        this.doctorID = doctorID;
        this.doctorName = doctorName;
        this.specialization = specialization;
        this.experience = experience;
        this.qualification = qualification;
        this.certifications = certifications;
        this.location = location;
        this.fee = fee;
    }

    public Doctors(String specialization, String experience, String qualification, String certifications, String location, String fee) {
        this.specialization = specialization;
        this.experience = experience;
        this.qualification = qualification;
        this.certifications = certifications;
        this.location = location;
        this.fee = fee;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public String getCertifications() {
        return certifications;
    }

    public void setCertifications(String certifications) {
        this.certifications = certifications;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getDoctorID() {
        return doctorID;
    }

    public void setDoctorID(String doctorID) {
        this.doctorID = doctorID;
    }
}


