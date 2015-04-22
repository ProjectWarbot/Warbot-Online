package edu.warbot.form;

import edu.warbot.models.Party;
import org.hibernate.validator.constraints.*;

import edu.warbot.models.Account;

import java.util.Date;
import java.util.HashSet;

public class SignupForm {

	private static final String NOT_BLANK_MESSAGE = "{notBlank.message}";
	private static final String EMAIL_MESSAGE = "{email.message}";

    @NotBlank(message = SignupForm.NOT_BLANK_MESSAGE)
	@Email(message = SignupForm.EMAIL_MESSAGE)
	private String email;

    @NotBlank(message = SignupForm.NOT_BLANK_MESSAGE)
	private String password;

    @NotBlank(message = SignupForm.NOT_BLANK_MESSAGE)
    private String lastName;

    @NotBlank(message = SignupForm.NOT_BLANK_MESSAGE)
    private String firstName;

    @NotBlank(message = SignupForm.NOT_BLANK_MESSAGE)
    private String screenName;


    public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public Account createAccount() {
        return new Account(getEmail(), getPassword(),firstName,lastName,screenName,
                false,false,
                new Date(),null,new Date(),"ROLE_USER",new HashSet<Party>());
	}
}
