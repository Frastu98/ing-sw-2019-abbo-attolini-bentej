package it.polimi.ingsw.communication;

import com.google.gson.Gson;
import it.polimi.ingsw.server.controller.effects.Action;
import it.polimi.ingsw.server.controller.effects.EffectInterface;
import it.polimi.ingsw.server.model.Damageable;
import it.polimi.ingsw.server.model.board.Square;
import it.polimi.ingsw.server.model.cards.PowerupCard;
import it.polimi.ingsw.server.model.cards.WeaponCard;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/**
 * This class handles the communication through socket, server side.
 * This also ensures that only one communication with the client happens at one
 * time.
 *
 * @author Abbo Giulio A.
 * @see Type
 */
public class SocketToClient implements ToClientInterface {
    /**
     * The socket through which communicate.
     */
    private final Socket socket;

    /**
     * This sets the socket.
     *
     * @param socket the socket through which communicate
     */
    SocketToClient(Socket socket) throws ToClientException {
        this.socket = socket;
        sendMessage(Type.GREET);
    }

    /**
     * Handles the communication for a list of lists.
     * This keeps asking the client if the answer is not valid.
     *
     * @param command the message to be sent
     * @param options the option to choose from
     * @return the index of the selected option
     * @throws ToClientException if there are problems with the socket
     */
    private int sendOptions(Type command, List<List<String>> options) throws ToClientException {

        /*Sending the data an retrieving the answer*/
        int choice;
        synchronized (socket) {

            /*Try-with-resources will call close() automatically*/
            try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                 PrintWriter out = new PrintWriter(socket.getOutputStream(),true)
            ) {
                Message message = new Message(command, options);
                out.println(new Gson().toJson(message));

                choice = new Gson().fromJson(in.readLine(), Message.class).getAnswer();
            } catch (IOException e) {
                throw new ToClientException("Socket exception", e);
            }
        }

        /*Checking if the answer is valid.*/
        if (choice >= 0 && choice < options.size())
            return choice;

