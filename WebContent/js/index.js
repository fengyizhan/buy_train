window.kafka={};
var $scope=window.kafka;
function bindAutoRefreshStorageMachineList()
{
	var storageMachineRefreshEnable=$("#storageMachineRefreshEnable");
	var storageMachineRefreshTime=$("#storageMachineRefreshTime");
	
	storageMachineRefreshEnable.change(function()
			{
				var checked=$(this).attr("checked");
				if(checked)
				{
					try{
						window.clearInterval($scope.storageMachineRefreshTime);
					}catch(e){}
					$scope.storageMachineRefreshTime=storageMachineRefreshTime.val()*1000;
					window.setInterval(function()
							{
								refreshStorageMachineList();
							}, $scope.storageMachineRefreshTime);
				}else
				{
					try{
						window.clearInterval($scope.storageMachineRefreshTime);
					}catch(e){}
				}
			});
}
function refreshStorageMachineList(){
	var storageMachineList=$("#storageMachineList");
	storageMachineList.attr('src', storageMachineList.attr('src'));
}
function addStorageMachine(){
	var form = document.getElementById("storageMachineListForm");
	form.action = window.contextPath+"/manage/addStorageMachine";
	form.method="post";
	form.submit();
}
function kafkaQueryFormSubmit()
{
	var partitionChart=document.getElementById("partitionChart");
	var topicListIframe=$("#topicList");
	if(!partitionChart)
	{
		var partitionCharts=topicListIframe.contents().find("#partitionChart");
		partitionChart=partitionCharts[0];
	}
	$(partitionChart).hide();
	$(partitionChart).appendTo(document.body);
	$("#kafkaQueryForm").submit();
	$scope.searchTopicForm.queryTopic();
}
function setIframeHeight(iframe)
{
	if (iframe)
	{
		var iframeWin = iframe.contentWindow || iframe.contentDocument.parentWindow;
		if (iframeWin.document.body) 
		{
//			var dynamicHeight=iframeWin.document.documentElement.scrollHeight || iframeWin.document.body.scrollHeight;
			//fix the height bug
			var dynamicHeight=$(iframe).contents().find(".kafkaStructure").height()+50;
			$(iframe).css("height",dynamicHeight+"px");
		}
	}
}
function showDetailPartition(group,topic,partition,btnObj)
{
	var eventObj=$(btnObj).parent().parent();
	window.kafka.searchForm.data.group=group;
	window.kafka.searchForm.data.topic=topic;
	window.kafka.searchForm.data.partition=partition;
	var detailTable=$(eventObj).parent().parent();
	var detailTable_width=detailTable.width();
	var partitionTables=detailTable.parents(".kafkaStructure").find(".partitionTable");
	partitionTables.find("tr").removeClass("table_partitionTable_tr_click");
	$(eventObj).addClass("table_partitionTable_tr_click");
	var partitionDetailShow=$(eventObj).parents("td.partitionDetailShow");
	var partitionChart=document.getElementById("partitionChart");
	var topicListIframe=$("#topicList");
	if(!partitionChart)
	{
		var partitionCharts=topicListIframe.contents().find("#partitionChart");
		partitionChart=partitionCharts[0];
	}
	$(partitionChart).css("width",(parseInt(detailTable_width))+"px");
	window.kafka.searchForm.queryDetail(partitionChart);
	$(partitionChart).appendTo(partitionDetailShow);
	$(partitionChart).show();
	setIframeHeight(topicListIframe.get(0));
}
function buildPartitionChart()
{
	$scope.searchForm =
	{
		data:{},
		queryDetail :function(partitionChart){
			$scope.searchForm.data["offset_threshold"]=$("#offset_threshold").val();
			$scope.searchForm.data["start_msg_timestam"]=$("#start_msg_timestam").val();
			$scope.searchForm.data["end_msg_timestam"]=$("#end_msg_timestam").val();
			$scope.initChart(partitionChart);
			$.post(window.contextPath+'/manage/detailStatistics',$scope.searchForm.data,
				function(response){
					$scope.drawChart(response);
				}
				).error(function(response)
				{
					
				});
		}
	};
	
	$scope.initChart=function(partitionChart)
	{
		var action_partitionChart=document.getElementById('partitionChart');
		if(partitionChart)
		{
			action_partitionChart=partitionChart;
		}
		var partitionChart = echarts.init(action_partitionChart);
		partitionChart.clear();
		var loadingOption={
			text:"数据加载中...",
			effect:"bar",
			effectOption:{
                backgroundColor:"#fff",
                opacity:0.5
            },
			textStyle : {
		        fontSize : 20
		    }
		};
		partitionChart.showLoading(loadingOption);
		$scope.partitionChart=partitionChart;
	};
	
	$scope.drawChart=function(data)
	{
		var partitionChart=$scope.partitionChart;
		var state=data.state;
		if(!state)
		{
			window.wxc.xcConfirm("后台数据异常，请稍候重试！","info");
			return;
		}
		var timeList=data.data.timeList;
		var lagList=data.data.lagList;
		var tooltipList=data.data.tooltipList;
		
		var axisData = timeList;
		            var option = {
		            		toolbox: {
		            	        show : false,
		            	        feature : {
		            	            mark : {show: true},
		            	            dataView : {show: true, readOnly: false},
		            	            magicType : {show: true, type: ['line', 'bar']},
		            	            restore : {show: true},
		            	            saveAsImage : {show: true}
		            	        }
		            	    },
		        			noDataLoadingOption:{
		        				text:"无数据",
		        				effect:"bar",
		        				textStyle : {
		        			        fontSize : 20
		        			    }
		        			},
		                tooltip : {
		                    trigger: 'axis',
		                    showDelay: 0,             // 显示延迟，添加显示延迟可以避免频繁切换，单位ms
		                    formatter: function (params) {
		                        var res = params[0].name;
		                        res += '<br/>' + params[0].seriesName;
		                        res += ': ' + params[0].value;
		                        var dataIndex=params[0].dataIndex;
		                        try
		                        {
		                        	var noteArray=tooltipList[dataIndex].split("|");
		                        	var note5=noteArray[4];
		                        	if(note5)
		                        	{
		                        		res += '<br/>数据总量';
				                        res += ': ' + note5;
		                        	}
		                        	var note1=noteArray[0];
		                        	if(note1)
		                        	{
		                        		res += '<br/>偏移量';
				                        res += ': ' + note1;
		                        	}
		                        	var note2=noteArray[1];
		                        	if(note2)
		                        	{
		                        		res += '<br/>创建时间';
				                        res += ': ' + note2;
		                        	}
		                        	var note3=noteArray[2];
		                        	if(note3)
		                        	{
		                        		res += '<br/>最后修改时间';
				                        res += ': ' + note3;
		                        	}
		                        	var note4=noteArray[3];
		                        	if(note4)
		                        	{
		                        		res += '<br/>统计批次号';
				                        res += ': ' + note4;
		                        	}
		                        }catch(e)
		                        {
		                        	
		                        }
		                        return res;
		                    }
		                },
		                legend: {
		                    data:['主题消息剩余数量']
		                },
		                dataZoom : {
		                    y: 360,
		                    show : true,
		                    realtime: true,
		                    start : 0,
		                    end : 100
		                },
		                grid: {
		                    left: '3%',
		                    right: '4%',
		                    bottom: '50',
		                    containLabel: true
		                },
		                xAxis : [
		                    {
		                        type : 'category',
		                        boundaryGap : true,
		                        axisLabel:{show:false},
		                        axisTick: {onGap:false},
		                        splitLine: {show:false},
		                        data : axisData
		                    }
		                ],
		                yAxis : [
		                    {
		                        type : 'value',
		                        scale:true,
		                        boundaryGap: [0.05, 0.05],
		                        splitArea : {show : true},
		            	        axisLabel: {
		                            formatter: function (v) {
		                                return v + ' 条'
		                            }
		                        }
		                    }
		                ],
		                series : [
		                    {
		                        name:'主题消息剩余数量',
		                        type:'line',
		                        data:lagList
		                    }
		                    
		                ]
		            };
		            partitionChart.setOption(option);
		            window.setTimeout(function (){
		                window.onresize = function () {
		                	partitionChart.resize();
		                }
		                partitionChart.hideLoading();
		            },200);
		                                
	};
}
function buildTopicChart()
{
	$scope.searchTopicForm =
	{
		data:{},
		queryTopic :function(){
			$scope.searchTopicForm.data["offset_threshold"]=$("#offset_threshold").val();
			$scope.searchTopicForm.data["start_msg_timestam"]=$("#start_msg_timestam").val();
			$scope.searchTopicForm.data["end_msg_timestam"]=$("#end_msg_timestam").val();
			$scope.initTopicChart();
			$.post(window.contextPath+'/manage/topicStatistics',$scope.searchTopicForm.data,
				function(response){
					$scope.drawTopicChart(response);
				}
				).error(function(response)
				{
					
				});
		}
	};
	
	$scope.initTopicChart=function()
	{
		var topicChart=document.getElementById('topicChart');
		$(topicChart).css("width",($(window).width()-80)+"px");
		var topicChart = echarts.init(topicChart);
		topicChart.clear();
		var loadingOption={
			text:"数据加载中...",
			effect:"bar",
			effectOption:{
                backgroundColor:"#fff",
                opacity:0.5
            },
			textStyle : {
		        fontSize : 20
		    }
		};
		topicChart.showLoading(loadingOption);
		$scope.topicChart=topicChart;
	};
	
	$scope.drawTopicChart=function(data)
	{
		var topicChart=$scope.topicChart;
		var state=data.state;
		if(!state)
		{
			window.wxc.xcConfirm("后台数据异常，请稍候重试！","info");
			return;
		}
		var topic_series=[];
		
		var topicMap=data.data.topicMap;
		var tootipMap=data.data.tootipMap;
		var topicListArray=[];
		var lagPMap=[];
		var maxPartitionNumber=5;
		for(var i=0;i<maxPartitionNumber;i++)
		{
			lagPMap[i]=[];
		}
		for(var topic in topicMap)
		{
			topicListArray.push(topic);
			var partitionLagNumberMap=topicMap[topic];
			for(var i=0;i<5;i++)
			{
				var p_lag=partitionLagNumberMap[i];
				lagPMap[i].push(p_lag);
			}
		}
		for(var i=0;i<maxPartitionNumber;i++)
		{
			var serie_data=lagPMap[i];
			var serie_object=
			{
	            name:'分区'+i,
	            type:'bar',
	            barWidth : 6,
	            data:serie_data
            };
			topic_series.push(serie_object);
		}
		var axisData = topicListArray;
		            var option = {
		            		toolbox: {
		            	        show : false,
		            	        feature : {
		            	            mark : {show: true},
		            	            dataView : {show: true, readOnly: false},
		            	            magicType : {show: true, type: ['line', 'bar']},
		            	            restore : {show: true},
		            	            saveAsImage : {show: true}
		            	        }
		            	    },
		        			noDataLoadingOption:{
		        				text:"无数据",
		        				effect:"bar",
		        				textStyle : {
		        			        fontSize : 20
		        			    }
		        			},
	        			tooltip: {
		        	            trigger: 'item',  
		        	            formatter: function(params) {  
		        	                var res = params.name+'<br/>';
		        	                var allnotes=tootipMap[params.name+"_"+params.seriesIndex];
		        	                var allnotesArray = allnotes.split('|');
		        	                var note0=allnotesArray[0];
		        	                var note1=allnotesArray[1];
		        	                var note2=allnotesArray[2];
		        	                var note3=allnotesArray[3];
		        	                var note4=allnotesArray[4];
		        	                var note5=allnotesArray[5];
		        	                var note6=allnotesArray[6];
		        	                
		        	                if(note4)
		        	                {
		        	                	res+="<b>"+params.seriesName+"</b>"+'</br></br>';  
		        	                }
		        	                if(note5)
		        	                {
		        	                	res+="总量" +' : '+note5+'</br>';  
		        	                }
		        	                if(note6)
		        	                {
		        	                	res+="剩余量" +' : '+note6+'</br>';  
		        	                }
		        	                if(note0)
		        	                {
		        	                	res+="偏移量" +' : '+note0+'</br>';  
		        	                }
		        	                if(note1)
		        	                {
		        	                	res+="创建时间" +' : '+note1+'</br>';  
		        	                }
		        	                if(note2)
		        	                {
		        	                	res+="最后修改时间" +' : '+note2+'</br>';  
		        	                }
		        	                if(note3)
		        	                {
		        	                	res+="批次号" +' : '+note3+'</br>';  
		        	                }
		        	                return res;  
		        	            }
		        	    },
		        	    legend: {
		                    data:['分区0','分区1','分区2','分区3','分区4']
		                },
		                grid: {
		                    left: '3%',
		                    right: '4%',
		                    bottom: '50',
		                    containLabel: true
		                },
		                yAxis : [
		                    {
		                        type : 'category',
		                        boundaryGap : true,
		                        data : axisData
		                    }
		                ],
		                xAxis : [
		                    {
		                        type : 'value'
		                    }
		                ],
		                series : topic_series
		            };
		            topicChart.setOption(option);
		            window.setTimeout(function (){
		                window.onresize = function () {
		                	topicChart.resize();
		                }
		                topicChart.hideLoading();
		            },200);
		                                
	};
}
$(document).ready(
		function()
		{
			buildTopicChart();
			buildPartitionChart();
			bindAutoRefreshStorageMachineList();
		}		

);