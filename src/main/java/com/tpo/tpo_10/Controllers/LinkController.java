package com.tpo.tpo_10.Controllers;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import com.tpo.tpo_10.DTOs.CreateLinkDto;
import com.tpo.tpo_10.DTOs.LinkDto;
import com.tpo.tpo_10.Services.LinkService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.stream.Collectors;

@Tag(name = "Api Controller")
@RestController
@RequestMapping("/api/links")
public class LinkController {

    private final LinkService linkService;
    private final ObjectMapper objectMapper;

    private final MessageSource messageSource;

    public LinkController(LinkService linkService, ObjectMapper objectMapper, MessageSource messageSource) {
        this.linkService = linkService;
        this.objectMapper = objectMapper;
        this.messageSource = messageSource;
    }

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createLink(
            @RequestBody CreateLinkDto linkObject
            ) {
        try {
            LinkDto createdLink = linkService.createLink(linkObject);
            if (createdLink == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            URI savedLinkLocation = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(createdLink.getId())
                    .toUri();
            return ResponseEntity.created(savedLinkLocation).body(createdLink);
        } catch (ConstraintViolationException e) {
            String message = e.getConstraintViolations().stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining(", /n"));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
        }
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getLink(
            @PathVariable String id
    ) {
        LinkDto link = linkService.getLink(id);
        if (link == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(link);
    }

    @PatchMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<?> updateLink(
            @PathVariable String id,
            @RequestBody JsonMergePatch patch
    ) {
        LinkDto link = linkService.getLink(id);
        if (link == null) {
            return ResponseEntity.notFound().build();
        }
        try{
            JsonNode patchNode = objectMapper.convertValue(patch, JsonNode.class);
            if (!patchNode.has("password")
                    || !linkService.checkPassword(id, patchNode.get("password").asText())
            ) {
                String message = messageSource.getMessage("error.password.incorrect", null, LocaleContextHolder.getLocale());
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(message);
            }
            LinkDto patchedLinkDto = applyPatch(patch, link);
            linkService.updateLink(patchedLinkDto);
        } catch (JsonPatchException | JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (ConstraintViolationException e) {
            String message = e.getConstraintViolations().stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining("/n"));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
        }
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<?> deleteLink(
            @PathVariable String id,
            @RequestHeader String pass
    ) {
        LinkDto link = linkService.getLink(id);
        if (link == null) {
            return ResponseEntity.noContent().build();
        }
        if (!linkService.checkPassword(id, pass)) {
            String message = messageSource.getMessage("error.password.incorrect", null, LocaleContextHolder.getLocale());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(message);
        }
        linkService.deleteLink(id);
        return ResponseEntity.noContent().build();
    }

    private LinkDto applyPatch(
            JsonMergePatch patch,
            LinkDto targetLink
    ) throws JsonPatchException, JsonProcessingException {
        JsonNode patched = patch.apply(objectMapper.valueToTree(targetLink));
        return objectMapper.treeToValue(patched, LinkDto.class);
    }
}
