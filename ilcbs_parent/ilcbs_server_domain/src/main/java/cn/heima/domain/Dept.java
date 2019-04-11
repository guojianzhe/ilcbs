package cn.heima.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;


@Table(name="DEPT_P")
@Entity
public class Dept implements Serializable{

	private static final long serialVersionUID = -121173696741639636L;
	
	@Id
	@Column(name="DEPT_ID")
	@GeneratedValue(generator="sys-uuid")
	@GenericGenerator(name="sys-uuid",strategy="uuid")
	private String id;			//部门编号
	@Column(name="DEPT_NAME")
	private String deptName;  	//部门名称
	@ManyToOne
	@JoinColumn(name="PARENT_ID",referencedColumnName="DEPT_ID")
	private Dept parent;        //代表父部门对象
	@Column(name="STATE")
	private String state;	    //状态 0:取消  1:运营
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public Dept getParent() {
		return parent;
	}
	public void setParent(Dept parent) {
		this.parent = parent;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	@Override
	public String toString() {
		return "Dept [id=" + id + ", deptName=" + deptName + ", parent=" + parent + ", state=" + state + "]";
	}
	
	
	
}
