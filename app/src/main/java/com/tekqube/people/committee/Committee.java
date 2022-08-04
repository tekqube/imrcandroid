package com.tekqube.people.committee;

import android.os.Parcel;
import android.os.Parcelable;

public class Committee implements Parcelable {
	String committeeName;
	String name;
	String phone;
	String photoUrl;

	public Committee(Parcel parcel) {
		committeeName = parcel.readString();
	    phone = parcel.readString();
	    name = parcel.readString();
		photoUrl = parcel.readString();
	}
	
	public Committee() {}
	
	public String getCommitteeName() {
		if(committeeName == null) {
			return "";
		}
		return committeeName;
	}
	public void setCommitteeName(String committeeName) {
		this.committeeName = committeeName;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhotoUrl() {
		return photoUrl;
	}

	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(committeeName);
		dest.writeString(name);
		dest.writeString(phone);
		dest.writeString(photoUrl);
	}
	 
	public void readFromParcel(Parcel in) {
		committeeName = in.readString();
	    name = in.readString();
	    phone = in.readString();
		photoUrl = in.readString();
	}
	
	public static final Parcelable.Creator<Committee> CREATOR = new Creator<Committee>() {

	    public Committee createFromParcel(Parcel source) {
	        return new Committee(source);
	    }

	    public Committee[] newArray(int size) {
	        return new Committee[size];
	    }
	};

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
}