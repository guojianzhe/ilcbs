package cn.heima.serviceImpl;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import cn.heima.dao.ContractProductDao;
import cn.heima.domain.ContractProduct;
import cn.heima.service.ContractProductService;
import cn.heima.utils.UtilFuns;
@Service("contractProductService")
public class ContractProductServiceImpl implements ContractProductService{

	@Autowired
	private ContractProductDao contractProductDao;
	
	@Override
	public List<ContractProduct> find(Specification<ContractProduct> spec) {
		// TODO Auto-generated method stub
		return contractProductDao.findAll(spec);
	}

	@Override
	public ContractProduct get(String id) {
		
		return contractProductDao.findOne(id);
	}

	@Override
	public Page<ContractProduct> findPage(Specification<ContractProduct> spec, Pageable pageable) {
		// TODO Auto-generated method stub
		return contractProductDao.findAll(spec, pageable);
	}

	@Override
	public void saveOrUpdate(ContractProduct entity) {
		
		if(UtilFuns.isEmpty(entity.getId())) {
			
		}
		
		contractProductDao.save(entity);
		
	}

	@Override
	public void saveOrUpdateAll(Collection<ContractProduct> entitys) {
		contractProductDao.save(entitys);
		
	}

	@Override
	public void deleteById(String id) {
		contractProductDao.delete(id);
		
	}

	@Override
	public void delete(String[] ids) {
		for (String id : ids) {
			contractProductDao.delete(id);
		}
		
	}

}
