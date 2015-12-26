package org.steve.sulev;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Steve Sulev on 26.12.2015.
 */
@DatabaseTable(tableName = "folders")
public class Folder {
    public static final String FOLDER_LOCATION = "location";

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(unique = true)
    private String location;

    Folder(){}
    public Folder(String location){
        this.location = location;
    }

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
