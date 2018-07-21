package org.shou.scheduler.job;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by admin on 2018/6/16.
 */
@DisallowConcurrentExecution
public class SchedulerTest implements Job{

    private static  final Logger logger = LoggerFactory.getLogger(SchedulerTest.class);

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println("jobExecutionContext = [" + jobExecutionContext + "]");
    }
}
