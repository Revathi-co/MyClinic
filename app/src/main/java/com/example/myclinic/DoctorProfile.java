package com.example.myclinic;

public class DoctorProfile {

    public String name;
    public String age;
    public String email;
    public String gender;
    public String phone;
    public String dob;

    public DoctorProfile() {

    }

    public DoctorProfile(String name, String age, String email, String gender, String phone, String dob) {
        this.name = name;
        this.age = age;
        this.email = email;
        this.gender = gender;
        this.phone = phone;
        this.dob = dob;

    }

    public DoctorProfile(String name, String age, String email, String gender, String phone) {
        this.name = name;
        this.age = age;
        this.email = email;
        this.gender = gender;
        this.phone = phone;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public  String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public  String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

}

