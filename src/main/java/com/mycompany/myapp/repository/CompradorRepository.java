package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Comprador;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Comprador entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CompradorRepository extends JpaRepository<Comprador, Long> {}
