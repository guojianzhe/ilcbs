package cn.heima.dao;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import cn.heima.domain.ContractProduct;

public interface ContractProductDao extends JpaRepository<ContractProduct, String>,JpaSpecificationExecutor<ContractProduct>{
	
}
