package org.steve.sulev;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Steve Sulev on 26.12.2015.
 */
@DatabaseTable(tableName = "video_tags")
public class VideoTags {
    public static final String FILE_ID = "file_id";
    public static final String TAG_ID = "tag_id";

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(foreign = true, foreignAutoRefresh = true, uniqueCombo = true, columnName = FILE_ID)
    private File file;

    @DatabaseField(foreign = true, foreignAutoRefresh = true, uniqueCombo = true, columnName = TAG_ID)
    private Tag tag;

    VideoTags(){}
    public VideoTags(File file, Tag tag){
        this.file = file;
        this.tag = tag;
    }

    public int getId() {
        return id;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }
}
