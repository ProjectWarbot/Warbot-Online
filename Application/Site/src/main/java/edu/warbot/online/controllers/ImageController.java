package edu.warbot.online.controllers;

import edu.warbot.online.models.Image;
import edu.warbot.online.services.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by beugnon on 12/06/15.
 * <p/>
 * Ce contrôleur permet de charger des images provenant d'une base de données
 *
 * @author beugnon
 */
@Controller
@RequestMapping("/images_center")
public class ImageController {


    @Autowired
    private ImageService itemService;

    @RequestMapping(value = "/imageDisplay", method = RequestMethod.GET)
    public void showImage(@RequestParam("id") Long itemId, HttpServletResponse response, HttpServletRequest request)
            throws ServletException, IOException {

        Image item = itemService.get(itemId);
        response.setContentType("image/jpeg, image/jpg, image/png, image/gif");
        response.getOutputStream().write(item.getItemImage());
        response.getOutputStream().close();
    }
}