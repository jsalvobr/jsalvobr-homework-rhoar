package com.redhat.rhoar.homework.freelancer.service;

import java.util.List;

import com.redhat.rhoar.homework.freelancer.model.Freelancer;

public interface FreelancerService {

	public List<Freelancer> getFreelancers();
	public Freelancer getFreelancer(String freelancerId);
	public Freelancer addFreelancer(Freelancer freelancerId);

}
