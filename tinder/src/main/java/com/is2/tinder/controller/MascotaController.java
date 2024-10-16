package com.is2.tinder.controller;


import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.is2.tinder.business.domain.entities.Mascota;
import com.is2.tinder.business.domain.entities.Usuario;
import com.is2.tinder.business.domain.enumeration.Sexo;
import com.is2.tinder.business.domain.enumeration.TipoMascota;
import com.is2.tinder.business.logic.error.ErrorService;
import com.is2.tinder.business.logic.service.MascotaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/mascota")
public class MascotaController {

    @Autowired
    private MascotaService mascotaService;

    @PostMapping("/eliminar-perfil")
    public String eliminar(HttpSession session, @RequestParam String id) {

        try {

            Usuario login = (Usuario) session.getAttribute("usuariosession");
            mascotaService.eliminarMascota(login.getId(), id);

        } catch (ErrorService ex) {
            Logger.getLogger(MascotaController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "redirect:/mascota/mis-mascotas";
    }

    @PostMapping("/alta-perfil")
    public String darAlta(HttpSession session, @RequestParam String id, @RequestParam Sexo sexo,
    @RequestParam String nombre, @RequestParam TipoMascota tipo, @RequestParam MultipartFile archivo ) {

        try {
            Usuario login = (Usuario) session.getAttribute("usuariosession");
            mascotaService.crearMascota(nombre, sexo, tipo, archivo, login.getId());

        } catch (ErrorService ex) {
            Logger.getLogger(MascotaController.class.getName()).log(Level.SEVERE, null, ex);

        }
        return "redirect:/mascota/mis-mascotas";
    }

    @GetMapping("/debaja-mascotas")
    public String mascotasDeBaja(HttpSession session, ModelMap model) {

        try {

            Usuario login = (Usuario) session.getAttribute("usuariosession");
            if (login == null) {
                return "redirect:/login";
            }

            Collection<Mascota> mascotas = mascotaService.listarMascotaDeBaja(login.getId());
            model.put("mascotas", mascotas);

            return "mascotasdebaja";

        }catch(Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    @GetMapping("/mis-mascotas")
    public String misMascotas(HttpSession session, ModelMap model) {

        try {

            Usuario login = (Usuario) session.getAttribute("usuariosession");
            if (login == null) {
                return "redirect:/login";
            }

            Collection<Mascota> mascotas = mascotaService.listarMascotaPorUsuario(login.getId());
            model.put("mascotas", mascotas);

            return "mascotas";

        }catch(Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    @GetMapping("/editar-perfil")
    public String editarPerfil(HttpSession session, @RequestParam(required = false) String id, @RequestParam(required = false) String accion, ModelMap model) {

        if (accion == null) {
            accion = "Crear";
        }

        Usuario login = (Usuario) session.getAttribute("usuariosession");
        if (login == null) {
            return "redirect:/login";
        }
        Mascota mascota = new Mascota();
        if (id != null && !id.isEmpty()) {
            try {
                mascota = mascotaService.buscarMascota(id);
            } catch (ErrorService ex) {
                Logger.getLogger(MascotaController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        model.put("perfil", mascota);
        model.put("accion", accion);
        model.put("sexos", Sexo.values());
        model.put("tipos", TipoMascota.values());
        return "mascota.html";
    }

    @PostMapping("/actualizar-perfil")
    public String crearMascota(ModelMap modelo, HttpSession session, MultipartFile archivo, @RequestParam String id, @RequestParam String nombre, @RequestParam Sexo sexo, @RequestParam TipoMascota tipo) {

        Usuario login = (Usuario) session.getAttribute("usuariosession");
        if (login == null) {
            return "redirect:/inicio";
        }

        try {

            if (id == null || id.isEmpty()) {
                mascotaService.crearMascota(nombre, sexo, tipo, archivo, login.getId());
            } else {
                mascotaService.crearMascota(nombre, sexo, tipo, archivo, login.getId());
            }

            return "redirect:/inicio";

        } catch (ErrorService ex) {

            Mascota mascota = new Mascota();
            mascota.setId(id);
            mascota.setNombre(nombre);
            mascota.setSexo(sexo);
            mascota.setTipo(tipo);

            modelo.put("accion", "Actualizar");
            modelo.put("sexos", Sexo.values());
            modelo.put("tipos", TipoMascota.values());
            modelo.put("error", ex.getMessage());
            modelo.put("perfil", login);

            return "mascota.html";
        }
    }
}
