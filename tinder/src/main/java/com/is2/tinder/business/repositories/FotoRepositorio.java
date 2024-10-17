package com.is2.tinder.business.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.is2.tinder.business.domain.entities.Foto;

public interface FotoRepositorio extends JpaRepository<Foto, Long> {
}
