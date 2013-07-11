package net.bioclipse.hpc.domains.hpc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JobState {
	final String STATE_PENDING = "PENDING";
	final String STATE_RUNNING = "RUNNING";
	final String STATE_SUSPENDED = "SUSPENDED";
	final String STATE_CANCELLED = "CANCELLED";
	final String STATE_COMPLETING = "COMPLETING";
	final String STATE_COMPLETED = "COMPLETED";
	final String STATE_CONFIGURING = "CONFIGURING";
	final String STATE_FAILED = "FAILED";
	final String STATE_TIMEOUT = "TIMEOUT";
	final String STATE_PREEMPTED = "PREEMPTED";
	final String STATE_NODE_FAIL = "NODE_FAIL";

	final List<String> ALLOWED_JOB_STATES = new ArrayList<String>(Arrays.asList(
			STATE_PENDING,
			STATE_RUNNING,
			STATE_SUSPENDED,
			STATE_CANCELLED,
			STATE_COMPLETING,
			STATE_COMPLETED,
			STATE_CONFIGURING,
			STATE_FAILED,
			STATE_TIMEOUT,
			STATE_PREEMPTED,
			STATE_NODE_FAIL ));
	
	private String jobState;
	private List<Job> jobs;

	/**
	 * Constructor
	 */
	public JobState(String jobState) {
		jobs = new ArrayList<Job>();

		if (ALLOWED_JOB_STATES.contains(jobState)) {
			this.jobState = jobState;			
		} else {
			this.jobState = "";
		}
	}

	// ------------ Getters and setters ------------
	
	/**
	 * @return the jobs
	 */
	public List<Job> getJobs() {
		return jobs;
	}

	/**
	 * @param jobs the jobs to set
	 */
	public void setJobs(List<Job> jobs) {
		this.jobs = jobs;
	}	
	
	public void addJob(Job job) {
		this.jobs.add(job);
	}
	
}
