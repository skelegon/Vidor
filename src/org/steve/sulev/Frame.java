package org.steve.sulev;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Steve Sulev on 26.12.2015.
 */
@DatabaseTable(tableName = "frames")
public class Frame {
    public static final String FILE_ID = "file_id";

    @DatabaseField
    private int file_id;

    @DatabaseField
    private int time;

    @DatabaseField(dataType = DataType.BYTE_ARRAY)
    private byte[] thumbnail;

    Frame(){}
    public Frame(int file_id, int time, byte[] thumbnail){
        this.file_id = file_id;
        this.time = time;
        this.thumbnail = thumbnail;
    }

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
