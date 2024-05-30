package com.tpo.tpo_10.Controllers;

import com.tpo.tpo_10.DTOs.LinkDto;
import com.tpo.tpo_10.Services.LinkService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@Tag(name = "Redirect Controller")
@RestController
@RequestMapping("/red")
public class RedirectController {
    private final LinkService linkService;

    public RedirectController(LinkService linkService) {
        this.linkService = linkService;
    }
    // redirect endpoint does not work in SwaggerUI perhaps due to CORS policies,
    // but it works just in URL search bar
    @GetMapping("/{id}")
    public RedirectView redirect(@PathVariable String id){
        LinkDto link = linkService.getLink(id);
        if (link != null) {
            System.out.println("Redirecting to: " + link.getTargetUrl());
            linkService.incrementVisits(id);
            return new RedirectView(link.getTargetUrl());
        } else {
            return new RedirectView("/404");
        }
    }
}
