$(document).ready(
		function(){
			window.kafka={};
			var $scope=window.kafka;
			$scope.searchForm =
			{
				submit :function(){
					$scope.searchForm.data = {
							"offset_threshold":$("#offset_threshold").val(),
							"start_msg_timestam": $("#start_msg_timestam").val(),
							"end_msg_timestam":$("#end_msg_timestam").val()
					};
					$scope.initChart();
					$.post(window.contextPath+'/manage/statistics',$scope.searchForm.data,
						function(response){
							$scope.drawChart(response);
						}
						).error(function(response)
						{
							
						});
				}
			};
			
			$scope.initChart=function()
			{
				var topicChart = echarts.init(document.getElementById('topicChart'));
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
			
			$scope.drawChart=function(data)
			{
				var topicChart=$scope.topicChart;
				var state=data.state;
				if(!state)
				{
					window.wxc.xcConfirm("后台数据异常，请稍候重试！","info");
					return;
				}
				var timeList=data.data.timeList;
				var logSizeList=data.data.logSizeList;
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
				                        	var note1=noteArray[0];
				                        	if(note1)
				                        	{
				                        		res += '<br/>偏移量：';
						                        res += ': ' + note1;
				                        	}
				                        	var note2=noteArray[1];
				                        	if(note2)
				                        	{
				                        		res += '<br/>创建时间：';
						                        res += ': ' + note2;
				                        	}
				                        	var note3=noteArray[2];
				                        	if(note3)
				                        	{
				                        		res += '<br/>最后修改时间：';
						                        res += ': ' + note3;
				                        	}
				                        	var note4=noteArray[3];
				                        	if(note4)
				                        	{
				                        		res += '<br/>统计批次号：';
						                        res += ': ' + note4;
				                        	}
				                        }catch(e)
				                        {
				                        	
				                        }
				                        return res;
				                    }
				                },
				                legend: {
				                    data:['数据总量','剩余数量']
				                },
				                dataZoom : {
				                    y: 220,
				                    show : true,
				                    realtime: true,
				                    start : 0,
				                    end : 100
				                },
				                grid: {
				                    x: 60,
				                    y: 10,
				                    x2:20,
				                    y2:25
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
				                        name:'数据总量',
				                        type:'line',
				                        data:logSizeList
				                    },
				                    {
				                        name:'剩余数量',
				                        type:'line',
				                        data:lagList
				                    }
				                    
				                ]
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

);