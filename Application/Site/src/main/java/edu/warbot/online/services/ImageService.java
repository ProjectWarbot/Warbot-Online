package edu.warbot.online.services;

import edu.warbot.online.models.Image;

import java.io.IOException;

/**
 * Created by beugnon on 12/06/15.
 */
public interface ImageService {


    Image get(Long itemId) throws IOException;
}
