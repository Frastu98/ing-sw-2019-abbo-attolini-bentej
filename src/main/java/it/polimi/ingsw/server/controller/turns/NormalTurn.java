package it.polimi.ingsw.server.controller.turns;

import it.polimi.ingsw.server.controller.effects.*;
import it.polimi.ingsw.server.model.Damageable;
import it.polimi.ingsw.server.model.board.GameBoard;
import it.polimi.ingsw.server.model.cards.PowerupCard;
import it.polimi.ingsw.server.model.cards.WeaponCard;
import it.polimi.ingsw.server.model.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A normal turn is composed by two phases. In the first phase, the player chooses two from three possible actions. In the final phase, the player
 * chooses whether to reload.
 *
 * @author Fahed Ben Tej
 */
public class NormalTurn implements TurnInterface {
    /**
     * Current Player
     */
    private Player player;
    /**
     * Board used in the Game.
     */
    private GameBoard board;
    /**
     * Possible Actions
     */
    private List<Action> actions;
    /**
     * Already targeted targets
     */
    private List<Damageable> alreadyTargeted;

    /**
     * Creates a normal turn.
     */
    public NormalTurn(Player currentPlayer, GameBoard board){
        this.player = currentPlayer;
        this.board = board;
        this.alreadyTargeted = new ArrayList<>();
        this.actions = new ArrayList<>();
    }

    /**
     * A player takes two actions then he has the option to reload. Between each of these steps he is asked if he want to use a Powerup Card.
     * @param currentPlayer 
     * @param board GameBoard
     */
    public void startTurn(Player currentPlayer, GameBoard board) {
        //TODO Refractor
        this.player = currentPlayer;
        this.board = board;


        for (int i = 0; i < 2; i++)
            askAndRunAction();

        askAndReload();
    }

    /**
     * Asks the player if he wants to use a Powerup Card. Then it runs its effect.
     */
    private void askAndRunPowerup(){
        if (player.getAllPowerup().isEmpty())
            return;

        PowerupCard card = player.getToClient().choosePowerup(player.getAllPowerup());

        if (card != null)
            card.getEffect().runEffect(player, board, alreadyTargeted);
    }
    /**
     * Asks the player to choose from a list of Actions and runs that action.
     */
    private void askAndRunAction() {
        actions = new ArrayList<>();
        //TODO define the constructor of Action ?
        actions.add(new Action("Move"));
        actions.add(new Action("Grab"));
        actions.add(new Action("Shoot"));

        Action chosenAction = player.getToClient().chooseAction(actions);

        chosenAction.runEffect(player, board, alreadyTargeted);

    }

    /**
     * Asks the player to choose whether to reload. Then it takes care of reloading.
     */
    private void askAndReload() {
        WeaponCard cardToReload;

        List<WeaponCard> weaponCards = player.getAllWeapons().stream()
                .filter(x -> player.canAfford(x.getCost(), false))
                .collect(Collectors.toList());

        cardToReload = player.getToClient().chooseWeaponToReload(weaponCards);

        if (cardToReload != null)
            player.reload(cardToReload);
    }

}