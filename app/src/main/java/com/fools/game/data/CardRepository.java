package com.fools.game.data;

import com.fools.game.models.Card;
import com.fools.game.models.Player;

import java.util.ArrayList;
import java.util.List;

public class CardRepository {

    public static List<Card> getStartingDeck() {
        List<Card> deck = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            deck.add(createStrike());
            deck.add(createDefend());
        }
        deck.add(createHeavyStrike());
        deck.add(createPotion());
        return deck;
    }

    public static Card createStrike() {
        return new Card("Strike", Card.Suit.WANDS, 1, "Deal 6 damage.", (s, t) -> t.takeDamage(6));
    }

    public static Card createDefend() {
        return new Card("Defend", Card.Suit.PENTACLES, 1, "Gain 5 block.", (s, t) -> s.addBlock(5));
    }

    public static Card createHeavyStrike() {
        return new Card("Heavy Strike", Card.Suit.WANDS, 2, "Deal 12 damage.", (s, t) -> t.takeDamage(12));
    }

    public static Card createPotion() {
        return new Card("Potion", Card.Suit.CUPS, 1, "Heal 4 HP.", (s, t) -> s.heal(4));
    }

    public static Card createQuickDraw() {
        return new Card("Quick Draw", Card.Suit.SWORDS, 0, "Draw 2 cards.", (s, t) -> {
            if (s instanceof Player) {
                ((Player) s).drawCards(2);
            }
        });
    }

    public static List<Card> getAllCards() {
        List<Card> allCards = new ArrayList<>();
        allCards.add(createStrike());
        allCards.add(createDefend());
        allCards.add(createHeavyStrike());
        allCards.add(createPotion());
        allCards.add(createQuickDraw());
        return allCards;
    }

    public static List<Card> getRandomRewards(int count) {
        List<Card> pool = getAllCards();
        java.util.Collections.shuffle(pool);
        return pool.subList(0, Math.min(count, pool.size()));
    }
}
