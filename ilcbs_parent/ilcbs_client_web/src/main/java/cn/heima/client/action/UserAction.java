package cn.heima.client.action;

import java.io.IOException;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import com.opensymphony.xwork2.ModelDriven;

import cn.heima.client.domain.UserClient;
import cn.heima.client.service.UserClientService;
import cn.heima.utils.ImageUtil;

@Namespace("/")
public class UserAction extends BaseAction {

	@Autowired
	private UserClientService userClientService;
	
	@Autowired
	private JmsTemplate jmsQueueTemplate;
	
	@Autowired
	private RedisTemplate<String, String> redisTemplate;
	
	private String telephone;
	private String vercode;
	private String phoneVercode;
	private String email;
	
	public String getVercode() {
		return vercode;
	}

	public void setVercode(String vercode) {
		this.vercode = vercode;
	}

	public String getPhoneVercode() {
		return phoneVercode;
	}

	public void setPhoneVercode(String phoneVercode) {
		this.phoneVercode = phoneVercode;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	@Action(value="userAction_genActiveCode")
	public String genActiveCode() throws IOException {
		
		HttpServletResponse response = ServletActionContext.getResponse();
		
		String rundomStr = ImageUtil.getRundomStr();
		
		session.put("rundomStr", rundomStr);
		//一定要将保存的session放在发送的图片上面否则发送不出去
		ImageUtil.getImage(rundomStr, response.getOutputStream());
		
		
		
		return NONE;
	}
	
	@Action(value="userAction_sendVerCode")
	public String sendVerCode() throws Exception {
		// TODO Auto-generated method stub
//		System.out.println(telephone);
//		System.out.println(model);
		// 发送一条消息
		jmsQueueTemplate.send("ilcbs_client", new MessageCreator() {
			
			@Override
			public Message createMessage(Session session) throws JMSException {
				// TODO Auto-generated method stub
				MapMessage mapMessage = session.createMapMessage();
				mapMessage.setString("telephone", telephone);
				return mapMessage;
			}
		});
		
		return NONE;
	}
	
	
	
	@Action(value="userAction_register")
	public String register() throws Exception {
		String returnStr = "2";
		//判断图片验证码是否正确
		String rundomStr = (String) session.get("rundomStr");
		if(!rundomStr.equalsIgnoreCase(vercode)) {
			returnStr ="0";
		}
		//判断手机验证码是否正确
		String phoneCode = redisTemplate.opsForValue().get(telephone);
		if(!phoneCode.equalsIgnoreCase(phoneVercode)) {
			returnStr = "1";
		}
		//如果都正确清空图片缓存和redis之中的缓存
		if(returnStr.equals("2")) {
			session.remove("rundomStr");
			redisTemplate.delete(telephone);
		}
		UserClient userClient = new UserClient();
		
		userClient.setTelephone(telephone);
		userClient.setEmail(email);
		
		userClientService.saveOrUpdate(userClient);
		HttpServletResponse response = ServletActionContext.getResponse();
		response.getWriter().write(returnStr);
		return NONE;
	}

}
