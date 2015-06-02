package edu.warbot.controllers;

import edu.warbot.editor.CodeEditorListener;
import edu.warbot.exceptions.NotFoundEntityException;
import edu.warbot.exceptions.UnauthorisedToEditLockException;
import edu.warbot.exceptions.UnauthorisedToEditNotMemberException;
import edu.warbot.game.Team;
import edu.warbot.models.Account;
import edu.warbot.models.Party;
import edu.warbot.models.WebAgent;
import edu.warbot.models.WebCode;
import edu.warbot.repository.AccountRepository;
import edu.warbot.repository.PartyRepository;
import edu.warbot.repository.WebAgentRepository;
import edu.warbot.services.CodeEditorService;
import edu.warbot.services.WarbotOnlineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by BEUGNON on 01/04/2015.
 * CodeEditorController, un contrôleur asynchrône pour gérer les requêtes de lecture et d'écriture liées
 * à l'éditeur de code
 *
 * @author Sébastien Beugnon
 */
@Async
@Controller
@Secured({"ROLE_USER", "ROLE_ADMIN"})
public class CodeEditorController {

    @Autowired
    private WebAgentRepository webAgentRepository;
    @Autowired
    private PartyRepository partyRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private CodeEditorService codeEditorService;
    @Autowired
    private SimpMessageSendingOperations messagingTemplate;
    @Autowired
    private WarbotOnlineService warbotOnlineService;
    @Autowired
    private CodeEditorListener locks;


    @SubscribeMapping("/editor/register")
    public void registerUser(Principal principal) {
        Assert.notNull(principal);
    }


    @MessageMapping("/editor/list")
    public List<WebAgent> giveListAgentFor(@RequestParam Long idParty)
            throws NotFoundEntityException {
        Party party = partyRepository.findOne(idParty);
        Assert.notNull(party);
        return webAgentRepository.findForParty(party);
    }

    @MessageMapping("/editor/get")
    public void getCodeForAgent(Principal principal, Trade trade)
            throws NotFoundEntityException {
        Party party = partyRepository.findOne(trade.getIdParty());
        Assert.notNull(party);
        WebAgent agent = webAgentRepository.findOne(trade.getIdWebAgent());
        Assert.notNull(agent);
        WebCode wc = codeEditorService.getWebCode(party, agent);
        Assert.notNull(wc);

        Map<String, Object> map = new HashMap<>();
        map.put(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON);

        Map<String, Object> m = new HashMap<>();
        m.put("webAgentId", agent.getId());
        m.put("content", wc.getContent());

        messagingTemplate.convertAndSendToUser(principal.getName(), "/editor/code", m, map);
    }

    @MessageMapping("/editor/lock")
    public boolean lock(@RequestParam Long idParty,
                        @RequestParam Long idWebAgent,
                        Principal principal) {
        Team
        Assert.notNull(principal);
        Account account = accountRepository.findByEmail(principal.getName());
        Assert.notNull(account);
        Party party = partyRepository.findOne(idParty);
        Assert.notNull(party);
        WebAgent agent = webAgentRepository.findOne(idWebAgent);
        Assert.notNull(agent);
        locks.lock(account, warbotOnlineService.findWebCodeForPartyAndAgent(party, agent));
        return true;
    }

    @MessageMapping("/editor/save")
    public boolean save(SaveTrade trade,
                        Principal principal) {
        Assert.notNull(principal);
        Account account = accountRepository.findByEmail(principal.getName());
        Assert.notNull(account);
        Party party = partyRepository.findOne(trade.getIdParty());
        Assert.notNull(party);
        WebAgent agent = webAgentRepository.findOne(trade.getIdWebAgent());
        Assert.notNull(agent);
        WebCode code = codeEditorService.getWebCode(party, agent);
        //code.getContent().replace("    ", "\t");
        code.setContent(trade.getContent());
        try {
            codeEditorService.saveWebCode(account, code);
            return true;
        } catch (UnauthorisedToEditNotMemberException | UnauthorisedToEditLockException e) {
            e.printStackTrace();
            return false;
        }
    }

    @MessageMapping("/editor/unlock")
    public boolean unlock(@RequestParam Long idParty,
                          @RequestParam Long idWebAgent,
                          Principal principal) {
        Assert.notNull(principal);
        Account account = accountRepository.findByEmail(principal.getName());
        Assert.notNull(account);
        Party party = partyRepository.findOne(idParty);
        Assert.notNull(party);
        WebAgent agent = webAgentRepository.findOne(idWebAgent);
        Assert.notNull(agent);
        return true;
    }

    public static class Trade {
        private Long idParty;
        private Long idWebAgent;

        public Trade() {
        }

        public Trade(Long idParty, Long idWebAgent) {
            this.idParty = idParty;
            this.idWebAgent = idWebAgent;
        }

        public Long getIdParty() {
            return idParty;
        }

        public void setIdParty(Long idParty) {
            this.idParty = idParty;
        }

        public Long getIdWebAgent() {
            return idWebAgent;
        }

        public void setIdWebAgent(Long idWebAgent) {
            this.idWebAgent = idWebAgent;
        }
    }

    public static class SaveTrade

    {
        private Long idParty;

        private Long idWebAgent;

        private String content;

        public SaveTrade() {
        }

        public SaveTrade(Long idParty, Long idWebAgent, String content) {
            this.idParty = idParty;
            this.idWebAgent = idWebAgent;
            this.content = content;
        }

        public Long getIdParty() {
            return idParty;
        }

        public void setIdParty(Long idParty) {
            this.idParty = idParty;
        }

        public Long getIdWebAgent() {
            return idWebAgent;
        }

        public void setIdWebAgent(Long idWebAgent) {
            this.idWebAgent = idWebAgent;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }


}
