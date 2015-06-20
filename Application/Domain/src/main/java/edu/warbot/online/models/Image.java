package edu.warbot.online.models;

import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 * Created by beugnon on 12/06/15.
 */
@Entity
@Table(name = "IMAGE")
public class Image extends AbstractPersistable<Long> {


    @Lob
    @Column
    private byte[] itemImage;


    public byte[] getItemImage() {
        return itemImage;
    }

    public void setItemImage(byte[] itemImage) {
        this.itemImage = itemImage;
    }
}