package com.tpo.tpo_10.Controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.LocaleResolver;

import java.util.Locale;

@Tag(name = "Locale Controller")
@RestController
@RequestMapping("/locale")
@SessionAttributes("currentLocale")
public class LocaleRestController {

    private final LocaleResolver localeResolver;

    public LocaleRestController(LocaleResolver localeResolver) {
        this.localeResolver = localeResolver;
    }

    @PostMapping("/set")
    public ResponseEntity<?> setLocale(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam String locale,
            Model model
    ) {
        Locale newLocale = Locale.forLanguageTag(locale);
        localeResolver.setLocale(request, response, newLocale);
        model.addAttribute("currentLocale", newLocale);
        return ResponseEntity.ok().build();
    }
}