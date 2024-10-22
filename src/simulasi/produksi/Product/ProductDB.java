/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package simulasi.produksi.Product;

import java.sql.ResultSet;
import java.sql.SQLException;
import static javafx.collections.FXCollections.observableArrayList;
import javafx.collections.ObservableList;
import simulasi.produksi.utility.DBConnection;

/**
 *
 * @author Peter
 */
public class ProductDB {
//    This class fetches Product data from DB

    private Product data = new Product();

    public Product getProductModel() {
        return (data);
    }

    public void setProductModel(Product newInst) {
        data = newInst;
    }

    public ObservableList<Product> loadData() {
        try {
            ObservableList<Product> tableData = observableArrayList();
            DBConnection con = new DBConnection();
            con.bukaKoneksi();
            con.statement = con.dbConnection.createStatement();
            ResultSet result = con.statement.executeQuery("Select id, name, req_tool, type, prod_time, prod_rate from product");
            int i = 1;
            while (result.next()) {
                Product selectedData = new Product();
                selectedData.setID(result.getString("id"));
                selectedData.setName(result.getString("name"));
                selectedData.setRequiredTool(result.getString("req_tool"));
                selectedData.setProductType(result.getString("type"));
                selectedData.setProductionTime(result.getInt("prod_time"));
                selectedData.setProductionRate(result.getInt("prod_rate"));
                tableData.add(selectedData);
                i++;
            }
            con.closeConnection();
            return tableData;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public int validateData(String IDNum) {
        int val = 0;
        try {
            DBConnection con = new DBConnection();
            con.bukaKoneksi();
            con.statement = con.dbConnection.createStatement();
            ResultSet result = con.statement.executeQuery("Select count(*) as jml from product where id = '" + IDNum + "'");
            while (result.next()) {
                val = result.getInt("jml");
            }
            con.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return val;
    }

    public boolean insertData() {
        boolean berhasil = false;
        DBConnection con = new DBConnection();
        try {
            con.bukaKoneksi();
            con.preparedStatement = con.dbConnection.prepareStatement("insert into product (id, name, req_tool, type, prod_time, prod_rate) values (?,?,?,?,?,?)");
            con.preparedStatement.setString(1, getProductModel().getID());
            con.preparedStatement.setString(2, getProductModel().getName());
            con.preparedStatement.setString(3, getProductModel().getRequiredTool());
            con.preparedStatement.setString(4, getProductModel().getProductType());
            con.preparedStatement.setInt(5, getProductModel().getProductionTime());
            con.preparedStatement.setInt(6, getProductModel().getProductionRate());
            con.preparedStatement.executeUpdate();
            berhasil = true;
        } catch (Exception e) {
            e.printStackTrace();
            berhasil = false;
        } finally {
            con.closeConnection();
            return berhasil;
        }
    }

    public boolean deleteData(String IDNum) {
        boolean berhasil = false;
        DBConnection con = new DBConnection();
        try {
            con.bukaKoneksi();;
            con.preparedStatement = con.dbConnection.prepareStatement("delete from product where id  = ?");
            con.preparedStatement.setString(1, IDNum);
            con.preparedStatement.executeUpdate();
            berhasil = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            con.closeConnection();
            return berhasil;
        }
    }

    public boolean updateData() {
        boolean berhasil = false;
        DBConnection con = new DBConnection();
        int rowsAffected;
        try {
            con.bukaKoneksi();
            con.preparedStatement = con.dbConnection.prepareStatement("update product set name = ?, req_tool = ?, type = ?, prod_time = ?, prod_rate = ?  where  id = ?");
            con.preparedStatement.setString(1, getProductModel().getName());
            con.preparedStatement.setString(2, getProductModel().getRequiredTool());
            con.preparedStatement.setString(3, getProductModel().getProductType());
            con.preparedStatement.setInt(4, getProductModel().getProductionTime());
            con.preparedStatement.setInt(5, getProductModel().getProductionRate());
            con.preparedStatement.setString(6, getProductModel().getID());
            rowsAffected = con.preparedStatement.executeUpdate();
            berhasil = rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
            berhasil = false;
        } finally {
            con.closeConnection();
            return berhasil;
        }
    }

}
