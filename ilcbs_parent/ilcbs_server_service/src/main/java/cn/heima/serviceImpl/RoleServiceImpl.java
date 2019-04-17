package cn.heima.serviceImpl;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import cn.heima.dao.RoleDao;
import cn.heima.domain.Role;
import cn.heima.service.RoleService;
import cn.heima.utils.UtilFuns;
@Service("roleService")
public class RoleServiceImpl implements RoleService{

	@Autowired
	private RoleDao roleDao;
	
	@Override
	public List<Role> find(Specification<Role> spec) {
		// TODO Auto-generated method stub
		return roleDao.findAll(spec);
	}

	@Override
	public Role get(String id) {
		
		return roleDao.findOne(id);
	}

	@Override
	public Page<Role> findPage(Specification<Role> spec, Pageable pageable) {
		// TODO Auto-generated method stub
		return roleDao.findAll(spec, pageable);
	}

	@Override
	public void saveOrUpdate(Role entity) {
		
		if(UtilFuns.isEmpty(entity.getId())) {
			
		}
		
		
		roleDao.save(entity);
		
	}

	@Override
	public void saveOrUpdateAll(Collection<Role> entitys) {
		roleDao.save(entitys);
		
	}

	@Override
	public void deleteById(String id) {
		roleDao.delete(id);
		
	}

	@Override
	public void delete(String[] ids) {
		for (String id : ids) {
			roleDao.delete(id);
		}
		
	}

}
