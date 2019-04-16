package cn.heima.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import cn.heima.domain.Dept;

public interface DeptDao extends JpaRepository<Dept, String>,JpaSpecificationExecutor<Dept>{
	
	@Query("from Dept where deptName = ?1")
	public List<Dept> findDeptByName(String name);
	
	
	public List<Dept> findDeptByState(Integer state);
}
