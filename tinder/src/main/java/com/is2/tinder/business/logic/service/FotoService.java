package com.is2.tinder.business.logic.service;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.is2.tinder.business.domain.entities.Foto;
import com.is2.tinder.business.logic.error.ErrorService;
import com.is2.tinder.business.repositories.FotoRepositorio;

import jakarta.transaction.Transactional;

@Service
public class FotoService {

    @Autowired
    private FotoRepositorio repo;

    @Transactional
    public Foto crearFoto(MultipartFile archivo) throws ErrorService {

        try {

            validar(archivo);

            Foto foto = new Foto();
            foto.setMime(archivo.getContentType());
            foto.setNombre(archivo.getName());
            foto.setBytes(archivo.getBytes());
            foto.setAlta(new Date());
            foto.setBaja(null);

            return repo.save(foto);

        } catch (ErrorService e) {
            throw e;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorService("Error de Sistemas");
        }
    }

    @Transactional
    public Foto actualizarFoto(Long idFoto, MultipartFile archivo) throws ErrorService {

        try {

            validar(archivo);

            Foto foto = buscarFoto(idFoto);
            foto.setMime(archivo.getContentType());
            foto.setNombre(archivo.getName());
            foto.setBytes(archivo.getBytes());

            return repo.save(foto);

        } catch (ErrorService e) {
            throw e;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorService("Error de Sistemas");
        }

    }

    @Transactional
    public void eliminarFoto(Long idFoto) throws ErrorService {
        try {
            Foto foto = buscarFoto(idFoto);
            foto.setBaja(new Date());
            repo.save(foto);

        } catch (ErrorService e) {
            throw e;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorService("Error de Sistemas");
        }
    }

    public Foto buscarFoto(Long idFoto) throws ErrorService {

        try {

            if (idFoto == null)
                throw new ErrorService("Debe indicar la foto");

            Optional<Foto> optional = repo.findById(idFoto);
            Foto foto = null;
            if (optional.isPresent()) {
                foto = optional.get();
                if (foto.isEliminado()) {
                    throw new ErrorService("No se encuentra la foto indicada");
                }
            }

            return foto;

        } catch (ErrorService ex) {
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorService("Error de sistema");
        }
    }

    private void validar(MultipartFile archivo) throws ErrorService {

        if (archivo == null || archivo.isEmpty())
            throw new ErrorService("Debe indicar el archivo");
    }

}
