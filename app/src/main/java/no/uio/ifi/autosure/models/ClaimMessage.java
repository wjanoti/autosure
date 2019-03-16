package no.uio.ifi.autosure.models;

import java.io.Serializable;

public class ClaimMessage  implements Serializable {

    private final String sender;
    private final String message;
    private final String date;

    public ClaimMessage(String sender, String message, String date) {
        this.sender = sender;
        this.message = message;
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public String getDate() {
        return date;
    }

    public String getSender() {
        return sender;
    }

    @Override
    public String toString() {
        return  "Sender: " + sender + ", " +
                "Message: " + message + ", " +
                "Date: " + date + ".";
    }
}
