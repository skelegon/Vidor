package org.steve.sulev;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/***
 * Importance of this class: Holds file-tag relations, also doubles as database model (for ORMLite)
 * Includes foreign file_id (from files table), foreign tag_id (from tags table)
 */

//Declare ORMLite table name and fields
//CREATE TABLE `video_tags` (`id` INTEGER PRIMARY KEY AUTOINCREMENT , `file_id` INTEGER , `tag_id` INTEGER , UNIQUE (`file_id`,`tag_id`) )
@DatabaseTable(tableName = "video_tags")
public class VideoTags {
    public static final String FILE_ID = "file_id";
    public static final String TAG_ID = "tag_id";

    @DatabaseField(generatedId = true)
    private int id;

    //Foreign fields (both are unique together)
    @DatabaseField(foreign = true, foreignAutoRefresh = true, uniqueCombo = true, columnName = FILE_ID)
    private File file;

    @DatabaseField(foreign = true, foreignAutoRefresh = true, uniqueCombo = true, columnName = TAG_ID)
    private Tag tag;

    VideoTags(){} //Empty constructor needed for ORMLite
    public VideoTags(File file, Tag tag){
        this.file = file;
        this.tag = tag;
    }

    //Getters and Setters, auto-generated (Some methods not yet used)
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
