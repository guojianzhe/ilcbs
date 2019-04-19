package cn.heima.test;

import org.apache.xbean.spring.context.ClassPathXmlApplicationContext;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

public class SpringMailTest {

	@Test
	public void test1() {
		
	ApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext-mail.xml");
		
		MailSender sender = (MailSender) ac.getBean("mailSender");
		SimpleMailMessage message = (SimpleMailMessage) ac.getBean("mailMessage");
		message.setTo("874375365@qq.com");
		message.setSubject("spring发送的");
		message.setText("spring发送简单很多");
		
		
		sender.send(message);
	}
}
