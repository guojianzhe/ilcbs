package cn.heima.web.action.cargo;

import java.awt.ContainerOrderFocusTraversalPolicy;
import java.util.Arrays;
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

import com.itextpdf.text.pdf.PdfStructTreeController.returnType;
import com.opensymphony.xwork2.ModelDriven;

import cn.heima.domain.ContractProduct;
import cn.heima.domain.Factory;
import cn.heima.service.ContractProductService;
import cn.heima.service.FactoryService;
import cn.heima.utils.Page;
import cn.heima.web.action.BaseAction;

@Namespace("/cargo")
@Result(name="tocreate",type="redirectAction",location="contractProductAction_tocreate?contract.id=${contract.id}")
public class ContractProductAction extends BaseAction implements ModelDriven<ContractProduct>{

	private ContractProduct model = new ContractProduct();
	
	Page page = new Page();
	
	
	@Override
	public ContractProduct getModel() {
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
	private ContractProductService contractProductService;
	
	@Autowired
	private FactoryService factoryService;
	/**
	 * 到新增页面
	 * @return
	 * @throws Exception
	 */
	@Action(value="contractProductAction_tocreate",results= {@Result(name="tocreate",location="/WEB-INF/pages/cargo/contract/jContractProductCreate.jsp")})
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
		
		Specification<ContractProduct> spec2 = new Specification<ContractProduct>() {
			
			@Override
			public Predicate toPredicate(Root<ContractProduct> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				
				return cb.equal(root.get("contract").get("id").as(String.class), model.getContract().getId());
			}
		};
		
		org.springframework.data.domain.Page<ContractProduct> page2 = contractProductService.findPage(spec2, new PageRequest(page.getPageNo()-1, page.getPageSize()));
		page.setResults(page2.getContent());
		page.setTotalPage(page2.getTotalPages());
		page.setTotalRecord(page2.getTotalElements());
		page.setUrl("contractProductAction_tocreate");
		
		super.push(page);
		
		return "tocreate";
	}
	/**
	 * 新增购销合同
	 * @return
	 * @throws Exception
	 */
	@Action(value="contractProductAction_insert")
	public String insert() throws Exception {
		
		contractProductService.saveOrUpdate(model);
		
		return "tocreate";
	}
	/**
	 * 到购销合同的修改页面
	 * @return
	 */
	@Action(value="contractProductAction_toupdate",results= {@Result(name="toupdate",location="/WEB-INF/pages/cargo/contract/jContractProductUpdate.jsp")})
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
		
		ContractProduct contractProduct = contractProductService.get(model.getId());
		super.push(contractProduct);
		
		return "toupdate";
	}
	
	/**
	 * 购销合同修改并提交
	 * @return
	 * @throws Exception
	 */
	@Action(value="contractProductAction_update")
	public String update() throws Exception {
		
		ContractProduct contractProduct = contractProductService.get(model.getId());
		contractProduct.setFactory(model.getFactory());
		contractProduct.setFactoryName(model.getFactoryName());
		contractProduct.setProductNo(model.getProductNo());
		contractProduct.setProductImage(model.getProductImage());
		contractProduct.setCnumber(model.getCnumber());
		contractProduct.setAmount(model.getAmount());
		contractProduct.setPackingUnit(model.getPackingUnit());
		contractProduct.setLoadingRate(model.getLoadingRate());
		contractProduct.setBoxNum(model.getBoxNum());
		contractProduct.setPrice(model.getPrice());
		contractProduct.setOrderNo(model.getOrderNo());
		contractProduct.setProductDesc(model.getProductDesc());
		contractProduct.setProductRequest(model.getProductRequest());
		
		
		contractProductService.saveOrUpdate(contractProduct);
		
		return "tocreate";
	}
	/**
	 * 删除购销合同
	 * @return
	 * @throws Exception
	 */
	@Action(value="contractProductAction_delete")
	public String delete() throws Exception{
		
		contractProductService.deleteById(model.getId());

		return "tocreate";
	}
	
	/**
	 * 提交合同
	 * @return
	 * @throws Exception
	 */
	@Action(value="contractProductAction_submit")
	public String submit() throws Exception {
		
		String[] ids = model.getId().split(", ");
		for (String id : ids) {
			
			ContractProduct contractProduct = contractProductService.get(id);
			
			contractProductService.saveOrUpdate(contractProduct);
		}
		
		return "alist";
	}
	
	/**
	 * 取消合同
	 * @return
	 * @throws Exception
	 */
	@Action(value="contractProductAction_cancel")
	public String cancel() throws Exception {
		
		String[] ids = model.getId().split(", ");
		for (String id : ids) {
			ContractProduct contractProduct = contractProductService.get(id);
			
			contractProductService.saveOrUpdate(contractProduct);
		}
		
		return "alist";
	}
}
