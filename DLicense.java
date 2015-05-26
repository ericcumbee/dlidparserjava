package com.ericcumbee.dlid;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by ericcumbee on 12/18/14.
 */
public class DLicense {
    public static enum Sex{
        MALE,FEMALE,NONE
    }
    String firstName;
    String[] middleName;
    String lastName;
    String nameSuffix;
    String street;
    String city;
    String state;
    String country;
    String postal;
    Sex sex;
    String ssn;
    Date dateOfBirth;
    String issuerId;
    String issuerName;
    Date expiryDate;
    String vehicleClass;
    String restrictionCodes;
    String endorsementCodes;
    String customerId;
    String documentDiscriminator;
    Date issueDate;
    ArrayList<String> errors;

    public ArrayList<String> getErrors() {
        return errors;
    }

    public void setErrors(ArrayList<String> errors) {
        this.errors = errors;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String[] getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String[] middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getNameSuffix() {
        return nameSuffix;
    }

    public void setNameSuffix(String nameSuffix) {
        this.nameSuffix = nameSuffix;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPostal() {
        return postal;
    }

    public void setPostal(String postal) {
        this.postal = postal;
    }

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    public String getSsn() {
        return ssn;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getIssuerId() {
        return issuerId;
    }

    public void setIssuerId(String issuerId) {
        this.issuerId = issuerId;
    }

    public String getIssuerName() {
        return issuerName;
    }

    public void setIssuerName(String issuerName) {
        this.issuerName = issuerName;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getVehicleClass() {
        return vehicleClass;
    }

    public void setVehicleClass(String vehicleClass) {
        this.vehicleClass = vehicleClass;
    }

    public String getRestrictionCodes() {
        return restrictionCodes;
    }

    public void setRestrictionCodes(String restrictionCodes) {
        this.restrictionCodes = restrictionCodes;
    }

    public String getEndorsementCodes() {
        return endorsementCodes;
    }

    public void setEndorsementCodes(String endorsementCodes) {
        this.endorsementCodes = endorsementCodes;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getDocumentDiscriminator() {
        return documentDiscriminator;
    }

    public Date getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }

    public void setDocumentDiscriminator(String documentDiscriminator) {
        this.documentDiscriminator = documentDiscriminator;
    }


}
