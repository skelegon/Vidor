package org.steve.sulev;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Importance of this class: Holds values for file (video) related info, also doubles as database model (for ORMLite)
 * Includes file parameters such as: id, name, file-size, missing (Missing from disk), folder (Foreign value from folders table)
 * video tags (Foreign value from video_tags table)
 */

//Declare ORMLite table name and fields
//CREATE TABLE `files` (`id` INTEGER PRIMARY KEY AUTOINCREMENT , `name` VARCHAR , `fileSize` BIGINT , `missing` BOOLEAN , `folder_id` INTEGER , UNIQUE (`name`,`folder_id`) )
@DatabaseTable(tableName = "files")
public class File {
    public static final String FILE_ID = "id";
    public static final String FOLDER_ID = "folder_id";

    //Primary key in database, auto increment values
    @DatabaseField(generatedId = true)
    private int id;

    //name and folder id are unique together
    @DatabaseField(uniqueCombo = true)
    private String name;

    @DatabaseField
    private Long fileSize;

    @DatabaseField
    private Boolean missing = false;

    //Foreign field (folder_id), unique together with name.
    @DatabaseField(foreign = true, columnName = FOLDER_ID, uniqueCombo = true, foreignAutoRefresh = true)
    private Folder folder;

    //VideoTags collection from foreign table (video_tags)
    @ForeignCollectionField
    private ForeignCollection<VideoTags> tags;

    File(){} //Empty constructor needed for ORMLite
    public File(String name, Long fileSize, Folder folder){
        this.name = name;
        this.fileSize = fileSize;
        this.folder = folder;
    }

    //Getters and Setters, auto-generated (Some methods not yet used)
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public Boolean getMissing() {
        return missing;
    }

    public void setMissing(Boolean missing) {
        this.missing = missing;
    }

    public Folder getFolder() {
        return folder;
    }

    public void setFolder(Folder folder) {
        this.folder = folder;
    }

    public ForeignCollection<VideoTags> getTags() {
        return tags;
    }

    //Custom equals method, needed in order to use equals and contains methods. (Compares name and class type)
    @Override
    public boolean equals(Object o){
        if(o == null) return false;
        if(!(o instanceof File)) return false;
        File f = (File)o;
        return this.name != null && this.name.equals(f.name);
    }
}
