package cn.heima.web.action.cargo;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import com.opensymphony.xwork2.ModelDriven;

import cn.heima.domain.ExtCproduct;
import cn.heima.domain.Factory;
import cn.heima.service.ExtCproductService;
import cn.heima.service.FactoryService;
import cn.heima.utils.Page;
import cn.heima.web.action.BaseAction;

@Namespace("/cargo")
@Result(name="tocreate",type="redirectAction",location="extCproductAction_tocreate?contractProduct.contract.id=${contractProduct.contract.id}&contractProduct.id=${contractProduct.id}")
public class ExtCproductAction extends BaseAction implements ModelDriven<ExtCproduct>{

	private ExtCproduct model = new ExtCproduct();
	
	Page page = new Page();
	@Override
	public ExtCproduct getModel() {
		// TODO Auto-generated method stub
		return model;
	}
	public Page getPage() {
		return page;
	}
	public void setPage(Page page) {
		this.page = page;
	}
	@Autowired
	private ExtCproductService extCproductService;
	
	@Autowired
	private FactoryService factoryService;
	/**
	 * 到新增页面
	 * @return
	 * @throws Exception
	 */
	@Action(value="extCproductAction_tocreate",results= {@Result(name="tocreate",location="/WEB-INF/pages/cargo/contract/jExtCproductCreate.jsp")})
	public String tocreate() throws Exception {
		
//		model.getContract().getId();
		Specification<Factory> spec = new Specification<Factory>() {

			@Override
			public Predicate toPredicate(Root<Factory> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				// TODO Auto-generated method stub
				return cb.equal(root.get("ctype").as(String.class), "货物");
			}
		};
		List<Factory> factoryList = factoryService.find(spec);
		
		super.put("factoryList", factoryList);
		
		Specification<ExtCproduct> spec2 = new Specification<ExtCproduct>() {
			
			@Override
			public Predicate toPredicate(Root<ExtCproduct> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				
				return cb.equal(root.get("contractProduct").get("id").as(String.class), model.getContractProduct().getId());
			}
		};
		
		org.springframework.data.domain.Page<ExtCproduct> page2 = extCproductService.findPage(spec2, new PageRequest(page.getPageNo()-1, page.getPageSize()));
		page.setResults(page2.getContent());
		page.setTotalPage(page2.getTotalPages());
		page.setTotalRecord(page2.getTotalElements());
		page.setUrl("extCproductAction_tocreate");
		
		super.push(page);
		
		return "tocreate";
	}
	/**
	 * 新增附件
	 * @return
	 * @throws Exception
	 */
	@Action(value="extCproductAction_insert")
	public String insert() throws Exception {
		
		extCproductService.saveOrUpdate(model);
		
		return "tocreate";
	}
	/**
	 * 到附件的修改页面
	 * @return
	 */
	@Action(value="extCproductAction_toupdate",results= {@Result(name="toupdate",location="/WEB-INF/pages/cargo/contract/jExtCproductUpdate.jsp")})
	public String toupdate() throws Exception{
		
		Specification<Factory> spec = new Specification<Factory>() {

			@Override
			public Predicate toPredicate(Root<Factory> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				// TODO Auto-generated method stub
				return cb.equal(root.get("ctype").as(String.class), "货物");
			}
		};
		List<Factory> factoryList = factoryService.find(spec);
		
		super.put("factoryList", factoryList);
		
		ExtCproduct extCproduct = extCproductService.get(model.getId());
		super.push(extCproduct);
		
		return "toupdate";
	}
	
	/**
	 * 附件修改并提交
	 * @return
	 * @throws Exception
	 */
	@Action(value="extCproductAction_update")
	public String update() throws Exception {
		
		ExtCproduct extCproduct = extCproductService.get(model.getId());
		extCproduct.setFactory(model.getFactory());
		extCproduct.setFactoryName(model.getFactoryName());
		extCproduct.setProductNo(model.getProductNo());
		extCproduct.setProductImage(model.getProductImage());
		extCproduct.setCnumber(model.getCnumber());
		extCproduct.setAmount(model.getAmount());
		extCproduct.setPackingUnit(model.getPackingUnit());
		extCproduct.setPrice(model.getPrice());
		extCproduct.setOrderNo(model.getOrderNo());
		extCproduct.setProductDesc(model.getProductDesc());
		extCproduct.setProductRequest(model.getProductRequest());
		
		extCproductService.saveOrUpdate(extCproduct);
		
		return "tocreate";
	}
	/**
	 * 删除附件
	 * @return
	 * @throws Exception
	 */
	@Action(value="extCproductAction_delete")
	public String delete() throws Exception{
		
		extCproductService.deleteById(model.getId());

		return "tocreate";
	}
	
	
}
