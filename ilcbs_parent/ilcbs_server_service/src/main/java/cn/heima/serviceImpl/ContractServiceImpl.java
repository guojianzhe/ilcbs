package cn.heima.serviceImpl;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import cn.heima.dao.ContractDao;
import cn.heima.domain.Contract;
import cn.heima.service.ContractService;
import cn.heima.utils.UtilFuns;
@Service("contractService")
public class ContractServiceImpl implements ContractService{

	@Autowired
	private ContractDao contractDao;
	
	@Override
	public List<Contract> find(Specification<Contract> spec) {
		// TODO Auto-generated method stub
		return contractDao.findAll(spec);
	}

	@Override
	public Contract get(String id) {
		
		return contractDao.findOne(id);
	}

	@Override
	public Page<Contract> findPage(Specification<Contract> spec, Pageable pageable) {
		// TODO Auto-generated method stub
		return contractDao.findAll(spec, pageable);
	}

	@Override
	public void saveOrUpdate(Contract entity) {
		
		if(UtilFuns.isEmpty(entity.getId())) {
			entity.setState(0);
			entity.setTotalAmount(0.0);
		}
		contractDao.save(entity);
	}

	@Override
	public void saveOrUpdateAll(Collection<Contract> entitys) {
		contractDao.save(entitys);
		
	}

	@Override
	public void deleteById(String id) {
		contractDao.delete(id);
		
	}

	@Override
	public void delete(String[] ids) {
		for (String id : ids) {
			contractDao.delete(id);
		}
		
	}

}
