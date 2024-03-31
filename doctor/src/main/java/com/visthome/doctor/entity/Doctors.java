package com.visthome.doctor.entity;

public class Doctors {
    String DoctorName;
    String PhoneNo;
    String email;
    String Password;
    String doctorUid;
    String token;

    public Doctors() {
    }

    public Doctors(String doctorName, String phoneNo, String email, String password, String doctorUid, String token) {
        DoctorName = doctorName;
        PhoneNo = phoneNo;
        this.email = email;
        Password = password;
        this.doctorUid = doctorUid;
        this.token = token;
    }

    public String getDoctorName() {
        return DoctorName;
    }

    public void setDoctorName(String doctorName) {
        DoctorName = doctorName;
    }

    public String getPhoneNo() {
        return PhoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        PhoneNo = phoneNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getDoctorUid() {
        return doctorUid;
    }

    public void setDoctorUid(String doctorUid) {
        this.doctorUid = doctorUid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
