package edu.warbot.online.models;

/**
 * Created by beugnon on 11/06/15.
 *
 * @author beugnon
 */
public interface WebGame {

    /**
     * Permet de mettre la pause sur une partie
     */
    void pause();

    /**
     * Permet de mettre d'arrêter la partie
     */
    void stop();

    /**
     * Permet de reprendre la partie
     */
    void resume();

    /**
     * Permet de suivre en détail un agent
     *
     * @param id l'id d'un agent ou null pour annuler le suivi de cet agent
     */
    void preciseAgent(String id);

    /**
     * Permet de changer la valeur de délai entre les pas de la simulation
     *
     * @param delay Délai entre les pas
     */
    void changeDelayBetweenTick(float delay);
}
