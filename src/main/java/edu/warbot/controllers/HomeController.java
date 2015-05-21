package edu.warbot.controllers;

import java.security.Principal;
import java.util.HashSet;
import java.util.List;

import edu.warbot.models.Account;
import edu.warbot.models.Party;
import edu.warbot.repository.AccountRepository;
import edu.warbot.repository.PartyRepository;
import edu.warbot.repository.WebAgentRepository;
import edu.warbot.services.WarbotOnlineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

@Controller
public class HomeController {

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index(Principal principal,Model model) {
		if(principal !=null)
		{
			Account account = accountRepository.findByEmail(principal.getName());
			Assert.notNull(account);
			model.addAttribute("account",account);
			List<Party> l = warbotOnlineService.findPartyByCreator(account);
			model.addAttribute("parties",l);
			return "home/homeSignedIn";
		}
		return "home/homeNotSignedIn";
	}

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private PartyRepository partyRepository;

	@Autowired
	private WarbotOnlineService warbotOnlineService;

	@Autowired
	private WebAgentRepository webAgentRepository;



	@RequestMapping(value = "/teamcode", method = RequestMethod.GET)
	public String teamcode(Principal principal,
						   Model model,
						   @RequestParam Long idParty,
						   @RequestParam(required = false) Long idParty2) {
		Assert.notNull(principal);
		Account account = accountRepository.findByEmail(principal.getName());
		Party party = warbotOnlineService.findPartyById(idParty);
		Assert.notNull(party);

		model.addAttribute("party", party);
		model.addAttribute("agents", webAgentRepository.findAllStarter());
		return "teamcode/teamcode";
	}


}
