package edu.warbot.party;

import edu.warbot.models.Account;
import edu.warbot.models.Party;
import edu.warbot.repository.AccountRepository;
import edu.warbot.repository.PartyRepository;
import edu.warbot.scriptcore.interpreter.ScriptInterpreterLangage;
import edu.warbot.services.UserService;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.security.Principal;
import java.util.Date;
import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.notNull;
import static org.mockito.Mockito.*;

/**
 * Created by BEUGNON on 31/03/2015.
 *
 * @author Sebastien Beugnon
 */
@RunWith(MockitoJUnitRunner.class)
public class PartyRepositoryTest {


    @InjectMocks
    private UserService userService = new UserService();

    @Mock
    private AccountRepository accountRepositoryMock;

    @Mock
    private PartyRepository partyRepository;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void shouldInitializeWithTwoDemoUsers()
    {
        // act
        userService.initialize();
        // assert
        verify(accountRepositoryMock, times(2)).save(any(Account.class));
    }

    @Test
    public void shouldCreateParty()
    {
        // arrange
        Account demoUser = new Account("user@example.com", "demo","firstName","lastName","screeName",
                true,false,new Date(),new Date(),new Date(),"ROLE_USER",new HashSet<Party>());
        when(accountRepositoryMock.save(demoUser)).thenReturn(demoUser);
        when(accountRepositoryMock.findByEmail("user@example.com")).thenReturn(demoUser);
        Party party = new Party("toto", ScriptInterpreterLangage.PYTHON);
        party.setCreator(demoUser);
        party.getMembers().add(demoUser);
        when(partyRepository.findByName(party.getName())).thenReturn(party);
        //act
        partyRepository.save(party);
        Party partyFound = partyRepository.findByName("toto");


        // assert
        assertThat(party.getMembers()).isEqualTo(partyFound.getMembers());
        assertThat(party.getCreator()).isEqualTo(partyFound.getCreator());
    }

    @Test
    public void shouldThrowNotFoundException()
    {
        // arrange
        thrown.expect(Exception.class);
        Account demoUser = new Account("user@example.com", "demo",
                "firstName","lastName","screeName",
                true,false,new Date(),new Date(),
                new Date(),"ROLE_USER",new HashSet<Party>());
        when(accountRepositoryMock.save(demoUser)).thenReturn(demoUser);
        when(accountRepositoryMock.findByEmail(demoUser.getEmail())).thenReturn(demoUser);
        Party party = new Party("toto", ScriptInterpreterLangage.PYTHON);
        when(partyRepository.save(party)).thenReturn(party);
        when(partyRepository.findByName(party.getName())).thenReturn(null);
        when(partyRepository.findByName("toto")).thenThrow(NullPointerException.class);

        //act
        partyRepository.findByName("toto");

    }



}
