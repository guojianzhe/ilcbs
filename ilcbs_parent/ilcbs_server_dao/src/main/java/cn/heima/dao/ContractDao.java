package cn.heima.dao;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import cn.heima.domain.Contract;

public interface ContractDao extends JpaRepository<Contract, String>,JpaSpecificationExecutor<Contract>{
	
}
