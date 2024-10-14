package com.is2.tinder.business.logic.service;

import com.is2.tinder.business.domain.entities.Mascota;
import com.is2.tinder.business.domain.entities.Voto;
import com.is2.tinder.business.logic.error.ErrorService;
import com.is2.tinder.business.repositories.VotoRepositorio;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class VotoService {

    @Autowired
    private VotoRepositorio repo;

    @Autowired
    private MascotaService serviceMascota;

    public void validar(String idMascota1, String idMascota2) throws ErrorService {
        try {
            if (idMascota1 == null || idMascota1.trim().isEmpty()) {
                throw new ErrorService("Debe indicar la mascota");
            }
            if (idMascota2 == null || idMascota2.trim().isEmpty()) {
                throw new ErrorService("Debe indicar la mascota");
            }
            if (idMascota1.equals(idMascota2)) {
                throw new ErrorService("Una mascota no se puede votar a sí misma");
            }
        } catch (ErrorService e) {
            throw e;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorService("Error del sistema");
        }

    }

    @Transactional
    public void crearVoto(String idUsuario, String idMascota1, String idMascota2) throws ErrorService {
        try {
            validar(idMascota1, idMascota2);

            Mascota mascota1 = serviceMascota.buscarMascotaPorId(idMascota1);
            if (mascota1.getUsuario().getId().equals(idUsuario)) {
                throw new ErrorService("El usuario no puede votar a su propia mascota");
            }
            Mascota mascota2 = serviceMascota.buscarMascotaPorId(idMascota2);

            Voto voto = new Voto();
            voto.setMascota1(mascota1);
            voto.setMascota2(mascota2);
            voto.setFecha(new Date());

            repo.save(voto);
        } catch (ErrorService e) {
            throw e;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorService("Error del sistema");
        }
    }

    public Voto buscarVoto(String id) throws ErrorService {

        try {

            if (id == null || id.trim().isEmpty()) {
                throw new ErrorService("Debe indicar el voto");
            }

            Optional<Voto> optional = repo.findById(id);
            Voto voto = null;
            if (optional.isPresent()) {
                voto = optional.get();
                if (voto.isEliminado()) {
                    throw new ErrorService("No se encuentra el voto indicado");
                }
            }
            return voto;

        } catch (ErrorService ex) {
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorService("Error de sistema");
        }
    }

    @Transactional
    public void responder(String idUsuario, String idVoto) throws ErrorService {

        try {

            Voto voto = buscarVoto(idVoto);

            if (!voto.getMascota2().getUsuario().getId().equals(idUsuario)) {
                throw new ErrorService("El usuario no puede responder así mismo");
            }

            voto.setRespuesta(new Date());
            repo.save(voto);

        } catch (ErrorService ex) {
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorService("Error de sistema");
        }

    }

}
