package cn.heima.client.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.heima.client.domain.CompanyClient;

public interface CompanyDao extends JpaRepository<CompanyClient, String> {

}
