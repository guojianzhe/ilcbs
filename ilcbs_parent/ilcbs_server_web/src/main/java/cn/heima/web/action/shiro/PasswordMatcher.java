package cn.heima.web.action.shiro;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.apache.shiro.crypto.hash.Md5Hash;

public class PasswordMatcher extends SimpleCredentialsMatcher{

	@Override
	public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
		
		System.out.println("调用了密码比较器");
		//从token获取输入的密码
		UsernamePasswordToken uToken = (UsernamePasswordToken) token;
		
		String password = new String(uToken.getPassword());
		
		
		Md5Hash md5Hash = new Md5Hash(password,uToken.getUsername(), 2);
		
		
		System.out.println(md5Hash.toString());
		//2.从info中获取密码(数据库中的用户密码)
		String dbPassword = (String)info.getCredentials();
		return equals(md5Hash.toString(),dbPassword);
	}

	
	
}
