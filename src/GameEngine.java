

public class GameEngine {

    private GameState state;
    private AIEngine ai;

 
    public GameEngine(GameState s, AIEngine a) {
        state = s;
        ai = a;
    }

    public void playerLead(Card card) {
        state.playerHand.remove(card);
        state.playerPlayed = card;
        state.phase = GameState.Phase.RESPOND;
    }

    public Card computerRespond(Card lead) {
        Card chosen = ai.chooseResponse(state.computerHand, lead);
        state.computerHand.remove(chosen);
        state.computerPlayed = chosen;
        return chosen;
    }

    public Card computerLead() {
        Card chosen = ai.chooseLead(state.computerHand);
        state.computerHand.remove(chosen);
        state.computerPlayed = chosen;
        state.phase = GameState.Phase.RESPOND;
        return chosen;
    }

    public void playerRespond(Card card, Card lead) {
        state.playerHand.remove(card);
        state.playerPlayed = card;
    }

    public boolean isLegalPlay(Card card, Card lead) {
        if (!playerHasSuit(lead.getSuit()))
            return true;
        return card.getSuit() == lead.getSuit();
    }

    public boolean playerHasSuit(Card.Suit suit) {
        for (Card c : state.playerHand)
            if (c.getSuit() == suit)
                return true;
        return false;
    }

    public String resolveTrick(GameState.Turn leader) {
        Card p = state.playerPlayed;
        Card c = state.computerPlayed;

        String result;

        if (p.getSuit() == c.getSuit()) {
            int cmp = p.getValue() - c.getValue();
            if (cmp > 0) {
                state.playerWins();
                result = p + " beats " + c + " -- You win";
            } else if (cmp < 0) {
                state.computerWins();
                result = c + " beats " + p + " -- Computer wins";
            } else {
                state.reset();
                state.round++;
                result = "Tie -- both discarded";
            }
        } else if (leader == GameState.Turn.PLAYER) {
            state.computerPicksUp();
            result = "Computer had no " + p.getSuit() + "  picks up both";
        } else {
            state.playerPicksUp();
            result = "You had no " + c.getSuit() + "  you pick up both";
        }

        state.log.add(result);
        return result;
    }
}