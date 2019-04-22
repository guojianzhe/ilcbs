package cn.heima.web.action.cargo;

import java.awt.ContainerOrderFocusTraversalPolicy;
import java.util.Arrays;

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
import org.springframework.data.jpa.domain.Specifications;

import com.itextpdf.text.pdf.PdfStructTreeController.returnType;
import com.opensymphony.xwork2.ModelDriven;

import cn.heima.domain.Contract;
import cn.heima.domain.User;
import cn.heima.service.ContractService;
import cn.heima.utils.Page;
import cn.heima.utils.SysConstant;
import cn.heima.web.action.BaseAction;

@Namespace("/cargo")
@Result(name = "alist", type = "redirectAction", location = "contractAction_list")
public class ContractAction extends BaseAction implements ModelDriven<Contract> {

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
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "contractAction_list", results = {
			@Result(name = "tolist", location = "/WEB-INF/pages/cargo/contract/jContractList.jsp") })
	public String list() throws Exception {

		final User user = (User) session.get(SysConstant.CURRENT_USER_INFO);

		Specification<Contract> spec = new Specification<Contract>() {

			@Override
			public Predicate toPredicate(Root<Contract> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				// TODO Auto-generated method stub
				Predicate equal = null;
				switch (user.getUserinfo().getDegree()) {
				case 0:
					
					break;
				case 1:

					break;
				case 2:

					break;
				
				case 3:
					equal = cb.equal(root.get("createDept").as(String.class), user.getDept().getId());
					break;
				default:
					 equal = cb.equal(root.get("createBy").as(String.class), user.getId());
					 break;
				}

				return equal;
			}

		};

		org.springframework.data.domain.Page<Contract> page2 = contractService.findPage(spec,
				new PageRequest(page.getPageNo() - 1, page.getPageSize()));
		// 对应的当前页记录
		page.setResults(page2.getContent());
		// 总页数
		page.setTotalPage(page2.getTotalPages());
		// 总记录数
		page.setTotalRecord(page2.getTotalElements());
		// 设置页面链接
		page.setUrl("contractAction_list");
		super.push(page);
		return "tolist";
	}

	/**
	 * 查看购销合同
	 * 
	 * @return
	 */
	@Action(value = "contractAction_toview", results = {
			@Result(name = "toview", location = "/WEB-INF/pages/cargo/contract/jContractView.jsp") })
	public String contractAction_toview() {

		Contract contract = contractService.get(model.getId());

		super.push(contract);
		return "toview";
	}

	/**
	 * 到新增页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "contractAction_tocreate", results = {
			@Result(name = "tocreate", location = "/WEB-INF/pages/cargo/contract/jContractCreate.jsp") })
	public String tocreate() throws Exception {

		return "tocreate";
	}

	/**
	 * 新增购销合同
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "contractAction_insert")
	public String insert() throws Exception {
		User user = (User) session.get(SysConstant.CURRENT_USER_INFO);
		
		model.setCreateBy(user.getId());
		model.setCreateDept(user.getDept().getId());
		
		contractService.saveOrUpdate(model);

		return "alist";
	}

	/**
	 * 到购销合同的修改页面
	 * 
	 * @return
	 */
	@Action(value = "contractAction_toupdate", results = {
			@Result(name = "toupdate", location = "/WEB-INF/pages/cargo/contract/jContractUpdate.jsp") })
	public String toupdate() throws Exception {

		Contract contract = contractService.get(model.getId());
		super.push(contract);

		return "toupdate";
	}

	/**
	 * 购销合同修改并提交
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "contractAction_update")
	public String update() throws Exception {

		Contract contract = contractService.get(model.getId());

		contract.setCustomName(model.getCustomName());
		contract.setPrintStyle(model.getPrintStyle());
		contract.setContractNo(model.getContractNo());
		contract.setOfferor(model.getOfferor());
		contract.setInputBy(model.getInputBy());
		contract.setCheckBy(model.getCheckBy());
		contract.setInspector(model.getInspector());
		contract.setSigningDate(model.getSigningDate());
		contract.setImportNum(model.getImportNum());
		contract.setShipTime(model.getShipTime());
		contract.setTradeTerms(model.getTradeTerms());
		contract.setDeliveryPeriod(model.getDeliveryPeriod());
		contract.setCrequest(model.getCrequest());
		contract.setRemark(model.getRemark());

		contractService.saveOrUpdate(contract);

		return "alist";
	}

	/**
	 * 删除购销合同
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "contractAction_delete")
	public String delete() throws Exception {

		System.out.println(model.getId());

		String[] ids = model.getId().split(", ");

		contractService.delete(ids);

		return "alist";
	}

	/**
	 * 提交合同
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "contractAction_submit")
	public String submit() throws Exception {

		String[] ids = model.getId().split(", ");
		for (String id : ids) {

			Contract contract = contractService.get(id);
			contract.setState(1);
			contractService.saveOrUpdate(contract);
		}

		return "alist";
	}

	/**
	 * 取消合同
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "contractAction_cancel")
	public String cancel() throws Exception {

		String[] ids = model.getId().split(", ");
		for (String id : ids) {
			Contract contract = contractService.get(id);
			contract.setState(0);
			contractService.saveOrUpdate(contract);
		}

		return "alist";
	}
}
