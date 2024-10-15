package com.is2.tinder.controller;


import com.is2.tinder.business.domain.entities.Usuario;
import com.is2.tinder.business.domain.entities.Zona;
import com.is2.tinder.business.logic.error.ErrorService;
import com.is2.tinder.business.logic.service.UsuarioService;
import com.is2.tinder.business.logic.service.ZonaService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequestMapping("/usuario") /* cualquier petición que empiece con /usuario será manejada por esta clase o alguno de sus métodos */
public class UsuarioController {

    @Autowired
    ZonaService zonaService;

    @Autowired
    UsuarioService usuarioService;

    @PostMapping("/loginUsuario")
    public String loginUsuario(@RequestParam String email, @RequestParam String clave, ModelMap modelo, HttpSession session) throws ErrorService {

        try {

            Usuario usuario = usuarioService.login(email, clave);
            session.setAttribute("usuariosession", usuario);

            return "redirect:/mascota/mis-mascotas";

        } catch (ErrorService e) {

            modelo.put("error", e.getMessage());
            modelo.put("email", email);
            modelo.put("clave", clave);

            return "login.html";

        } catch (Exception ex) {

            ex.printStackTrace();
            modelo.put("error", ex.getMessage());

            return "login.html";
        }

    }

    @PostMapping("/registrar")
    public String crearUsuario(ModelMap modelo, MultipartFile archivo, @RequestParam String nombre, @RequestParam String apellido, @RequestParam String mail, @RequestParam String clave1, @RequestParam String clave2, @RequestParam String idZona) {

        try {

            usuarioService.crearUsuario(archivo, nombre, apellido, mail, clave1, clave2, idZona);

            modelo.put("titulo", "Bienvenido a Tinder de Mascotas. ");
            modelo.put("descripcion", "¡Tu usuario fue registrado!. ");
            return "exito.html";

        } catch (ErrorService ex) {

            try {


                List<Zona> zonas = zonaService.listarZona();
                modelo.put("zonas", zonas);

            } catch (ErrorService e) {}

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

        } catch(Exception e) {

            e.printStackTrace();
            modelo.put("error", e.getMessage());

            return "registro.html";
        }

    }


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

        return "perfil.html";
    }

    @PostMapping("/actualizar-perfil")
    public String modificarUsuario(ModelMap model, HttpSession session, MultipartFile archivo, @RequestParam String id, @RequestParam String nombre, @RequestParam String apellido, @RequestParam String mail, @RequestParam String clave1, @RequestParam String clave2, @RequestParam String idZona) {

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
                model.put("zonas", zonas);

            } catch (ErrorService e) {
                model.put("error", e.getMessage());
            }

            model.put("error", ex.getMessage());
            model.put("perfil", usuario);

            return "perfil.html";

        }catch(Exception e) {

            e.printStackTrace();
            model.put("error", e.getMessage());

            return "perfil.html";
        }
    }

}
