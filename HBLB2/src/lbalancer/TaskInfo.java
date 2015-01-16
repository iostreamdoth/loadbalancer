package lbalancer;

import java.util.Date;
import org.xlightweb.IHttpExchange;

public class TaskInfo {
	public String TaskName;
	public int intTaskPriority;
	public String requester;
	IHttpExchange exchange;
	Date dtStartDate;
	boolean isTaskPreemptive;
	public long lngTaskId;
	int intSeverID;
	int intProcessingTime;
	int memory;
	int storage;

	
	

	public TaskInfo() {
		// TODO Auto-generated constructor stub
		dtStartDate = new Date();
		intTaskPriority = 2;

	}

	public void reducePriority() {
		if (intTaskPriority > 0) {
			intTaskPriority -= 1;
		}

	}

	public int getPriority() {
		//Priority decision to be taken here
		//0-Low
		//1-Medium
		//2-High
		return intTaskPriority;
	}
}
