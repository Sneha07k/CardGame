package cardgame;

public class Card {

    public enum Suit { SPADES, HEARTS, DIAMONDS, CLUBS }

    public enum Rank {
        TWO(2,"2"), THREE(3,"3"), FOUR(4,"4"), FIVE(5,"5"),
        SIX(6,"6"), SEVEN(7,"7"), EIGHT(8,"8"), NINE(9,"9"),
        TEN(10,"10"), JACK(11,"J"), QUEEN(12,"Q"), KING(13,"K"), ACE(14,"A");

        int value;
        String symbol;

        Rank(int v, String s) {
            value = v;
            symbol = s;
        }
    }

    private Suit suit;
    private Rank rank;

    public Card(Suit s, Rank r) {
        suit = s;
        rank = r;
    }

    public Suit getSuit() { return suit; }
    public Rank getRank() { return rank; }
    public int getValue() { return rank.value; }

    public String suitSymbol() {
        if (suit == Suit.SPADES) return "♠";
        if (suit == Suit.HEARTS) return "♥";
        if (suit == Suit.DIAMONDS) return "♦";
        return "♣";
    }

    public String toString() {
        return rank.symbol + suitSymbol();
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Card)) return false;

        Card c = (Card) o;
        return suit == c.suit && rank == c.rank;
    }

    public int hashCode() {
        return suit.ordinal() * 13 + rank.ordinal();
    }
}