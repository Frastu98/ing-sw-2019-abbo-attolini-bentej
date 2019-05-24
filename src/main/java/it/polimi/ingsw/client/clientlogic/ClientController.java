package it.polimi.ingsw.client.clientlogic;

import it.polimi.ingsw.communication.MessageType;
import it.polimi.ingsw.communication.Notification;
import it.polimi.ingsw.communication.ProtocolMessage;
import it.polimi.ingsw.communication.Update;

import java.util.Arrays;

/**
 * Represents the Controller of the client. It elaborates the received Notifications, Updates and Commands .
 *
 * @author Fahed B. Tej
 */
public class ClientController {

    /**
     * Represents the state of the Game.
     */
    private MatchState matchState;

    /**
     * Handles notifications. Notifications are defined as events that don't change the state of the game
     *
     * @param notifications events that don't change the state of the game
     */
    public void handleNotifications(Notification[] notifications) {


    }

    /**
     * Handles updates. Updates are events that change the state of the game.
     *
     * @param updates events that change the state of the game.
     */
    public void handleUpdates(Update[] updates) {
        Arrays.asList(updates).forEach(matchState::handleUpdate);
    }

    /**
     * Handles a question. It takes care of asking the user of the answer and returns it to the caller.
     *
     * @param message a question
     * @return the index of the answer. In case of notifications and updates, it returns 0.
     */
    public int handleQuestion(MessageType message, String[][] options) {
        switch (message) {
            case NOTIFICATION:
            case UPDATE:
                throw new IllegalArgumentException("This method does not handle Notifications or Updates");
            case EFFECTS_SEQUENCE:
                chooseEffectSequence(message, options);
                break;
            case SPAWN:
                chooseSpawn(message, options);
                break;
            case POWERUP:
                chooseSpawn(message, options);
                break;
            case DESTINATION:
                chooseDestination(message, options);
                break;
            case WEAPON:
                chooseWeapon(message, options);
                break;
            case WEAPON_TO_BUY:
                chooseWeaponToBuy(message, options);
                break;
            case WEAPON_TO_DISCARD:
                chooseWeaponToDiscard(message, options);
                break;
            case WEAPON_TO_RELOAD:
                chooseWeaponToReload(message, options);
                break;
            case ACTION:
                chooseAction(message, options);
                break;
            case POWERUP_FOR_PAYING:
                choosePowerup(message, options);
                break;
            case USE_TAGBACK:
                chooseUseTagback(message, options);
                break;
            case TARGET:
                chooseTarget(message, options);
            default:
                // nothing


        }
        return 0;
    }

    /**
     * Chooses an Effect from the given options
     *
     * @param message type of question
     * @param options possible answers
     * @return
     */
    public int chooseEffectSequence(MessageType message, String[][] options) {
        return 0;
    }

    /**
     * Chooses an Spawn from the given options
     *
     * @param message type of question
     * @param options possible answers
     * @return the index of the answer
     */
    public int chooseSpawn(MessageType message, String[][] options) {
        return 0;
    }

    /**
     * Chooses a Powerup from the given options
     *
     * @param message type of question
     * @param options possible answers
     * @return the index of the answer
     */
    public int choosePowerup(MessageType message, String[][] options) {
        return 0;
    }

    /**
     * Chooses a Destination from the given options
     *
     * @param message type of question
     * @param options possible answers
     * @return the index of the answer
     */
    public int chooseDestination(MessageType message, String[][] options) {
        return 0;
    }

    /**
     * Chooses a Weapon from the given options
     *
     * @param message type of question
     * @param options possible answers
     * @return the index of the answer
     */
    public int chooseWeapon(MessageType message, String[][] options) {
        return 0;
    }

    /**
     * Chooses a Weapon to buy from the given options
     *
     * @param message type of question
     * @param options possible answers
     * @return the index of the answer
     */
    public int chooseWeaponToBuy(MessageType message, String[][] options) {

        return 0;
    }

    /**
     * Chooses a Weapon to discard from the given options
     *
     * @param message type of question
     * @param options possible answers
     * @return the index of the answer
     */
    public int chooseWeaponToDiscard(MessageType message, String[][] options) {

        return 0;
    }

    /**
     * Chooses a Weapon to reload from the given options
     *
     * @param message type of question
     * @param options possible answers
     * @return the index of the answer
     */
    public int chooseWeaponToReload(MessageType message, String[][] options) {

        return 0;
    }

    /**
     * Chooses an action from the given options
     *
     * @param message type of question
     * @param options possible answers
     * @return the index of the answer
     */
    public int chooseAction(MessageType message, String[][] options) {

        return 0;
    }

    /**
     * Chooses a Powerup for pating from the given options
     *
     * @param message type of question
     * @param options possible answers
     * @return the index of the answer
     */
    public int choosePowerupForPaying(MessageType message, String[][] options) {

        return 0;
    }

    /**
     * Chooses a tagback use from the given options
     *
     * @param message type of question
     * @param options possible answers
     * @return the index of the answer
     */
    public int chooseUseTagback(MessageType message, String[][] options) {

        return 0;
    }

    /**
     * Chooses a target from the given options
     *
     * @param message type of question
     * @param options possible answers
     * @return the index of the answer
     */
    public int chooseTarget(MessageType message, String[][] options) {

        return 0;
    }

}
