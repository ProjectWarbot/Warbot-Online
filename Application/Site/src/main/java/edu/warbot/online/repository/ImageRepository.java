package edu.warbot.online.repository;

import edu.warbot.online.models.Image;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by beugnon on 12/06/15.
 */
public interface ImageRepository extends JpaRepository<Image, Long> {

}
