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
import org.springframework.web.bind.annotation.RequestParam;

import com.is2.tinder.business.domain.entities.Zona;
import com.is2.tinder.business.logic.service.UsuarioService;
import com.is2.tinder.business.logic.service.ZonaService;

import jakarta.servlet.http.HttpSession;

/**
 *
 * @author IS2
 */
@Controller
public class InicioController {

    @Autowired
    private ZonaService zonaService;

    /////////////////////////////////////////////
    /////////////////////////////////////////////
    ///////// PÁGINA PRINCIPAL ///////////
    /////////////////////////////////////////////
    /////////////////////////////////////////////

    @GetMapping("/")
    public String index() {
        return "index.html";
    }

    @GetMapping("/inicio")
    public String inicio() {
        return "index.html";
    }

    /////////////////////////////////////////////
    ///////// NAVEGAR OPCIÓN LOGIN //////////////
    ////////////////////////////////////////////

    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error, @RequestParam(required = false) String logout,
            ModelMap model) {
        if (error != null) {
            model.put("error", "Nombre de Usuario o clave incorrecto");
        }
        if (logout != null) {
            model.put("logout", "Ha salido correctamente de la plataforma.");
        }
        return "login.html";
    }

    @GetMapping("/logout")
    public String logaout(HttpSession session) {
        session.setAttribute("usuariosession", null);
        return "redirect:/inicio";
    }

    /////////////////////////////////////////////
    ///////// NAVEGAR OPCIÓN REGISTRO ///////////
    /////////////////////////////////////////////

    @GetMapping("/registro")
    public String registro(ModelMap modelo) {

        try {

            List<Zona> zonas = zonaService.listarZona();
            modelo.put("zonas", zonas);

            return "registro.html";

        } catch (Exception e) {
            e.printStackTrace();
            modelo.put("error", e.getMessage());
            return "";
        }
    }

}
