package be.ugent.iii.laborowsets.data;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IOfficeStorage {
    int countOffices();

    List<IOffice> allOffices();

    void addOffice(String officeCode, String addressline1, String addressline2, String city, String country,
                   String phone, String postalcode, String state, String territory);

    void changeOfficePhone(String code, String phone);

    void saveAll();

    Map<String, Set<String>> mailsCity();
}
