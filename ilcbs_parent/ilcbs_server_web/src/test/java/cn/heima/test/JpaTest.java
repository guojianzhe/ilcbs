package cn.heima.test;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.heima.dao.DeptDao;
import cn.heima.domain.Dept;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class JpaTest {
	
	@Autowired
	private DeptDao deptDao;
	
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
	
}
