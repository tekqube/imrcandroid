package com.tekqube.imrc;

import java.io.Serializable;

public class Schedule  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String time;
	int day;
	public String room;
	public String session;
	String targetGroup;
	String briefDescription;
	String panel;
	String requirements;
	String avRequirements;
	String startTime;
	String endTime;
	String start24Time;
	String end24Time;
	String teamResponsibility;
	int identifier;
	String coordinators;
	String volunteerName;
	String volunteerPhone;
	String imageName;
	String speakerName;
	String speakerDescription;

	public String getSpeakerName() {
		return speakerName;
	}

	public void setSpeakerName(String speakerName) {
		this.speakerName = speakerName;
	}

	public String getSpeakerDescription() {
		return speakerDescription;
	}

	public void setSpeakerDescription(String speakerDescription) {
		this.speakerDescription = speakerDescription;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public Schedule(String string) {
		this.session = string;
	}
	
	public Schedule() {
		// TODO Auto-generated constructor stub
	}

	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public int getDay() {
		return day;
	}
	public void setDay(int day) {
		this.day = day;
	}

	public String getSession() {
		return session;
	}
	public void setSession(String session) {
		this.session = session;
	}
	public String getTargetGroup() {
		return targetGroup;
	}
	public void setTargetGroup(String targetGroup) {
		this.targetGroup = targetGroup;
	}
	public String getBriefDescription() {
		return briefDescription;
	}
	public void setBriefDescription(String briefDescription) {
		this.briefDescription = briefDescription;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getStart24Time() {
		return start24Time;
	}
	public void setStart24Time(String start24Time) {
		this.start24Time = start24Time;
	}
	public String getEnd24Time() {
		return end24Time;
	}
	public void setEnd24Time(String end24Time) {
		this.end24Time = end24Time;
	}
	public int getIdentifier() {
		return identifier;
	}
	public void setIdentifier(int identifier) {
		this.identifier = identifier;
	}

	public String getPanel() {
		return panel;
	}

	public void setPanel(String panel) {
		this.panel = panel;
	}

	public String getRequirements() {
		return requirements;
	}

	public void setRequirements(String requirements) {
		this.requirements = requirements;
	}

	public String getAvRequirements() {
		return avRequirements;
	}

	public void setAvRequirements(String avRequirements) {
		this.avRequirements = avRequirements;
	}

	public String getCoordinators() {
		return coordinators;
	}

	public void setCoordinators(String coordinators) {
		this.coordinators = coordinators;
	}

	public String getTeamResponsibility() {
		return teamResponsibility;
	}

	public void setTeamResponsibility(String teamResponsibility) {
		this.teamResponsibility = teamResponsibility;
	}

	public String getRoom() {
		return room;
	}

	public void setRoom(String room) {
		this.room = room;
	}

	public String getVolunteerName() {
		return volunteerName;
	}

	public void setVolunteerName(String volunteerName) {
		this.volunteerName = volunteerName;
	}

	public String getVolunteerPhone() {
		return volunteerPhone;
	}

	public void setVolunteerPhone(String volunteerPhone) {
		this.volunteerPhone = volunteerPhone;
	}
}
