package cardgame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {

    private List<Card> cards;

    private Deck(List<Card> list) {
        cards = list;
    }

    public static Deck build() {
        List<Card> list = new ArrayList<>();

        for (Card.Suit s : Card.Suit.values()) {
            for (Card.Rank r : Card.Rank.values()) {
                list.add(new Card(s, r));
            }
        }

        return new Deck(list);
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public List<Card> deal(int k) {
        if (k > cards.size()) {
            throw new RuntimeException("Not enough cards");
        }

        List<Card> hand = new ArrayList<>(cards.subList(0, k));
        cards.subList(0, k).clear();

        return hand;
    }

    public int remaining() {
        return cards.size();
    }

    public boolean isEmpty() {
        return cards.isEmpty();
    }
}