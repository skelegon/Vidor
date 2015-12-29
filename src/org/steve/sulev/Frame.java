package org.steve.sulev;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Importance of this class: Holds thumbnail (frame) related info, also doubles as database model
 * Includes file_id, frame time (x seconds from the beginning of the video), thumbnail (Image BLOB, byte array)
 */
//CREATE TABLE `frames` (`file_id` INTEGER , `time` INTEGER , `thumbnail` BLOB )
@DatabaseTable(tableName = "frames")
public class Frame {
    public static final String FILE_ID = "file_id";

    @DatabaseField
    private int file_id;

    @DatabaseField
    private int time;

    //Annotation to set field type (required for SQLite)
    @DatabaseField(dataType = DataType.BYTE_ARRAY)
    private byte[] thumbnail;

    Frame(){} //Empty constructor needed for ORMLite
    public Frame(int file_id, int time, byte[] thumbnail){
        this.file_id = file_id;
        this.time = time;
        this.thumbnail = thumbnail;
    }

    //Getters and Setters, auto-generated (Some methods not yet used)
    public int getFile_id() {
        return file_id;
    }

    public void setFile_id(int file_id) {
        this.file_id = file_id;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public byte[] getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(byte[] thumbnail) {
        this.thumbnail = thumbnail;
    }
}
