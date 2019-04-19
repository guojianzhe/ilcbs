package cn.heima.dao;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import cn.heima.domain.Factory;

public interface FactoryDao extends JpaRepository<Factory, String>,JpaSpecificationExecutor<Factory>{
	

}
