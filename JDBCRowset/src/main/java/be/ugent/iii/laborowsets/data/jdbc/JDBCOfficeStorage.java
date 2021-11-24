package be.ugent.iii.laborowsets.data.jdbc;

import be.ugent.iii.laborowsets.data.IOffice;
import be.ugent.iii.laborowsets.data.IOfficeStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.JoinRowSet;
import javax.sql.rowset.RowSetFactory;
import javax.sql.rowset.RowSetProvider;
import javax.sql.rowset.spi.SyncProviderException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
@PropertySource("classpath:sqlStatements.properties")
public class JDBCOfficeStorage implements IOfficeStorage {

    @Value("${sql.countoffices}")
    private String qCountOffices;
    @Value("${sql.selectoffices}")
    private String qSelectOffices;
    @Value("${sql.selectemployees}")
    private String qSelectEmployees;

    @Value("${sql.officecode}")
    String officecode;
    @Value("${sql.city}")
    String city;
    @Value("${sql.phone}")
    String phone;
    @Value("${sql.addressline1}")
    String addressline1;
    @Value("${sql.addressline2}")
    String addressline2;
    @Value("${sql.state}")
    String state;
    @Value("${sql.country}")
    String country;
    @Value("${sql.postalcode}")
    String postalcode;
    @Value("${sql.territory}")
    String territory;

    /**
     * We use RowSet when u want to loop multiple times over your data. Also when u have to join multiple tables, then u need to use JoinRowset.
     * But JoinRowset requites cachedRowset so u will have to create that.
     */

    private DataSource dataSource;
    private CachedRowSet officesRowSet;
    private CachedRowSet employeesRowSet;

    private Connection connection() throws SQLException {
        return dataSource.getConnection();
    }

    private Office createOffice(CachedRowSet rs) throws SQLException{
        return new Office(rs.getString(officecode),
                rs.getString(city),
                rs.getString(phone),
                rs.getString(addressline1),
                rs.getString(addressline2),
                rs.getString(state),
                rs.getString(country),
                rs.getString(postalcode),
                rs.getString(territory));
    }

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        try {
            RowSetFactory rowSetFactory = RowSetProvider.newFactory();
            Connection connection = dataSource.getConnection();
            connection.setAutoCommit(false);

            //offices
            officesRowSet = rowSetFactory.createCachedRowSet();
            officesRowSet.setCommand(qSelectOffices);
            int[] id = {1}; //array want meerdere columns kunnen primary key bevatten
            officesRowSet.setKeyColumns(id);
            officesRowSet.execute(connection);

            //employees
            employeesRowSet = rowSetFactory.createCachedRowSet();
            employeesRowSet.setCommand(qSelectEmployees);
            employeesRowSet.setKeyColumns(id);
            employeesRowSet.execute(connection);
        } catch (SQLException e) {
            Logger.getLogger(JDBCOfficeStorage.class.getName()).log(Level.SEVERE, "aanmaken RowSet offices mislukt");
        }
    }

    @Override
    public int countOffices() {
        try (Connection conn = connection()) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(qCountOffices);
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            Logger.getLogger(JDBCOfficeStorage.class.getName()).log(Level.SEVERE, "opzetten verbinding mislukt");
        }
        return 0;
    }

    @Override
    public List<IOffice> allOffices() {
        List<IOffice> iOffices = new ArrayList<>();
        try {
            //beforeFirst makes the "pointer" point to first column.
            officesRowSet.beforeFirst();
            while (officesRowSet.next()) {
                iOffices.add(createOffice(officesRowSet));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return iOffices;
    }

    //Here we are getting all the parameters, where as in the other exercise we got an object of the type ICustomer. So we needed to create a new customer
    //Also this rowset stuff works a bit like Pointers from C.
    @Override
    public void addOffice(String officeCode, String addressline1, String addressline2, String city, String country, String phone, String postalcode, String state, String territory) {
        try {
            officesRowSet.moveToInsertRow();
            officesRowSet.updateString(this.officecode, officeCode);
            officesRowSet.updateString(this.addressline1, addressline1);
            officesRowSet.updateString(this.addressline2, addressline2);
            officesRowSet.updateString(this.city, city);
            officesRowSet.updateString(this.country, country);
            officesRowSet.updateString(this.phone, phone);
            officesRowSet.updateString(this.postalcode, postalcode);
            officesRowSet.updateString(this.state, state);
            officesRowSet.updateString(this.territory, territory);
            officesRowSet.insertRow();
            officesRowSet.moveToCurrentRow();
        } catch (SQLException ex) {
            Logger.getLogger(JDBCOfficeStorage.class.getName()).log(Level.SEVERE, "office toevoegen mislukt", ex);
        }
    }

    @Override
    public void changeOfficePhone(String code, String phone) {
        try {
            officesRowSet.beforeFirst();
            while (officesRowSet.next()) {
                if (officesRowSet.getString(this.officecode).equals(code)) {
                    officesRowSet.updateString(this.phone, phone);
                    officesRowSet.updateRow();
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(JDBCOfficeStorage.class.getName()).log(Level.SEVERE, "aanpassen telefoon mislukt", ex);
        }
    }

    @Override
    public void saveAll() {
        try {
            officesRowSet.acceptChanges();
        } catch (SyncProviderException e) {
            Logger.getLogger(JDBCOfficeStorage.class.getName()).log(Level.SEVERE, "bewaren mislukt", e);
        }
    }

    @Override
    /**
     * Geeft alle mailadressen van werknemers verzameld per stad waarin hun kantoor ligt terug.
     * So we need to connect 2 rowset where they have the same column name. In this case was officeCode.
     */
    public Map<String, Set<String>> mailsCity() {
        Map<String, Set<String>> mails = new HashMap<>();
        try {
            JoinRowSet jrs = RowSetProvider.newFactory().createJoinRowSet();
            jrs.addRowSet(officesRowSet, "officecode"); //join officerowset columnname officeCode with that of employeeRowSet
            jrs.addRowSet(employeesRowSet, "officeCode");
            jrs.beforeFirst();

            while (jrs.next()) {
                String email = jrs.getString("email");
                String city = jrs.getString("city");
                if (!mails.containsKey(city)) {
                    mails.put(city, new HashSet<>());
                } else {
                    mails.get(city).add(email);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(JDBCOfficeStorage.class.getName()).log(Level.SEVERE, "opvragen mails mislukt", ex);
        }
        return mails;
    }
}