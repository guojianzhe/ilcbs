package cn.heima.dao;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


import cn.heima.domain.Module;

public interface ModuleDao extends JpaRepository<Module, String>,JpaSpecificationExecutor<Module>{

}
