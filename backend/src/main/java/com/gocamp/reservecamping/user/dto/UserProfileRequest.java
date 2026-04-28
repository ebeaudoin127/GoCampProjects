package com.gocamp.reservecamping.user.dto;

public class UserProfileRequest {

    private String firstname;
    private String lastname;
    private String phone;

    private String address;
    private String city;
    private String postalCode;

    private Long countryId;
    private Long provinceStateId;



    // GETTERS
    public String getFirstname() { return firstname; }
    public String getLastname() { return lastname; }
    public String getPhone() { return phone; }
    public String getAddress() { return address; }
    public String getCity() { return city; }
    public String getPostalCode() { return postalCode; }
    public Long getCountryId() { return countryId; }
    public Long getProvinceStateId() { return provinceStateId; }


    // SETTERS
    public void setFirstname(String firstname) { this.firstname = firstname; }
    public void setLastname(String lastname) { this.lastname = lastname; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setAddress(String address) { this.address = address; }
    public void setCity(String city) { this.city = city; }
    public void setPostalCode(String postalCode) { this.postalCode = postalCode; }
    public void setCountryId(Long countryId) { this.countryId = countryId; }
    public void setProvinceStateId(Long provinceStateId) { this.provinceStateId = provinceStateId; }
}