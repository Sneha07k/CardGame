package cardgame;

import java.util.*;

public class Main {

    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("=== CARD GAME ===");

        while (true) {
            Difficulty d = chooseDifficulty();
            runGame(d);

            System.out.print("Play again? (y/n): ");
            if (!sc.nextLine().trim().equalsIgnoreCase("y")) break;
        }

        System.out.println("Goodbye.");
    }

 

    static void runGame(Difficulty d) {
        Deck deck = new Deck();
        deck.shuffle();

        List<Card> playerHand = deck.deal(10);
        List<Card> cpuHand    = deck.deal(10);

        GameState.Turn first = new Random().nextBoolean()
                ? GameState.Turn.PLAYER
                : GameState.Turn.COMPUTER;

        GameState  state  = new GameState(playerHand, cpuHand, first);
        AIEngine   ai     = new AIEngine(d);
        GameEngine engine = new GameEngine(state, ai);

        System.out.println("\nDifficulty : " + d);
        System.out.println("First turn : " + (first == GameState.Turn.PLAYER ? "You" : "Computer"));

        while (!state.isGameOver()) {
            System.out.println("\n Round " + state.round + " ---");
            System.out.println("Your cards: " + state.playerHand.size() + "  CPU cards: " + state.computerHand.size());
            playRound(state, engine);
        }

        // Result
        System.out.println("\n=== GAME OVER ===");
        if (state.getWinner() == GameState.Turn.PLAYER)
            System.out.println("YOU WIN!");
        else
            System.out.println("Computer wins.");

        System.out.println("Tricks -- You: " + state.playerTricks + "  CPU: " + state.computerTricks);
        System.out.println("\nRound log:");
        for (String line : state.log) System.out.println("  " + line);
    }



    static void playRound(GameState state, GameEngine engine) {
        GameState.Turn leader = state.currentTurn;
        Card playerCard, cpuCard;

        if (leader == GameState.Turn.PLAYER) {
            printHand(state.playerHand);
            playerCard = pickCard(state.playerHand, engine, null);
            engine.playerLead(playerCard);
            System.out.println("You played   : " + playerCard);

            cpuCard = engine.computerRespond(playerCard);
            System.out.println("CPU played   : " + cpuCard);

        } else {
           
            cpuCard = engine.computerLead();
            System.out.println("CPU leads    : " + cpuCard);

            boolean mustFollow = engine.playerHasSuit(cpuCard.getSuit());
            if (mustFollow)
                System.out.println("(You must follow suit: " + cpuCard.getSuit() + ")");
            else
                System.out.println("(You have no " + cpuCard.getSuit() + " -- play anything)");

            printHand(state.playerHand);
            playerCard = pickCard(state.playerHand, engine, cpuCard);
            engine.playerRespond(playerCard, cpuCard);
            System.out.println("You played   : " + playerCard);
        }

        String result = engine.resolveTrick(leader);
        System.out.println(result);

        state.log.add("Round " + (state.round - 1) + ": You=" + playerCard + " CPU=" + cpuCard);
    }


    static Card pickCard(List<Card> hand, GameEngine engine, Card lead) {
        while (true) {
            System.out.print("Pick (1-" + hand.size() + "): ");
            String input = sc.nextLine().trim();

            int idx;
            try {
                idx = Integer.parseInt(input) - 1;
            } catch (NumberFormatException e) {
                System.out.println("Enter a number.");
                continue;
            }

            if (idx < 0 || idx >= hand.size()) {
                System.out.println("Out of range.");
                continue;
            }

            Card chosen = hand.get(idx);

            if (lead != null && !engine.isLegalPlay(chosen, lead)) {
                System.out.println("Must follow suit: " + lead.getSuit());
                continue;
            }

            return chosen;
        }
    }

    static void printHand(List<Card> hand) {
        System.out.println("Your hand:");
        for (int i = 0; i < hand.size(); i++)
            System.out.print("[" + (i + 1) + "]" + hand.get(i) + " ");
        System.out.println();
    }

  

    static Difficulty chooseDifficulty() {
        System.out.println("\n1. Easy\n2. Medium\n3. Hard");
        while (true) {
            System.out.print("Choice: ");
            String s = sc.nextLine().trim();
            if (s.equals("1")) return Difficulty.EASY;
            if (s.equals("2")) return Difficulty.MEDIUM;
            if (s.equals("3")) return Difficulty.HARD;
            System.out.println("Enter 1, 2, or 3.");
        }
    }
}