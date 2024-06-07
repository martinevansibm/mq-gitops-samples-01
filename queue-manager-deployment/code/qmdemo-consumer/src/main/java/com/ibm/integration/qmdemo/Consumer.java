package com.ibm.integration.qmdemo;

import javax.jms.Message;
import javax.jms.MessageConsumer;

import java.net.MalformedURLException;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.Session;
import com.ibm.mq.jms.MQConnectionFactory;
import com.ibm.msg.client.wmq.WMQConstants;
import java.net.MalformedURLException;
import java.net.URL;

public class Consumer {

        public static void main(String[] args) {


                URL chanTab = null;

		try {
			chanTab = new URL("http://ccdt-service.mq-demo.svc.cluster.local:8080/ccdt.json");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
       
                try {

                        MQConnectionFactory cf = new MQConnectionFactory();

                        cf.setCCDTURL(chanTab);
                        cf.setQueueManager("*ANY_QM");
                        cf.setTransportType(WMQConstants.WMQ_CM_CLIENT);
                        cf.setAppName("MY-CONSUMER");
                        cf.setClientReconnectOptions(WMQConstants.WMQ_CLIENT_RECONNECT);
                        cf.setClientReconnectTimeout(320);
        

                        Connection con = null;

                        System.out.println(cf.toString());

                        System.out.println("Starting Consumer - creating connection...");
                        con = cf.createConnection("app","newpassword");
                        System.out.println("Creating session...");
                        Session session = con.createSession(false,Session.AUTO_ACKNOWLEDGE);
                        Destination getFrom = session.createQueue("APP.DEMO.1");
                        MessageConsumer consumer = session.createConsumer(getFrom);
                        
                        int messageCount=1;
                        con.start();
                        
                        while (true) {

        					Message message = consumer.receive();
        					System.out.println("Received message, count: " + messageCount);
        					messageCount++;
                                                Thread.sleep(500);
        				}
                        

                } catch (Exception e) {

                        e.printStackTrace();
                        for (Throwable cause = e.getCause(); cause != null; cause = cause.getCause()) {
                           System.err.println("Caused by:");
                           cause.printStackTrace();
                        }

                }
}
}
