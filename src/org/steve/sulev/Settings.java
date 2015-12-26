package org.steve.sulev;

import java.util.List;

/***
 *
 *
 *
 */
public class Settings {
    private String vlcLocation;
    private String thumbnailHighlightColor;
    private int thumbnailCount;
    private int thumbnailWidth;
    private List<String> folders;

    public String getVlcLocation() {
        return vlcLocation;
    }

    public void setVlcLocation(String vlcLocation) {
        this.vlcLocation = vlcLocation;
    }

    public String getThumbnailHighlightColor() {
        return thumbnailHighlightColor;
    }

    public void setThumbnailHighlightColor(String thumbnailHighlightColor) {
        this.thumbnailHighlightColor = thumbnailHighlightColor;
    }

    public int getThumbnailCount() {
        return thumbnailCount;
    }

    public void setThumbnailCount(int thumbnailCount) {
        this.thumbnailCount = thumbnailCount;
    }

    public int getThumbnailWidth() {
        return thumbnailWidth;
    }

    public void setThumbnailWidth(int thumbnailWidth) {
        this.thumbnailWidth = thumbnailWidth;
    }

    public List<String> getFolders() {
        return folders;
    }

    public void setFolders(List<String> folders) {
        this.folders = folders;
    }
}
