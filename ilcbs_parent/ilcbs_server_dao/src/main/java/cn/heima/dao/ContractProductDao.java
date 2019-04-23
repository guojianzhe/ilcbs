package cn.heima.dao;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import cn.heima.domain.ContractProduct;

public interface ContractProductDao extends JpaRepository<ContractProduct, String>,JpaSpecificationExecutor<ContractProduct>{
	
	@Query(value="from ContractProduct where to_char(contract.shipTime,'yyyy-MM') = ?1")
	public List<ContractProduct> findCpByShipTime(String shipTime);
}
