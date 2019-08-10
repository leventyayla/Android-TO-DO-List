package tr.com.leventyayla.to_dolist.models;

import java.util.Date;

import io.realm.RealmObject;

public class TODOItem extends RealmObject {

    private String name;
    private String description;
    private Date deadline;
    private boolean isCompleted; //For status

    public TODOItem() {
    }

    public TODOItem(String name, String description, Date deadline, boolean isCompleted) {
        this.name = name;
        this.description = description;
        this.deadline = deadline;
        this.isCompleted = isCompleted;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }
}
