package cn.heima.web.action.cargo;


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

import cn.heima.domain.Contract;
import cn.heima.domain.Export;
import cn.heima.service.ContractService;
import cn.heima.utils.Page;
import cn.heima.web.action.BaseAction;

@Namespace("/cargo")
public class ExportAction extends BaseAction implements ModelDriven<Export> {

	private Export model = new Export();
	@Override
	public Export getModel() {
		// TODO Auto-generated method stub
		return model;
	}
	
	private Page page = new Page();
	
	public Page getPage() {
		return page;
	}
	public void setPage(Page page) {
		this.page = page;
	}

	@Autowired
	private ContractService contractService;
	
	
	/**
	 * 合同管理内购销合同列表
	 * @return
	 * @throws Exception
	 */
	@Action(value="exportAction_contractList",results= {@Result(name="contractList",location="/WEB-INF/pages/cargo/export/jContractList.jsp")})
	public String contractList() throws Exception {
		
		Specification<Contract> spec = new Specification<Contract>() {

			@Override
			public Predicate toPredicate(Root<Contract> root, CriteriaQuery<?> arg1, CriteriaBuilder cb) {
				// TODO Auto-generated method stub
				return cb.equal(root.get("state").as(Integer.class), 1);
			}
		};
		org.springframework.data.domain.Page<Contract> page2 = contractService.findPage(spec, new PageRequest(page.getPageNo()-1, page.getPageSize()));
		
		page.setResults(page2.getContent());
		page.setTotalPage(page2.getTotalPages());
		page.setTotalRecord(page2.getTotalElements());
		page.setUrl("exportAction_contractList");
		super.push(page);
		return "contractList";
	}
	
	@Action(value="exportAction_tocreate")
	public String tocreate() throws Exception{
		
		
		return "tocreate";
	}



//
//
//	@Action(value="exportAction_list",results= {@Result(name="tolist",location="/WEB-INF/pages/cargo/export/jExportList.jsp")})
//	public String list() {
//		
//		
//		
//		return "tolist";
//	}

}
