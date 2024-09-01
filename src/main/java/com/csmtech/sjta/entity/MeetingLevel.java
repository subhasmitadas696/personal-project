package com.csmtech.sjta.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "m_meeting_level", schema = "public")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MeetingLevel {

	@Id
	@Column(name = "meeting_level_id")
	private Short meetingLevelId;

	@Column(name = "meeting_level")
	private String levelOfMeeting;

}
