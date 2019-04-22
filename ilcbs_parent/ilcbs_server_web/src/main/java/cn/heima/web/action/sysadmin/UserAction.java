package cn.heima.web.action.sysadmin;


import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import org.springframework.data.jpa.domain.Specifications;

import com.itextpdf.text.pdf.PdfStructTreeController.returnType;
import com.opensymphony.xwork2.ModelDriven;

import cn.heima.dao.ContractDao;
import cn.heima.domain.Contract;
import cn.heima.domain.Dept;
import cn.heima.domain.Role;
import cn.heima.domain.User;
import cn.heima.service.DeptService;
import cn.heima.service.RoleService;
import cn.heima.service.UserService;
import cn.heima.utils.Page;
import cn.heima.utils.SysConstant;
import cn.heima.web.action.BaseAction;

@Namespace("/sysadmin")
@Result(name="alist",type="redirectAction",location="userAction_list")
public class UserAction extends BaseAction implements ModelDriven<User>
{
	
	@Autowired
	private UserService userService;
	

	@Autowired
	private DeptService deptService;

	@Autowired
	private RoleService roleService;
	private User model = new User();
	private Page page = new Page();
	
	private String[] roleIds;
	
	public String[] getRoleIds() {
		return roleIds;
	}
	public void setRoleIds(String[] roleIds) {
		this.roleIds = roleIds;
	}
	public Page getPage() {
		return page;
	}
	public void setPage(Page page) {
		this.page = page;
	}
	
	@Override
	public User getModel() {
		
		return model;
	}
	/**
	 * 去列表页面
	 * @return
	 * @throws Exception
	 */
	@Action(value="userAction_list",results= {@Result(name="list",location="/WEB-INF/pages/sysadmin/user/jUserList.jsp")})
	public String list() throws Exception{
		
		org.springframework.data.domain.Page<User> findPage = userService.findPage(null, new PageRequest(page.getPageNo()-1, page.getPageSize()));
		
		//总页数
		page.setTotalPage(findPage.getTotalPages());
		//总记录数
		page.setTotalRecord(findPage.getTotalElements());
		//对应的当前页记录
		page.setResults(findPage.getContent());
		//设置页面链接
		page.setUrl("userAction_list");
		
		
		super.push(page);
		
		return "list";
	}

	/**
	 * 查看单个用户
	 * @return
	 */
	@Action(value="userAction_toview",results= {@Result(name="toview",location="/WEB-INF/pages/sysadmin/user/jUserView.jsp")})
	public String toview() {
		
		User user = userService.get(model.getId());
		
		super.push(user);
		
		return "toview";
	}
	
	/**
	 * 去新增页面
	 * @return
	 */
	@Action(value="userAction_tocreate",results= {@Result(name="tocreate",location="/WEB-INF/pages/sysadmin/user/jUserCreate.jsp")})
	public String tocreate() {
		Specification<User> userSpec = new Specification<User>() {
			@Override
			public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Path<Object> path = root.get("state");
				return cb.equal(path.as(Integer.class), 1);
			}
		};

		List<User> userList = userService.find(userSpec);
		super.put("userList", userList);
		//deptList 该用户所在的部门
		Specification<Dept> deptSpec = new Specification<Dept>() {
			@Override
			public Predicate toPredicate(Root<Dept> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				return cb.equal(root.get("state").as(Integer.class), 1);
			}
		};
		List<Dept> deptList = deptService.find(deptSpec);
		super.put("deptList", deptList);
		return "tocreate";
	}
	/**
	 * 新增部门
	 */
	@Action(value="userAction_insert")
	public String insert() {
		
		userService.saveOrUpdate(model);
		return "alist";
	}
	/**
	 * 去修改页面
	 */
	@Action(value="userAction_toupdate",results= {@Result(name="toupdate",location="/WEB-INF/pages/sysadmin/user/jUserUpdate.jsp")})
	public String toupdate() {
		
		
		User user = userService.get(model.getId());
		super.push(user);


		Specification<Dept> spec = new Specification<Dept>() {

			@Override
			public Predicate toPredicate(Root<Dept> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

				
				Path<Object> path = root.get("state");
				return cb.equal(path.as(Integer.class), 1);
			}
		};

		List<Dept> deptList = deptService.find(spec);
		
		super.put("deptList", deptList);


		return "toupdate";
	}
	/**
	 * 保存修改的部门
	 */
	
	@Action(value="userAction_update")
	public String update() {
		
		
		System.out.println(model);
		User user = userService.get(model.getId());
		user.getUserinfo().setName(model.getUserinfo().getName());
		user.setState(model.getState());
		user.setDept(model.getDept());
//		
		userService.saveOrUpdate(user);
		
		return "alist";
	}
	/**
	 * 删除部门
	 */
	@Action(value="userAction_delete")
	public String delete() throws Exception{
		
		String[] ids = model.getId().split(", ");
		
		userService.delete(ids);
		
		return "alist";
	}
	
	/**
	 * 跳转到用户角色分配页面
	 * @return
	 * @throws Exception
	 */
	@Action(value="userAction_torole",results= {@Result(name="torole",location="/WEB-INF/pages/sysadmin/user/jUserRole.jsp")})
	public String torole() throws Exception{
		//用户角色列表
		User user = userService.get(model.getId());
		super.push(user);
		//将角色全部查出来
		List<Role> roleList = roleService.find(null);
		//将角色列表压入栈顶
		super.put("roleList", roleList);
		//将用户所有的角色查询出来
		Set<Role> roles = user.getRoles();
		StringBuilder name = new StringBuilder();
		for (Role role : roles) {
			name.append(role.getName()+",");
		}
		super.put("roleStr", name.toString());
		
		return "torole";
	}
	
	/**
	 * 保存用户的角色
	 * @return
	 * @throws Exception
	 */
	@Action(value="userAction_role")
	public String role() throws Exception{
		HashSet<Role> roles = new HashSet<Role>();
		for (String rid : roleIds) {
			roles.add(roleService.get(rid));
		}
		
		User user = userService.get(model.getId());
		
		user.setRoles(roles);
		
		userService.saveOrUpdate(user);
		
		
		return "alist";
	}
}
