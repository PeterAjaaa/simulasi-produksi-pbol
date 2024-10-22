/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package simulasi.produksi.Worker;

import java.sql.ResultSet;
import java.sql.SQLException;
import static javafx.collections.FXCollections.observableArrayList;
import javafx.collections.ObservableList;
import simulasi.produksi.utility.DBConnection;

/**
 *
 * @author Peter
 */
public class WorkerDB {
//    This class fetches Tool data from DB

    private Worker data = new Worker();

    public Worker getWorkerModel() {
        return (data);
    }

    public void setWorkerModel(Worker newInst) {
        data = newInst;
    }

    public ObservableList<Worker> loadData() {
        try {
            ObservableList<Worker> tableData = observableArrayList();
            DBConnection con = new DBConnection();
            con.bukaKoneksi();
            con.statement = con.dbConnection.createStatement();
            ResultSet result = con.statement.executeQuery("Select id, name, proficiency, work_hours, availability from worker");
            int i = 1;
            while (result.next()) {
                Worker selectedData = new Worker();
                selectedData.setID(result.getString("id"));
                selectedData.setName(result.getString("name"));
                selectedData.setProficiency(result.getString("proficiency"));
                selectedData.setWorkHours(result.getInt("work_hours"));
                selectedData.setAvailability(result.getString("availability"));
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
            ResultSet result = con.statement.executeQuery("Select count(*) as jml from worker where id = '" + IDNum + "'");
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
            con.preparedStatement = con.dbConnection.prepareStatement("insert into worker (id, name, proficiency, work_hours, availability) values (?,?,?,?,?)");
            con.preparedStatement.setString(1, getWorkerModel().getID());
            con.preparedStatement.setString(2, getWorkerModel().getName());
            con.preparedStatement.setString(3, getWorkerModel().getProficiency());
            con.preparedStatement.setInt(4, getWorkerModel().getWorkHours());
            con.preparedStatement.setString(5, getWorkerModel().getAvailability());
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
            con.preparedStatement = con.dbConnection.prepareStatement("delete from worker where id  = ?");
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
            con.preparedStatement = con.dbConnection.prepareStatement("update worker set name = ?, proficiency = ?, work_hours = ?, availability = ?  where  id = ?");
            con.preparedStatement.setString(1, getWorkerModel().getName());
            con.preparedStatement.setString(2, getWorkerModel().getProficiency());
            con.preparedStatement.setInt(3, getWorkerModel().getWorkHours());
            con.preparedStatement.setString(4, getWorkerModel().getAvailability());
            con.preparedStatement.setString(5, getWorkerModel().getID());
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
