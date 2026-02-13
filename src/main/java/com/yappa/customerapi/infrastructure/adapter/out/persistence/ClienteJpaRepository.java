package com.yappa.customerapi.infrastructure.adapter.out.persistence;

import com.yappa.customerapi.infrastructure.adapter.out.persistence.entity.ClienteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteJpaRepository extends JpaRepository<ClienteEntity, Long> {}
