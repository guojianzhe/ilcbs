package cn.heima.serviceImpl;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import cn.heima.dao.FactoryDao;
import cn.heima.domain.Factory;
import cn.heima.service.FactoryService;
import cn.heima.utils.UtilFuns;
@Service("factoryService")
public class FactoryServiceImpl implements FactoryService{

	@Autowired
	private FactoryDao factoryDao;
	
	@Override
	public List<Factory> find(Specification<Factory> spec) {
		// TODO Auto-generated method stub
		return factoryDao.findAll(spec);
	}

	@Override
	public Factory get(String id) {
		
		return factoryDao.findOne(id);
	}

	@Override
	public Page<Factory> findPage(Specification<Factory> spec, Pageable pageable) {
		// TODO Auto-generated method stub
		return factoryDao.findAll(spec, pageable);
	}

	@Override
	public void saveOrUpdate(Factory entity) {
		
		if(UtilFuns.isEmpty(entity.getId())) {
			
		}
		
		factoryDao.save(entity);
		
	}

	@Override
	public void saveOrUpdateAll(Collection<Factory> entitys) {
		factoryDao.save(entitys);
		
	}

	@Override
	public void deleteById(String id) {
		factoryDao.delete(id);
		
	}

	@Override
	public void delete(String[] ids) {
		for (String id : ids) {
			factoryDao.delete(id);
		}
		
	}

}
