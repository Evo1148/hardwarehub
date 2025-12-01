package com.hardwarehub.hardwarehub.repository;

import com.hardwarehub.hardwarehub.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {


}
