package com.redhat.rhoar.homework.freelancer.model;

import java.io.Serializable;
import java.util.Arrays;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "FREELANCER", uniqueConstraints = @UniqueConstraint(columnNames = "freelancerId"))
public class Freelancer implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 8845477575223466600L;

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid" )
	private String freelancerId;

	private String firstName;

	private String lastName;

	private String emailAddress;

	private String[] skills;

	public String getFreelancerId() {
		return freelancerId;
	}
	public void setFreelancerId(String freelancerId) {
		this.freelancerId = freelancerId;
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
	public String getEmailAddress() {
		return emailAddress;
	}
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	public String[] getSkills() {
		return skills;
	}
	public void setSkills(String[] skills) {
		this.skills = skills;
	}
	@Override
	public String toString() {
		return "Freelancer [freelancerId=" + freelancerId + ", firstName=" + firstName + ", lastName=" + lastName
				+ ", emailAddress=" + emailAddress + ", skills=" + Arrays.toString(skills) + "]";
	}




}
