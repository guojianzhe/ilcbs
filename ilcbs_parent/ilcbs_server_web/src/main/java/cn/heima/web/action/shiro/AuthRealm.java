package cn.heima.web.action.shiro;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;

import cn.heima.domain.User;
import cn.heima.service.UserService;

public class AuthRealm extends AuthorizingRealm{

	@Autowired
	private UserService userService;
	
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection arg0) {
		// TODO Auto-generated method stub
		System.out.println("调用了授权方法");
		return null;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken arg0) throws AuthenticationException {
		// TODO Auto-generated method stub
		System.out.println("调用了认证方法");
		UsernamePasswordToken token = (UsernamePasswordToken) arg0;
		
		final String username = token.getUsername();
		
		Specification<User> spec = new Specification<User>() {

			@Override
			public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				
				return cb.equal(root.get("userName").as(String.class), username);
			}
		};
		
		List<User> userList = userService.find(spec);
		if(userList!=null && userList.size()>0) {
			User user = userList.get(0);
			//主要对象 principal:主要对象(登陆的用户) credentials:密码
			
			return  new SimpleAuthenticationInfo(user, user.getPassword(),getName());
		}
		
		return null;
	}

}
