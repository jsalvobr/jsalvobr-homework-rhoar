package com.redhat.rhoar.homework.freelancer.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Component;

import com.redhat.rhoar.homework.freelancer.model.Freelancer;

@Component
public class FreelancerServiceImpl implements FreelancerService {

	@PersistenceContext()
    private EntityManager em;


	public List<Freelancer> getFreelancers() {
		List<Freelancer> frelancers  = em.createQuery("SELECT f FROM Freelancer f").getResultList();
		return frelancers;
	}

	public Freelancer getFreelancer(String freelancerId) {
		Freelancer freelancer = em.find(Freelancer.class, freelancerId);
		return freelancer;
	}
	
	@Transactional
	public Freelancer addFreelancer(Freelancer freelancer) {
		em.persist(freelancer);
		return freelancer;
	}

}
