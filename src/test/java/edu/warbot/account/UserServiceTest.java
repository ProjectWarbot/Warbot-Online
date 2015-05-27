package edu.warbot.account;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

import edu.warbot.models.Account;
import edu.warbot.models.Party;
import edu.warbot.repository.AccountRepository;
import edu.warbot.services.UserService;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
@Ignore

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

	@InjectMocks
	private UserService userService = new UserService();

	@Mock
	private AccountRepository accountRepositoryMock;

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void shouldInitializeWithTwoDemoUsers() {
		// act
		userService.initialize();
		// assert
		verify(accountRepositoryMock, times(2)).save(any(Account.class));
	}

	@Test
	public void shouldThrowExceptionWhenUserNotFound() {
		// arrange
		thrown.expect(UsernameNotFoundException.class);
		thrown.expectMessage("user not found");

		when(accountRepositoryMock.findByEmail("user@example.com")).thenReturn(null);
		// act
		userService.loadUserByUsername("user@example.com");
	}

	@Test
	public void shouldReturnUserDetails() {
		// arrange
		Account demoUser = new Account("user@example.com", "demo","firstName","lastName","screeName",
                true,false,new Date(),new Date(),new Date(),"ROLE_USER",new HashSet<Party>(),new HashSet<Party>());
		when(accountRepositoryMock.findByEmail("user@example.com")).thenReturn(demoUser);

		// act
		UserDetails userDetails = userService.loadUserByUsername("user@example.com");

		// assert
		assertThat(demoUser.getEmail()).isEqualTo(userDetails.getUsername());
		assertThat(demoUser.getPassword()).isEqualTo(userDetails.getPassword());
        assertThat(hasAuthority(userDetails, demoUser.getRole()));
	}

	@Test
	public void shouldCreateAccount()
	{
		//arrange
		Account demoUser = new Account("toto@gmail.com",
				"totoé","totoç","totoLn","toto"
				,true,false,new Date(),new Date(),
				new Date(),"ROLE_USER",new HashSet<Party>(),new HashSet<Party>());
		when(accountRepositoryMock.findByEmail("toto@gmail.com")).thenReturn(demoUser);

		// act
		Account account = accountRepositoryMock.findByEmail("toto@gmail.com");

		// assert
		assertThat(demoUser.getEmail()).isEqualTo(account.getEmail());
		assertThat(demoUser.getInscriptionDate()).isEqualTo(account.getInscriptionDate());

	}

	private boolean hasAuthority(UserDetails userDetails, String role) {
		Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
		for(GrantedAuthority authority : authorities) {
			if(authority.getAuthority().equals(role)) {
				return true;
			}
		}
		return false;
	}
}
