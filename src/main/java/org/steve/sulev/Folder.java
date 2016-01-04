package org.steve.sulev;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Importance of this class: Holds folder location value, also doubles as database model
 * Includes folder id and folder name
 */
//CREATE TABLE `folders` (`id` INTEGER PRIMARY KEY AUTOINCREMENT , `location` VARCHAR ,  UNIQUE (`location`))
@DatabaseTable(tableName = "folders")
public class Folder {
    public static final String FOLDER_LOCATION = "location";

    //Primary key in DB, auto-increments
    @DatabaseField(generatedId = true)
    private int id;

    //Unique constraint on location field
    @DatabaseField(unique = true)
    private String location;

    Folder(){} //Empty constructor needed for ORMLite
    public Folder(String location){
        this.location = location;
    }

    //Getters and Setters, auto-generated
    public int getId() {
        return id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
