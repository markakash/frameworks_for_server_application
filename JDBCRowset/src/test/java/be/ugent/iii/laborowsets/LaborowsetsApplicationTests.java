package be.ugent.iii.laborowsets;


import be.ugent.iii.laborowsets.data.IOffice;
import be.ugent.iii.laborowsets.data.IOfficeStorage;
import be.ugent.iii.laborowsets.data.jdbc.Office;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class LaborowsetsApplicationTests {

    @Autowired
    IOfficeStorage storage;

    @Test
    void countOffices() {
        assertThat(storage.countOffices()).isGreaterThan(0);
    }

    @Test
    void getOffices() {
        assertThat(storage.allOffices()).size().isGreaterThan(0);
    }

    @Test
    void addOffice() {
        int sizeBefore = storage.countOffices();
        String addressline1 = "new address";
        String addressline2 = "second part";
        String city = "new city";
        String country = "new country";
        String phone = "new phone";
        String postalcode = "new postalcode";
        String state = "new state";
        String territory = "";
        List<IOffice> officesNew = new ArrayList<>();
        for (int i = 1; i < 4; i++) {
            String code = Integer.toString(sizeBefore + i);
            Office office = new Office(code, city, phone, addressline1, addressline2, state,
                    country, postalcode, territory);
            officesNew.add(office);
            storage.addOffice(code, addressline1, addressline2, city, country, phone, postalcode, state, territory);
        }
        List<IOffice> officesAfter = storage.allOffices();
        assertThat(officesAfter).containsAll(officesNew);
        assertThat(storage.countOffices()).isEqualTo(sizeBefore);
        storage.saveAll();
        assertThat(storage.allOffices()).containsAll(officesNew);
        assertThat(storage.countOffices()).isEqualTo(sizeBefore + 3);
    }

    @Test
    void changePhone() {
        int sizeBefore = storage.countOffices();
        String addressline1 = "new address";
        String addressline2 = "second part";
        String city = "new city";
        String country = "new country";
        String phone = "new phone";
        String postalcode = "new postalcode";
        String state = "new state";
        String territory = "";
        String code = Integer.toString(sizeBefore + 1);
        Office officeBefore = new Office(code, city, phone, addressline1, addressline2, state,
                country, postalcode, territory);
        storage.addOffice(code, addressline1, addressline2, city, country, phone, postalcode, state, territory);
        storage.saveAll();
        assertThat(storage.allOffices()).contains(officeBefore);
        phone = "changed phone";
        storage.changeOfficePhone(code, phone);
        storage.saveAll();
        Office officeAfter = new Office(code, city, phone, addressline1, addressline2, state,
                country, postalcode, territory);
        List<IOffice> offices = storage.allOffices();
        assertThat(offices).contains(officeAfter).doesNotContain(officeBefore);
    }

    @Test
    void emailsCity() {
        Map<String, Set<String>> mails = storage.mailsCity();
        assertThat(mails).containsKeys("Tokyo", "London").size().isGreaterThan(0);
    }

}
