package cn.heima.test;

import java.util.Properties;

import javax.mail.Address;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.junit.Test;

public class MailTest {
	@Test
	public void test() throws Exception {
		
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.163.com");
		props.put("mail.smtp.auth", "true");
		Session session = Session.getInstance(props);
		session.setDebug(true);
		//构造信息体
		MimeMessage message = new MimeMessage(session);
		//发件地址
		Address address = new InternetAddress("guojianzhe789@163.com");
		message.setFrom(address);
		//收件地址
		InternetAddress toAddress = new InternetAddress("874375365@qq.com");
		message.setRecipient(MimeMessage.RecipientType.TO, toAddress);
		//主题
		message.setSubject("你好主题");
		
		//正文
		message.setText("你好正文");
		message.saveChanges();
		
		Transport transport = session.getTransport("smtp");
		transport.connect("smtp.163.com", "guojianzhe789@163.com", "gjz123456");
		transport.sendMessage(message, message.getAllRecipients());
		transport.close();
		
	}
}
