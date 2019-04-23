package cn.heima.serviceImpl;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import cn.heima.dao.ExportDao;
import cn.heima.domain.Export;
import cn.heima.service.ExportService;
import cn.heima.utils.UtilFuns;
@Service("exportService")
public class ExportServiceImpl implements ExportService{

	@Autowired
	private ExportDao exportDao;
	
	@Override
	public List<Export> find(Specification<Export> spec) {
		// TODO Auto-generated method stub
		return exportDao.findAll(spec);
	}

	@Override
	public Export get(String id) {
		
		return exportDao.findOne(id);
	}

	@Override
	public Page<Export> findPage(Specification<Export> spec, Pageable pageable) {
		// TODO Auto-generated method stub
		return exportDao.findAll(spec, pageable);
	}

	@Override
	public void saveOrUpdate(Export entity) {
		
		if(UtilFuns.isEmpty(entity.getId())) {
			
		}
		exportDao.save(entity);
	}

	@Override
	public void saveOrUpdateAll(Collection<Export> entitys) {
		exportDao.save(entitys);
		
	}

	@Override
	public void deleteById(String id) {
		exportDao.delete(id);
		
	}

	@Override
	public void delete(String[] ids) {
		for (String id : ids) {
			exportDao.delete(id);
		}
		
	}

}
