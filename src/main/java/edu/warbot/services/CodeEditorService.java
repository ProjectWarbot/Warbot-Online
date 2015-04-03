package edu.warbot.services;

import edu.warbot.exceptions.UnauthorisedToEditLockException;
import edu.warbot.exceptions.UnauthorisedToEditNotMemberException;
import edu.warbot.models.Account;
import edu.warbot.models.Party;
import edu.warbot.models.WebAgent;
import edu.warbot.models.WebCode;
import edu.warbot.repository.WebCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by BEUGNON on 01/04/2015.
 *
 * @author Sébastien Beugnon
 */
@Service
public class CodeEditorService
{

    protected static class Lock implements Comparable<Lock>
    {
        private Account editor;

        private Party party;

        private WebAgent agent;

        public Lock(Account editor, Party party, WebAgent agent)
        {
            this.editor = editor;
            this.party = party;
            this.agent = agent;
        }

        public Account getEditor() {
            return editor;
        }

        public void setEditor(Account editor) {
            this.editor = editor;
        }

        public Party getParty() {
            return party;
        }

        public void setParty(Party party) {
            this.party = party;
        }

        public WebAgent getAgent() {
            return agent;
        }

        public void setAgent(WebAgent agent) {
            this.agent = agent;
        }

        @Override
        public int compareTo(Lock o) {
            if(o==null)
                return -1;
            int i = getParty().getId().compareTo(o.getParty().getId());
            if(i!=0)
                return i;
            i = getAgent().getId().compareTo(o.getAgent().getId());
            return i;
        }
    }

    private Set<Lock> lockManager;


    @Autowired
    private WebCodeRepository webCodeRepository;

    public CodeEditorService()
    {
        this.lockManager = new TreeSet<>();
    }

    public Set<Lock> getLockManager() {
        return lockManager;
    }


    public synchronized boolean lockForEdit(Account editor, Party party, WebAgent agent)
    {
        Lock lock = new Lock(editor,party,agent);
        if(!getLockManager().contains(lock)) {
            getLockManager().add(lock);
            return true;
        }
        return false;
    }

    @Transactional
    public void saveWebCode(Account editor, WebCode webCode)
            throws UnauthorisedToEditLockException,
            UnauthorisedToEditNotMemberException {
        if(!webCode.getParty().getMembers().contains(editor))
            throw new UnauthorisedToEditNotMemberException(editor,webCode.getParty());
        if(haveLockWith(editor,webCode))
        {
            webCode.setLastModification(new Date());
            webCodeRepository.save(webCode);
        }
        else
            throw new UnauthorisedToEditLockException(editor,webCode.getAgent());
    }

    public synchronized boolean haveLockWith(Account editor, WebCode webCode)
    {
        Lock lock = new Lock(editor,webCode.getParty(),webCode.getAgent());
        return getLockManager().contains(lock);
    }

    public synchronized boolean unlockForEdit(Account editor, Party party, WebAgent agent)
    {
        Lock lock = new Lock(editor,party,agent);
        if(getLockManager().contains(lock))
        {
            Iterator<Lock> iterator = getLockManager().iterator();
            boolean f = false;
            boolean sameEditor = false;
            while(iterator.hasNext() && !f)
            {
                Lock l = iterator.next();
                if(l.compareTo(lock) == 0)
                {
                    f=true;
                    if(l.getEditor().getId().compareTo(editor.getId())==0)
                        sameEditor=true;
                }
            }
            if(sameEditor) {
                getLockManager().remove(lock);
                return true;
            }
            return false;
        }
        return true;
    }

    public WebCode getWebCodeReadOnly(Party party, WebAgent agent)
    {
        return webCodeRepository.findWebCodeForTeamAndWebAgent(party,agent);
    }

}
