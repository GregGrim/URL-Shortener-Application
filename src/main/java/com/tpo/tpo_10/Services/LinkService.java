package com.tpo.tpo_10.Services;

import com.tpo.tpo_10.DTOs.CreateLinkDto;
import com.tpo.tpo_10.DTOs.LinkDto;
import com.tpo.tpo_10.Entities.Link;
import com.tpo.tpo_10.Repositories.LinkRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;


@Service
public class LinkService {

    private final LinkRepository linkRepository;
    private final LinkDTOMapper linkDTOMapper;

    private Validator validator;

    public LinkService(LinkRepository linkRepository, LinkDTOMapper linkDTOMapper, Validator validator) {
        this.linkRepository = linkRepository;
        this.linkDTOMapper = linkDTOMapper;
        this.validator = validator;
    }

    public LinkDto createLink(
            CreateLinkDto createLinkDto
    ) {
        Set<ConstraintViolation<CreateLinkDto>> violations = validator.validate(createLinkDto);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        if (linkRepository.getLinkByName(createLinkDto.getName()) != null) {
            return null;
        } else {
            Link newLink = linkRepository.saveLink(linkDTOMapper.map(createLinkDto));
            return linkDTOMapper.map(newLink);
        }
    }

    public LinkDto getLink(String id) {
        Link link = linkRepository.getLink(id);
        if (link == null) {
            return null;
        }
        return linkDTOMapper.map(link);
    }

    public LinkDto getLinkByName(String name) {
        Link link = linkRepository.getLinkByName(name);
        if (link == null) {
            return null;
        }
        return linkDTOMapper.map(link);
    }

    public void updateLink(LinkDto linkDto) {
        Set<ConstraintViolation<LinkDto>> violations = validator.validate(linkDto);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        Link oldLink = linkRepository.getLink(linkDto.getId());
        Link link = linkDTOMapper.map(linkDto);
        link.setPassword(oldLink.getPassword());
        linkRepository.saveLink(link);
    }

    public void deleteLink(String id) {
        linkRepository.deleteLink(id);
    }

    public void incrementVisits(String id) {
        Link link = linkRepository.getLink(id);
        link.setVisits(link.getVisits() + 1);
        linkRepository.saveLink(link);
    }

    public boolean checkPassword(String id, String passwordFromPatch) {
        Link link = linkRepository.getLink(id);
        return passwordFromPatch.equals(link.getPassword());
    }

    public void changeLinkPassword(String id, String password) {
        Link link = linkRepository.getLink(id);
        link.setPassword(password);
        linkRepository.saveLink(link);
    }
}
