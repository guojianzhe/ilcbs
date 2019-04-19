package cn.heima.web.action.cargo;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import com.itextpdf.text.pdf.PdfStructTreeController.returnType;
import com.opensymphony.xwork2.ModelDriven;

import cn.heima.domain.Contract;
import cn.heima.service.ContractService;
import cn.heima.utils.Page;
import cn.heima.web.action.BaseAction;

@Namespace("/cargo")
@Result(name="alist",type="redirectAction",location="contractAction_list")
public class ContractAction extends BaseAction implements ModelDriven<Contract>{

	private Contract model = new Contract();
	
	@Override
	public Contract getModel() {
		// TODO Auto-generated method stub
		return model;
	}
	
	Page page = new Page();

	public Page getPage() {
		return page;
	}
	public void setPage(Page page) {
		this.page = page;
	}
	@Autowired
	private ContractService contractService;
	
	/**
	 * 购销合同列表页
	 * @return
	 * @throws Exception
	 */
	@Action(value="contractAction_list",results= {@Result(name="tolist",location="/WEB-INF/pages/cargo/contract/jContractList.jsp")})
	public String list() throws Exception{
		org.springframework.data.domain.Page<Contract> page2 = contractService.findPage(null, new PageRequest(page.getPageNo()-1, page.getPageSize()));
		//对应的当前页记录
		page.setResults(page2.getContent());
		//总页数
		page.setTotalPage(page2.getTotalPages());
		//总记录数
		page.setTotalRecord(page2.getTotalElements());
		//设置页面链接
		page.setUrl("contractAction_list");
		super.push(page);
		return "tolist";
	}
	
	/**
	 * 查看购销合同
	 * @return
	 */
	@Action(value="contractAction_toview",results= {@Result(name="toview",location="/WEB-INF/pages/cargo/contract/jContractView.jsp")})
	public String contractAction_toview() {
		
		Contract contract = contractService.get(model.getId());
		
		super.push(contract);
		return "toview";
	}
	/**
	 * 到新增页面
	 * @return
	 * @throws Exception
	 */
	@Action(value="contractAction_tocreate",results= {@Result(name="tocreate",location="/WEB-INF/pages/cargo/contract/jContractCreate.jsp")})
	public String tocreate() throws Exception {
		
		return "tocreate";
	}
	/**
	 * 新增购销合同
	 * @return
	 * @throws Exception
	 */
	@Action(value="contractAction_insert")
	public String insert() throws Exception {
		
		contractService.saveOrUpdate(model);
		
		return "alist";
	}
	/**
	 * 到购销合同的修改页面
	 * @return
	 */
	@Action(value="contractAction_toupdate",results= {@Result(name="toupdate",location="/WEB-INF/pages/cargo/contract/jContractUpdate.jsp")})
	public String toupdate() throws Exception{
		
		Contract contract = contractService.get(model.getId());
		super.push(contract);
		
		return "toupdate";
	}

	@Action(value="contractAction_update")
	public String update() throws Exception {
		
		Contract contract = contractService.get(model.getId());
		
//		contract.set
		
		
		contractService.saveOrUpdate(model);
		
		return "alist";
	}
}
