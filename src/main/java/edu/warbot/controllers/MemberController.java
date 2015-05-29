package edu.warbot.controllers;

import edu.warbot.models.Account;
import edu.warbot.models.Party;
import edu.warbot.repository.AccountRepository;
import edu.warbot.services.WarbotOnlineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.access.annotation.Secured;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by beugnon on 21/05/15.
 */
@RestController
@Secured({"ROLE_USER", "ROLE_ADMIN"})
public class MemberController {

    @Autowired
    private WarbotOnlineService warbotOnlineService;

    @Autowired
    private AccountRepository accountRepository;

    @RequestMapping(value = "/party/add/members")
    public String addMember(Principal principal, RedirectAttributes ra, @RequestParam Long idParty, @RequestParam Long idUser) {
        Account account = accountRepository.findByEmail(principal.getName());
        Party party = warbotOnlineService.findPartyById(idParty);
        if (party.getCreator().equals(account)) {
            Account member = accountRepository.findOne(idUser);
            Assert.notNull(member);
            if (!party.getMembers().contains(member))
                warbotOnlineService.addMember(party, member);
        }
        ra.addAttribute("id", party.getId());
        return "redirect:/party/show";
    }

    @RequestMapping(value = "/party/remove/members")
    public String removeMember(Principal principal, RedirectAttributes ra, @RequestParam Long idParty, @RequestParam Long idUser) {
        Account account = accountRepository.findByEmail(principal.getName());
        Party party = warbotOnlineService.findPartyById(idParty);

        if (party.getCreator().equals(account)) {
            Account member = accountRepository.findOne(idUser);
            Assert.notNull(member);
            if (party.getMembers().contains(member))
                warbotOnlineService.removeMember(party, member);
        }
        ra.addAttribute("id", party.getId());
        return "redirect:/party/show";
    }

    @Async
    @ResponseBody
    @RequestMapping(value = "/party/list/unmembers")
    public Map<Long, String> allUnMembers(Principal principal, @RequestParam Long idParty
            , @RequestParam(required = false) String letters) {
        Account account = accountRepository.findByEmail(principal.getName());
        Party party = warbotOnlineService.findPartyById(idParty);
        Assert.notNull(party);
        Map<Long, String> unmembers = new HashMap<>();
        if (party.getCreator().equals(account)) {

            Iterable<Account> iterable = accountRepository.findAll();
            Iterator<Account> iterator = iterable.iterator();
            while (iterator.hasNext()) {
                Account acc = iterator.next();
                if (!party.getMembers().contains(acc) && acc.getScreenName().toLowerCase().startsWith(letters.toLowerCase()))
                    unmembers.put(acc.getId(), acc.getScreenName());
            }
        }

        return unmembers;
    }



}
