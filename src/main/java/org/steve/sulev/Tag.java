package org.steve.sulev;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Importance of this class: defines tag related variables, also doubles as database model (for ORMLite)
 */

//Declare ORMLite table name and fields
//CREATE TABLE `tags` (`id` INTEGER PRIMARY KEY AUTOINCREMENT , `name` VARCHAR ,  UNIQUE (`name`))
@DatabaseTable(tableName = "tags")
public class Tag {
    public static final String TAG_NAME = "name";

    //Primary key, auto increment
    @DatabaseField(generatedId = true)
    private int id;

    //Tag name must be unique
    @DatabaseField(unique = true)
    private String name;

    Tag(){} //Empty constructor for ORMLite
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

    //custom equals method for contains function (Compares name and class type)
    @Override
    public boolean equals(Object o){
        if(o == null) return false;
        if(!(o instanceof Tag)) return false;
        Tag f = (Tag) o;
        return this.name != null && this.name.equals(f.name);
    }
}
