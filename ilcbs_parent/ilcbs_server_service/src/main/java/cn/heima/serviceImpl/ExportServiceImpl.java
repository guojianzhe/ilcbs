package cn.heima.serviceImpl;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.OneToMany;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import cn.heima.dao.ContractDao;
import cn.heima.dao.ContractProductDao;
import cn.heima.dao.ExportDao;
import cn.heima.domain.Contract;
import cn.heima.domain.ContractProduct;
import cn.heima.domain.Export;
import cn.heima.domain.ExportProduct;
import cn.heima.domain.ExtCproduct;
import cn.heima.domain.ExtEproduct;
import cn.heima.service.ExportService;
import cn.heima.utils.UtilFuns;
@Service("exportService")
public class ExportServiceImpl implements ExportService{

	@Autowired
	private ExportDao exportDao;
	
	@Autowired
	private ContractDao contractDao;
	
	@Autowired
	private ContractProductDao contractProductDao;
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
			//设置默认值(状态,时间)
			entity.setState(0);  //0草稿 1已上线
			entity.setInputDate(new Date());
			
			String[] ids = entity.getContractIds().split(", ");
			
			
			StringBuilder sb = new StringBuilder();
			for (String cid : ids) {
				Contract contract = contractDao.findOne(cid);
				String contractNo = contract.getContractNo();
				sb.append(contractNo).append(" ");
				contract.setState(2);
				
			}
			//需要将contract中的contractNo拼接成字符串放到报运对象的CustomerContract
			entity.setCustomerContract(sb.toString());
			
			
			//数据搬家,将购销合同下的所有货物转成报运单货物
			//通过跳跃查询的方式一次性取出购销合同下货物的list
			
			
			List<ContractProduct> cpList = contractProductDao.findByContractIds(ids);
			
			HashSet<ExportProduct> exportProducts = new HashSet<>();
			for (ContractProduct contractProduct : cpList) {
				ExportProduct exportProduct = new ExportProduct();
				exportProduct.setBoxNum(contractProduct.getBoxNum());
				//数据对考
				BeanUtils.copyProperties(contractProduct, exportProduct);
				
				exportProduct.setId(null);
				
				exportProduct.setExport(entity);
				exportProducts.add(exportProduct);
				
				//将所有货物下的附件转成保运单货物的附件
				
				Set<ExtCproduct> extCproducts = contractProduct.getExtCproducts();
				HashSet<ExtEproduct> extEproducts = new HashSet<>();
				for (ExtCproduct extCproduct : extCproducts) {
					ExtEproduct extEproduct = new ExtEproduct();
					BeanUtils.copyProperties(extCproduct, extEproduct); 
					extEproduct.setId(null);
					
					//@OneToMany(mappedBy="exportProduct",cascade=CascadeType.ALL)
					//让多的一方维护外键
					extEproduct.setExportProduct(exportProduct);
					
					
					extEproducts.add(extEproduct);
				}
				
				exportProduct.setExtEproducts(extEproducts);
				
				
				
			}
			
			entity.setExportProducts(exportProducts); 
			
			//将所有货物下的附件转成报运单的附件
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
