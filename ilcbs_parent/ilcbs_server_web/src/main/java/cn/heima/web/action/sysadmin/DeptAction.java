package cn.heima.web.action.sysadmin;

import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

import com.itextpdf.text.pdf.PdfStructTreeController.returnType;
import com.opensymphony.xwork2.ModelDriven;

import cn.heima.domain.Dept;
import cn.heima.web.action.BaseAction;

public class DeptAction extends BaseAction implements ModelDriven<Dept>
{

	private static final long serialVersionUID = -2378891858335431965L;
	
	private Dept model = new Dept();
	
	@Override
	public Dept getModel() {
		
		return model;
	}
	@Action(value="deptAction",results= {@Result(name="list",location="/WEB-INF/pages/sysadmin/dept/jDeptList.jsp")})
	public String list() {
		
		
		return "list";
	}

}
