package tr.com.leventyayla.to_dolist.models;

import io.realm.RealmList;
import io.realm.RealmObject;

public class TODOList extends RealmObject {

    private String name;
    private RealmList<TODOItem> items;

    public TODOList() {
    }

    public TODOList(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RealmList<TODOItem> getItems() {
        return items;
    }
}
