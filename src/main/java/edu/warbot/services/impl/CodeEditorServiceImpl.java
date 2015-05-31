package edu.warbot.services.impl;

import edu.warbot.editor.CodeEditorListener;
import edu.warbot.exceptions.UnauthorisedToEditLockException;
import edu.warbot.exceptions.UnauthorisedToEditNotMemberException;
import edu.warbot.models.Account;
import edu.warbot.models.Party;
import edu.warbot.models.WebAgent;
import edu.warbot.models.WebCode;
import edu.warbot.repository.WebCodeRepository;
import edu.warbot.services.CodeEditorService;
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
        return webCodeRepository.findWebCodeForTeamAndWebAgent(party, agent);
    }

    public boolean deleteCodeForParty(Long partyId) {
        return webCodeRepository.deleteCodeForParty(partyId);
    }

}
