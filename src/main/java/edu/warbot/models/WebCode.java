package edu.warbot.models;

import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by BEUGNON on 30/03/2015.
 *
 * @author Sébastien Beugnon
 */
@Entity
@Table(name = "CODE")
public class WebCode extends AbstractPersistable<Long> {
}
