/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package simulasi.produksi.WorkSession;

/**
 *
 * @author Peter
 */
import java.util.List;
import simulasi.produksi.utility.DBConnection;

public class WorkSessionDB {
    private WorkSession data = new WorkSession();

    public WorkSession getWorkSessionModel() {
        return (data);
    }

    public void setWorkSessionModel(WorkSession newInst) {
        data = newInst;
    }
    
    public boolean insertData() {
        boolean berhasil = false;
        DBConnection con = new DBConnection();
        List<String> workersInvolved = getWorkSessionModel().getWorkersInvolved();
        String workersInvolvedString = String.join(",", workersInvolved);
        try {
            con.bukaKoneksi();
            con.preparedStatement = con.dbConnection.prepareStatement("insert into work_session (id, products_made, start_time, end_time, worker_involved, status) values (?,?,?,?,?,?)");
            con.preparedStatement.setString(1, getWorkSessionModel().getTransactionId());
            con.preparedStatement.setString(2, getWorkSessionModel().getProductMade());
            con.preparedStatement.setString(3, getWorkSessionModel().getStartTime());
            con.preparedStatement.setString(4, getWorkSessionModel().getEndTime());
            con.preparedStatement.setString(5,workersInvolvedString);
            con.preparedStatement.setString(6, getWorkSessionModel().getStatus());
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
}
