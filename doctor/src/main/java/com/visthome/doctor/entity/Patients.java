package com.visthome.doctor.entity;

import java.util.Date;

public class Patients {
    String patientUid;
    String doctorName;
    String doctorUid;
    String doctorEmail;
    String uploadId;
    String firstName;
    String middleName;
    String lastName;
    String address;
    String job;
    String phoneNo;
    String diseases;
    String timestamp;
    String patientEmail;
    String patientPass;
    Date date;
    String customDate;
    String customTime;

    public Patients() {
    }

    public Patients(String patientUid, String doctorName, String doctorUid, String doctorEmail, String uploadId, String firstName, String middleName, String lastName, String address, String job, String phoneNo, String diseases, String timestamp, String patientEmail, String patientPass, Date date, String customDate, String customTime) {
        this.patientUid = patientUid;
        this.doctorName = doctorName;
        this.doctorUid = doctorUid;
        this.doctorEmail = doctorEmail;
        this.uploadId = uploadId;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.address = address;
        this.job = job;
        this.phoneNo = phoneNo;
        this.diseases = diseases;
        this.timestamp = timestamp;
        this.patientEmail = patientEmail;
        this.patientPass = patientPass;
        this.date = date;
        this.customDate = customDate;
        this.customTime = customTime;
    }

    public String getPatientUid() {
        return patientUid;
    }

    public void setPatientUid(String patientUid) {
        this.patientUid = patientUid;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getDoctorUid() {
        return doctorUid;
    }

    public void setDoctorUid(String doctorUid) {
        this.doctorUid = doctorUid;
    }

    public String getDoctorEmail() {
        return doctorEmail;
    }

    public void setDoctorEmail(String doctorEmail) {
        this.doctorEmail = doctorEmail;
    }

    public String getUploadId() {
        return uploadId;
    }

    public void setUploadId(String uploadId) {
        this.uploadId = uploadId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getDiseases() {
        return diseases;
    }

    public void setDiseases(String diseases) {
        this.diseases = diseases;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getPatientEmail() {
        return patientEmail;
    }

    public void setPatientEmail(String patientEmail) {
        this.patientEmail = patientEmail;
    }

    public String getPatientPass() {
        return patientPass;
    }

    public void setPatientPass(String patientPass) {
        this.patientPass = patientPass;
    }

    public String getCustomDate() {
        return customDate;
    }

    public void setCustomDate(String customDate) {
        this.customDate = customDate;
    }

    public String getCustomTime() {
        return customTime;
    }

    public void setCustomTime(String customTime) {
        this.customTime = customTime;
    }
}
