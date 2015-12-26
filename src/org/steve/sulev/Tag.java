package org.steve.sulev;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Steve Sulev on 26.12.2015.
 */
@DatabaseTable(tableName = "tags")
public class Tag {
    public static final String TAG_NAME = "name";

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(unique = true)
    private String name;

    Tag(){}
    public Tag(String name){
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o){
        if(o == null) return false;
        if(!(o instanceof Tag)) return false;
        Tag f = (Tag) o;
        return this.name != null && this.name.equals(f.name);
    }
}
