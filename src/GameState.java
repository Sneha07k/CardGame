package cardgame;

import java.util.ArrayList;
import java.util.List;

public class GameState {

    public enum Turn { PLAYER, COMPUTER }
    public enum Phase { LEAD, RESPOND }

    private List<Card> playerHand;
    private List<Card> computerHand;

    private Turn currentTurn;
    private Phase phase;

    private Card playerPlayed;
    private Card computerPlayed;

    private int playerTricks = 0;
    private int computerTricks = 0;
    private int roundNumber = 1;

    private List<String> log = new ArrayList<>();

    public GameState(List<Card> p, List<Card> c, Turn t) {
        playerHand = new ArrayList<>(p);
        computerHand = new ArrayList<>(c);
        currentTurn = t;
        phase = Phase.LEAD;
    }

    public List<Card> getPlayerHand() { return playerHand; }
    public List<Card> getComputerHand() { return computerHand; }
    public Turn getCurrentTurn() { return currentTurn; }
    public Phase getPhase() { return phase; }
    public Card getPlayerPlayed() { return playerPlayed; }
    public Card getComputerPlayed() { return computerPlayed; }
    public int getPlayerTricks() { return playerTricks; }
    public int getComputerTricks() { return computerTricks; }
    public int getRoundNumber() { return roundNumber; }
    public List<String> getTrickLog() { return log; }

    public void setPhase(Phase p) { phase = p; }
    public void setCurrentTurn(Turn t) { currentTurn = t; }
    public void setPlayerPlayed(Card c) { playerPlayed = c; }
    public void setComputerPlayed(Card c) { computerPlayed = c; }

    public void playerWinsTrick() {
        playerTricks++;
        currentTurn = Turn.PLAYER;
        reset();
        roundNumber++;
    }

    public void computerWinsTrick() {
        computerTricks++;
        currentTurn = Turn.COMPUTER;
        reset();
        roundNumber++;
    }

    public void playerPicksUp() {
        playerHand.add(playerPlayed);
        playerHand.add(computerPlayed);
        currentTurn = Turn.COMPUTER;
        reset();
        roundNumber++;
    }

    public void computerPicksUp() {
        computerHand.add(playerPlayed);
        computerHand.add(computerPlayed);
        currentTurn = Turn.PLAYER;
        reset();
        roundNumber++;
    }

    private void reset() {
        playerPlayed = null;
        computerPlayed = null;
        phase = Phase.LEAD;
    }

    public void removeFromPlayerHand(Card c) { playerHand.remove(c); }
    public void removeFromComputerHand(Card c) { computerHand.remove(c); }

    public void addLog(String s) { log.add(s); }

    public boolean isGameOver() {
        return playerHand.isEmpty() || computerHand.isEmpty();
    }

    public Turn getWinner() {
        if (playerHand.isEmpty()) return Turn.PLAYER;
        if (computerHand.isEmpty()) return Turn.COMPUTER;
        return null;
    }
}