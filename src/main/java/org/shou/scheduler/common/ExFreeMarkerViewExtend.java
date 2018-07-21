package org.shou.scheduler.common;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.view.freemarker.FreeMarkerView;

public class ExFreeMarkerViewExtend extends FreeMarkerView {
	
	private final Logger logger =  LoggerFactory.getLogger(getClass());

	
	protected void exposeHelpers(Map<String, Object> model, HttpServletRequest request){
		try {
			super.exposeHelpers(model, request);
		} catch (Exception e) {
			logger.error( "FreeMarkerViewExtend 加载父类出现异常。请检查。",e);
		}
		model.put(Constant.CONTEXT_PATH, request.getContextPath());
		model.putAll(new LinkedHashMap<String,Object>());
		model.put("_time", new Date().getTime());
		model.put("NOW_YEAY", Constant.NOW_YEAY);//今年
		model.put("_v", Constant.VERSION);//版本号，重启的时间
		model.put("basePath", request.getContextPath());//base目录。
		
	}
}
