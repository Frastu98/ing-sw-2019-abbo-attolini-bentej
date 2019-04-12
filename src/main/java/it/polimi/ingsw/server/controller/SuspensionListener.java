package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.server.model.player.Player;

/**
 * 
 */
public interface SuspensionListener {

    /**
     * @param player
     */
    public void playerSuspension(Player player);

    /**
     * @param player
     */
    public void playerResumption(Player player);

}