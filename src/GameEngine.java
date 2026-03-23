public class GameEngine {

    private GameState state;

    public GameEngine(GameState s) {
        state = s;
    }

    boolean isLegalPlay(Card card, Card lead) {
        if (!playerHasSuit(lead.getSuit())) return true;
        return card.getSuit() == lead.getSuit();
    }

    boolean playerHasSuit(Card.Suit suit) {
        for (Card c : state.playerHand)
            if (c.getSuit() == suit) return true;
        return false;
    }

    String resolveTrick(GameState.Turn leader) {
        Card p = state.playerPlayed;
        Card c = state.computerPlayed;

        if (p.getSuit() == c.getSuit()) {
            int cmp = p.getValue() - c.getValue();
            if (cmp > 0) { state.playerWins();   return p + " beats " + c + " -- You win"; }
            if (cmp < 0) { state.computerWins(); return c + " beats " + p + " -- Computer wins"; }
            state.reset(); state.round++;
            return "Tie -- both discarded";
        }

        if (leader == GameState.Turn.PLAYER) {
            state.computerPicksUp();
            return "Computer had no " + p.getSuit() + "  picks up both";
        } else {
            state.playerPicksUp();
            return "You had no " + c.getSuit() + "  you pick up both";
        }
    }
}