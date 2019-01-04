package th.or.dga.royaljitarsa.database;

import io.realm.RealmObject;

public class ContentDetail extends RealmObject {
    private String contentCategoryID;
    private String contentInfoID;
    private String detailNo;
    private String detailTypeID;
    private String detailTypeName;
    private String detailContent;

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

    public String getDetailNo() {
        return detailNo;
    }

    public void setDetailNo(String detailNo) {
        this.detailNo = detailNo;
    }

    public String getDetailTypeID() {
        return detailTypeID;
    }

    public void setDetailTypeID(String detailTypeID) {
        this.detailTypeID = detailTypeID;
    }

    public String getDetailTypeName() {
        return detailTypeName;
    }

    public void setDetailTypeName(String detailTypeName) {
        this.detailTypeName = detailTypeName;
    }

    public String getDetailContent() {
        return detailContent;
    }

    public void setDetailContent(String detailContent) {
        this.detailContent = detailContent;
    }
}
