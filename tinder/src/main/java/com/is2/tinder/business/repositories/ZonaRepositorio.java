/*
 * Copyright (c) 2024. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.is2.tinder.business.repositories;

import com.is2.tinder.business.domain.entities.Zona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ZonaRepositorio extends JpaRepository<Zona, String> {
    @Query("SELECT z FROM Zona z WHERE z.eliminado = FALSE")
    List<Zona> listarZonaActiva();
    @Query("SELECT z FROM Zona z WHERE z.nombre = :nombre AND z.eliminado = FALSE")
    Zona buscarZonaPorNombre(@Param("nombre")String nombre);
}
