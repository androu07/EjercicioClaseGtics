package com.example.lab9.Repository;

import com.example.lab9.Entity.Personajes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonajesRepository extends JpaRepository<Personajes, Integer> {
}
