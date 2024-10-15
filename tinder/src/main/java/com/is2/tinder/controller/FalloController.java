package com.is2.tinder.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class FalloController implements ErrorController {

    @RequestMapping(value = "/error", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView renderErrorPage(HttpServletRequest httpRequest) {
        /*Se encarga de generar una página de error personalizada según el código de error HTTP recibido.*/

        ModelAndView errorPage = new ModelAndView("error");
        String errorMsg = "";
        //obtengo el código de error
        int httpErrorCode = (Integer) httpRequest.getAttribute("javax.servlet.error.status_code");
                                                                //disponible cuando ocurre un error HTTP
        switch (httpErrorCode) {
            case 400 -> errorMsg = "El recurso solicitado no existe";
            case 401 -> errorMsg = "No se encuentra autorizado";
            case 403 -> errorMsg = "No tiene permisos para acceder al recurso";
            case 404 -> errorMsg = "El recurso solicitado no fue encontrado";
            case 500 -> errorMsg = "Ocurrió un error interno";
        }
        errorPage.addObject("codigo", httpErrorCode);
        errorPage.addObject("mensaje", errorMsg);
        return errorPage;
    }

}
