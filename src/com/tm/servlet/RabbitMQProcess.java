package com.tm.servlet;

import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.QueueingConsumer.Delivery;

/**
 * 加解密通讯MQ工具类
 * @author fyz
 *
 */

public class RabbitMQProcess {
	static ConnectionFactory connFac = new ConnectionFactory() ;
	static
	{
        connFac.setUsername("app");
        connFac.setPassword("nI8Faitd6PvpAL61");
        connFac.setVirtualHost("/");
        connFac.setHost("101.236.34.150");
	}
    public static void SendMessage(String msg) throws Exception
    {
        
        //创建一个连接
        Connection conn = connFac.newConnection() ;
        //创建一个渠道
        Channel channel = conn.createChannel() ;
        //定义Queue名称
        String queueName = "noAction" ;
        //为channel定义queue的属性，queueName为Queue名称
        channel.queueDeclare( queueName , false, false, false, null) ;
        //发送消息
        channel.basicPublish("", queueName , null , msg.getBytes());

        channel.close();
        conn.close();
    }

    public static void recMessage(String queue) throws Exception
    {
        
        Connection conn = connFac.newConnection() ;
        Channel channel = conn.createChannel() ;
        String queueName = "noAction";
        if(queue!=null)
        {
        	queueName=queue;
        }
        channel.queueDeclare(queueName, false, false, false, null) ;

        QueueingConsumer consumer = new QueueingConsumer(channel) ;
        channel.basicConsume(queueName, false, consumer) ;
        while(true){
        	if(!channel.isOpen())
        	{
        		break;
        	}
            Delivery delivery = consumer.nextDelivery() ;

            String msg = new String(delivery.getBody()) ;
            System.out.println("msg:"+msg);
            Gson gson = new Gson();
            MessageEntity ent = gson.fromJson(msg,MessageEntity.class);
            if(ent.enType==0) //加密
            {
                System.out.println("加密");
            	//ent.setValue((String) MainHook.checkcode.invoke(null,"",ent.value));
            }
            else if(ent.enType==1) //解密
            {
            	System.out.println("解密");
                //ent.setValue((String) MainHook.decheckcode.invoke(null,"",ent.value));
            }

            //发送结果
            //SendMessage(gson.toJson(ent));
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
        }

    }

    public static void main(String[] args) throws Throwable {

        SendMessage("{\"Id\":\"9527\",\"enType\":0,\"value\":\"asdfasdf\"}");
        //recMessage("actioned");
    }
}
