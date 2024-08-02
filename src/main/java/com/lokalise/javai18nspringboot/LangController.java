// API class
package com.lokalise.javai18nspringboot;

import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class LangController {

    @Autowired
    private MessageSource msgSource;

    @GetMapping("/api/message") 
    public String mensaje(@RequestParam(name = "lang", required = false) String lang) {
        
        // variable para detectar el idioma proporcionado o usar el inglés por defecto
        Locale idioma = new Locale(lang != null ? lang : "en");
        
        // retornamos el mensaje asociado a la clave "salutation" en el idioma elegido
        return msgSource.getMessage("salutation", null, idioma);
    }
    
}