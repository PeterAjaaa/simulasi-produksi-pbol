/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package simulasi.produksi.Tool;

import java.sql.ResultSet;
import java.sql.SQLException;
import static javafx.collections.FXCollections.observableArrayList;
import javafx.collections.ObservableList;
import simulasi.produksi.utility.DBConnection;

/**
 *
 * @author Peter
 */
public class ToolDB {
//    This class fetches Tool data from DB

    private Tool data = new Tool();

    public Tool getToolModel() {
        return (data);
    }

    public void setToolmodel(Tool newInst) {
        data = newInst;
    }

    public ObservableList<Tool> loadData() {
        try {
            ObservableList<Tool> tableData = observableArrayList();
            DBConnection con = new DBConnection();
            con.bukaKoneksi();
            con.statement = con.dbConnection.createStatement();
            ResultSet result = con.statement.executeQuery("Select id, name, type, availability, efficiency from tool");
            int i = 1;
            while (result.next()) {
                Tool selectedData = new Tool();
                selectedData.setID(result.getString("id"));
                selectedData.setName(result.getString("name"));
                selectedData.setType(result.getString("type"));
                selectedData.setAvailability(result.getString("availability"));
                selectedData.setEfficiency(result.getInt("efficiency"));
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
            ResultSet result = con.statement.executeQuery("Select count(*) as jml from tool where id = '" + IDNum + "'");
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
            con.preparedStatement = con.dbConnection.prepareStatement("insert into tool (id, name, type, availability, efficiency) values (?,?,?,?,?)");
            con.preparedStatement.setString(1, getToolModel().getID());
            con.preparedStatement.setString(2, getToolModel().getName());
            con.preparedStatement.setString(3, getToolModel().getType());
            con.preparedStatement.setString(4, getToolModel().getAvailability());
            con.preparedStatement.setInt(5, getToolModel().getEfficiency());
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
            con.preparedStatement = con.dbConnection.prepareStatement("delete from tool where id  = ?");
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
            con.preparedStatement = con.dbConnection.prepareStatement("update tool set name = ?, type = ?, availability = ?, efficiency = ?  where  id = ?");
            con.preparedStatement.setString(1, getToolModel().getName());
            con.preparedStatement.setString(2, getToolModel().getType());
            con.preparedStatement.setString(3, getToolModel().getAvailability());
            con.preparedStatement.setInt(4, getToolModel().getEfficiency());
            con.preparedStatement.setString(5, getToolModel().getID());
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
