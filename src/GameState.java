package cardgame;

import java.util.ArrayList;
import java.util.List;

public class GameState {

    enum Turn  { PLAYER, COMPUTER }
    enum Phase { LEAD, RESPOND }

    List<Card> playerHand;
    List<Card> computerHand;

    Turn  currentTurn;
    Phase phase;

    Card playerPlayed;
    Card computerPlayed;

    int playerTricks   = 0;
    int computerTricks = 0;
    int round          = 1;

    List<String> log = new ArrayList<>();

    public GameState(List<Card> p, List<Card> c, Turn t) {
        playerHand   = new ArrayList<>(p);
        computerHand = new ArrayList<>(c);
        currentTurn  = t;
        phase        = Phase.LEAD;
    }


    void playerWins() {
        playerTricks++;
        currentTurn = Turn.PLAYER;
        reset();
        round++;
    }

    void computerWins() {
        computerTricks++;
        currentTurn = Turn.COMPUTER;
        reset();
        round++;
    }

    void playerPicksUp() {
        playerHand.add(playerPlayed);
        playerHand.add(computerPlayed);
        currentTurn = Turn.COMPUTER;
        reset();
        round++;
    }

    void computerPicksUp() {
        computerHand.add(playerPlayed);
        computerHand.add(computerPlayed);
        currentTurn = Turn.PLAYER;
        reset();
        round++;
    }

    void reset() {
        playerPlayed   = null;
        computerPlayed = null;
        phase          = Phase.LEAD;
    }

 

    boolean isGameOver() {
        return playerHand.isEmpty() || computerHand.isEmpty();
    }

    Turn getWinner() {
        if (playerHand.isEmpty())   return Turn.PLAYER;
        if (computerHand.isEmpty()) return Turn.COMPUTER;
        return null;
    }
}