package edu.warbot.services;

import edu.warbot.agents.enums.WarAgentType;
import edu.warbot.models.Party;
import edu.warbot.models.WebAgent;
import edu.warbot.models.WebCode;
import edu.warbot.repository.AccountRepository;
import edu.warbot.repository.PartyRepository;
import edu.warbot.repository.WebAgentRepository;
import edu.warbot.repository.WebCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * Created by beugnon on 08/04/15.
 *
 */
@Service
@Transactional
public class WarbotOnlineServiceImpl implements WarbotOnlineService
{
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
    public void init()
    {
        webAgentRepository.save(new WebAgent(WarAgentType.WarBase,true,false));
        webAgentRepository.save(new WebAgent(WarAgentType.WarExplorer,true,false));
        webAgentRepository.save(new WebAgent(WarAgentType.WarRocketLauncher,true,false));
        webAgentRepository.save(new WebAgent(WarAgentType.WarEngineer,true,false));
        webAgentRepository.save(new WebAgent(WarAgentType.WarTurret,true,false));
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
}
