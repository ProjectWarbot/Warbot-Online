package edu.warbot.online.services.impl;

import edu.warbot.online.models.Image;
import edu.warbot.online.repository.ImageRepository;
import edu.warbot.online.services.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by beugnon on 12/06/15.
 */
@Service
@Transactional
public class ImageServiceImpl implements ImageService {

    @Autowired
    private ImageRepository imageRepository;

    @Override
    public Image get(Long itemId) {
        return imageRepository.findOne(itemId);
    }
}
