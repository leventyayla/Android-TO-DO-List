package tr.com.leventyayla.to_dolist.models;

public class FilterSettings {

    private String name;
    private boolean isCompleted;
    private boolean isExpired;

    public FilterSettings(String name, boolean isCompleted, boolean isExpired) {
        this.name = name;
        this.isCompleted = isCompleted;
        this.isExpired = isExpired;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public boolean isExpired() {
        return isExpired;
    }

    public void setExpired(boolean expired) {
        isExpired = expired;
    }
}
