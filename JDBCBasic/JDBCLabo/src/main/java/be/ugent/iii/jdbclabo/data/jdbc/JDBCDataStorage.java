package be.ugent.iii.jdbclabo.data.jdbc;

import be.ugent.iii.jdbclabo.data.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
@PropertySource("classpath:databankconstanten.properties")
public class JDBCDataStorage implements IDataStorage {

    @Value("${connectiestring}")
    private String connString;
    @Value("${username}")
    private String login;
    @Value("${password}")
    private String wachtwoord;


    // kolommen producten
    @Value("${prod_code}")
    private String prodCode;
    @Value("${prod_name}")
    private String prodName;
    @Value("${prod_line}")
    private String prodLine;
    @Value("${prod_scale}")
    private String prodScalee;
    @Value("${prod_vendor}")
    private String prodVendor;
    @Value("${prod_description}")
    private String prodDescript;
    @Value("${prod_stock}")
    private String prodStock;
    @Value("${prod_price}")
    private String prodPrice;
    @Value("${prod_msrp}")
    private String prodMsrp;

    // kolommen customer
    @Value("${klant_number}")
    private String klantNumber;
    @Value("${klant_name}")
    private String klantName;
    @Value("${klant_lname}")
    private String klantLName;
    @Value("${klant_fname}")
    private String klantFName;
    @Value("${klant_phone}")
    private String klantPhone;
    @Value("${klant_address1}")
    private String klantAddress1;
    @Value("${klant_address2}")
    private String klantAddress2;
    @Value("${klant_city}")
    private String klantCity;
    @Value("${klant_state}")
    private String klantState;
    @Value("${klant_pcode}")
    private String klantPCode;
    @Value("${klant_country}")
    private String klantCountry;
    @Value("${klant_repnumber}")
    private String klantRepNumber;
    @Value("${klant_climit}")
    private String klantCLimit;

    // kolommen orders
    @Value("${order_number}")
    private String orderNumber;
    @Value("${order_date}")
    private String orderDate;
    @Value("${order_rdate}")
    private String orderRDate;
    @Value("${order_sdate}")
    private String orderSDate;
    @Value("${order_status}")
    private String orderStatus;
    @Value("${order_comments}")
    private String orderComments;
    @Value("${order_cnumber}")
    private String orderCNumber;

    // SQL-opdrachten
    @Value("${insert_order}")
    private String insertOrder;
    @Value("${insert_orderdetail}")
    private String insertOrderDetail;
    @Value("${select_products}")
    private String selectProducts;
    @Value("${select_customers}")
    private String selectCustomers;
    @Value("${select_orders_klant}")
    private String selectOrdersKlant;
    @Value("${select_max_customer_number}")
    private String selectMaxCustomerNumber;
    @Value("${select_max_order_number}")
    private String selectMaxOrderNumber;
    @Value("${insert_customer}")
    private String insertCustomer;
    @Value(("${update_customer}"))
    private String updateCustomer;
    @Value("${delete_customer}")
    private String deleteCustomer;
    @Value("${procedure_get_total}")
    private String procGetTotal;

    // fouten
    @Value("${fout_products}")
    private String foutProducts;
    @Value("${fout_customers}")
    private String foutCustomers;
    @Value("${fout_orders}")
    private String foutOrders;
    @Value("${fout_customer_number")
    private String foutCustomerNumber;
    @Value("${fout_order_number}")
    private String foutOrderNumber;
    @Value("${fout_add_order}")
    private String foutAddOrder;
    @Value("${fout_insert_customer}")
    private String foutInsertCustomer;
    @Value("${fout_update_customer}")
    private String foutUpdateCustomer;
    @Value("${fout_delete_customer}")
    private String foutDeleteCustomer;
    @Value("${fout_get_total}")
    private String foutGetTotal;

    private DataSource dataSource;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    //Maak product
    private IProduct createProduct(ResultSet rs) throws SQLException {

        return new Product(rs.getString(prodCode),
                rs.getString(prodName),
                rs.getString(prodLine),
                rs.getString(prodScalee),
                rs.getString(prodVendor),
                rs.getString(prodDescript),
                rs.getInt(prodStock),
                rs.getDouble(prodPrice),
                rs.getDouble(prodMsrp));
    }

    //Maak customer
    private ICustomer createCustomer(ResultSet rs) throws SQLException {

        return new Customer(rs.getInt(klantNumber),
                rs.getString(klantName),
                rs.getString(klantLName),
                rs.getString(klantFName),
                rs.getString(klantPhone),
                rs.getString(klantAddress1),
                rs.getString(klantAddress2),
                rs.getString(klantCity),
                rs.getString(klantState),
                rs.getString(klantPCode),
                rs.getString(klantCountry),
                rs.getInt(klantRepNumber),
                rs.getDouble(klantCLimit));
    }

