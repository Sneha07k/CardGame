package cardgame;

import java.util.List;

public class GameEngine {

    private GameState state;
    private AIEngine ai;

    public GameEngine(GameState s, AIEngine a) {
        state = s;
        ai = a;
    }

    public boolean isLegalPlay(Card card, Card lead) {
        boolean hasSuit = state.getPlayerHand().stream()
                .anyMatch(c -> c.getSuit() == lead.getSuit());

        if (!hasSuit) return true;
        return card.getSuit() == lead.getSuit();
    }

    public boolean playerHasSuit(Card.Suit suit) {
        return state.getPlayerHand().stream()
                .anyMatch(c -> c.getSuit() == suit);
    }

    public Card computerLead() {
        Card c = ai.chooseLead(state.getComputerHand());

        state.removeFromComputerHand(c);
        state.setComputerPlayed(c);
        ai.recordPlayed(c);

        state.setPhase(GameState.Phase.RESPOND);
        return c;
    }

    public Card computerRespond(Card lead) {
        boolean hasSuit = state.getComputerHand().stream()
                .anyMatch(c -> c.getSuit() == lead.getSuit());

        if (!hasSuit) {
            ai.recordPlayerVoid(lead.getSuit());
        }

        Card c = ai.chooseResponse(state.getComputerHand(), lead);

        state.removeFromComputerHand(c);
        state.setComputerPlayed(c);
        ai.recordPlayed(c);

        return c;
    }

    public void playerLead(Card c) {
        state.removeFromPlayerHand(c);
        state.setPlayerPlayed(c);
        ai.recordPlayed(c);

        state.setPhase(GameState.Phase.RESPOND);
    }

    public boolean playerRespond(Card c, Card lead) {
        if (!isLegalPlay(c, lead)) return false;

        if (c.getSuit() != lead.getSuit()) {
            ai.recordPlayerVoid(lead.getSuit());
        }

        state.removeFromPlayerHand(c);
        state.setPlayerPlayed(c);
        ai.recordPlayed(c);

        return true;
    }

    public TrickResult resolveTrick(GameState.Turn lead) {
        Card p = state.getPlayerPlayed();
        Card c = state.getComputerPlayed();

        if (p.getSuit() == c.getSuit()) {
            int cmp = Integer.compare(p.getValue(), c.getValue());

            if (cmp > 0) {
                state.playerWinsTrick();
                return new TrickResult(TrickResult.Outcome.PLAYER_WINS,
                        p + " beats " + c + " - You win");

            } else if (cmp < 0) {
                state.computerWinsTrick();
                return new TrickResult(TrickResult.Outcome.COMPUTER_WINS,
                        c + " beats " + p + " - Computer wins");

            } else {
                state.setPhase(GameState.Phase.LEAD);
                state.setPlayerPlayed(null);
                state.setComputerPlayed(null);

                return new TrickResult(TrickResult.Outcome.TIE,
                        "Tie: " + p + " vs " + c);
            }

        } else {
            if (lead == GameState.Turn.PLAYER) {
                state.computerPicksUp();
                return new TrickResult(TrickResult.Outcome.COMPUTER_PICKS_UP,
                        "Computer picks both cards");

            } else {
                state.playerPicksUp();
                return new TrickResult(TrickResult.Outcome.PLAYER_PICKS_UP,
                        "You pick both cards");
            }
        }
    }

    public static class TrickResult {

        public enum Outcome {
            PLAYER_WINS, COMPUTER_WINS, TIE,
            PLAYER_PICKS_UP, COMPUTER_PICKS_UP
        }

        public Outcome outcome;
        public String message;

        TrickResult(Outcome o, String m) {
            outcome = o;
            message = m;
        }
    }
}