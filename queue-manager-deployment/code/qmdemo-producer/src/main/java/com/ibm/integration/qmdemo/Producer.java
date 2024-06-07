package com.ibm.integration.qmdemo;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;

import java.net.MalformedURLException;
import java.net.URL;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.Session;
import com.ibm.mq.jms.MQConnectionFactory;
import com.ibm.msg.client.wmq.WMQConstants;

import java.net.MalformedURLException;
import java.net.URL;


public class Producer {

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
			cf.setAppName("MY-PRODUCER");
			cf.setClientReconnectOptions(WMQConstants.WMQ_CLIENT_RECONNECT);
			cf.setClientReconnectTimeout(320);

			Connection con = null;
			
			System.out.println(cf.toString());

			System.out.println("Starting Producer - creating connection...");
			con = cf.createConnection("app","newpassword");
			System.out.println("Creating session...");
			Session session = con.createSession(false,Session.AUTO_ACKNOWLEDGE);
			Destination sendTo = session.createQueue("DEV.QUEUE.1");
			MessageProducer producer = session.createProducer(sendTo);

			Message msg = session.createTextMessage("Test some data here"); 

			System.out.println("Sending...");

				for (int i = 0; i < 1000; i++) {
					producer.send(msg);
					Thread.sleep(3000);
				}

			con.close();
			System.out.println("Finished...");

		} catch (Exception e) {

                        e.printStackTrace();
                        for (Throwable cause = e.getCause(); cause != null; cause = cause.getCause()) {
                           System.err.println("Caused by:");
                           cause.printStackTrace();
                        }

		}
}
}
