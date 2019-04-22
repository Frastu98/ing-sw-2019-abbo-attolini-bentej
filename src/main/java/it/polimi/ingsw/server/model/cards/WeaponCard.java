package it.polimi.ingsw.server.model.cards;

import it.polimi.ingsw.server.controller.effects.EffectInterface;
import it.polimi.ingsw.server.model.AmmoCube;
import it.polimi.ingsw.server.model.Damageable;
import it.polimi.ingsw.server.model.board.GameBoard;
import it.polimi.ingsw.server.model.player.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * This represents a weapon card, with a cost and sequences of effects.
 * Objects of this class are instantiated by a
 * {@link it.polimi.ingsw.server.persistency.WeaponLoader}.
 *
 * @author Abbo Giulio A.
 * @see it.polimi.ingsw.server.persistency.WeaponLoader
 * @see AmmoCube
 * @see it.polimi.ingsw.server.controller.effects.EffectInterface
 */
public class WeaponCard extends AbstractCard {
    /**
     * The cubes that must be payed to reload this weapon.
     */
    private AmmoCube[] cost;

    /**
     * All the possible sequences of effects for this weapon.
     */
    private String[][] effectIdSequences;

    /**
     * Whether the effects can be used only in the specified order.
     * <p>
     * If true, then the possible sequences are as set in
     * {@linkplain #effectIdSequences}; if false then only the starting
     * elements are taken into account and the sequences consist in all the
     * possible combinations.
     */
    private boolean onlySpecifiedOrder;

    /**
     * Constructor used for testing, effects are initialized to [["Test"]].
     *
     * @param id                 the id to locate the resources for this object
     * @param cost               the cubes that must be payed to reload this
     *                           weapon
     * @param effectSequences    all the possible sequences of effects for this
     * @param onlySpecifiedOrder if true the effects can be used only in the
     *                           specified order; else only the first
     *                           elements of {@code effectSequences} are
     *                           considered, and can be used in every
     *                           combination possible.
     */
    public WeaponCard(String id, List<AmmoCube> cost,
                      String[][] effectSequences, boolean onlySpecifiedOrder) {
        super(id);
        this.cost = cost.toArray(new AmmoCube[0]);
        this.onlySpecifiedOrder = onlySpecifiedOrder;
        effectIdSequences = effectSequences;
    }

    /**
     * Returns a list of all the possible sequences of effects that this card
     * allows.
     * <p>
     * The returned list will contain all the possible starting points for
     * each of the effect sequences, and these elements contain a link to the
     * next element of the sequence.
     * For example, if the possible sequences are ABC, BBB, ABA, then the lis
     * will contain A, B, A (this one with different links from the first A).
     *
     * @return a list of all the possible sequences of effect allowed
     */
    public List<EffectInterface> getPossibleSequences() {
        List<EffectInterface> list = new ArrayList<>();

        /*If the order is not fixed, this calculates all the possible
        permutations*/
        if (!onlySpecifiedOrder) {
            /*Preparing the elements*/
            List<String> elements = new ArrayList<>();
            for (String[] s : effectIdSequences)
                elements.add(s[0]);

            /*Putting the permutations in the variable*/
            List<String[]> perm = new ArrayList<>();
            permutations(elements, 0, perm);

            /*Replacing the sequence with the one with permutations*/
            effectIdSequences = perm.toArray(new String[0][0]);
        }

        /*Decorating the effects*/
        //FIXME after effects are implemented
        for (String[] effectIdSequence : effectIdSequences) {
            MockEffect last = null;
            for (int j = effectIdSequence.length - 1; j >= 0; j--) {
                last = new MockEffect(effectIdSequence[j]).setNext(last);
            }
            list.add(last);
        }
        return list;
    }

    /**
     * Returns the cubes that must be payed to reload this weapon.
     *
     * @return the cubes that must be payed to reload this weapon
     */
    public List<AmmoCube> getCost() {
        return new ArrayList<>(Arrays.asList(cost));
    }

    /**
     * Returns all the different permutations of the provided elements.
     *
     * @param elements  the elements of the sequence
     * @param pos       the first element to work on
     * @param collector where all the permutations will be stored
     */
    private void permutations(List<String> elements, int pos,
                              List<String[]> collector) {
        /*If there are no more elements*/
        if (pos == elements.size()) {

            /*Checking whether the same sequence already exists*/
            for (String[] strings : collector) {
                boolean different = false;
                for (int i = 0; i < strings.length; i++) {
                    if (!strings[i].equals(elements.get(i)))
                        different = true;
                }
                if (!different)
                    return;
            }

            /*Adding the sequence only if it does not already exist*/
            collector.add(elements.toArray(new String[0]));
        }

        /*Iterating on the remaining elements*/
        for (int i = pos; i < elements.size(); i++) {

            /*Fixating an element at the beginning*/
            List<String> left = new ArrayList<>(elements);
            String removed = left.get(i);
            left.remove(i);
            left.add(pos, removed);

            /*Recursive call on those left*/
            permutations(left, pos + 1, collector);
        }
    }

    //TODO delete when effects will be implemented
    private class MockEffect implements EffectInterface {
        private String name;
        private WeaponCard.MockEffect next;

        public MockEffect(String name) {
            this.name = name;
        }

        @Override
        public void runEffect(Player subjectPlayer, GameBoard board, List<Damageable> alredyTargeted) {
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public EffectInterface getDecorated() {
            return next;
        }

        public MockEffect setNext(WeaponCard.MockEffect next) {
            this.next = next;
            return this;
        }

        @Override
        public Iterator<EffectInterface> iterator() {
            return null;
        }
    }
}