package org.shou.scheduler.quartz;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;
import org.shou.scheduler.bean.TaskGroup;
import org.shou.scheduler.bean.TaskObject;
import org.shou.scheduler.common.CommUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import net.sf.json.JSONObject;

/**
 * Created by admin on 2018/6/9.
 */
@Component
public class QuartzManager {

    private static  final Logger logger = LoggerFactory.getLogger(QuartzManager.class);

    private static String configLocation="/QuartzManager.xml";
    private static Scheduler server = null;
    private final static Map<String,TaskGroup> TASK_GROUP_MAP = new ConcurrentHashMap<String,TaskGroup>();
    private static SchedulerFactory gSchedulerFactory = new StdSchedulerFactory();
    private static volatile QuartzManager quartzManager = null;
    public QuartzManager() {}

   /* public  static QuartzManager getInstance(){
       if(quartzManager==null){
            synchronized (QuartzManager.class) {
                if(quartzManager==null){
                    quartzManager = new QuartzManager();
                }
            }
        }
        return quartzManager;
    }*/

    private void initConfig(InputStream inputStream) throws ClassNotFoundException,DocumentException ,UnsupportedEncodingException {
        SAXReader sr = new SAXReader();
        sr.setEncoding("UTF-8");
        //获得文档对象模型
        Document doc = sr.read(new InputStreamReader(inputStream,"UTF-8"));
        //获得根元素
        Element root = doc.getRootElement();
        List<Element> elements = root.elements("group");
        if(elements!=null){
            for (Element nodes: elements) {
                String id = nodes.attributeValue("id");
                String name =nodes.attributeValue("name");
                boolean  disable = Boolean.parseBoolean(nodes.attributeValue("disable","false"));
                TaskGroup taskGroup = new TaskGroup();
                taskGroup.setId(id);
                taskGroup.setName(name);
                taskGroup.setDisable(disable);
                TASK_GROUP_MAP.put(id,taskGroup);
                List<Element> tasks =  nodes.elements("task_object");
                if(tasks!=null){
                    for (Element task: tasks) {
                        boolean task_disable =  Boolean.parseBoolean(task.attributeValue("disable","false"));
                        if(disable){
                            task_disable = disable;
                        }
                        String task_id = task.attributeValue("id");//任务id
                        String task_name = task.attributeValue("name");//任务名
                        int task_status =Integer.parseInt(task.attributeValue("status"));//运行状态
                        String task_expression = task.attributeValue("expression");//表达式
						CronScheduleBuilder.cronSchedule(task_expression);
                        Class<?> task_class = Class.forName(task.attributeValue("class"));
                        int task_type = Integer.parseInt(task.attributeValue("type"));// 表达式显示类型
                        Element node =  task.element("json_params");//参数
                        Element explain = task.element("detailed_description");//详情
                        TaskObject taskObject= new TaskObject();
                        taskObject.setDisable(task_disable);
                        taskObject.setId(task_id);
                        taskObject.setName(task_name);
                        taskObject.setStatus(task_status);
                        taskObject.setExpression(task_expression);
                        taskObject.setClassImpl(task_class);
                        taskObject.setType(task_type);
                        if(node!=null){
                            taskObject.setParams(node.getTextTrim());
                        }
                        if(explain!=null){
                            taskObject.setExplain(explain.getTextTrim());
                        }
                        taskGroup.getTasks().add(taskObject);
                    }
                }
            }
        }
        if(logger.isDebugEnabled()){
            logger.debug("初始化调度配置文件信息完毕.{}",TASK_GROUP_MAP);
        }
    }


    public synchronized void init() throws SchedulerException {
        if (server == null) {
            server = gSchedulerFactory.getScheduler();
            InputStream inputStream = null;
            try{
                inputStream  = QuartzManager.class.getResourceAsStream(configLocation);
                initConfig(inputStream);
            }catch (ClassNotFoundException e){
                logger.error("",e);
            }catch (DocumentException e){
                logger.error("",e);
            }catch (UnsupportedEncodingException e ){
                logger.error("",e);
            }finally {
            	IOUtils.closeQuietly(inputStream);
            }
            start();
        }
    }

    public synchronized void shutDown() throws SchedulerException {
        /*if (server != null && !server.isShutdown()) {
            server.shutdown();
        }*/
        Scheduler sched = gSchedulerFactory.getScheduler();
        if (!sched.isShutdown()) {
            sched.shutdown();
            logger.debug("关闭所有任务");
        }
    }

    public synchronized void start() throws SchedulerException {
        Scheduler sched = gSchedulerFactory.getScheduler();
        sched.start();
        for (Map.Entry<String,TaskGroup> entry: TASK_GROUP_MAP.entrySet()) {
            TaskGroup  taskGroup =  entry.getValue();
            if(!taskGroup.isDisable()){
                List<TaskObject> tasks =  taskGroup.getTasks();
                for (TaskObject task: tasks) {
                    if(!task.isDisable()){
                        try{
                            runJob(task.getName(),task.getClassImpl(),entry.getKey(),task.getExpression(),task.getParams());
                            task.setStatus(1);
                        }catch (SchedulerException e){
                            logger.error("",e);
                            task.setStatus(2);
                        }
                    }
                }
            }
        }
    }

