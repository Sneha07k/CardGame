import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {

    private List<Card> cards = new ArrayList<>();

    public Deck() {
        for (Card.Suit s : Card.Suit.values()) {
            for (Card.Rank r : Card.Rank.values()) {
                cards.add(new Card(s, r));
            }
        }
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public List<Card> deal(int n) {
        List<Card> hand = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            hand.add(cards.remove(0));
        }
        return hand;
    }
}