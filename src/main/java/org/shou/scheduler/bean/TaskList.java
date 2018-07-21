package org.shou.scheduler.bean;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="task_list",namespace="http://org.shou.quartzManager")
public class TaskList implements java.io.Serializable {
	private TaskGroup taskGroup;

	@XmlElement(name="group")
	public TaskGroup getTaskGroup() {
		return taskGroup;
	}

	public void setTaskGroup(TaskGroup taskGroup) {
		this.taskGroup = taskGroup;
	}

	@Override
	public String toString() {
		return "TaskList {taskGroup=" + taskGroup + "}";
	}
}
