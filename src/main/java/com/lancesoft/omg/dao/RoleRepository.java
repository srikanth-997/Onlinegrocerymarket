package com.lancesoft.omg.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lancesoft.omg.dto.Authorities;

public interface RoleRepository  extends JpaRepository<Authorities,Integer>{

}
