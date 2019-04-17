package cn.heima.serviceImpl;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import cn.heima.dao.ModuleDao;
import cn.heima.domain.Module;
import cn.heima.service.ModuleService;
import cn.heima.utils.UtilFuns;
@Service("moduleService")
public class ModuleServiceImpl implements ModuleService{

	@Autowired
	private ModuleDao moduleDao;
	
	@Override
	public List<Module> find(Specification<Module> spec) {
		// TODO Auto-generated method stub
		return moduleDao.findAll(spec);
	}

	@Override
	public Module get(String id) {
		
		return moduleDao.findOne(id);
	}

	@Override
	public Page<Module> findPage(Specification<Module> spec, Pageable pageable) {
		// TODO Auto-generated method stub
		return moduleDao.findAll(spec, pageable);
	}

	@Override
	public void saveOrUpdate(Module entity) {
		
		if(UtilFuns.isEmpty(entity.getId())) {
			
		}
		
		
		moduleDao.save(entity);
		
	}

	@Override
	public void saveOrUpdateAll(Collection<Module> entitys) {
		moduleDao.save(entitys);
		
	}

	@Override
	public void deleteById(String id) {
		moduleDao.delete(id);
		
	}

	@Override
	public void delete(String[] ids) {
		for (String id : ids) {
			moduleDao.delete(id);
		}
		
	}

}
