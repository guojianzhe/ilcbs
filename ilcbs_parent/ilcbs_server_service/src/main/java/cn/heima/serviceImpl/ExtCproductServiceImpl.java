package cn.heima.serviceImpl;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import cn.heima.dao.ContractDao;
import cn.heima.dao.ExtCproductDao;
import cn.heima.domain.Contract;
import cn.heima.domain.ExtCproduct;
import cn.heima.service.ExtCproductService;
import cn.heima.utils.UtilFuns;
@Service("extCproductService")
public class ExtCproductServiceImpl implements ExtCproductService{

	@Autowired
	private ExtCproductDao extCproductDao;
	
	@Autowired
	private ContractDao contractDao;
	
	@Override
	public List<ExtCproduct> find(Specification<ExtCproduct> spec) {
		// TODO Auto-generated method stub
		return extCproductDao.findAll(spec);
	}

	@Override
	public ExtCproduct get(String id) {
		
		return extCproductDao.findOne(id);
	}

	@Override
	public Page<ExtCproduct> findPage(Specification<ExtCproduct> spec, Pageable pageable) {
		// TODO Auto-generated method stub
		return extCproductDao.findAll(spec, pageable);
	}

	@Override
	public void saveOrUpdate(ExtCproduct entity) {
		
		if(UtilFuns.isEmpty(entity.getId())) {
			Double amount = 0.0;
			if(UtilFuns.isNotEmpty(entity.getPrice()) && UtilFuns.isNotEmpty(entity.getCnumber())){
				amount = entity.getPrice()*entity.getCnumber();
			}
			entity.setAmount(amount);
			Contract contract = contractDao.findOne(entity.getContractProduct().getContract().getId());
			
			contract.setTotalAmount(contract.getTotalAmount()+entity.getAmount());
			
		}else {
			Double oldAmount = entity.getAmount();
			Double amount = 0.0;
			if(UtilFuns.isNotEmpty(entity.getPrice()) && UtilFuns.isNotEmpty(entity.getCnumber())){
				amount = entity.getPrice()*entity.getCnumber();
			}
			if(oldAmount!=amount) {
				entity.setAmount(amount);
				Contract contract = contractDao.findOne(entity.getContractProduct().getContract().getId());
				contract.setTotalAmount(contract.getTotalAmount()+amount-oldAmount);
				contractDao.save(contract);
			}
			
			
		}
		
		extCproductDao.save(entity);
		
	}

	@Override
	public void saveOrUpdateAll(Collection<ExtCproduct> entitys) {
		extCproductDao.save(entitys);
		
	}

	@Override
	public void deleteById(String id) {
		
		ExtCproduct extCproduct = extCproductDao.findOne(id);
		
		Contract contract = extCproduct.getContractProduct().getContract();
		
		contract.setTotalAmount(contract.getTotalAmount()-extCproduct.getAmount());
		
		contractDao.save(contract);
		extCproductDao.delete(id);
		
	}

	@Override
	public void delete(String[] ids) {
		for (String id : ids) {
			extCproductDao.delete(id);
		}
		
	}

}
