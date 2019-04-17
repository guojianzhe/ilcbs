package cn.heima.web.action.sysadmin;


import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import com.opensymphony.xwork2.ModelDriven;

import cn.heima.domain.Dept;
import cn.heima.domain.Module;
import cn.heima.service.DeptService;
import cn.heima.service.ModuleService;
import cn.heima.utils.Page;
import cn.heima.web.action.BaseAction;

@Namespace("/sysadmin")
@Result(name="alist",type="redirectAction",location="moduleAction_list")
public class ModuleAction extends BaseAction implements ModelDriven<Module>
{
	
	@Autowired
	private ModuleService moduleService;
	
	@Autowired
	private DeptService deptService;
	
	private Module model = new Module();
	private Page page = new Page();
	public Page getPage() {
		return page;
	}
	public void setPage(Page page) {
		this.page = page;
	}
	
	@Override
	public Module getModel() {
		
		return model;
	}
	@Action(value="moduleAction_list",results= {@Result(name="list",location="/WEB-INF/pages/sysadmin/module/jModuleList.jsp")})
	public String list() throws Exception{
		
		org.springframework.data.domain.Page<Module> findPage = moduleService.findPage(null, new PageRequest(page.getPageNo()-1, page.getPageSize()));
		
		//总页数
		page.setTotalPage(findPage.getTotalPages());
		//总记录数
		page.setTotalRecord(findPage.getTotalElements());
		//对应的当前页记录
		page.setResults(findPage.getContent());
		//设置页面链接
		page.setUrl("moduleAction_list");
		
		super.push(page);
		
		return "list";
	}
	/**
	 * 查看单个用户
	 * @return
	 */
	@Action(value="moduleAction_toview",results= {@Result(name="toview",location="/WEB-INF/pages/sysadmin/module/jModuleView.jsp")})
	public String toview() {
		
		Module module = moduleService.get(model.getId());
		
		super.push(module);
		
		return "toview";
	}
	/**
	 * 去新增页面
	 * @return
	 */
	@Action(value="moduleAction_tocreate",results= {@Result(name="tocreate",location="/WEB-INF/pages/sysadmin/module/jModuleCreate.jsp")})
	public String tocreate() {
		
		
		return "tocreate";
	}
	/**
	 * 新增部门
	 */
	@Action(value="moduleAction_insert")
	public String insert() {
		
		System.out.println(model);
		moduleService.saveOrUpdate(model);
		return "alist";
	}
	/**
	 * 去修改页面
	 */
	@Action(value="moduleAction_toupdate",results= {@Result(name="toupdate",location="/WEB-INF/pages/sysadmin/module/jModuleUpdate.jsp")})
	public String toupdate() {
		
		
		Module module = moduleService.get(model.getId());
		super.push(module);
		
		return "toupdate";
	}
	/**
	 * 保存修改的部门
	 */
	
	@Action(value="moduleAction_update")
	public String update() {
		
		
		System.out.println(model);
		Module module = moduleService.get(model.getId());
		module.setName(model.getName());
		module.setLayerNum(module.getLayerNum());
		module.setCpermission(model.getCpermission());
		module.setCurl(model.getCurl());
		module.setCtype(model.getCtype());
		module.setState(model.getState());
		module.setBelong(model.getBelong());
		module.setCwhich(model.getCwhich());
		module.setRemark(model.getRemark());
		module.setOrderNo(model.getOrderNo());
		moduleService.saveOrUpdate(module);
		/*
		 * 	
	       	
	        	
	        <tr>
	            <td class="columnTitle">从属：</td>
	            <td class="tableContent"><input type="text" name="belong" value="${belong}"/></td>
	            <td class="columnTitle">复用标识：</td>
	            <td class="tableContent"><input type="text" name="cwhich" value="${cwhich}"/></td>
	        </tr>			
	        <tr>
	            <td class="columnTitle">说明：</td>
	            <td class="tableContent">
	            	<textarea name="remark" style="height:120px;">${remark}</textarea>
	            </td>
	            <td class="columnTitle">排序号：</td>
	            <td class="tableContent"><input type="text" name="orderNo" value="${orderNo}"/></td>
	        </tr>			
		 */
		return "alist";
	}
	/**
	 * 删除部门
	 */
	@Action(value="moduleAction_delete")
	public String delete() throws Exception{
		
		String[] ids = model.getId().split(", ");
		
		moduleService.delete(ids);
		
		return "alist";
	}
	
	
}
