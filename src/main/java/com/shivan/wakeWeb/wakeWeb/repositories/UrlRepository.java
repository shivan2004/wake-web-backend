package com.shivan.wakeWeb.wakeWeb.repositories;

import com.shivan.wakeWeb.wakeWeb.entities.Url;
import com.shivan.wakeWeb.wakeWeb.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UrlRepository extends JpaRepository<Url, Long> {
    List<Url> findAllByUser(User user);

    List<Url> findAllByUrl(String url);

//    Long countAllByActive(true);

    Long countAllByIsActiveIsTrue();
}
