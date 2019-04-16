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

import com.itextpdf.text.pdf.PdfStructTreeController.returnType;
import com.opensymphony.xwork2.ModelDriven;

import cn.heima.domain.Dept;
import cn.heima.service.DeptService;
import cn.heima.utils.Page;
import cn.heima.web.action.BaseAction;

@Namespace("/sysadmin")
@Result(name="alist",type="redirectAction",location="deptAction_list")
public class DeptAction extends BaseAction implements ModelDriven<Dept>
{
	
	@Autowired
	private DeptService deptService;
	
	
	private Dept model = new Dept();
	private Page page = new Page();
	public Page getPage() {
		return page;
	}
	public void setPage(Page page) {
		this.page = page;
	}
	
	@Override
	public Dept getModel() {
		
		return model;
	}
	@Action(value="deptAction_list",results= {@Result(name="list",location="/WEB-INF/pages/sysadmin/dept/jDeptList.jsp")})
	public String list() throws Exception{
		
		org.springframework.data.domain.Page<Dept> findPage = deptService.findPage(null, new PageRequest(page.getPageNo()-1, page.getPageSize()));
		
		//总页数
		page.setTotalPage(findPage.getTotalPages());
		//总记录数
		page.setTotalRecord(findPage.getTotalElements());
		//对应的当前页记录
		page.setResults(findPage.getContent());
		//设置页面链接
		page.setUrl("deptAction_list");
		
		
		super.push(page);
		
		return "list";
	}
	@Action(value="deptAction_toview",results= {@Result(name="toview",location="/WEB-INF/pages/sysadmin/dept/jDeptView.jsp")})
	public String toview() {
		
		Dept dept = deptService.get(model.getId());
		
		super.push(dept);
		
		return "toview";
	}
	/**
	 * 去新增页面
	 * @return
	 */
	@Action(value="deptAction_tocreate",results= {@Result(name="tocreate",location="/WEB-INF/pages/sysadmin/dept/jDeptCreate.jsp")})
	public String tocreate() {
		
		Specification<Dept> spec = new Specification<Dept>() {

			@Override
			public Predicate toPredicate(Root<Dept> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				
				Path<Object> path = root.get("state");
				return cb.equal(path.as(Integer.class), 1);
			}
		};
		
		
		List<Dept> deptList = deptService.find(spec);
		
		super.put("deptList", deptList);
		
		return "tocreate";
	}
	/**
	 * 新增部门
	 */
	@Action(value="deptAction_insert")
	public String insert() {
		
		deptService.saveOrUpdate(model);
		return "alist";
	}
	/**
	 * 去修改页面
	 */
	@Action(value="deptAction_toupdate",results= {@Result(name="toupdate",location="/WEB-INF/pages/sysadmin/dept/jDeptUpdate.jsp")})
	public String toupdate() {
		
		
		Dept dept = deptService.get(model.getId());
		super.push(dept);
		Specification<Dept> spec = new Specification<Dept>() {

			@Override
			public Predicate toPredicate(Root<Dept> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				
				Path<Object> path = root.get("state");
				return cb.equal(path.as(Integer.class), 1);
			}
		};
		List<Dept> deptList = deptService.find(spec);
		
		deptList.remove(dept);
		super.put("deptList", deptList);
		return "toupdate";
	}
	/**
	 * 保存修改的部门
	 */
	
	@Action(value="deptAction_update")
	public String update() {
		
		
		System.out.println(model);
		Dept dept = deptService.get(model.getId());
//		
		dept.setDeptName(model.getDeptName());
		dept.setParent(model.getParent());
		
		deptService.saveOrUpdate(dept);
		
		return "alist";
	}
	/**
	 * 删除部门
	 */
	@Action(value="deptAction_delete")
	public String delete() throws Exception{
		
		String[] ids = model.getId().split(", ");
		
		deptService.delete(ids);
		
		return "alist";
	}
	
	
}
