package org.shou.scheduler.common;

import net.sf.json.JSONObject;

@SuppressWarnings({ "serial", "rawtypes" })
public final class ServerResponse {
	
	private transient static final String  status = "status" ;
	private transient static final String msg = "msg";
	private transient static final String data = "data";
	private ServerResponse(){}
	
	@SuppressWarnings("unchecked")
	public  static  JSONObject createBySuccess(){
    	JSONObject json = new JSONObject(); 
    	json.put(ServerResponse.status, ResponseCode.SUCCESS.getCode());
        return json;
    }

    @SuppressWarnings("unchecked")
	public static JSONObject createBySuccessMessage(String msg){
    	JSONObject json =  new JSONObject();
    	json.put(ServerResponse.status, ResponseCode.SUCCESS.getCode());
    	json.put(ServerResponse.msg,msg);
        return json;
    }

    @SuppressWarnings("unchecked")
	public static JSONObject createBySuccess(Object data){
    	JSONObject json =  new JSONObject();
    	json.put(ServerResponse.status, ResponseCode.SUCCESS.getCode());
    	json.put(ServerResponse.data,data);
        return json;
    }

    @SuppressWarnings("unchecked")
	public static JSONObject createBySuccess(String msg,Object data){
    	JSONObject json =  new JSONObject();
    	json.put(ServerResponse.status, ResponseCode.SUCCESS.getCode());
    	json.put(ServerResponse.msg,msg);
    	json.put(ServerResponse.data,data);
        return json;
    }

    public static JSONObject createByError(){
        return createByErrorMessage(ResponseCode.ERROR.getDesc());
    }

    public static JSONObject createByErrorMessage(String errorMessage){
        return createByErrorCodeMessage(ResponseCode.ERROR,errorMessage);
    }

    @SuppressWarnings("unchecked")
	public static JSONObject createByErrorCodeMessage(ResponseCode errorCode,String errorMessage){
    	JSONObject json =  new JSONObject();
    	json.put(ServerResponse.status, errorCode.getCode());
    	json.put(ServerResponse.msg,errorMessage);
        return json;
    }

}
