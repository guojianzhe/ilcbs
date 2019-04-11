package cn.heima.test;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.heima.dao.DeptDao;
import cn.heima.domain.Dept;
import cn.heima.service.DeptService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class JpaTest {
	
	@Autowired
	private DeptDao deptDao;
	@Autowired
	private DeptService deptService;
	@Test
	public void testFindDept() {
		Dept dept = deptDao.findOne("100");
		System.out.println(dept.getDeptName());
	}
	@Test
	public void testFindByName() {
		List<Dept> findDeptByName = deptDao.findDeptByName("调研组");
		for (Dept dept : findDeptByName) {
			System.out.println(dept.getDeptName());
			
		}
		
	}
	@Test
	public void testquery() {
		
		Dept dept = new Dept();
		dept.setDeptName("11111");
		dept.setState("1");
		
		deptService.saveOrUpdate(dept);
		System.out.println("111");
		
	}
	
	
}
