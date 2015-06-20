package edu.warbot.online.repository;

import edu.warbot.online.models.Account;
import edu.warbot.online.models.Party;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by BEUGNON on 18/03/2015.
 *
 * @author Sebastien Beugnon
 */

public interface PartyRepository extends JpaRepository<Party, Long> {

    @Query("Select p From #{#entityName} p Where p.name LIKE ?1")
    Party findByName(String name);

    @Query("Select p From #{#entityName} p Where p.creator LIKE ?1")
    List<Party> findAllByCreator(Account creator);

    @Query("Select p From #{#entityName} p Where ?1 In p.members")
    List<Party> findAllByMembersContaining(Account member);

    long countByCreator(Account creator);

    long countByMembersContaining(Account member);
}
