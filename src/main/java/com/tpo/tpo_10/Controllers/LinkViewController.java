package com.tpo.tpo_10.Controllers;

import com.tpo.tpo_10.DTOs.CreateLinkDto;
import com.tpo.tpo_10.DTOs.LinkDto;
import com.tpo.tpo_10.Services.LinkService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ConstraintViolation;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Locale;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/links")
public class LinkViewController {

    private final LinkService linkService;

    private final LocaleResolver localeResolver;

    private final MessageSource messageSource;

    public LinkViewController(LinkService linkService, LocaleResolver localeResolver, MessageSource messageSource) {
        this.linkService = linkService;
        this.localeResolver = localeResolver;
        this.messageSource = messageSource;
    }

    @GetMapping("/view")
    public String viewPage(Model model, HttpServletRequest request) {
        model.addAttribute("newLink", new CreateLinkDto());
        Locale currentLocale = localeResolver.resolveLocale(request);
        model.addAttribute("currentLocale", currentLocale);
        return "view";
    }

    @PostMapping("/create")
    public String createLink(
            @ModelAttribute CreateLinkDto linkDto,
            RedirectAttributes redirectAttributes
    ) {
        try{
            LinkDto createdLink = linkService.createLink(linkDto);
            if (createdLink == null) {
                String message = messageSource.getMessage("error.link.exists", null, LocaleContextHolder.getLocale());
                redirectAttributes.addFlashAttribute("error", message);
            } else {
                String message = messageSource.getMessage("success.link.created", null, LocaleContextHolder.getLocale());
                redirectAttributes.addFlashAttribute("success", message);
                redirectAttributes.addFlashAttribute("created_link", createdLink);
            }
        } catch (ConstraintViolationException e) {
            String message = e.getConstraintViolations().stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining("<br/>"));
            redirectAttributes.addFlashAttribute("error", message);
        }
        return "redirect:/links/view";
    }

    @GetMapping("/search")
    public String searchLink(
            @RequestParam String name,
            @RequestParam String password,
            RedirectAttributes redirectAttributes
    ){
        LinkDto searchedLink = linkService.getLinkByName(name);
        if (searchedLink == null) {
            String message = messageSource.getMessage("error.link.notfound", null, LocaleContextHolder.getLocale());
            redirectAttributes.addFlashAttribute("error", message);
            return "redirect:/links/view";
        } else if (!linkService.checkPassword(searchedLink.getId(), password)) {
            String message = messageSource.getMessage("error.password.incorrect", null, LocaleContextHolder.getLocale());
            redirectAttributes.addFlashAttribute("error", message);
            return "redirect:/links/view";
        } else {
            redirectAttributes.addFlashAttribute("searched_link", searchedLink);
            String message = messageSource.getMessage("success.link.found", null, LocaleContextHolder.getLocale());
            redirectAttributes.addFlashAttribute("success", message);
            redirectAttributes.addFlashAttribute("password", password);
            return "redirect:/links/view";
        }
    }

    @PostMapping("/edit") // patch request is not supported by html forms
    public String editLink(
            @RequestParam String id,
            @RequestParam String name,
            @RequestParam String redirectUrl,
            @RequestParam String password,
            RedirectAttributes redirectAttributes
    ) {
        LinkDto linkDto = linkService.getLink(id);
        LinkDto linkWithSameName = linkService.getLinkByName(name);
        if (linkWithSameName!=null && !linkWithSameName.getId().equals(id)) {
            String message = messageSource.getMessage("error.link.exists", null, LocaleContextHolder.getLocale());
            redirectAttributes.addFlashAttribute("error", message);
            return "redirect:/links/view";
        }
        linkDto.setName(name);
        linkDto.setRedirectUrl(redirectUrl);
        try{
            linkService.updateLink(linkDto);
            linkService.changeLinkPassword(id, password);
            String message = messageSource.getMessage("success.link.updated", null, LocaleContextHolder.getLocale());
            redirectAttributes.addFlashAttribute("success", message);
            return "redirect:/links/view";
        } catch (ConstraintViolationException e) {
            String message = e.getConstraintViolations().stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining("<br/>"));
            redirectAttributes.addFlashAttribute("error", message);
            return "redirect:/links/view";
        }
    }

    @DeleteMapping("/delete/{id}")
    public String deleteLink(
            @PathVariable String id
    ) {
        linkService.deleteLink(id);
        return "redirect:/links/view";
    }
}
