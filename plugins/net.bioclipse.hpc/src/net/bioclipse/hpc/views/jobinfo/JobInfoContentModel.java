package net.bioclipse.hpc.views.jobinfo;

import java.util.List;

import net.bioclipse.hpc.domains.hpc.JobState;

public class JobInfoContentModel {
	private List<JobState> jobStates;
	
	/**
	 * Constructor
	 */
	public JobInfoContentModel() {
		// Add some default jobStates
		addJobState(new JobState(JobState.STATE_RUNNING));
		addJobState(new JobState(JobState.STATE_PENDING));
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

}
