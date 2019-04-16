package cn.heima.dao;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


import cn.heima.domain.User;

public interface UserDao extends JpaRepository<User, String>,JpaSpecificationExecutor<User>{

}
