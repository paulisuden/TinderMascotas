package com.is2.tinder.business.repositories;

import com.is2.tinder.business.domain.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepositorio extends JpaRepository<Usuario, String> {
    @Query("SELECT u FROM Usuario u WHERE u.mail = :mail AND u.baja IS NULL")
    Usuario buscarUsuarioPorMail(@Param("mail") String mail);
    @Query("SELECT u FROM Usuario u WHERE u.mail = :mail AND u.clave = :clave AND u.baja IS NULL")
    Usuario buscarUsuarioPorMailYClave(@Param("mail")String mail, @Param("clave")String clave);
}
