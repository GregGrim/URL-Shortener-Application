package com.tpo.tpo_10.Controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.LocaleResolver;

import java.util.Locale;

@Controller
@SessionAttributes("currentLocale")
public class LocaleViewController {

    private final LocaleResolver localeResolver;

    public LocaleViewController(LocaleResolver localeResolver) {
        this.localeResolver = localeResolver;
    }

    @PostMapping("/set-locale")
    public String setLocale(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam String locale,
            Model model
    ) {
        Locale newLocale = Locale.forLanguageTag(locale);
        localeResolver.setLocale(request, response, newLocale);
        model.addAttribute("currentLocale", newLocale);
        return "redirect:/links/view";
    }
}
