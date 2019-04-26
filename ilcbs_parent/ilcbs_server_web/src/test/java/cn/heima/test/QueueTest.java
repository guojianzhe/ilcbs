package cn.heima.test;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.xml.soap.Text;

import org.apache.activemq.spring.ActiveMQConnectionFactory;
import org.junit.Test;

public class QueueTest {
	@Test
	public void testQueueSend() throws Exception {
		ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory();
		Connection connection = factory.createConnection();
		//开启连接
		connection.start();
		//创建session 1参数:是否使用事务  2参数应答的方式  常用的是自动应答
		Session session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
		Queue queue = session.createQueue("黑马297_queue");
		
		MessageProducer producer = session.createProducer(queue);
		for (int i = 0; i < 10; i++) {
			TextMessage message = session.createTextMessage();
			
			message.setText("hello"+i);
			
			producer.send(message);
		}
		
		
		session.commit();
		session.close();
		connection.close();
	}
	
	@Test
	public void testQueueReceiveListener() throws Exception {
		ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory();
		Connection connection = factory.createConnection();
		connection.start();
		
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		
		Queue queue = session.createQueue("黑马297_queue");
		
		MessageConsumer consumer = session.createConsumer(queue);
		consumer.setMessageListener(new MessageListener() {
			
			@Override
			public void onMessage(Message arg0) {
				
				try {
					TextMessage message = (TextMessage) arg0;
					String text = message.getText();
					System.out.println(text);
				} catch (JMSException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		while (true); 
	}
	
}
