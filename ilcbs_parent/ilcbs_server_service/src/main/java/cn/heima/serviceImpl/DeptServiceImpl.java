package cn.heima.serviceImpl;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import cn.heima.dao.DeptDao;
import cn.heima.domain.Dept;
import cn.heima.service.DeptService;
import cn.heima.utils.UtilFuns;
@Service("deptService")
public class DeptServiceImpl implements DeptService{

	@Autowired
	private DeptDao deptDao;
	
	@Override
	public List<Dept> find(Specification<Dept> spec) {
		// TODO Auto-generated method stub
		return deptDao.findAll(spec);
	}

	@Override
	public Dept get(String id) {
		
		return deptDao.findOne(id);
	}

	@Override
	public Page<Dept> findPage(Specification<Dept> spec, Pageable pageable) {
		// TODO Auto-generated method stub
		return deptDao.findAll(spec, pageable);
	}

	@Override
	public void saveOrUpdate(Dept entity) {
		
		if(UtilFuns.isEmpty(entity.getId())) {
			entity.setState(1);
		}
		
		deptDao.save(entity);
		
	}

	@Override
	public void saveOrUpdateAll(Collection<Dept> entitys) {
		deptDao.save(entitys);
		
	}

	@Override
	public void deleteById(String id) {
		deptDao.delete(id);
		
	}

	@Override
	public void delete(String[] ids) {
		for (String id : ids) {
			deptDao.delete(id);
		}
		
	}

}
