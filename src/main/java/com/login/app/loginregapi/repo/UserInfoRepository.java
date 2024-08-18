package com.login.app.loginregapi.repo;

import com.login.app.loginregapi.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {
    UserInfo save(UserInfo userInfo);
}
