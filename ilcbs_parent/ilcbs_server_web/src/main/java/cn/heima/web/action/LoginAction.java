package cn.heima.web.action;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.Subject;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.itextpdf.text.pdf.PdfStructTreeController.returnType;

import cn.heima.domain.User;
import cn.heima.utils.SysConstant;
import cn.heima.utils.UtilFuns;

/**
 * @Description: 登录和退出类
 * @Author:		传智播客 java学院	传智.宋江
 * @Company:	http://java.itcast.cn
 * @CreateDate:	2014年10月31日
 * 
 * 继承BaseAction的作用
 * 1.可以与struts2的API解藕合
 * 2.还可以在BaseAction中提供公有的通用方法
 */
@Namespace("/")
@Results({
	@Result(name="login",location="/WEB-INF/pages/sysadmin/login/login.jsp"),
	@Result(name="success",location="/WEB-INF/pages/home/fmain.jsp"),
	@Result(name="logout",location="/index.jsp")})
public class LoginAction extends BaseAction {

	private static final long serialVersionUID = 1L;

	private String username;
	private String password;



	//SSH传统登录方式
	@Action("loginAction_login")
	public String login() throws Exception {
		
//		if(true){
//			String msg = "登录错误，请重新填写用户名密码!";
//			this.addActionError(msg);
//			throw new Exception(msg);
//		}
//		User user = new User(username, password);
//		User login = userService.login(user);
//		if (login != null) {
//			ActionContext.getContext().getValueStack().push(user);
//			session.put(SysConstant.CURRENT_USER_INFO, login);	//记录session
//			return SUCCESS;
//		}
//		return "login";
		User user1 = (User)session.get(SysConstant.CURRENT_USER_INFO);
		System.out.println(user1);
//		if(UtilFuns.isEmpty(username)) {
//			return "login";
//		}else 
		if(user1!=null){
			System.out.println(user1);
			return "success";
		}else if(UtilFuns.isEmpty(username)){
			return "login";
		}
		
		Subject subject = SecurityUtils.getSubject();
		
		Md5Hash md5Hash = new Md5Hash(password, username, 2);
		
		UsernamePasswordToken token = new UsernamePasswordToken(username,md5Hash.toString());
		try {
			subject.login(token);
			User user = (User)subject.getPrincipal();
			session.put(SysConstant.CURRENT_USER_INFO, user);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			super.put("errorInfo", "您的用户名密码错误");
			return "login";
		}
			
	}
	
	
	//退出
	@Action("loginAction_logout")
	public String logout(){
		session.remove(SysConstant.CURRENT_USER_INFO);		//删除session
		SecurityUtils.getSubject().logout();   //登出
		return "logout";
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}

