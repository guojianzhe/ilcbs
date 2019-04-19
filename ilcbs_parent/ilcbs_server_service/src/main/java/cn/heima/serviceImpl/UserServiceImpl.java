package cn.heima.serviceImpl;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import cn.heima.dao.UserDao;
import cn.heima.domain.User;
import cn.heima.service.UserService;
import cn.heima.utils.MailUtil;
import cn.heima.utils.SysConstant;
import cn.heima.utils.UtilFuns;
@Service("userService")
public class UserServiceImpl implements UserService{

	@Autowired
	private UserDao userDao;
	
	@Override
	public List<User> find(Specification<User> spec) {
		// TODO Auto-generated method stub
		return userDao.findAll(spec);
	}

	@Override
	public User get(String id) {
		
		return userDao.findOne(id);
	}

	@Override
	public Page<User> findPage(Specification<User> spec, Pageable pageable) {
		// TODO Auto-generated method stub
		return userDao.findAll(spec, pageable);
	}

	@Override
	public void saveOrUpdate(User entity) {

		if(UtilFuns.isEmpty(entity.getId())) {
			String uid = UUID.randomUUID().toString();
			
			entity.setId(uid);
			entity.getUserinfo().setId(uid);
			
			//准备用户md5密码
			
			Md5Hash md5Hash = new Md5Hash(SysConstant.DEFAULT_PASS, entity.getUserName(), 2);
			
			entity.setPassword(md5Hash.toString());
			
			String email = entity.getUserinfo().getEmail();
			String subject = entity.getUserName()+":欢迎您!";
			String content = "您的密码是:"+SysConstant.DEFAULT_PASS;
			
			
			try {
				MailUtil.sendMsg(email, subject, content);
			} catch (Exception e) {
				
				e.printStackTrace();
			}
			
			
		}

		
		
		userDao.save(entity);
		
	}

	@Override
	public void saveOrUpdateAll(Collection<User> entitys) {
		userDao.save(entitys);
		
	}

	@Override
	public void deleteById(String id) {
		userDao.delete(id);
		
	}

	@Override
	public void delete(String[] ids) {
		for (String id : ids) {
			userDao.delete(id);
		}
		
	}

}
