
package com.is2.tinder.business.repositories;

import com.is2.tinder.business.domain.entities.Mascota;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MascotaRepositorio extends JpaRepository<Mascota, String> {
    @Query("SELECT m FROM Mascota m WHERE m.usuario.id = :id AND m.baja IS NULL")
    List<Mascota> listarMascotasPorUsuario(@Param("id") String id);

    @Query("SELECT m FROM Mascota m WHERE m.id = :id AND m.baja IS NOT NULL")
    List<Mascota> listarMascotasDeBaja(@Param("id") String id);

    @Query("SELECT m FROM Mascota m WHERE m.id = :id AND m.nombre = :nombre AND m.baja IS NULL")
    Mascota buscarMascotaPorIdYNombre(@Param("id") String id, @Param("nombre") String nombre);

    @Query("SELECT m FROM Mascota m WHERE m.usuario.id = :idUsuario AND m.nombre = :nombre AND m.baja IS NULL")
    Mascota buscarMascotaDeUsuario(@Param("idUsuario") String idUsuario, @Param("nombre") String nombreMascota);

    @Query("SELECT m FROM Mascota m WHERE m.id = :id AND m.baja IS NULL")
    Mascota buscarMascotaPorId(@Param("id") String id);
}
