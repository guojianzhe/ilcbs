package cn.heima.serviceImpl;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import cn.heima.dao.UserDao;
import cn.heima.domain.User;
import cn.heima.service.UserService;
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
