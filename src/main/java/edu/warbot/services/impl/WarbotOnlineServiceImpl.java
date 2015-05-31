package edu.warbot.services.impl;

import edu.warbot.agents.enums.WarAgentType;
import edu.warbot.models.Account;
import edu.warbot.models.Party;
import edu.warbot.models.WebAgent;
import edu.warbot.models.WebCode;
import edu.warbot.repository.AccountRepository;
import edu.warbot.repository.PartyRepository;
import edu.warbot.repository.WebAgentRepository;
import edu.warbot.repository.WebCodeRepository;
import edu.warbot.services.CodeEditorService;
import edu.warbot.services.WarbotOnlineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * Created by beugnon on 08/04/15.
 */
@Service
@Transactional
public class WarbotOnlineServiceImpl implements WarbotOnlineService {
    @Autowired
    private WebAgentRepository webAgentRepository;

    @Autowired
    private PartyRepository partyRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CodeEditorService codeEditorService;

    @Autowired
    private WebCodeRepository webCodeRepository;

    @PostConstruct
    public void init() {
        if (webAgentRepository.findAll().size() == 0) {
            webAgentRepository.save(new WebAgent(WarAgentType.WarBase, true, false));
            webAgentRepository.save(new WebAgent(WarAgentType.WarExplorer, true, false));
            webAgentRepository.save(new WebAgent(WarAgentType.WarRocketLauncher, true, false));
            webAgentRepository.save(new WebAgent(WarAgentType.WarEngineer, true, false));
            webAgentRepository.save(new WebAgent(WarAgentType.WarTurret, true, false));
            webAgentRepository.save(new WebAgent(WarAgentType.WarKamikaze, true, false));
        }
    }

    @Override
    public Party createParty(Party party) {
        return partyRepository.save(party);
    }

    @Override
    public List<WebAgent> findAgentsForParty(Party party) {
        return webAgentRepository.findForParty(party);
    }

    @Override
    public List<WebCode> findWebCodesForParty(Party party) {
        return webCodeRepository.findWebCodeForTeam(party);
    }

    @Override
    public WebCode findWebCodeForPartyAndAgent(Party party, WebAgent agent) {
        return webCodeRepository.findWebCodeForTeamAndWebAgent(party, agent);
    }

    @Override
    public Party findPartyById(Long id) {
        return partyRepository.findOne(id);
    }

    @Override
    public Party findPartyByName(String name) {
        return partyRepository.findByName(name);
    }

    @Override
    public Iterable<Party> findAllParty() {
        return partyRepository.findAll();
    }

    @Override
    public List<Party> findPartyByCreator(Account account) {
        return partyRepository.findByCreator(account);
    }

    @Override
    public boolean deleteParty(Long id) {
        return partyRepository.delete(id);
    }

    @Override
    public void addMember(Party party, Account account) {
        Party party2 = findPartyById(party.getId());
        party2.addMember(account);
        partyRepository.save(party2);
    }

    @Override
    public void removeMember(Party party, Account member) {
        Party party2 = findPartyById(party.getId());
        party2.getMembers().remove(member);
        partyRepository.save(party2);
    }
}
