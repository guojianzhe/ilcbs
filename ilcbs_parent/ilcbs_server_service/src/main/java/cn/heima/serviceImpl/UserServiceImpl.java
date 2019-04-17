package cn.heima.serviceImpl;

import java.util.Collection;
import java.util.List;
<<<<<<< HEAD
import java.util.UUID;
=======
>>>>>>> 651dbe2e76e9a8ab698735b03e8153a6d32a5f83

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
		
<<<<<<< HEAD
		if(UtilFuns.isEmpty(entity.getId())) {
			String uid = UUID.randomUUID().toString();
			
			entity.setId(uid);
			entity.getUserinfo().setId(uid);
			
		}
=======
>>>>>>> 651dbe2e76e9a8ab698735b03e8153a6d32a5f83
		
		
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
