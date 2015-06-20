package edu.warbot.online.services.impl;

import edu.warbot.online.editor.CodeEditorListener;
import edu.warbot.online.exceptions.UnauthorisedToEditLockException;
import edu.warbot.online.exceptions.UnauthorisedToEditNotMemberException;
import edu.warbot.online.models.Account;
import edu.warbot.online.models.Party;
import edu.warbot.online.models.WebAgent;
import edu.warbot.online.models.WebCode;
import edu.warbot.online.repository.WebCodeRepository;
import edu.warbot.online.services.CodeEditorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * Created by BEUGNON on 01/04/2015.
 *
 * @author Sébastien Beugnon
 */
@Service
@Transactional
public class CodeEditorServiceImpl implements CodeEditorService {

    @Autowired
    private WebCodeRepository webCodeRepository;

    @Autowired
    private CodeEditorListener codeEditorListener;

    public void saveWebCode(Account editor, WebCode webCode)
            throws UnauthorisedToEditLockException,
            UnauthorisedToEditNotMemberException {
//        if(!webCode.getParty().getMembers().contains(editor))
        //          throw new UnauthorisedToEditNotMemberException(editor,webCode.getParty());
        webCode.setLastModification(new Date());
        webCodeRepository.save(webCode);
        codeEditorListener.unlock(editor, webCode);
    }

    public WebCode getWebCode(Party party, WebAgent agent) {
        return webCodeRepository.findWebCodeByPartyAndAgent(party, agent);
    }

    public void deleteCodeForParty(Party partyId) {
        webCodeRepository.deleteByParty(partyId);
    }

}
