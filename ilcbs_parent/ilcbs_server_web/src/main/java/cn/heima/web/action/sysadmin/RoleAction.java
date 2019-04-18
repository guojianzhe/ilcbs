package cn.heima.web.action.sysadmin;


import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import com.itextpdf.text.pdf.PdfStructTreeController.returnType;
import com.opensymphony.xwork2.ModelDriven;

import cn.heima.domain.Dept;
import cn.heima.domain.Module;
import cn.heima.domain.Role;
import cn.heima.exception.SysException;
import cn.heima.service.DeptService;
import cn.heima.service.ModuleService;
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
	
	@Autowired
	private ModuleService moduleService;
	private String moduleIds;
	public String getModuleIds() {
		return moduleIds;
	}
	public void setModuleIds(String moduleIds) {
		this.moduleIds = moduleIds;
	}



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
	
	/**
	 * 权限管理页面
	 * @return
	 * @throws Exception
	 */
	@Action(value="roleAction_tomodule",results= {@Result(name="tomodule",location="/WEB-INF/pages/sysadmin/role/jRoleModule.jsp")})
	public String tomodule() throws Exception{
		
		try {
			Role role = roleService.get(model.getId());
			
			super.push(role);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SysException("请勾选一个角色后在点击模块按钮");
		}
		
		return "tomodule";
		
	}
	
	/**
	 * ajax得到权限管理树
	 * [{ id:11, pId:1, name:"随意勾选 1-1", open:true},{ id:111, pId:11, name:"随意勾选 1-1-1",checked:true}]
	 * @return
	 * @throws Exception
	 */
	@Action(value="roleAction_genzTreeNodes")
	public String genzTreeNodes() throws Exception{
		//1.根据id获取角色对象
		Role role = roleService.get(model.getId());
		//2.用户所拥有的所有模块
		Set<Module> roleModules = role.getModules();
		//3.查询所有的模块
		
		Specification<Module> spec = new Specification<Module>() {

			@Override
			public Predicate toPredicate(Root<Module> root, CriteriaQuery<?> arg1, CriteriaBuilder cb) {
				// TODO Auto-generated method stub
				return cb.equal(root.get("state").as(Integer.class), 1);
			}
		};
		List<Module> modulesList = moduleService.find(spec);
		
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for (Module module : modulesList) {
			sb.append("{");
			sb.append("\"id\":\"").append(module.getId()).append("\"");
			sb.append(",\"pId\":\"").append(module.getParentId()).append("\"");
			sb.append(",\"name\":\"").append(module.getCpermission()).append("\"");
			//判断当前角色是否拥有该模块
			if(roleModules.contains(module)) {
				sb.append(",\"checked\":\"true\"");
			}
			
			sb.append("},");
		}
		sb.deleteCharAt(sb.length()-1);
		sb.append("]");
	
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(sb.toString());
		
		return NONE;
	}
	
	
	
	/**
	 * 保存角色
	 * @return
	 * @throws Exception
	 */
	@Action(value="roleAction_module")
	public String module() throws Exception {
		//1.根据id获取当前role对象
		Role role = roleService.get(model.getId());
		
		
		//2.获取用户选择的所有的模块对象
		String[] split = moduleIds.split(",");
		HashSet<Module> modules= new HashSet<>();
		for (String mId : split) {
			Module module = moduleService.get(mId);
			modules.add(module);
		}
		//3.设置role和module的关系
		role.setModules(modules);
		//4.保存到role数据库
		roleService.saveOrUpdate(role);
		
		return "alist";
	}
	
}
