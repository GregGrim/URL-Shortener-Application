package com.tpo.tpo_10.Repositories;

import com.tpo.tpo_10.Entities.Link;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ILinkRepository extends CrudRepository<Link, String> {

    @Query("SELECT l FROM Link l WHERE l.name = ?1")
    Link findByName(String name);

    @Query("SELECT l FROM Link l WHERE l.targetUrl = ?1")
    Link findByTargetUrl(String url);
}
