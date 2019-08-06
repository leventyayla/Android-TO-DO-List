package tr.com.leventyayla.to_dolist.models;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class TODOList extends RealmObject {
    @PrimaryKey
    private long id;
    private String name;
    private RealmList<TODOItem> items;

    public TODOList() {
    }

    public long getId() {
        return id;
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
