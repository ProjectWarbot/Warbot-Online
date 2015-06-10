package edu.warbot.online.form;

import org.hibernate.validator.constraints.NotBlank;


/**
 * Created by quent on 12/05/2015.
 */
public class ContactForm {
    private static final String NOT_BLANK_MESSAGE = "{notBlank.message}";

    @NotBlank(message = ContactForm.NOT_BLANK_MESSAGE)
    public String login;

    @NotBlank(message = ContactForm.NOT_BLANK_MESSAGE)
    public String email;

    @NotBlank(message = ContactForm.NOT_BLANK_MESSAGE)
    public String message;

    public String[] createMail() {
        String[] mail = new String[3];
        mail[0] = login;
        mail[1] = email;
        mail[2] = message;
        return mail;


    }

}

