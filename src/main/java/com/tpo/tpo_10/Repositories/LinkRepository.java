package com.tpo.tpo_10.Repositories;

import com.tpo.tpo_10.Entities.Link;
import com.tpo.tpo_10.Services.H2Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class LinkRepository {
    final H2Service h2Service;

    @Autowired
    private ILinkRepository linkRepositoryManager;

    public LinkRepository(ILinkRepository linkRepositoryManager){
        this.linkRepositoryManager = linkRepositoryManager;
        this.h2Service = new H2Service();
        h2Service.createDB();
    }

    public Link saveLink(Link link){
        return linkRepositoryManager.save(link);
    }
    public Link getLink(String id){
        return linkRepositoryManager.findById(id).orElse(null);
    }

    public Link getLinkByName(String name){
        return linkRepositoryManager.findByName(name);
    }

    public void deleteLink(String id){
        linkRepositoryManager.deleteById(id);
    }

    public boolean existsByTargetUrl(String url) {
        return linkRepositoryManager.findByTargetUrl(url) != null;
    }
}
