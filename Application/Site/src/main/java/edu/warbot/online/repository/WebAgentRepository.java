package edu.warbot.online.repository;


import edu.warbot.online.models.WebAgent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface WebAgentRepository extends JpaRepository<WebAgent, Long> {


    WebAgent findByType(String type);

    List<WebAgent> findAllByIsPremiumTrueAndIsActivatedTrue();

    List<WebAgent> findAllByIsActivatedTrue();

    List<WebAgent> findAllByIsPremiumTrue();

    List<WebAgent> findAllByIsPremiumFalseAndIsActivatedTrue();

}
