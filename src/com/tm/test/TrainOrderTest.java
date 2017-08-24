package com.tm.test;


import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ccservice.Util.db.DBHelper;
import com.ccservice.Util.db.DataRow;
import com.ccservice.Util.db.DataTable;
import com.ccservice.Util.time.TimeUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tm.entity.TrainMachine;
import com.tm.entity.TrainOrder;
import com.tm.entity.TrainPassenger;
import com.tm.persistence.TrainOrderMapper;
import com.tm.service.TrainMachineService;
import com.tm.service.TrainOrderService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/config/*.xml")
public class TrainOrderTest {
	static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
	@Autowired
	private TrainOrderService trainOrderService;
	@Autowired
	private TrainMachineService trainMachineService;
	@Autowired
	private TrainOrderMapper trainOrderMapper;
	
	@Test
    public void main() {
        String sql = "SELECT top 100 o.CreateTime,o.[PKId],createStatus,EndTime '截止时间',bespeakDateList '出行日期',WantedSeatType '备选坐席'"
                + ",o.fromcity,o.tocity,LoginName12306,LoginPassword12306,AllNeedTrainCode "
                + "FROM [B2B_DB_BESPEAK].[dbo].[TrainOrderBespeak] o with(nolock) "
                + "where 1=1 and createStatus=0 and LoginName12306!='' " + "and LoginPassword12306!='' "
                        + " and pkid >10570065 order by o.pkid ";
        try {
            DataTable dt = DBHelper.GetDataTable(sql);
            if (dt != null) {
                List<DataRow> listDr = dt.GetRow();
                for (int i = 0; i < listDr.size(); i++) {
                    DataRow dr = listDr.get(i);
                    String PKId = dr.GetColumnString("PKId");
                    String 截止时间 = dr.GetColumnString("截止时间");//2017-08-25 16:36
                    String 出行日期 = dr.GetColumnString("出行日期");
                    String 备选坐席 = dr.GetColumnString("备选坐席");
                    String fromcity = dr.GetColumnString("fromcity");
                    String tocity = dr.GetColumnString("tocity");
                    String LoginName12306 = dr.GetColumnString("LoginName12306");
                    String LoginPassword12306 = dr.GetColumnString("LoginPassword12306");
                    String AllNeedTrainCode = dr.GetColumnString("AllNeedTrainCode");//备选车次
                    //                    System.out.println(PKId+":"+截止时间+":"+出行日期+":"+备选坐席+":"+fromcity+":"+tocity+":"+LoginName12306+":"+LoginPassword12306+":"+AllNeedTrainCode);
                    TrainOrder order = new TrainOrder();
                    //10909260  长沙  唐山  2017-08-31  T370,Z14 硬卧 zhangnapefhajnef
                    order.setOrderNumber(UUID.randomUUID().toString());
                    order.setOrderNumberAgent(PKId);
                    order.setGrapTicketMachineNo("0");
                    order.setAgentId(48);
                    order.setOrderState(2);
                    order.setCreateTime(new Timestamp(new Date().getTime()));
                    order.setStartDate(出行日期);
                    order.setFrom_station_name(fromcity);
                    order.setTo_station_name(tocity);
                    order.setSecondTrainNo(AllNeedTrainCode);
                    order.setSecondSeats(备选坐席);
                    order.setOfficialAccount(LoginName12306);
                    order.setOfficialPassword(LoginPassword12306);
                    order.setDeadlineTime(TimeUtil.parseTimestampbyString("yyyy-MM-dd HH:mm", 截止时间));
                    List<TrainPassenger> passengers = new ArrayList<TrainPassenger>();
                    String sql_ticket = "select t.name,t.IdNumber,t.IdType,t.TicketType,t.Departure,t.Arrival "
                            + "from TrainTicketBespeak t with(nolock) where orderid=" + PKId + "";
                    DataTable dt_ticket = DBHelper.GetDataTable(sql_ticket);
                    if (dt_ticket != null) {
                        List<DataRow> listDr_ticket = dt_ticket.GetRow();
                        for (int j = 0; j < listDr_ticket.size(); j++) {
                            DataRow dr_ticket = listDr_ticket.get(j);
                            TrainPassenger a = new TrainPassenger();
                            a.setIdentifyNumber(dr_ticket.GetColumnString("IdNumber"));
                            a.setIdentifyType(dr_ticket.GetColumnInt("IdType"));
                            a.setName(dr_ticket.GetColumnString("name"));
                            a.setTicketType(dr_ticket.GetColumnString("TicketType"));
                            passengers.add(a);
                        }
                    }
                   
                    order.setPassengers(passengers);
                    System.out.println("test_insertOrder order:" + gson.toJson(order));
                    trainOrderService.insertOrder(order);

                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

	@Test
	public void test_insertOrder(){
		TrainOrder order=new TrainOrder();
		//10909260	长沙	唐山	2017-08-31	T370,Z14 硬卧 zhangnapefhajnef
		order.setOrderNumber(UUID.randomUUID().toString());
		order.setOrderNumberAgent("10909260");
		order.setGrapTicketMachineNo("1");
		order.setAgentId(48);
		order.setOrderState(2);
		order.setCreateTime(new Timestamp(new Date().getTime()));
		order.setStartDate("2017-08-31");
		order.setFrom_station_name("长沙");
		order.setTo_station_name("唐山");
		order.setSecondTrainNo("T370");
		order.setSecondSeats("硬卧");
		order.setOfficialAccount("zhangnapefhajnef");
		order.setOfficialPassword("asd123456");
		List<TrainPassenger> passengers=new ArrayList<TrainPassenger>();
		TrainPassenger a=new TrainPassenger();
		a.setIdentifyNumber("13434343");
		a.setIdentifyType(1);
		a.setName("fengyizhan");
		a.setTicketType("1");
		
		TrainPassenger b=new TrainPassenger();
		b.setIdentifyNumber("3434343434");
		b.setIdentifyType(1);
		b.setName("chendong");
		b.setTicketType("2");
		passengers.add(a);
		passengers.add(b);
		order.setPassengers(passengers);
		
		System.out.println("test_insertOrder order:"+gson.toJson(order));
		//trainOrderService.insertOrder(order);
		
	}
	
	@Test
	public void test_findByOrderNumber(){
		TrainOrder order = trainOrderService.findByOrderNumber("833c3681-8eae-4237-91cf-daad2bc70166");
		System.out.println("test_findByOrderNumber order:"+gson.toJson(order));
	}
	
	@Test
	public void test_findByMachineNumber(){
		TrainMachine machine = trainMachineService.findMachineByNumber("1");
		System.out.println("test_findByMachineNumber machine:"+gson.toJson(machine));
	}
	
	@Test
	public void test_findByMachineNo(){
		List<TrainOrder> list = trainOrderService.findByMachineNo("2");
		for(TrainOrder order:list)
		{
			System.out.println("test_findByMachineNo order:"+gson.toJson(order));
		}
	}
	
	@Test
	public void test_cancelByOrderNumber(){
		trainOrderService.cancelByOrderNumber("1428bb94-0da5-4eaa-91c6-1a17a856678f");
	}
	
	@Test
	public void test_updateOrder(){

		TrainOrder order=new TrainOrder();
		//10909260	长沙	唐山	2017-08-31	T370,Z14 硬卧 zhangnapefhajnef
		order.setOrderNumber("833c3681-8eae-4237-91cf-daad2bc70166");
		order.setOrderNumberAgent("10909261");
		order.setOrderNumber12306("orderNumber12306");
		order.setTotalPrice(305d);
		List<TrainPassenger> passengers=new ArrayList<TrainPassenger>();
		TrainPassenger a=new TrainPassenger();
		a.setPKID(8l);
		a.setIdentifyNumber("13434343abc");
		a.setIdentifyType(1);
		a.setName("fengyizhan123");
		a.setTicketType("1");
		a.setPrice(100d);
		a.setRoom_no("room01");
		a.setSeat_name("硬卧");
		a.setSeatType("abc");
		
		TrainPassenger b=new TrainPassenger();
		b.setPKID(9l);
		b.setIdentifyNumber("3434343434abc");
		b.setIdentifyType(1);
		b.setName("chendong123");
		b.setTicketType("1");
		b.setPrice(110d);
		b.setRoom_no("room02");
		b.setSeat_name("软卧");
		b.setSeatType("efg");
		passengers.add(a);
		passengers.add(b);
		order.setPassengers(passengers);
		trainOrderMapper.updateOrder(order);
		System.out.println("test_updateOrder order:"+gson.toJson(order));
		//trainOrderService.updateOrder(order);
	}
	
	@Test
	public void test_findByMachineNoByPage(){
		PageHelper.startPage(2,2);
		List<TrainOrder> results=trainOrderMapper.findByMachineNo("1");
		PageInfo pageInfo=new PageInfo(results);
		System.out.println("pageInfo total size:"+pageInfo.getTotal()+",total page:"+pageInfo.getPages());
		for(TrainOrder order:results)
		{
			System.out.println("test_findByMachineNo order:"+gson.toJson(order));
		}
	}
}
