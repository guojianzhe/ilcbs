package cn.heima.jms;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.aliyuncs.exceptions.ClientException;

import cn.heima.utils.RandomCode;
import cn.heima.utils.SmsUtils;

@Component
public class QueueListener implements MessageListener{

	@Autowired
	private RedisTemplate<String, String> redisTemplate;
	
	@Override
	public void onMessage(Message arg0) {
		
		try {
			MapMessage message = (MapMessage) arg0;
			String telephone = message.getString("telephone");
			System.out.println(telephone);
			//向该手机号发送随机验证码
			String genCode = RandomCode.genCode() +"";
			
			SmsUtils.sendSms(telephone, genCode);
			//将短信验证码存到redis
			redisTemplate.opsForValue().set(telephone, genCode);
			
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
}
