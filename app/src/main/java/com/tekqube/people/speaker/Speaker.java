package com.tekqube.people.speaker;

import android.os.Parcel;
import android.os.Parcelable;

public class Speaker implements Parcelable {
	String imageUrl;
	String name;
	String title;
	String qualification;
	String location;
	String keyAchihevements;
	String community;
	
	public Speaker(Parcel parcel) {
		imageUrl = parcel.readString();
	    name = parcel.readString();
	    title = parcel.readString();
		qualification = parcel.readString();
	    location = parcel.readString();
		keyAchihevements = parcel.readString();
		community = parcel.readString();
	}
	
	public Speaker() {
		
	}
	
	
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getQualification() {
		return qualification;
	}
	public void setQualification(String qualification) {
		this.qualification = qualification;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getKeyAchihevements() {
		return keyAchihevements;
	}
	public void setKeyAchihevements(String keyAchihevements) {
		this.keyAchihevements = keyAchihevements;
	}
	public String getCommunity() {
		return community;
	}
	public void setCommunity(String community) {
		this.community = community;
	}
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel arg0, int arg1) {
		// TODO Auto-generated method stub
		arg0.writeString(imageUrl);
		arg0.writeString(name);
		arg0.writeString(title);
		arg0.writeString(qualification);
		arg0.writeString(location);
		arg0.writeString(keyAchihevements);
		arg0.writeString(community);
	}
	
	public void readFromParcel(Parcel in) {
		imageUrl = in.readString();
	    name = in.readString();
	    title = in.readString();
		qualification = in.readString();
	    location = in.readString();
	    keyAchihevements = in.readString();
	    community = in.readString();
	}
	
	public static final Parcelable.Creator<Speaker> CREATOR = new Creator<Speaker>() {

	    public Speaker createFromParcel(Parcel source) {

	        return new Speaker(source);
	    }

	    public Speaker[] newArray(int size) {

	        return new Speaker[size];
	    }

	};

}
