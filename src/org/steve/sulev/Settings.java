package org.steve.sulev;

import java.util.List;

/***
 * Importance of this class: Holds all necessary configuration values.
 * The Settings class combines setters and getters of information used under the Settings tab.
 * This includes:
 *      1. VLC media player location
 *      2. Thumbnail highlight color
 *      3. Thumbnail count
 *      4. Thumbnail width
 *      5. List of folders
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
