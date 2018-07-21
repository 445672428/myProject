package org.shou.scheduler.bean;

import java.util.*;

import javax.xml.bind.annotation.XmlAnyAttribute;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by admin on 2018/6/9.
 */
public class TaskGroup  implements  java.io.Serializable{
    private String id ;
    private boolean disable;
    private String name;

    private List<TaskObject> tasks = Collections.synchronizedList(new ArrayList<TaskObject>());

    @XmlAttribute(name = "id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    @XmlAttribute(name="disable")
    public boolean isDisable() {
        return disable;
    }

    public void setDisable(boolean disable) {
        this.disable = disable;
    }
    @XmlAttribute(name="name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlElement(name="task_object")
    public List<TaskObject> getTasks() {
        return tasks;
    }

    public void setTasks(List<TaskObject> tasks) {
        this.tasks = tasks;
    }

    @Override
    public String toString() {
        return "TaskGroup{" +
                "id='" + id + '\'' +
                ", disable=" + disable +
                ", name='" + name + '\'' +
                ", tasks=" + tasks +
                '}';
    }
}
