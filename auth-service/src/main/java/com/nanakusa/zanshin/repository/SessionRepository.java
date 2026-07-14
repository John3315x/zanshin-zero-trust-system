package com.nanakusa.zanshin.repository;

import com.nanakusa.zanshin.entity.Session;
import com.nanakusa.zanshin.entity.UserResponse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionRepository extends JpaRepository<Session, Long> {
    Session findByTokenId(String tokenId);

    int countByUserIdAndRevokedFalse(Long userId);
}