        /*The answer is not valid: sending an error and asking again*/
        sendMessage(Type.ERROR);
        return sendOptions(command, options);
    }

    /**
     * Handles the communication for a question with no options provided.
     * The answer can not be null or an empty string.
     * This keeps asking the client if the answer is not valid.
     *
     * @param command the message to be sent
     * @return the answer
     * @throws ToClientException if there are problems with the socket
     */
    private String sendNotification(Type command) throws ToClientException {

        /*Sending the data an retrieving the answer*/
        String choice;
        synchronized (socket) {

            /*Try-with-resources will call close() automatically*/
            try {
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(),
                        true);
                out.println(command);
                choice = in.readLine();
            } catch (IOException e) {
                throw new ToClientException("Socket exception", e);
            }
        }

        /*Checking that the answer is not null or empty.*/
        if (choice != null && !"".equals(choice))
            return choice;

        /*The answer is not valid: sending an error and asking again*/
        sendMessage(Type.ERROR);
        return sendNotification(command);
    }

    /**
     * Sends a command that does not need an answer.
     *
     * @param command the message
     * @throws ToClientException if there are problems with the socket
     */
    private void sendMessage(Type command) throws ToClientException {
        synchronized (socket) {
            try {
                PrintWriter out = new PrintWriter(socket.getOutputStream(),
                        true);
                out.println(command);
            } catch (IOException e) {
                throw new ToClientException("Socket exception", e);
            }
        }
    }

    /**
     * Handles the interaction using {@linkplain EffectInterface}.
     * Each choice could be a chain of effects.
     *
     * @param command the message to be sent
     * @param options the options to choose from
     * @return the selected effect
     * @throws ToClientException if there are problems with the socket
     */
    private EffectInterface askEffect(Type command,
                                      List<EffectInterface> options)
            throws ToClientException {

        /*Creating a list of names for each chain of effects*/
        List<List<String>> names = new ArrayList<>();
        for (EffectInterface effect : options) {
            List<String> chain = new ArrayList<>();
            effect.forEach(el -> chain.add(el.getName()));
            names.add(chain);
        }

        /*Asking and returning the right element*/
        int choice = sendOptions(command, names);
        return options.get(choice);
    }

    /**
     * Handles the interaction using {@linkplain PowerupCard}.
     *
     * @param command the message to be sent
     * @param options the options to choose from
     * @return the selected powerup
     * @throws ToClientException if there are problems with the socket
     */
    private PowerupCard askPowerup(Type command,
                                   List<PowerupCard> options)
            throws ToClientException {
        return options.get(sendOptions(command, options.stream()
                .map(PowerupCard::getId).map(Arrays::
                        asList).collect(Collectors.toList())));
    }

    /**
     * Handles the interaction using {@linkplain Square}.
     *
     * @param command the message to be sent
     * @param options the options to choose from
     * @return the selected square
     * @throws ToClientException if there are problems with the socket
     */
    private Square askSquare(Type command, List<Square> options)
            throws ToClientException {
        return options.get(sendOptions(command, options.stream()
                .map(Square::getID).map(Arrays::
                        asList).collect(Collectors.toList())));
    }

    /**
     * Handles the interaction using {@linkplain WeaponCard}.
     *
     * @param command the message to be sent
     * @param options the options to choose from
     * @return the selected weapon
     * @throws ToClientException if there are problems with the socket
     */
    private WeaponCard askWeapon(Type command,
                                 List<WeaponCard> options)
            throws ToClientException {
        return options.get(sendOptions(command, options.stream()
                .map(WeaponCard::getId).map(Arrays::
                        asList).collect(Collectors.toList())));
    }

    /**
     * Handles the interaction using {@linkplain Action}.
     *
     * @param command the message to be sent
     * @param options the options to choose from
     * @return the selected action
     * @throws ToClientException if there are problems with the socket
     */
    private Action askAction(Type command, List<Action> options)
            throws ToClientException {
        return options.get(sendOptions(command, options.stream()
                .map(Action::getName).map(Arrays::
                        asList).collect(Collectors.toList())));
    }

    /**
     * Handles the interaction using lists of {@linkplain Damageable}.
     *
     * @param command the message to be sent
     * @param options the options to choose from
     * @return the selected damageable list
     * @throws ToClientException if there are problems with the socket
     */
    private List<Damageable> askDamageableList(Type command,
                                               List<List<Damageable>> options)
            throws ToClientException {
        int choice = sendOptions(command, options.stream()
                .map(o -> o.stream()
                        .map(Damageable::getName).collect(Collectors.toList()))
                .collect(Collectors.toList()));
        return options.get(choice);
    }

    /**
     * {@inheritDoc}
     * This stops the execution until the client makes the right choice.
     *
     * @throws ToClientException if there are problems whit the socket
     */
    @Override
    public EffectInterface chooseEffectsSequence(List<EffectInterface> options)
            throws ToClientException {
        return askEffect(Type.EFFECTS_SEQUENCE, options);
    }

    /**
     * {@inheritDoc}
     * This stops the execution until the client makes the right choice.
     *
     * @throws ToClientException if there are problems whit the socket
     */
    @Override
    public PowerupCard chooseSpawn(List<PowerupCard> options)
            throws ToClientException {
        return askPowerup(Type.SPAWN, options);
    }

    /**
     * {@inheritDoc}
     * This stops the execution until the client makes the right choice.
     *
     * @throws ToClientException if there are problems whit the socket
     */
    @Override
    public PowerupCard choosePowerup(List<PowerupCard> options)
            throws ToClientException {
        return askPowerup(Type.POWERUP, options);
    }

    /**
     * {@inheritDoc}
     * This stops the execution until the client makes the right choice.
     *
     * @throws ToClientException if there are problems whit the socket
     */
    @Override
    public Square chooseDestination(List<Square> options)
            throws ToClientException {
        return askSquare(Type.DESTINATION, options);
    }

    /**
     * {@inheritDoc}
     * This stops the execution until the client makes the right choice.
     *
     * @throws ToClientException if there are problems whit the socket
     */
    @Override
    public WeaponCard chooseWeaponCard(List<WeaponCard> options)
            throws ToClientException {
        return askWeapon(Type.WEAPON, options);
    }

    /**
     * {@inheritDoc}
     * This stops the execution until the client makes the right choice.
     *
     * @throws ToClientException if there are problems whit the socket
     */
    @Override
    public WeaponCard chooseWeaponToBuy(List<WeaponCard> options)
            throws ToClientException {
        return askWeapon(Type.WEAPON_TO_BUY, options);
    }

    /**
     * {@inheritDoc}
     * This stops the execution until the client makes the right choice.
     *
     * @throws ToClientException if there are problems whit the socket
     */
    @Override
    public WeaponCard chooseWeaponToDiscard(List<WeaponCard> options)
            throws ToClientException {
        return askWeapon(Type.WEAPON_TO_DISCARD, options);
    }

    /**
     * {@inheritDoc}
     * This stops the execution until the client makes the right choice.
     *
     * @throws ToClientException if there are problems whit the socket
     */
    @Override
    public WeaponCard chooseWeaponToReload(List<WeaponCard> options)
            throws ToClientException {
        return askWeapon(Type.WEAPON_TO_RELOAD, options);
    }

    /**
     * {@inheritDoc}
     * This stops the execution until the client makes the right choice.
     *
     * @throws ToClientException if there are problems whit the socket
     */
    @Override
    public Action chooseAction(List<Action> options)
            throws ToClientException {
        return askAction(Type.ACTION, options);
    }

    /**
     * {@inheritDoc}
     * This stops the execution until the client makes the right choice.
     *
     * @throws ToClientException if there are problems whit the socket
     */
    @Override
    public PowerupCard choosePowerupForPaying(List<PowerupCard> options)
            throws ToClientException {
        return askPowerup(Type.POWERUP_FOR_PAYING, options);
    }

    /**
     * {@inheritDoc}
     * This stops the execution until the client makes the right choice.
     *
     * @throws ToClientException if there are problems whit the socket
     */
    @Override
    public PowerupCard askUseTagback(List<PowerupCard> options)
            throws ToClientException {
        return askPowerup(Type.USE_TAGBACK, options);
    }

    /**
     * {@inheritDoc}
     * This stops the execution until the client makes the right choice.
     *
     * @throws ToClientException if there are problems whit the socket
     */
    @Override
    public List<Damageable> chooseTarget(List<List<Damageable>> options)
            throws ToClientException {
        return askDamageableList(Type.TARGET, options);
    }

    /**
     * {@inheritDoc}
     * This stops the execution until the client makes the right choice.
     *
     * @throws ToClientException if there are problems whit the socket
     */
    @Override
    public String chooseUserName() throws ToClientException {
        return sendNotification(Type.NICKNAME);
    }

    /**
     * {@inheritDoc}
     * This stops the execution until the connection is closed.
     *
     * @throws ToClientException if there are problems with the socket
     */
    @Override
    public void quit() throws ToClientException {
        sendNotification(Type.QUIT);
    }
}
