import java.util.Objects;

public class Card {

    public enum Suit {
        SPADES, HEARTS, DIAMONDS, CLUBS
    }

    public enum Rank {
        TWO(2, "2"), THREE(3, "3"), FOUR(4, "4"), FIVE(5, "5"),
        SIX(6, "6"), SEVEN(7, "7"), EIGHT(8, "8"), NINE(9, "9"),
        TEN(10, "10"), JACK(11, "J"), QUEEN(12, "Q"), KING(13, "K"), ACE(14, "A");

        final int value;
        final String symbol;

        Rank(int v, String s) {
            value = v;
            symbol = s;
        }
    }

    public Suit suit;
    public Rank rank;

    public Card(Suit s, Rank r) {
        suit = s;
        rank = r;
    }

    public int getValue() {
        return rank.value;
    }

    public Suit getSuit() {
        return suit;
    }

    @Override
    public String toString() {
        String s = "S";
        if (suit == Suit.HEARTS)
            s = "H";
        if (suit == Suit.DIAMONDS)
            s = "D";
        if (suit == Suit.CLUBS)
            s = "C";
        return rank.symbol + "[" + s + "]";
    }
}