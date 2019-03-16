package no.uio.ifi.autosure.models;

import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

public class Claim extends ClaimItem implements Serializable {

    private final String submissionDate;
    private final String ocurrenceDate;
    private final String plate;
    private final String description;
    private ClaimStatus status;
    private final List<ClaimMessage> claimMessageList;

    public Claim(int claimId, String claimTitle, String submissionDate, String occurrenceDate, String plate,
                       String description, String status, List<ClaimMessage> msgList) {
        super(claimId, claimTitle);
        this.submissionDate = submissionDate;
        ocurrenceDate = occurrenceDate;
        this.plate = plate;
        this.description = description;
        setStatus(status);
        claimMessageList = msgList;
    }

    public Claim(int claimId, String claimTitle, String submissionDate, String occurrenceDate, String plate, String description, String status) {
        this(claimId, claimTitle, submissionDate, occurrenceDate, plate, description, status, new ArrayList<ClaimMessage>());
    }

    public String getSubmissionDate() {
        return submissionDate;
    }

    public String getOccurrenceDate() {
        return ocurrenceDate;
    }

    public String getPlate() {
        return plate;
    }

    public String getDescription() {
        return description;
    }

    public ClaimStatus getStatus() {
        return status;
    }

    public void setStatus(String status) {
        switch (status.toLowerCase()) {
            case "pending":
                this.status = ClaimStatus.PENDING;
                break;
            case "accepted":
                this.status = ClaimStatus.ACCEPTED;
                break;
            case "denied":
                this.status = ClaimStatus.DENIED;
                break;
        }
    }

    public List<ClaimMessage> getClaimMessageList() {
        return claimMessageList;
    }

    public boolean addClaimMessage(ClaimMessage claimMessage) {
        return claimMessageList.add(claimMessage);
    }

    public boolean removeClaimMessage(ClaimMessage claimMessage) {
        boolean res = false;
        while(claimMessageList.contains(claimMessage)) {
            res = claimMessageList.remove(claimMessage);
        }
        return res;
    }

    @Override
    public String toString() {
        return  super.toString() + ", " +
                "Submission Date: " + submissionDate + ", " +
                "Number Plate: " + plate + ", " +
                "Description: " + description + ", " +
                "Messages: " + claimMessageList + ", " +
                "Status: " + status + ".";
    }

}