    public synchronized void pause() throws SchedulerException{
        Scheduler sched = gSchedulerFactory.getScheduler();
        sched.pauseAll();
    }

    public synchronized void resume() throws SchedulerException{
        Scheduler sched = gSchedulerFactory.getScheduler();
        sched.resumeAll();
    }

    public synchronized void purseJob(String jobName,String groupName) throws SchedulerException{
        Scheduler sched = gSchedulerFactory.getScheduler();
        JobKey jobKey = new JobKey(jobName, groupName);
        sched.pauseJob(jobKey);
        logger.info("暂停任务:"+jobName);
    }

    public synchronized void resumeJob(String jobName,String groupName) throws SchedulerException{
        Scheduler sched = gSchedulerFactory.getScheduler();
        JobKey jobKey = new JobKey(jobName, groupName);
        sched.resumeJob(jobKey);
        logger.info("恢复任务:"+jobName);
    }


    public synchronized void runJob(String jobName, Class classImpl,String jobGroupName,String cron,String params) throws SchedulerException{
        Scheduler sched = gSchedulerFactory.getScheduler();
        if (!sched.isShutdown()) {
            JobBuilder jobBuilder  = JobBuilder.newJob(classImpl)
                    .withIdentity(jobName, jobGroupName);
            if(StringUtils.isNotBlank(params)){
                JSONObject json = JSONObject.fromObject(params);
                JobDataMap jobDataMap  = new JobDataMap();
                Iterator<?> interator =  json.keys();
                while(interator.hasNext()){
                    Object obj =  interator.next();
                    String key  = String.valueOf(obj);
                    jobDataMap.put(key,json.get(obj));
                }
                jobBuilder.setJobData(jobDataMap);
            }
            JobDetail jobDetail =jobBuilder.build();
            Trigger trigger = TriggerBuilder.newTrigger()// 创建一个新的TriggerBuilder来规范一个触发器
                    .withIdentity(new StringBuffer("trigger_").append(jobName).toString(),
                            new StringBuffer("trigger_").append(jobGroupName).toString())// 给触发器起一个名字和组名
                    .startNow()// 立即执行
                    .withSchedule(CronScheduleBuilder.cronSchedule(cron)) // 触发器的执行时间
                    .build();// 产生触发器
            sched.scheduleJob(jobDetail, trigger);
            logger.info("任务加入调度器:{}[{}], 时间格式:{}",jobName,classImpl.getSimpleName(),cron);
        }
    }

    public synchronized void modifyJobTime(String jobName, String jobGroupName,String cron) throws SchedulerException{
        Scheduler sched = gSchedulerFactory.getScheduler();
        TriggerKey triggerKey = new TriggerKey(jobName, new StringBuffer("trigger_").append(jobGroupName).toString());
        CronTrigger trigger = (CronTrigger) sched.getTrigger(triggerKey);
        if (trigger == null) {
            return;
        }
        String oldTime = trigger.getCronExpression();
        if (!oldTime.equalsIgnoreCase(cron)) {
          /*  JobDetail jobDetail = sched.getJobDetail(new JobKey(jobName,jobGroupName));
            Class objJobClass = jobDetail.getJobClass();*/
            sched.rescheduleJob(trigger.getKey(),trigger);
            //addJob(jobName, objJobClass,jobGroupName, cron);
            logger.debug("修改任务:{},{}",jobName,cron);
        }
    }

    public synchronized boolean removeJob(String jobName,String jobGroupName) throws SchedulerException{
        Scheduler sched = gSchedulerFactory.getScheduler();
        return sched.deleteJob(new JobKey(jobName,jobGroupName));
    }

    public synchronized List<JobExecutionContext> getCurrentlyExecutingJobs(){
        List<JobExecutionContext> list = new ArrayList<JobExecutionContext>();
        try {
            Scheduler sched = gSchedulerFactory.getScheduler();
            list = sched.getCurrentlyExecutingJobs();
        } catch (SchedulerException e) {
            logger.error("",e);
        }
        return list;
    }



    /**
     * 获取触发器的状态
     * 暂停：1 正常：0
     * @param jobName
     * @return
     * @throws SchedulerException
     */
    public synchronized Trigger.TriggerState getTriggerState(String jobName,String jobGroupName) throws SchedulerException{
        int state = -1;
        if(StringUtils.isNotBlank(jobName)){
            Scheduler sched = gSchedulerFactory.getScheduler();
            TriggerKey triggerKey = new TriggerKey(jobName, new StringBuffer("trigger_").append(jobGroupName).toString());
            Trigger.TriggerState  s = sched.getTriggerState(triggerKey);
            return s;
        }
        return null;
    }
}
