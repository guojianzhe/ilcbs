package cn.heima.serviceImpl;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import cn.heima.dao.ExportProductDao;
import cn.heima.domain.ExportProduct;
import cn.heima.service.ExportProductService;
import cn.heima.utils.UtilFuns;
@Service("exportProductService")
public class ExportProductServiceImpl implements ExportProductService{

	@Autowired
	private ExportProductDao exportProductDao;
	
	@Override
	public List<ExportProduct> find(Specification<ExportProduct> spec) {
		// TODO Auto-generated method stub
		return exportProductDao.findAll(spec);
	}

	@Override
	public ExportProduct get(String id) {
		
		return exportProductDao.findOne(id);
	}

	@Override
	public Page<ExportProduct> findPage(Specification<ExportProduct> spec, Pageable pageable) {
		// TODO Auto-generated method stub
		return exportProductDao.findAll(spec, pageable);
	}

	@Override
	public void saveOrUpdate(ExportProduct entity) {
		
		if(UtilFuns.isEmpty(entity.getId())) {
			
		}
		
		exportProductDao.save(entity);
		
	}

	@Override
	public void saveOrUpdateAll(Collection<ExportProduct> entitys) {
		exportProductDao.save(entitys);
		
	}

	@Override
	public void deleteById(String id) {
		exportProductDao.delete(id);
		
	}

	@Override
	public void delete(String[] ids) {
		for (String id : ids) {
			exportProductDao.delete(id);
		}
		
	}

}
