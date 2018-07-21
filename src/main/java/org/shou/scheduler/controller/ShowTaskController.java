package org.shou.scheduler.controller;

import javax.xml.bind.annotation.XmlElement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by admin on 2018/6/19.
 */
@Controller
@Scope(value="prototype")
@RequestMapping("task")
public class ShowTaskController {
    private static  final Logger logger = LoggerFactory.getLogger(ShowTaskController.class);

    @RequestMapping(value="{page}",method= RequestMethod.GET)
    public ModelAndView toPage(@PathVariable("page")String page){
        return new ModelAndView(String.format("task/%s", page));

    }
    
}
