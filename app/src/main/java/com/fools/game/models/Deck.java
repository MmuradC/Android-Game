package com.fools.game.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
    private List<Card> drawPile;
    private List<Card> hand;
    private List<Card> discardPile;

    public Deck(List<Card> startingCards) {
        drawPile = new ArrayList<>(startingCards);
        hand = new ArrayList<>();
        discardPile = new ArrayList<>();
        Collections.shuffle(drawPile);
    }

    public void draw(int amount) {
        for (int i = 0; i < amount; i++) {
            if (drawPile.isEmpty()) {
                if (discardPile.isEmpty()) {
                    break; // No cards left to draw
                }
                drawPile.addAll(discardPile);
                discardPile.clear();
                Collections.shuffle(drawPile);
            }
            hand.add(drawPile.remove(drawPile.size() - 1));
        }
    }

    public void addCardToDeck(Card card) {
        // Add to discard pile so it cycles in on the next shuffle
        discardPile.add(card);
    }

    public void discardHand() {
        discardPile.addAll(hand);
        hand.clear();
    }

    public void discardCard(Card card) {
        if (hand.remove(card)) {
            discardPile.add(card);
        }
    }

    public List<Card> getHand() {
        return new ArrayList<>(hand);
    }

    public int getDrawPileSize() { return drawPile.size(); }
    public int getDiscardPileSize() { return discardPile.size(); }
}
