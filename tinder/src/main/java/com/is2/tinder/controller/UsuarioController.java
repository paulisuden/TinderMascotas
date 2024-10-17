/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.is2.tinder.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.is2.tinder.business.domain.entities.Usuario;
import com.is2.tinder.business.domain.entities.Zona;
import com.is2.tinder.business.logic.error.ErrorService;
import com.is2.tinder.business.logic.service.UsuarioService;
import com.is2.tinder.business.logic.service.ZonaService;

import jakarta.servlet.http.HttpSession;

/**
 *
 * @author IS2
 */
@Controller
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ZonaService zonaService;

    /////////////////////////////////////////////
    /////////////////////////////////////////////
    //////////////// VIEW: Login /////////////
    /////////////////////////////////////////////
    /////////////////////////////////////////////

    @PostMapping("/loginUsuario")
    public String loginUsuario(
            @RequestParam String email,
            @RequestParam String clave,
            ModelMap modelo,
            HttpSession session) {
        try {

            Usuario usuario = usuarioService.login(email, clave);
            session.setAttribute("usuariosession", usuario);

            return "redirect:/mascota/mis-mascotas";

        } catch (ErrorService ex) {

            modelo.put("error", ex.getMessage());
            modelo.put("email", email);
            modelo.put("clave", clave);
            System.out.println("Error al hacer el login: " + ex.toString());
            return "login.html";

        } catch (Exception e) {
            e.printStackTrace();
            modelo.put("error", e.getMessage());
            System.out.println("Error al hacer el login: " + e.toString());

            return "login.html";
        }

    }

    /////////////////////////////////////////////
    /////////////////////////////////////////////
    //////////////// VIEW: Registro /////////////
    /////////////////////////////////////////////
    /////////////////////////////////////////////

    @PostMapping("/registrar")
    public String crearUsuario(
            @RequestParam String nombre,
            @RequestParam String apellido,
            @RequestParam String mail,
            @RequestParam String clave1,
            @RequestParam String clave2,
            @RequestParam String idZona,
            ModelMap modelo,
            MultipartFile archivo) {

        try {
            usuarioService.crearUsuario(
                    archivo,
                    nombre,
                    apellido,
                    mail,
                    clave1,
                    clave2,
                    idZona);

            modelo.put("titulo", "Bienvenido al Tinder de Mascotas. ");
            modelo.put("descripcion", "Tu usuario fue registrado de manera satisfactoria. ");
            return "exito.html";

        } catch (ErrorService ex) {
            modelo.put("error", ex.getMessage());
            modelo.put("nombre", nombre);
            modelo.put("apellido", apellido);
            modelo.put("mail", mail);
            modelo.put("clave1", clave1);
            modelo.put("clave2", clave2);
            return "registro.html";

        } catch (Exception e) {
            e.printStackTrace();
            modelo.put("error", e.getMessage());
            return "registro.html";
        }

    }

    /////////////////////////////////////////////
    /////////////////////////////////////////////
    //////////////// VIEW: Perfil ///////////////
    /////////////////////////////////////////////
    /////////////////////////////////////////////

    @GetMapping("/editar-perfil")
    public String editarPerfil(HttpSession session, ModelMap model) {

        try {

            List<Zona> zonas = zonaService.listarZona();
            model.put("zonas", zonas);

            Usuario usuario = (Usuario) session.getAttribute("usuariosession");
            model.addAttribute("perfil", usuario);

        } catch (ErrorService e) {
            model.addAttribute("error", e.getMessage());
        }

        return "perfil-usuario.html";
    }

    @PostMapping("/actualizar-perfil")
    public String modificarUsuario(
            ModelMap modelo,
            HttpSession session,
            MultipartFile archivo,
            @RequestParam String id,
            @RequestParam String nombre,
            @RequestParam String apellido,
            @RequestParam String mail,
            @RequestParam String clave1,
            @RequestParam String clave2,
            @RequestParam String idZona) {

        Usuario usuario = null;
        try {

            Usuario login = (Usuario) session.getAttribute("usuariosession");
            if (login == null || !login.getId().equals(id)) {
                return "redirect:/inicio";
            }

            usuario = usuarioService.buscarUsuario(id);
            usuarioService.modificarUsuario(archivo, id, nombre, apellido, mail, clave2, clave2, idZona);
            session.setAttribute("usuariosession", usuario);

            return "exito.html";

        } catch (ErrorService ex) {

            try {
                List<Zona> zonas = zonaService.listarZona();
                modelo.put("zonas", zonas);
            } catch (ErrorService e) {
            }

            modelo.put("error", ex.getMessage());
            modelo.put("perfil", usuario);

            return "perfil-usuario.html";

        } catch (Exception e) {
            e.printStackTrace();
            modelo.put("error", e.getMessage());
            return "perfil-usuario.html";
        }
    }

}
