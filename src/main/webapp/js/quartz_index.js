$(function(){
	$('#quartz_list').treegrid({   
	  url:'../json/treegrid_data.json',   

      method: "GET",
      loadMsg: "数据加载中，请稍后...",
      idField: 'id',
	   treeField:'name', 
	   animate:true,
	   fitColumns: true,
       fit:true,
	   toolbar:[
		   {
			   text:'Add',
				iconCls:'icon-add',
				handler:function(){alert('add')}
		   },{
			   text:'Cut',
				iconCls:'icon-cut',
				handler:function(){alert('cut')}
		   },'-',{
				text:'Save',
				iconCls:'icon-save',
				handler:function(){alert('save')}
			}
	   ],   
	   columns:[[
			   {field:'id',title:'编码ID',width:30,hidden:true}, 
		       {field:'name',title:'任务名称',width:70},   
		       {field:'status',title:'任务状态',width:50,align:'center',
		    	   formatter:function(value,rowData,rowIndex){
		    		   //调度状态 ：1 运行,2停止(disable=true时 状态必须为2,状态为2时不一定disable为true),3暂停  ,4 类异常,5 表达式异常-
		    		   if(value==1){
		    			   return "运行中";
		    		   }else if(value==2){
		    			   return "停止";
		    		   }else if(value==3){
		    			   return "暂停";
		    		   }else if(value==4){
		    			   return "异常:路径不存在";
		    		   }else if(value==5){
		    			   return "异常:表达式不正确";
		    		   }
		    		   return value;
		    	   }
		       },   
		       {field:'class',title:'任务路径',width:50,hidden:true},   
		       {field:'expression',title:'执行表达式',width:50,
		    	   formatter:function(value,rowData,rowIndex){
		    		   //1.表达式,2.时分秒,3.间隔秒,4.间隔分,5.间隔小时 
		    		   if(value==null || value==""){
		    			   return "";
		    		   }
		    		   var cols = value.split(" ");
		    		   if(cols.length<6){
		    			   return value;
		    		   }
		    		   var type = rowData.type;
		    		   if(type==1){
		    			   return value;
		    		   }else if(type==2){
	    				   return cols[2]+":"+cols[1]+":"+cols[0];
		    		   }else if(type==3){
		    			   var second =  cols[0].split("/");
		    			   return second[1];
		    		   }else if(type==4){
		    			   var second =  cols[1].split("/");
		    			   return second[1];
		    		   }else if(type==5){
		    			   var second =  cols[2].split("/");
		    			   return second[2];
		    		   }
		    	   }
		       	}
		    	 ,{field:'task_object',title:'任务JSON参数',width:80}
		    	 ,{field:'detailed_description',title:'任务说明',width:80}
		      /* ,{field:'operation',title:'操作',width:120,
	    		   formatter:function(value,rowData,rowIndex){
	    			   //调度状态 ：1 运行,2停止(disable=true时 状态必须为2,状态为2时不一定disable为true),3暂停  ,4 类异常,5 表达式异常-
	    			   if(rowData.status==1){
	    				   return value;
	    			   }else if(rowData.status==2){
	    				   return value;
	    			   }else if(rowData.status==3){
	    				   return value;
	    			   }else if(rowData.status==4){
	    				   return value;
	    			   }else if(rowData.status==5){
	    				   return value;
	    			   }
	    			   return value;
	    		   }
		       		,styler:function(value,rowData,rowIndex){
		       			
		       		}
		       }*/
	      ]]
			,rowStyler:function(rowIndex,rowData){
				//rowIndex： 行的索引，从 0 开始。
				//rowData： 此行相应的记录。
				//1 运行,2停止(disable=true时 状态必须为2,状态为2时不一定disable为true),3暂停  ,4 类异常,5 表达式异常-
				var startCss = "";
				var endCss = "";
				var ie678 = "FILTER: progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#b8c4cb,endColorStr=red);";//IE6,7,8
				var ie10= 	"background: -ms-linear-gradient(top, #fff,  #0000ff); ";//IE10
				var fiox = "background:-moz-linear-gradient(top,#b8c4cb,#f6f6f8);";//
				var Chrome = "background:-webkit-gradient(linear, 0% 0%, 0% 100%,from(#b8c4cb), to(#f6f6f8));";//Chrome
				var Safari = "background: -webkit-gradient(linear, 0% 0%, 0% 100%, from(#fff), to(#0000ff)); ";//Safari 4-5, Chrome 1-9
				var Safari = "background: -webkit-linear-gradient(top, #fff, #0000ff);";//Safari5.1 Chrome 10+
				var Operaa = "background: -o-linear-gradient(top, #fff, #0000ff); ";//Opera 11.10+
				if(typeof rowIndex =='object'){
					if(rowIndex.status==1){
						//return  'background:#98FB98'; 
					}else if(rowIndex.status==2){
						//return 'background:#FFE4E1' ;
					}
				}
			}
	 });  
});
function isInJSON (json,value){
	for(var key in json){
       if(key==value){
    	   return true;
       }
    }
    return false;
}
function isInArray (arr,value){
	for(var i = 0; i < arr.length; i++){
        if(value === arr[i]){
            return true;
        }
    }
    return false;
}