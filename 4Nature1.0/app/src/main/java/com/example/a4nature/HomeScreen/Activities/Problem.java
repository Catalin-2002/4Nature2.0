package com.example.a4nature.HomeScreen.Activities;

public class Problem {
    private String name;
    private String category;
    private String description;
    private String photoProblemId;
    private String photoSolutionId;
    private double latitude;
    private double longitude;
    private boolean archievedByAdmin;

    public Problem() {

    }

    public Problem(String name, String category, String description, String photoProblemId, String photoSolutionId, double latitude, double longitude, boolean archievedByAdmin) {
        this.name = name;
        this.category = category;
        this.description = description;
        this.photoProblemId = photoProblemId;
        this.photoSolutionId = photoSolutionId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.archievedByAdmin = archievedByAdmin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhotoProblemId() {
        return photoProblemId;
    }

    public void setPhotoProblemId(String photoProblemId) {
        this.photoProblemId = photoProblemId;
    }

    public String getPhotoSolutionId() {
        return photoSolutionId;
    }

    public void setPhotoSolutionId(String photoSolutionId) {
        this.photoSolutionId = photoSolutionId;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public boolean isArchievedByAdmin() {
        return archievedByAdmin;
    }

    public void setArchievedByAdmin(boolean archievedByAdmin) {
        this.archievedByAdmin = archievedByAdmin;
    }
}
