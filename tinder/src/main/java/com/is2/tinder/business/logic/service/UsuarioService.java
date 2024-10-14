package com.is2.tinder.business.logic.service;

import com.is2.tinder.business.domain.entities.Usuario;
import com.is2.tinder.business.domain.entities.Zona;
import com.is2.tinder.business.logic.error.ErrorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import jakarta.persistence.NoResultException;

import java.util.Date;
import java.util.List;
import java.util.Optional;


@Service
public class UsuarioService{

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private ZonaService zonaService;

    @Autowired
    private FotoService fotoServicie;
    public void validar(String nombre, String apellido, String mail, String clave, String confirmacion, String idZona) throws ErrorService {

        try {

            if (nombre == null || nombre.trim().isEmpty()) {
                throw new ErrorService("Debe indicar el nombre");
            }

            if (apellido == null || apellido.trim().isEmpty()) {
                throw new ErrorService("Debe indicar el apellido");
            }

            if (mail == null || mail.trim().isEmpty()) {
                throw new ErrorService("Debe indicar el mail");
            }

            if (clave == null || clave.trim().isEmpty() || clave.trim().length() < 6) {
                throw new ErrorService("Debe indicar la clave, la cual debe tener más de 6 caracteres");
            }

            if (confirmacion == null || confirmacion.trim().isEmpty() || confirmacion.trim().length() < 6) {
                throw new ErrorService("Debe indicar la confirmación de la clave");
            }

            if (!clave.equals(confirmacion.trim())) {
                throw new ErrorService("La clave y su confirmación deben ser iguales");
            }
            if (idZona == null || idZona.trim().isEmpty()) {
                throw new ErrorService("Debe indicar la zona");
            }

        } catch (ErrorService e) {
            throw e;
        } catch (Exception ex){
            ex.printStackTrace();
            throw new ErrorService("Error de Sistemas");
        }
    }

    @Transactional
    public void crearUsuario(MultipartFile archivo, String nombre, String apellido, String mail, String clave, String clave2, String idZona) throws ErrorService {

        try {

            validar(nombre, apellido, mail, clave, clave2, idZona);

            Zona zona = zonaService.buscarZona(idZona);

            try {
                Usuario usuarioAux = repository.buscarUsuarioPorMail(email);
                if (usuarioAux != null && usuarioAux.getBaja() == null) {
                    throw new ErrorService("Existe un usuario con el mail indicado");
                }
            } catch (NoResultException ex) {}

            Usuario usuario = new Usuario();
            usuario.setNombre(nombre);
            usuario.setApellido(apellido);
            usuario.setMail(mail);
            usuario.setZona(zona);
            usuario.setClave(clave);
            usuario.setAlta(new Date());
            usuario.setBaja(null);

            Foto foto = fotoServicie.guardar(archivo);
            usuario.setFoto(foto);

            repository.save(usuario);

        }catch(ErrorService e) {
            throw e;
        }catch(Exception e) {
            e.printStackTrace();
            throw new ErrorService("Error de Sistemas");
        }
    }

    @Transactional
    public void modificarUsuario(MultipartFile archivo, String idUsuario, String nombre, String apellido, String mail, String clave, String clave2, String idZona) throws ErrorService {

        try {

            validar(nombre, apellido, mail, clave, clave2, idZona);

            Zona zona = zonaService.buscarZona(idZona);

            Usuario usuario = buscarUsuario(idUsuario);

            try {
                Usuario usuarioAux = repository.buscarUsuarioPorMail(mail);
                if (usuarioAux != null && usuarioAux.getBaja() == null && !usuarioAux.getId().equals(idUsuario)) {
                    throw new ErrorService("Existe un usuario con el e-mial indicado");
                }
            } catch (NoResultException ex) {}

            usuario.setNombre(nombre);
            usuario.setApellido(apellido);
            usuario.setMail(mail);
            usuario.setZona(zona);
            usuario.setClave(clave);

            String idFoto = null;
            if (usuario.getFoto() != null) {
                idFoto = usuario.getFoto().getId();
            }

            Foto foto = fotoServicie.actualizar(idFoto, archivo);
            usuario.setFoto(foto);

            repository.save(usuario);

        }catch(ErrorService e) {
            throw e;
        }catch(Exception e) {
            e.printStackTrace();
            throw new ErrorService("Error de Sistemas");
        }
    }

    public Usuario login(String mail, String clave) throws ErrorService {

        try {

            if (mail == null || mail.trim().isEmpty()) {
                throw new ErrorService("Debe indicar el usuario");
            }

            if (clave == null || clave.trim().isEmpty()) {
                throw new ErrorService("Debe indicar la clave");
            }

            Usuario usuario = null;
            try {
                usuario = repository.buscarUsuarioPorEmailYClave(mail, clave);
            } catch (NoResultException ex) {
                throw new ErrorService("No existe usuario para el correo y clave indicado");
            }

            return usuario;

        }catch(ErrorService e) {
            throw e;
        }catch(Exception e) {
            e.printStackTrace();
            throw new ErrorService("Error de Sistemas");
        }
    }

    @Transactional
    public void deshabilitar(String id) throws ErrorService {

        try {

            Usuario usuario = buscarUsuario(id);
            usuario.setBaja(new Date());
            repository.save(usuario);

        }catch(ErrorService e) {
            throw e;
        }catch(Exception e) {
            e.printStackTrace();
            throw new ErrorService("Error de Sistemas");
        }

    }

    @Transactional
    public void habilitar(String id) throws ErrorService {

        try {

            Usuario usuario = buscarUsuario(id);
            usuario.setBaja(null);
            repository.save(usuario);

        }catch(ErrorService e) {
            throw e;
        }catch(Exception e) {
            e.printStackTrace();
            throw new ErrorService("Error de Sistemas");
        }

    }

    @Transactional(readOnly=true)
    public Usuario buscarUsuario(String idUsuario) throws ErrorService {

        try {

            if (idUsuario == null || idUsuario.trim().isEmpty()) {
                throw new ErrorService("Debe indicar el usuario");
            }

            Optional<Usuario> optional = repository.findById(idUsuario);
            Usuario usuario = null;
            if (optional.isPresent()) {
                usuario= optional.get();
                if (usuario.getBaja() != null){
                    throw new ErrorService("No se encuentra el usuario indicado");
                }
            }

            return usuario;

        } catch (ErrorService ex) {
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorService("Error de sistema");
        }

    }

    @Transactional(readOnly=true)
    public List<Usuario> listarUsuario()throws ErrorService {

        try {

            return repository.findAll();

        }catch(Exception e) {
            e.printStackTrace();
            throw new ErrorService("Error de Sistemas");
        }

    }

}

