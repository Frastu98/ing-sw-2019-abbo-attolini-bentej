package it.polimi.ingsw.server.controller.effects;

import it.polimi.ingsw.communication.ToClientException;
import it.polimi.ingsw.server.model.Damageable;
import it.polimi.ingsw.server.model.board.GameBoard;
import it.polimi.ingsw.server.model.board.SpawnSquare;
import it.polimi.ingsw.server.model.board.Square;
import it.polimi.ingsw.server.model.cards.AmmoCard;
import it.polimi.ingsw.server.model.cards.PowerupCard;
import it.polimi.ingsw.server.model.cards.WeaponCard;
import it.polimi.ingsw.server.model.player.Player;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A player can grab either an {@link AmmoCard} or a {@link WeaponCard}.
 * The items depicted in the ammo card are added to the player and it is
 * discarded; if the player is in a {@link Square} with a
 * {@link it.polimi.ingsw.server.model.board.WeaponMarket} then he can choose
 * a weapon to buy, if necessary a weapon to put down and a powerup to pay
 * the cost.
 *
 * @author Fahed Ben Tej
 * @author Abbo Giulio A.
 * @see AmmoCard
 * @see WeaponCard
 */
public class Grab implements EffectInterface {

    /**
     * Runs the Grab effect for the specified player.
     *
     * @param subjectPlayer  the player who calls this effect
     * @param allTargets     not used, can be null
     * @param board          the game board
     * @param allTargeted    not used, can be null
     * @param damageTargeted not used, can be null
     */
    public void runEffect(Player subjectPlayer, List<Damageable> allTargets, GameBoard board,
                          List<Damageable> allTargeted, List<Damageable> damageTargeted) {
        Square position = subjectPlayer.getPosition();

        /*We can grab an AmmoCard if there's an AmmoTile*/
        AmmoCard card = position.getAmmoCard();
        if (card != null) {
            subjectPlayer.addAmmo(card.getCubes());

            /*If the tile depicts a powerup card, draw one*/
            if (card.hasPowerup() && subjectPlayer.getAllPowerup().size() >= 3)
                subjectPlayer.addPowerup(board.getPowerupCard());

            /*Recycling the ammo card*/
            board.putAmmoCard(card);
            return;
        }

        /*We can grab a Weapon if there's a Market in our position*/
        if (position.getRoom().getSpawnSquare().equals(position)) {

            /*Getting the weapons in market*/
            SpawnSquare spawnSquare = position.getRoom().getSpawnSquare();
            List<WeaponCard> weaponAvailable = spawnSquare.getMarket().getCards();

            /*Adding the weapons that the player can buy to weaponsAffordable*/
            List<WeaponCard> weaponsAffordable = weaponAvailable.stream()
                    .filter(weaponCard -> subjectPlayer.canAfford(weaponCard.getCost(), true) ||
                            !subjectPlayer.canAffordWithPowerups(weaponCard.getCost(), true).isEmpty())
                    .collect(Collectors.toList());

            /*If there are no weapons it is player's mistake and he grabs nothing*/
            if (weaponsAffordable.isEmpty())
                return;

            /*Asking the player which weapon he wants to buy*/
            WeaponCard weaponToBuy;
            WeaponCard weaponToDiscard = null;
            PowerupCard powerupToPay = null;
            try {
                weaponToBuy = subjectPlayer.getToClient().chooseWeaponToBuy(weaponsAffordable);

                /*If the player has 3 weapons, he has to discard one.*/
                if (subjectPlayer.getNumOfWeapons() >= 3)
                    weaponToDiscard = subjectPlayer.getToClient().chooseWeaponToDiscard(subjectPlayer.getAllWeapons());

                /*If necessary, choosing a powerup to pay*/
                if (!subjectPlayer.canAfford(weaponToBuy.getCost(), true))
                    powerupToPay = subjectPlayer.getToClient().choosePowerupForPaying(
                            subjectPlayer.canAffordWithPowerups(weaponToBuy.getCost(), true));
            } catch (ToClientException e) {

                /*If the user is disconnected he grabs nothing*/
                return;
            }

            /*The player buys the weapon with the chosen powerup*/
            if (weaponToDiscard != null)
                subjectPlayer.discard(weaponToDiscard);
            spawnSquare.pickWeapon(weaponToBuy);
            subjectPlayer.buy(weaponToBuy, powerupToPay);
            if (weaponToDiscard != null)
                spawnSquare.getMarket().addCard(weaponToDiscard);
        }
    }

    /**
     * Returns the name of this Effect: "Grab"
     *
     * @return "Grab"
     */
    public String getName() {
        return "Grab";
    }

    /**
     * Has no decorated.
     *
     * @return null
     */
    public EffectInterface getDecorated() {
        return null;
    }

    /**
     * Not supported
     *
     * @throws UnsupportedOperationException if called
     */
    @Override
    public void addToChain(EffectInterface last) {
        throw new UnsupportedOperationException();
    }

    /**
     * Has no iterator
     *
     * @throws UnsupportedOperationException if called
     */
    @Override
    public Iterator<EffectInterface> iterator() {
        throw new UnsupportedOperationException();
    }
}