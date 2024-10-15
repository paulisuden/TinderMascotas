package com.is2.tinder.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import com.is2.tinder.business.domain.entities.Zona;
import com.is2.tinder.business.logic.service.ZonaService;

@Controller
public class InicioController {

    @Autowired
    private ZonaService zonaService;

    @GetMapping("/")
    public String index() {
        return "index.html";
    }

    @GetMapping("/inicio")
    public String inicio() {
        return "index.html";
    }

    @GetMapping("/login-usuario")
    public String login() {
        return "login-usuario.html";
    }

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
