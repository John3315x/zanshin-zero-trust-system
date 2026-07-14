package com.nanakusa.zanshin.repository;


import com.nanakusa.zanshin.entity.SecurityLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SecurityLogRepository extends JpaRepository<SecurityLog, Long> {

}
