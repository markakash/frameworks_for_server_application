package be.ugent.iii.laborowsets.data.jdbc;

import be.ugent.iii.laborowsets.data.IOffice;

import java.util.Objects;

public class Office implements IOffice {

    String officecode, city, phone, addressline1, addressline2, state, country, postalcode, territory;

    public Office(String officecode, String city, String phone, String addressline1, String addressline2, String state, String country, String postalcode, String territory) {
        this.officecode = officecode;
        this.city = city;
        this.phone = phone;
        this.addressline1 = addressline1;
        this.addressline2 = addressline2;
        this.state = state;
        this.country = country;
        this.postalcode = postalcode;
        this.territory = territory;
    }

    @Override
    public String getOfficecode() {
        return officecode;
    }

    @Override
    public String getCity() {
        return city;
    }

    @Override
    public String getPhone() {
        return phone;
    }

    @Override
    public String getAddressline1() {
        return addressline1;
    }

    @Override
    public String getAddressline2() {
        return addressline2;
    }

    @Override
    public String getState() {
        return state;
    }

    @Override
    public String getCountry() {
        return country;
    }

    @Override
    public String getPostalcode() {
        return postalcode;
    }

    @Override
    public String getTerritory() {
        return territory;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Office office = (Office) o;
        return Objects.equals(officecode, office.officecode) &&
                Objects.equals(city, office.city) &&
                Objects.equals(phone, office.phone) &&
                Objects.equals(addressline1, office.addressline1) &&
                Objects.equals(addressline2, office.addressline2) &&
                Objects.equals(state, office.state) &&
                Objects.equals(country, office.country) &&
                Objects.equals(postalcode, office.postalcode) &&
                Objects.equals(territory, office.territory);
    }

    @Override
    public int hashCode() {
        return Objects.hash(officecode, city, phone, addressline1, addressline2, state, country, postalcode, territory);
    }
}
