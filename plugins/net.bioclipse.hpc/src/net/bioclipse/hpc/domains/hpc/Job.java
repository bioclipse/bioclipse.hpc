package net.bioclipse.hpc.domains.hpc;

public class Job {
	final String PARTITON_TYPE_CORE = "core";
	final String PARTITON_TYPE_NODE = "node";
	final String PARTITON_TYPE_DEVEL = "devel";
	final String PARTITON_TYPE_GPU = "gpu";
	
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
	
	private String id;
	private String partition;
	private String name;
	private String userName;
	private String state;
	private String timeElapsed;
	
	private int nodesCount;
	private String nodelist;
	
	/**
	 * Constructor
	 */
	public Job() {}
	
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return the partition
	 */
	public String getPartition() {
		return partition;
	}
	/**
	 * @param partition the partition to set
	 */
	public void setPartition(String partition) {
		this.partition = partition;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}
	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}
	/**
	 * @param state the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}
	/**
	 * @return the timeElapsed
	 */
	public String getTimeElapsed() {
		return timeElapsed;
	}
	/**
	 * @param timeElapsed the timeElapsed to set
	 */
	public void setTimeElapsed(String timeElapsed) {
		this.timeElapsed = timeElapsed;
	}
	/**
	 * @return the nodesCount
	 */
	public int getNodesCount() {
		return nodesCount;
	}
	/**
	 * @param nodesCount the nodesCount to set
	 */
	public void setNodesCount(int nodesCount) {
		this.nodesCount = nodesCount;
	}
	/**
	 * @return the nodelist
	 */
	public String getNodelist() {
		return nodelist;
	}
	/**
	 * @param nodelist the nodelist to set
	 */
	public void setNodelist(String nodelist) {
		this.nodelist = nodelist;
	}

}
