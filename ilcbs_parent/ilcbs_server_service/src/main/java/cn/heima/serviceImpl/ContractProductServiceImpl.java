package cn.heima.serviceImpl;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import cn.heima.dao.ContractDao;
import cn.heima.dao.ContractProductDao;
import cn.heima.domain.Contract;
import cn.heima.domain.ContractProduct;
import cn.heima.domain.ExtCproduct;
import cn.heima.service.ContractProductService;
import cn.heima.utils.UtilFuns;
@Service("contractProductService")
public class ContractProductServiceImpl implements ContractProductService{

	@Autowired
	private ContractProductDao contractProductDao;
	
	@Autowired
	private ContractDao contractDao;
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
			//分散计算
			Double amount = 0.0;
			if(UtilFuns.isNotEmpty(entity.getCnumber()) && UtilFuns.isNotEmpty(entity.getPrice())) {
				amount = entity.getCnumber() * entity.getPrice();
			}
			entity.setAmount(amount);
			
			Contract contract = contractDao.findOne(entity.getContract().getId());
			contract.setTotalAmount(contract.getTotalAmount()+amount);
			
			contractDao.save(contract);
			
		}else {
			//保存旧的商品总金额
			Double oldAmount = entity.getAmount();
			Double amount = 0.0;
			if(UtilFuns.isNotEmpty(entity.getCnumber()) && UtilFuns.isNotEmpty(entity.getPrice())) {
				amount = entity.getCnumber() * entity.getPrice();
			}
			//货物的总金额
			if(oldAmount!=amount) {
				entity.setAmount(amount);
				Contract contract = contractDao.findOne(entity.getContract().getId());
				contract.setTotalAmount(contract.getTotalAmount()+amount-oldAmount);
				contractDao.save(contract);
			}
		}
		contractProductDao.save(entity);
		
	}

	@Override
	public void saveOrUpdateAll(Collection<ContractProduct> entitys) {
		contractProductDao.save(entitys);
		
	}

	@Override
	public void deleteById(String id) {
		
		ContractProduct contractProduct = contractProductDao.findOne(id);
		Contract contract = contractProduct.getContract();
		
		//购销合同中的总金额减去货物中的总金额
		contract.setTotalAmount(contract.getTotalAmount()-contractProduct.getAmount());
		//循环货物下的附件
		Set<ExtCproduct> extCproducts = contractProduct.getExtCproducts();
		
		for (ExtCproduct extCproduct : extCproducts) {
			//购销合同总金额减去附件中的金额
			contract.setTotalAmount(contract.getTotalAmount()-extCproduct.getAmount());
		}
		contractDao.save(contract);
		
		contractProductDao.delete(id);
		
	}

	@Override
	public void delete(String[] ids) {
		for (String id : ids) {
			contractProductDao.delete(id);
		}
		
	}

	@Override
	public List<ContractProduct> findCpByShipTime(String shipTime) {
		
		return contractProductDao.findCpByShipTime(shipTime);
	}

}
