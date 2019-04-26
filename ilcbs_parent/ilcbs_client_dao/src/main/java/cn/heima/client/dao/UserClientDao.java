package cn.heima.client.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.heima.client.domain.UserClient;

public interface UserClientDao extends JpaRepository<UserClient, String> {
	
	UserClient findByUserNameAndPassword(String userName,String password);

	UserClient findByEmail(String email);
}
