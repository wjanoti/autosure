package no.uio.ifi.autosure.models;

public class ClaimItem {

    private String title;
    private int id;

    public ClaimItem(int id, String title) {
        this.title = title;
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public int getId() {
        return id;
    }

}
