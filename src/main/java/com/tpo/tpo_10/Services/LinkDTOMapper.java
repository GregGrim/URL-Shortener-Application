package com.tpo.tpo_10.Services;

import com.tpo.tpo_10.DTOs.CreateLinkDto;
import com.tpo.tpo_10.DTOs.LinkDto;
import com.tpo.tpo_10.Entities.Link;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class LinkDTOMapper {
    String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    public LinkDTOMapper(){

    }

    public Link map (CreateLinkDto createLinkDto){
        Link link = new Link();
        link.setId(generateId());
        if (createLinkDto.getPassword() != null)
            link.setPassword(createLinkDto.getPassword());
        link.setName(createLinkDto.getName());
        link.setTargetUrl(createLinkDto.getTargetUrl());
        link.setRedirectUrl(generateRedirectUrl(link.getId()));
        link.setVisits(0);
        return link;
    }

    public LinkDto map (Link link){
        LinkDto linkDto = new LinkDto();
        linkDto.setId(link.getId());
        linkDto.setName(link.getName());
        linkDto.setTargetUrl(link.getTargetUrl());
        linkDto.setRedirectUrl(link.getRedirectUrl());
        linkDto.setVisits(link.getVisits());
        return linkDto;
    }

    public Link map(LinkDto linkDto) {
        Link link = new Link();
        link.setId(linkDto.getId());
        link.setName(linkDto.getName());
        link.setTargetUrl(linkDto.getTargetUrl());
        link.setRedirectUrl(linkDto.getRedirectUrl());
        link.setVisits(linkDto.getVisits());
        return link;
    }


    private String generateId(){
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < 10; i++) {
            int index = random.nextInt(alphabet.length());
            char randomChar = alphabet.charAt(index);
            stringBuilder.append(randomChar);
        }
        return stringBuilder.toString();

    }
    private String generateRedirectUrl(String id){
        return "http://localhost:8080/red/"+id;
    }


}
