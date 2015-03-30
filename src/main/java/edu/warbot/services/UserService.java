package edu.warbot.services;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;

import javax.annotation.PostConstruct;

import edu.warbot.repository.AccountRepository;
import edu.warbot.models.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {
	
	@Autowired
	private AccountRepository accountRepository;

	@PostConstruct
	public void initialize()
	{
		accountRepository.save(new Account("user", "demo","toto","toto","demoUser",true,false,new Date(),new Date(),new Date(),"ROLE_USER",new HashSet<>()));
		accountRepository.save(new Account("admin", "admin","toto","toto","demoAdmin",true,true,new Date(),new Date(),new Date(),"ROLE_ADMIN",new HashSet<>()));
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Account account = accountRepository.findByEmail(username);
		if(account == null) {
			throw new UsernameNotFoundException("user not found");
		}
		return createUser(account);
	}

	public void signin(Account account) {
		SecurityContextHolder.getContext().setAuthentication(authenticate(account));
	}
	
	private Authentication authenticate(Account account) {
		return new UsernamePasswordAuthenticationToken(createUser(account), null, Collections.singleton(createAuthority(account)));		
	}
	
	private User createUser(Account account) {
		return new User(account.getEmail(), account.getPassword(), Collections.singleton(createAuthority(account)));
	}

	private GrantedAuthority createAuthority(Account account) {
		return new SimpleGrantedAuthority(account.getRole());
	}

}
