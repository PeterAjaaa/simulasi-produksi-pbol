/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package simulasi.produksi.utility;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

/**
 *
 * @author Peter
 */
public class DBConnection {

    public Connection dbConnection;
    public Statement statement;
    public PreparedStatement preparedStatement;

    public DBConnection() {
        this.dbConnection = null;
    }

    public void bukaKoneksi() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            dbConnection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/simulasi-produksi?user=root&password=");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void closeConnection() {
        try {
            if (statement != null) {
                statement.close();
            }
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (dbConnection != null) {
                dbConnection.close();
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

}
