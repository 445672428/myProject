package org.shou.scheduler.bean;

/**
 * Created by admin on 2018/6/9.
 */
public enum TaskAttribute {
    DISABLE("disable"),
    NAME("name"),
    ID("id"),
    CLASS("class"),
    TYPE("type"),
    EXPRESSION("expression"),
    STATUS("status");
    final String name;
    private TaskAttribute(String name){
        this.name = name;
    }

    @Override
    public String toString() {
        return "TaskAttribute{" +
                "name='" + name + '\'' +
                '}';
    }
}
