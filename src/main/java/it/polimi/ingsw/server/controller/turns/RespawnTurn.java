package it.polimi.ingsw.server.controller.turns;

import it.polimi.ingsw.communication.ToClientException;
import it.polimi.ingsw.server.model.board.GameBoard;
import it.polimi.ingsw.server.model.cards.PowerupCard;
import it.polimi.ingsw.server.model.player.Player;

/**
 * Once a player is dead, at the end of the active player, we have to:
 * (1) score its killboard
 * (2) remove every damage from player board.
 * (3) make him draw a powerup card. He chooses the powerup which determines his next spawn.
 * This class takes care only of point (3).
 *
 * @author Fahed Ben Tej
 */
public class RespawnTurn implements TurnInterface {


    /**
     * Makes the player draw a powerup card. He chooses the powerup which determines his next spawn.
     * @param currentPlayer player respawning.
     * @param board GameBoard used in the board.
     *
     * @return -1 if Final Frenzy is triggered, else 0
     */
    public int startTurn(Player currentPlayer, GameBoard board) {

        // player draws card
        currentPlayer.addPowerup(board.getPowerupCard());
        // we make the player choose a powerup
        PowerupCard cardChosen = null;
        try {
            cardChosen =
                    currentPlayer.getToClient().chooseSpawn(currentPlayer.getAllPowerup());
        } catch (ToClientException e) {
            //TODO Handle if the user is disconnected
        }
        // discard that card
        currentPlayer.removePowerup(cardChosen);
        // set the player position in the spawn determined by that square
        currentPlayer.setPosition(board.findSpawn(cardChosen.getCube()));

        return 0;
    }

}