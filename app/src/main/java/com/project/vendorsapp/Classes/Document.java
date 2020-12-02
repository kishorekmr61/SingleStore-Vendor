package com.project.vendorsapp.Classes;

public class Document {
    String Documentid;
    String Title;
    String Imagepath;

    public Document(String documentid, String title, String imagepath) {
        Documentid = documentid;
        Title = title;
        Imagepath = imagepath;
    }

    public String getDocumentid() {
        return Documentid;
    }

    public void setDocumentid(String documentid) {
        Documentid = documentid;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getImagepath() {
        return Imagepath;
    }

    public void setImagepath(String imagepath) {
        Imagepath = imagepath;
    }


}
