package com.example.poowadon.liveat500px.dao;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by poowadon on 09/08/2559.
 */
public class PhotoItemDao implements Parcelable{

    /*@SerializedName is for mapping java variable with json variable**/
    @SerializedName("id") private int id;
    @SerializedName("link") private String link;
    @SerializedName("image_url") private String imageUrl;
    @SerializedName("caption") private String caption;
    @SerializedName("user_id") private String userId;
    @SerializedName("username") private String userName;
    @SerializedName("profile_picture") private String profilePicture;
    @SerializedName("tags") private List<String> tags = new ArrayList<String>();
    @SerializedName("created_time") private Date createdTime;
    @SerializedName("camera") private String camera;
    @SerializedName("lens") private String lens;
    @SerializedName("focal_length") private String focalLength;
    @SerializedName("iso") private String iso;
    @SerializedName("shutter_speed") private String shutterSpeed;
    @SerializedName("aperture") private String aperture;

    public PhotoItemDao(){

    }

    protected PhotoItemDao(Parcel in) {
        id = in.readInt();
        link = in.readString();
        imageUrl = in.readString();
        caption = in.readString();
        userId = in.readString();
        userName = in.readString();
        profilePicture = in.readString();
        tags = in.createStringArrayList();
        camera = in.readString();
        lens = in.readString();
        focalLength = in.readString();
        iso = in.readString();
        shutterSpeed = in.readString();
        aperture = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(link);
        dest.writeString(imageUrl);
        dest.writeString(caption);
        dest.writeString(userId);
        dest.writeString(userName);
        dest.writeString(profilePicture);
        dest.writeStringList(tags);
        dest.writeString(camera);
        dest.writeString(lens);
        dest.writeString(focalLength);
        dest.writeString(iso);
        dest.writeString(shutterSpeed);
        dest.writeString(aperture);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PhotoItemDao> CREATOR = new Creator<PhotoItemDao>() {
        @Override
        public PhotoItemDao createFromParcel(Parcel in) {
            return new PhotoItemDao(in);
        }

        @Override
        public PhotoItemDao[] newArray(int size) {
            return new PhotoItemDao[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAperture() {
        return aperture;
    }

    public void setAperture(String aperture) {
        this.aperture = aperture;
    }

    public String getCamera() {
        return camera;
    }

    public void setCamera(String camera) {
        this.camera = camera;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public String getFocalLength() {
        return focalLength;
    }

    public void setFocalLength(String focalLength) {
        this.focalLength = focalLength;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getIso() {
        return iso;
    }

    public void setIso(String iso) {
        this.iso = iso;
    }

    public String getLens() {
        return lens;
    }

    public void setLens(String lens) {
        this.lens = lens;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getShutterSpeed() {
        return shutterSpeed;
    }

    public void setShutterSpeed(String shutterSpeed) {
        this.shutterSpeed = shutterSpeed;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
