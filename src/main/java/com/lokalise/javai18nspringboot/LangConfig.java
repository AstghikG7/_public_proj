// Configuration class 
package com.lokalise.javai18nspringboot;

import java.util.Locale;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

@Configuration
public class LangConfig implements WebMvcConfigurer {
    
    @Bean // método para cargar los archivos de mensajes (.propiedades)
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasenames("lang/messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    
    @Bean // método para definir y almacenar el idioma 
    public LocaleResolver localeResolver() {
        CookieLocaleResolver slr=new CookieLocaleResolver();
        slr.setDefaultLocale(new Locale("en"));
        return slr;
    }
    

    @Bean // método que permite cambiar el idioma usando el parámetro "lang" 
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
        lci.setParamName("lang");
        return lci;
    }

    @Override // método que permite el cambio del idioma al procesar la solicitud HTTP
    public void addInterceptors(InterceptorRegistry intercepto) {
        intercepto.addInterceptor(localeChangeInterceptor());
    }
    
    
    
}
