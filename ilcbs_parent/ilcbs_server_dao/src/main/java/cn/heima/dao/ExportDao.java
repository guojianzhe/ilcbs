package cn.heima.dao;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import cn.heima.domain.Export;

public interface ExportDao extends JpaRepository<Export, String>,JpaSpecificationExecutor<Export>{
	
}
