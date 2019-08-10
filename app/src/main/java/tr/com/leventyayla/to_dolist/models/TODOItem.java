package tr.com.leventyayla.to_dolist.models;

import io.realm.RealmObject;

public class TODOItem extends RealmObject {

    private String name;
    private String description;
    private long deadline;
    private boolean isCompleted; //For status

    public TODOItem() {
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public long getDeadline() {
        return deadline;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDeadline(long deadline) {
        this.deadline = deadline;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }
}
