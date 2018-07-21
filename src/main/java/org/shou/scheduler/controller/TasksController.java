package org.shou.scheduler.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.impl.StaticLoggerBinder;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by admin on 2018/6/9.
 */
@RestController("quartz")
public class TasksController {
    private static  final Logger logger = LoggerFactory.getLogger(TasksController.class);

//增删改
    public void aa(){
        StaticLoggerBinder.getSingleton().getLoggerFactory();
    }
}
