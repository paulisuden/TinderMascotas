package com.is2.tinder.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.is2.tinder.business.domain.entities.Zona;
import com.is2.tinder.business.logic.error.ErrorService;
import com.is2.tinder.business.logic.service.UsuarioService;
import com.is2.tinder.business.logic.service.ZonaService;

public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ZonaService zonaService;

    @PostMapping("/registrar")
    public String crearUsuario(
            ModelMap modelo,
            MultipartFile archivo,
            @RequestParam String nombre,
            @RequestParam String apellido,
            @RequestParam String mail,
            @RequestParam String clave1,
            @RequestParam String clave2,
            @RequestParam String idZona) {

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
            modelo.put("descripcion", "Usuario registrado exitosamente. ");
            return "exito.html";

        } catch (ErrorService ex) {

            try {
                List<Zona> zonas = zonaService.listarZona();
                modelo.put("zonas", zonas);
            } catch (ErrorService e) {
            }

            modelo.put("error", ex.getMessage());
            modelo.put("nombre", nombre);
            modelo.put("apellido", apellido);
            modelo.put("mail", mail);
            modelo.put("clave1", clave1);
            modelo.put("clave2", clave2);
            return "registro.html";

        } catch (Exception e) {
            // Cuando hay excepcion se queda en la pantalla pero muestra el error
            e.printStackTrace();
            modelo.put("error", e.getMessage());
            return "registro.html";
        }

    }
}
