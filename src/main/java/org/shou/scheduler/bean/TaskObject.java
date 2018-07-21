package org.shou.scheduler.bean;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/**
 * Created by admin on 2018/6/9.
 */
public class TaskObject implements  java.io.Serializable {
    /** 调度唯一标识 */
    private String id ;
    /** 调度名称 */
    private String name;
    /** 是否禁用调度 */
    private boolean disable;
    /** 实现类 */
    private Class classImpl;
    /** 调度显示类型 1.表达式,2.时分秒,3.间隔秒,4.间隔分,5.间隔小时*/
    private int type ;
    /** 调度CronTrigger表达式 */
    private String expression;
    /** 调度状态  */
    private int status;
    /**调度参数*/
    private String params ;
    /**详细说明*/
    private String explain;
    @XmlAttribute(name="id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    @XmlAttribute(name="name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    @XmlAttribute(name="disable")
    public boolean isDisable() {
        return disable;
    }

    public void setDisable(boolean disable) {
        this.disable = disable;
    }
    @XmlAttribute(name="class")
    public Class getClassImpl() {
        return classImpl;
    }

    public void setClassImpl(Class classImpl) {
        this.classImpl = classImpl;
    }
    @XmlAttribute(name="type")
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
    @XmlAttribute(name="expression")
    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }
    
    @XmlAttribute(name="status")
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @XmlElement(name="json_params")
    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }
    @XmlElement(name="detailed_description")
    public String getExplain() {
        return explain;
    }

    public void setExplain(String explain) {
        this.explain = explain;
    }

    @Override
    public String toString() {
        return "TaskObject{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", disable=" + disable +
                ", classImpl=" + classImpl +
                ", type=" + type +
                ", expression='" + expression + '\'' +
                ", status=" + status +
                ", params='" + params + '\'' +
                ", explain='" + explain + '\'' +
                '}';
    }
}
