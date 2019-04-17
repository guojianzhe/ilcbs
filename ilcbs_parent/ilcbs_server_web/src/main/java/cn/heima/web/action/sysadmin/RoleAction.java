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
import cn.heima.domain.Role;
import cn.heima.service.DeptService;
import cn.heima.service.RoleService;
import cn.heima.utils.Page;
import cn.heima.web.action.BaseAction;

@Namespace("/sysadmin")
@Result(name="alist",type="redirectAction",location="roleAction_list")
public class RoleAction extends BaseAction implements ModelDriven<Role>
{
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private DeptService deptService;
	
	private Role model = new Role();
	private Page page = new Page();
	public Page getPage() {
		return page;
	}
	public void setPage(Page page) {
		this.page = page;
	}
	
	@Override
	public Role getModel() {
		
		return model;
	}
	@Action(value="roleAction_list",results= {@Result(name="list",location="/WEB-INF/pages/sysadmin/role/jRoleList.jsp")})
	public String list() throws Exception{
		
		org.springframework.data.domain.Page<Role> findPage = roleService.findPage(null, new PageRequest(page.getPageNo()-1, page.getPageSize()));
		
		//总页数
		page.setTotalPage(findPage.getTotalPages());
		//总记录数
		page.setTotalRecord(findPage.getTotalElements());
		//对应的当前页记录
		page.setResults(findPage.getContent());
		//设置页面链接
		page.setUrl("roleAction_list");
		
		
		super.push(page);
		
		return "list";
	}
	/**
	 * 查看单个用户
	 * @return
	 */
	@Action(value="roleAction_toview",results= {@Result(name="toview",location="/WEB-INF/pages/sysadmin/role/jRoleView.jsp")})
	public String toview() {
		
		Role role = roleService.get(model.getId());
		
		super.push(role);
		
		return "toview";
	}
	/**
	 * 去新增页面
	 * @return
	 */
	@Action(value="roleAction_tocreate",results= {@Result(name="tocreate",location="/WEB-INF/pages/sysadmin/role/jRoleCreate.jsp")})
	public String tocreate() {
		
		
		return "tocreate";
	}
	/**
	 * 新增部门
	 */
	@Action(value="roleAction_insert")
	public String insert() {
		
		roleService.saveOrUpdate(model);
		return "alist";
	}
	/**
	 * 去修改页面
	 */
	@Action(value="roleAction_toupdate",results= {@Result(name="toupdate",location="/WEB-INF/pages/sysadmin/role/jRoleUpdate.jsp")})
	public String toupdate() {
		
		
		Role role = roleService.get(model.getId());
		super.push(role);
		
		return "toupdate";
	}
	/**
	 * 保存修改的部门
	 */
	
	@Action(value="roleAction_update")
	public String update() {
		
		
		System.out.println(model);
		Role role = roleService.get(model.getId());
		role.setName(model.getName());
		role.setRemark(model.getRemark());
		roleService.saveOrUpdate(role);
		
		return "alist";
	}
	/**
	 * 删除部门
	 */
	@Action(value="roleAction_delete")
	public String delete() throws Exception{
		
		String[] ids = model.getId().split(", ");
		
		roleService.delete(ids);
		
		return "alist";
	}
	
	
}
