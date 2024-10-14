package com.is2.tinder.business.logic.service;

import java.util.Collection;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.is2.tinder.business.domain.entities.Foto;
import com.is2.tinder.business.domain.entities.Mascota;
import com.is2.tinder.business.domain.enumeration.Sexo;
import com.is2.tinder.business.domain.enumeration.TipoMascota;
import com.is2.tinder.business.logic.error.ErrorService;
import com.is2.tinder.business.repositories.MascotaRepositorio;

import jakarta.persistence.NoResultException;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MascotaService {
    @Autowired
    private MascotaRepositorio repo;

    // @Autowired
    // private UsuarioService usuarioService;
    @Autowired
    private FotoService fotoService;

    @Transactional
    public Mascota crearMascota(
            String nombre,
            Sexo sexo,
            TipoMascota tipo,
            MultipartFile archivoFoto,
            String idUsuario) throws ErrorService {

        try {
            validar(nombre, sexo);

            try {
                Mascota mascotaAux = repo.buscarMascotaDeUsuario(idUsuario, nombre);
                if (mascotaAux != null && !mascotaAux.isEliminado())
                    throw new ErrorService("Usted ya tiene una mascota registrada con este nombre");
            } catch (NoResultException ex) {
            }

            Mascota mascota = new Mascota();
            mascota.setId(UUID.randomUUID().toString());
            mascota.setNombre(nombre);
            mascota.setSexo(sexo);
            mascota.setTipo(tipo);
            mascota.setAlta(new Date());
            mascota.setBaja(null);
            // mascota.setUsuario(usuario);

            Foto foto = fotoService.crearFoto(archivoFoto);
            mascota.setFoto(foto);

            repo.save(mascota);
            return mascota;

        } catch (ErrorService err) {
            throw err;
        } catch (Exception ex) {
            throw new ErrorService("Error de sistemas");
        }

    }

    @Transactional
    public void modificarMascota(
            String idMascota,
            String nombre,
            Sexo sexo,
            TipoMascota tipo,
            MultipartFile archivoFoto,
            String idUsuario) throws ErrorService {

        try {

            validar(nombre, sexo);

            Mascota mascota = buscarMascota(idMascota);

            if (!mascota.getUsuario().getId().equals(idUsuario))
                throw new ErrorService("Puede modificar solo mascotas de su pertenencia");

            try {
                Mascota mascotaAux = repo.buscarMascotaDeUsuario(idUsuario, nombre);
                if (mascotaAux != null && !mascotaAux.isEliminado()) {
                    throw new ErrorService("Existe una mascota de usted con el nombre indicado");
                }
            } catch (NoResultException ex) {
            }

            mascota.setNombre(nombre);
            mascota.setSexo(sexo);

            // Obtiene la foto y actualiza sus contenidos
            String idFoto = null;
            if (mascota.getFoto() != null) {
                idFoto = mascota.getFoto().getId();
            }

            Foto foto = fotoService.actualizarFoto(idFoto, archivoFoto);
            mascota.setFoto(foto);
            mascota.setTipo(tipo);

            repo.save(mascota);

        } catch (ErrorService e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrorService("Error de Sistemas");
        }
    }

    @Transactional
    public void eliminarMascota(
            String idUsuario,
            String idMascota) throws ErrorService {

        try {

            Mascota mascota = buscarMascota(idMascota);

            if (!mascota.getUsuario().getId().equals(idUsuario))
                throw new ErrorService("No puede modificar mascotas que no le pertenezcan");

            mascota.setBaja(new Date());

            repo.save(mascota);

        } catch (ErrorService e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrorService("Error de Sistemas");
        }
    }

    @Transactional(readOnly = true)
    public Mascota buscarMascota(String idMascota) throws ErrorService {

        try {

            if (idMascota == null || idMascota.trim().isEmpty())
                throw new ErrorService("Debe indicar una mascota");

            Optional<Mascota> optional = repo.findById(idMascota);
            Mascota mascota = null;
            if (optional.isPresent()) {
                mascota = optional.get();
                if (mascota.isEliminado()) {
                    throw new ErrorService("No se encuentra la mascota indicada");
                }
            }

            return mascota;

        } catch (ErrorService e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrorService("Error de Sistemas");
        }
    }

    public Collection<Mascota> listarMascotaPorUsuario(String idUsuario) throws ErrorService {

        try {

            if (idUsuario == null || idUsuario.trim().isEmpty())
                throw new ErrorService("Debe indicar el usuario");

            return repo.listarMascotasPorUsuario(idUsuario);

        } catch (ErrorService e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrorService("Error de Sistemas");
        }
    }

    public Collection<Mascota> listarMascotaDeBaja(String idUsuario) throws ErrorService {

        try {

            if (idUsuario == null || idUsuario.trim().isEmpty())
                throw new ErrorService("Debe indicar el usuario");

            return repo.listarMascotasDeBaja(idUsuario);

        } catch (ErrorService e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrorService("Error de Sistemas");
        }

    }

    private void validar(String nombre, Sexo sexo) throws ErrorService {

        if (nombre == null || nombre.trim().isEmpty())
            throw new ErrorService("Debe indicar el nombre");
        if (sexo == null)
            throw new ErrorService("Debe indicar el sexo");
    }

}
