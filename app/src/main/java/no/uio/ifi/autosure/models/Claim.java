package no.uio.ifi.autosure.models;

import java.io.Serializable;

public class Claim extends ClaimItem implements Serializable {

    private String submissionDate;
    private String plate;
    private String description;
    private ClaimStatus status;

    public Claim(int claimId, String claimTitle, String submissionDate, String plate,
                 String description, String status) {
        super(claimId, claimTitle);
        this.submissionDate = submissionDate;
        this.plate = plate;
        this.description = description;
        setStatus(status);
    }

    public String getSubmissionDate() {
        return submissionDate;
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

}
