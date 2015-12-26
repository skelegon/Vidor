package org.steve.sulev;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import com.sun.org.apache.xpath.internal.functions.FuncGenerateId;

/**
 * Created by Steve Sulev on 26.12.2015.
 */
@DatabaseTable(tableName = "files")
public class File {
    public static final String FILE_ID = "id";
    public static final String FOLDER_ID = "folder_id";

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(uniqueCombo = true)
    private String name;

    @DatabaseField
    private Long fileSize;

    @DatabaseField
    private Boolean missing = false;

    @DatabaseField(foreign = true, columnName = FOLDER_ID, uniqueCombo = true, foreignAutoRefresh = true)
    private Folder folder;

    @ForeignCollectionField
    private ForeignCollection<VideoTags> tags;

    File(){}
    public File(String name, Long fileSize, Folder folder){
        this.name = name;
        this.fileSize = fileSize;
        this.folder = folder;
    }

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

    @Override
    public boolean equals(Object o){
        if(o == null) return false;
        if(!(o instanceof File)) return false;
        File f = (File)o;
        return this.name != null && this.name.equals(f.name);
    }

}
