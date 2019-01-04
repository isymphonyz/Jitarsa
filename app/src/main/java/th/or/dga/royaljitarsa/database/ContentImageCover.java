package th.or.dga.royaljitarsa.database;

import io.realm.RealmObject;

public class ContentImageCover extends RealmObject {
    private String contentCategoryID;
    private String contentInfoID;
    private String imageCoverID;
    private String imageCoverURL;

    public String getContentCategoryID() {
        return contentCategoryID;
    }

    public void setContentCategoryID(String contentCategoryID) {
        this.contentCategoryID = contentCategoryID;
    }

    public String getContentInfoID() {
        return contentInfoID;
    }

    public void setContentInfoID(String contentInfoID) {
        this.contentInfoID = contentInfoID;
    }

    public String getImageCoverID() {
        return imageCoverID;
    }

    public void setImageCoverID(String imageCoverID) {
        this.imageCoverID = imageCoverID;
    }

    public String getImageCoverURL() {
        return imageCoverURL;
    }

    public void setImageCoverURL(String imageCoverURL) {
        this.imageCoverURL = imageCoverURL;
    }
}
