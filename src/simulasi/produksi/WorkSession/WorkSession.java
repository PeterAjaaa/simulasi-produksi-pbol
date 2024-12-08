/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package simulasi.produksi.WorkSession;

/**
 *
 * @author Peter
 */
import java.time.LocalDateTime;
import java.util.List;

public class WorkSession {
    private String transactionId;
    private String productMade;
    private String startTime;
    private String endTime;
    private List<String> workersInvolved;
    private String status;

    public WorkSession() {}

    public WorkSession(String transactionId, String productMade, 
                       String startTime, String endTime, 
                       List<String> workersInvolved) {
        this.transactionId = transactionId;
        this.productMade = productMade;
        this.startTime = startTime;
        this.endTime = endTime;
        this.workersInvolved = workersInvolved;
        this.status = "COMPLETED";
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getProductMade() {
        return productMade;
    }

    public void setProductMade(String productMade) {
        this.productMade = productMade;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public List<String> getWorkersInvolved() {
        return workersInvolved;
    }

    public void setWorkersInvolved(List<String> workersInvolved) {
        this.workersInvolved = workersInvolved;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    @Override
    public String toString() {
        return "WorkSession{" +
                "transactionId='" + transactionId + '\'' +
                ", productMade='" + productMade + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", workersInvolved=" + workersInvolved +
                ", status='" + status + '\'' +
                '}';
    }
}