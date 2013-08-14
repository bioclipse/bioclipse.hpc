package net.bioclipse.hpc.views.jobinfo;

import java.util.ArrayList;
import java.util.List;

import net.bioclipse.hpc.domains.hpc.JobState;

public class JobInfoContentModel {
	private List<JobState> jobStates;
	
	/**
	 * Constructor
	 */
	public JobInfoContentModel() {
		// Initialize the jobStates list
		jobStates = new ArrayList<JobState>();
		
		// Add some default jobStates
		JobState jobStateRunning = new JobState(JobState.STATE_RUNNING);
		JobState jobStatePending = new JobState(JobState.STATE_PENDING);
		addJobState(jobStateRunning);
		addJobState(jobStatePending);
	}

	public void clearJobStates() {
		jobStates.clear();
	}
	
	public void clearJobsinJobStates() {
		for (JobState jobState : this.jobStates) {
			jobState.clearJobs();
		}
	}
	
	/**
	 * @return the jobStates
	 */
	public List<JobState> getJobStates() {
		return jobStates;
	}

	/**
	 * @param jobStates the jobStates to set
	 */
	public void setJobStates(List<JobState> jobStates) {
		this.jobStates = jobStates;
	}
	
	/**
	 * @param jobState the jobState to add
	 */
	public void addJobState(JobState jobState) {
		this.jobStates.add(jobState);
	}

	public JobState getJobStateByJobStateString(String jobStateStr) {
		for (JobState jobState : jobStates) {
			if (jobState.getJobState().equals(jobStateStr)) {
				return jobState;
			}
		}
		return null;
	}

}