    private IOrder createOrder(ResultSet rs) throws SQLException {
        return new Order(rs.getInt(orderNumber),
                rs.getDate(orderDate),
                rs.getDate(orderRDate),
                rs.getDate(orderSDate),
                rs.getString(orderStatus),
                rs.getString(orderComments),
                rs.getInt(orderCNumber),
                null);
    }

    private void addOrder(Connection conn, IOrder order) throws SQLException {
        try(PreparedStatement stmt = conn.prepareStatement(insertOrder)) {
            stmt.setInt(1, order.getNumber());
            stmt.setDate(2, new java.sql.Date(order.getOrdered().getTime()));
            stmt.setDate(3, new java.sql.Date(order.getRequired().getTime()));
            stmt.setNull(4, Types.DATE);
            stmt.setString(5, order.getStatus());
            if (order.getComments() != null && !order.getComments().equals("")) {
                stmt.setString(6, order.getComments());
            } else {
                stmt.setNull(6, Types.VARCHAR);
            }
            stmt.setInt(7, order.getCustomerNumber());
            stmt.executeUpdate();
        }
    }

    private void addOrderDetail(Connection conn, IOrderDetail orderDetail) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(insertOrderDetail)) {
            stmt.setInt(1, orderDetail.getOrderNumber());
            stmt.setString(2, orderDetail.getProductCode());
            stmt.setInt(3, orderDetail.getQuantity());
            stmt.setDouble(4, orderDetail.getPrice());
            stmt.setInt(5, orderDetail.getOrderLineNumber());
            stmt.executeUpdate();
        }
    }

    private void addCustomer(Connection conn, ICustomer iCustomer) throws SQLException {
        try(PreparedStatement stmt = conn.prepareStatement(insertCustomer)) {
            stmt.setInt(1, iCustomer.getCustomerNumber());
            stmt.setString(2, iCustomer.getCustomerName());
            stmt.setString(3, iCustomer.getContactLastName());
            stmt.setString(4, iCustomer.getContactFirstName());
            stmt.setString(5, iCustomer.getPhone());
            stmt.setString(6, iCustomer.getAddressLine1());
            if (iCustomer.getAddressLine2() != null && !iCustomer.getAddressLine2().equals("")) {
                stmt.setString(7, iCustomer.getAddressLine2());
            } else {
                stmt.setNull(7, Types.VARCHAR);
            }
            stmt.setString(8, iCustomer.getCity());
            if (iCustomer.getState() != null && !iCustomer.getState().equals("")) {
                stmt.setString(9, iCustomer.getState());
            } else {
                stmt.setNull(9, Types.VARCHAR);
            }
            stmt.setString(10, iCustomer.getPostalCode());
            stmt.setString(11, iCustomer.getCountry());
            if (iCustomer.getSalesRepEmployeeNumber() != 0) {
                stmt.setInt(12, iCustomer.getSalesRepEmployeeNumber());
            } else {
                stmt.setNull(12, Types.INTEGER);
            }
            if (iCustomer.getCreditLimit() != 0) {
                stmt.setDouble(13, iCustomer.getCreditLimit());
            } else {
                stmt.setNull(13, Types.DOUBLE);
            }
            stmt.executeUpdate();
        }
    }

    private Connection geefVerbinding() throws SQLException {
        return dataSource.getConnection();
    }

    @Override
    public List<IProduct> getProducts() throws DataExceptie {
        List<IProduct> products;
        try {
            try (Connection conn = geefVerbinding();
                 Statement stmt = conn.createStatement()) {
                System.out.println(conn.getClass());
                System.out.println(stmt.getClass());
                ResultSet rs = stmt.executeQuery(selectProducts);
                System.out.println(rs.getClass());
                products = new ArrayList<>();
                while (rs.next()) {
                    products.add(createProduct(rs));
                }
            }
        } catch (SQLException ex) {
            throw new DataExceptie(foutProducts);
        }
        return products;
    }

    @Override
    public List<ICustomer> getCustomers() throws DataExceptie {
        List<ICustomer> customers;
        try {
            try(Connection conn = geefVerbinding();
               Statement stmt = conn.createStatement()) {
                ResultSet rs = stmt.executeQuery(selectCustomers);
                customers = new ArrayList<>();
                while (rs.next()) {
                    customers.add(createCustomer(rs));
                }
            }
        } catch (SQLException ex) {
            throw new DataExceptie(foutCustomers);
        }
        return customers;
    }

    // List of orders without the details
    @Override
    public List<IOrder> getOrders(int customerNumber) throws DataExceptie {
        List<IOrder> iOrders;
        try {
            try(Connection conn = geefVerbinding();
               PreparedStatement stmt = conn.prepareStatement(selectOrdersKlant)) {
                stmt.setInt(1, customerNumber);
                ResultSet rs = stmt.executeQuery();
                iOrders = new ArrayList<>();
                while (rs.next()) {
                    iOrders.add(createOrder(rs));
                }
            }
        } catch (SQLException ex) {
            throw new DataExceptie(foutOrders);
        }
        return iOrders;
    }

    @Override
    public int maxCustomerNumber() throws DataExceptie {
        return duplicateCode(selectMaxCustomerNumber, foutCustomerNumber, foutCustomers);
    }

    @Override
    public int maxOrderNumber() throws DataExceptie {
        return duplicateCode(selectMaxOrderNumber, foutOrderNumber, foutOrderNumber);
    }

    private int duplicateCode(String selectMaxOrderNumber, String foutOrderNumber, String foutOrderNumber2) throws DataExceptie {
        try {
            try(Connection conn = geefVerbinding();
                Statement stmt = conn.createStatement()) {
                ResultSet rs = stmt.executeQuery(selectMaxOrderNumber);
                if (rs.next()) {
                    return rs.getInt(1);
                } else {
                    throw new DataExceptie(foutOrderNumber);
                }
            }
        } catch (SQLException ex) {
            throw new DataExceptie(foutOrderNumber2);
        }
    }

    @Override
    public void addOrder(IOrder order) throws DataExceptie {
        try {
            Connection conn = geefVerbinding();
            try {
                conn.setAutoCommit(false);
                addOrder(conn, order);
                for (IOrderDetail iOrderDetail : order.getDetails()) {
                    addOrderDetail(conn, iOrderDetail);
                }
                conn.commit();
            } catch (SQLException se) {
                conn.rollback();
                throw new DataExceptie(foutAddOrder);
            } finally {
                conn.setAutoCommit(true);
                conn.close();
            }
        } catch (SQLException ex) {
            throw new DataExceptie(foutAddOrder);
        }
    }

    @Override
    public void addCustomer(ICustomer customer) throws DataExceptie {
        try {
            Connection conn = geefVerbinding();
            try {
                conn.setAutoCommit(false);
                addCustomer(conn, customer);
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw new DataExceptie(foutInsertCustomer);
            }
        } catch (SQLException ex) {
            throw new DataExceptie(foutInsertCustomer);
        }
    }

    @Override
    public void modifyCustomer(ICustomer customer) throws DataExceptie {
        try {
            try(Connection conn = geefVerbinding(); PreparedStatement stmt = conn.prepareStatement(updateCustomer)) {
                stmt.setString(1,customer.getCustomerName());
                stmt.setString(2,customer.getContactLastName());
                stmt.setString(3,customer.getContactFirstName());
                stmt.setString(4,customer.getPhone());
                stmt.setString(5,customer.getAddressLine1());
                if(customer.getAddressLine2() != null && !customer.getAddressLine2().equals("")){
                    stmt.setString(6,customer.getAddressLine2());
                }else{
                    stmt.setNull(6,java.sql.Types.VARCHAR);
                }
                stmt.setString(7,customer.getCity());
                if(customer.getState() != null && !customer.getState().equals("")){
                    stmt.setString(8,customer.getState());
                }else{
                    stmt.setNull(8,java.sql.Types.VARCHAR);
                }
                if(customer.getPostalCode() != null && !customer.getPostalCode().equals("")){
                    stmt.setString(9,customer.getPostalCode());
                }else{
                    stmt.setNull(9,java.sql.Types.VARCHAR);
                }
                stmt.setString(10,customer.getCountry());
                if(customer.getSalesRepEmployeeNumber() != 0){
                    stmt.setInt(11,customer.getSalesRepEmployeeNumber());
                }else{
                    stmt.setNull(11,java.sql.Types.INTEGER);
                }
                if(customer.getCreditLimit() != 0){
                    stmt.setDouble(12,customer.getCreditLimit());
                }else{
                    stmt.setNull(12,java.sql.Types.DOUBLE);
                }
                stmt.setInt(13,customer.getCustomerNumber());
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataExceptie(foutUpdateCustomer);
        }
    }

    @Override
    public void deleteCustomer(int customerNumber) throws DataExceptie {
        try {
            try(Connection conn = geefVerbinding();
                //parameter voor prepaired statement vind je in properties file (hier databankenconstanten.properties) ook de type ervan(vb voor call) staat erbij
            PreparedStatement stmt = conn.prepareStatement(deleteCustomer)) { //als je een parameter hebt dan prepaired statement
                stmt.setObject(1, customerNumber);
                stmt.executeUpdate();
            }
        } catch (SQLException ex) {
            throw new DataExceptie(deleteCustomer);
        }
    }

    @Override
    public double getTotal(int customerNumber) throws DataExceptie {
        double total;
        try {
            try(Connection conn = geefVerbinding();
            CallableStatement stmt = conn.prepareCall(procGetTotal)) {
                stmt.setInt(2, customerNumber);
                stmt.registerOutParameter(1, Types.DOUBLE);
                stmt.executeUpdate();
                total = stmt.getDouble(1);
            }
        } catch (SQLException e) {
            throw new DataExceptie(foutGetTotal);
        }
        return total;
    }
}