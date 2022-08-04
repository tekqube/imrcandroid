package com.tekqube.people.attendee;

public class Attendee {
	String firstName;
	String lastName;
	String cityName;
	String stateName;
	String country;
	String phone;
	String otherNames;
	String chapterName;

	public String getChapterName() {
		return chapterName;
	}

	public void setChapterName(String chapterName) {
		this.chapterName = chapterName;
	}

	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
	    this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getCityName() {
		if (cityName == null) return "";
		return cityName;
	}
	public void setCityName(String cityName) {
        if(cityName.equals("0.0")) {
            this.cityName = "";
        }else {
            this.cityName = cityName;
        }
	}
	public String getStateName() {
	    if(stateName == null || stateName.equals("0.0")){
	        return "";
        }
		return stateName;
	}
	public void setStateName(String stateName) {
        if(stateName == null || stateName.equals("0.0")) {
            this.stateName = "";
        }else {
            this.stateName = stateName;
        }
	}

	public String getCountry() {
	    if(country == null || country.equals("0.0")) {
	        return "";
        }
		return country;
	}

	public void setCountry(String country) {
	    if( country == null || country.equals("0.0")) {
            this.country = "";
        }else {
            this.country = country;
        }
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getOtherNames() {
		return otherNames;
	}

	public void setOtherNames(String otherNames) {
		this.otherNames = otherNames;
	}
}
