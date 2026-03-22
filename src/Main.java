package cardgame;

import java.util.*;

public class Main {

    static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        printBanner();

        boolean again = true;
        while (again) {
            Difficulty d = chooseDifficulty();
            runGame(d);

            System.out.print("\nPlay again? (y/n): ");
            again = sc.nextLine().trim().equalsIgnoreCase("y");
        }

        System.out.println("\nThanks for playing.");
    }

    static void runGame(Difficulty d) {
        Deck deck = Deck.build();
        deck.shuffle();

        List<Card> player = deck.deal(10);
        List<Card> cpu = deck.deal(10);

        GameState.Turn turn = new Random().nextBoolean()
                ? GameState.Turn.PLAYER
                : GameState.Turn.COMPUTER;

        GameState state = new GameState(player, cpu, turn);
        AIEngine ai = new AIEngine(d);
        GameEngine engine = new GameEngine(state, ai);

        System.out.println("\nGame started");
        System.out.println("Difficulty: " + d);
        System.out.println("First turn: " + (turn == GameState.Turn.PLAYER ? "You" : "Computer"));

        while (!state.isGameOver()) {
            System.out.println("\nRound " + state.getRoundNumber());
            System.out.println("You: " + state.getPlayerHand().size() + " | CPU: " + state.getComputerHand().size());

            playRound(state, engine);
        }

        showResult(state);
    }

    static void playRound(GameState s, GameEngine e) {
        GameState.Turn lead = s.getCurrentTurn();

        Card playerCard, cpuCard;

        if (lead == GameState.Turn.PLAYER) {
            printHand(s.getPlayerHand());
            playerCard = pickCard(s.getPlayerHand(), e, null);

            e.playerLead(playerCard);
            System.out.println("You played: " + playerCard);

            cpuCard = e.computerRespond(playerCard);
            System.out.println("Computer played: " + cpuCard);

        } else {
            cpuCard = e.computerLead();
            System.out.println("Computer leads: " + cpuCard);

            printHand(s.getPlayerHand());
            playerCard = pickCard(s.getPlayerHand(), e, cpuCard);

            e.playerRespond(playerCard, cpuCard);
            System.out.println("You played: " + playerCard);
        }

        GameEngine.TrickResult result = e.resolveTrick(lead);
        System.out.println(result.message);

        s.addLog("Round " + (s.getRoundNumber() - 1) + ": " + playerCard + " vs " + cpuCard);
    }

    static Card pickCard(List<Card> hand, GameEngine e, Card lead) {
        while (true) {
            System.out.print("Pick card: ");
            String input = sc.nextLine();

            int idx;
            try {
                idx = Integer.parseInt(input) - 1;
            } catch (Exception ex) {
                System.out.println("Enter a valid number");
                continue;
            }

            if (idx < 0 || idx >= hand.size()) {
                System.out.println("Invalid choice");
                continue;
            }

            Card chosen = hand.get(idx);

            if (lead != null && e != null && !e.isLegalPlay(chosen, lead)) {
                System.out.println("You must follow suit");
                continue;
            }

            return chosen;
        }
    }

    static void printHand(List<Card> hand) {
        System.out.println("\nYour hand:");

        for (int i = 0; i < hand.size(); i++) {
            System.out.print("[" + (i + 1) + "] " + hand.get(i) + "  ");
        }

        System.out.println();
    }

    static void showResult(GameState s) {
        System.out.println("\nGame Over");

        if (s.getWinner() == GameState.Turn.PLAYER) {
            System.out.println("You win");
        } else {
            System.out.println("Computer wins");
        }

        System.out.println("Score: " + s.getPlayerTricks() + " - " + s.getComputerTricks());

        for (String log : s.getTrickLog()) {
            System.out.println(log);
        }
    }

    static Difficulty chooseDifficulty() {
        System.out.println("\n1. Easy");
        System.out.println("2. Medium");
        System.out.println("3. Hard");

        while (true) {
            System.out.print("Choice: ");
            String s = sc.nextLine();

            if (s.equals("1")) return Difficulty.EASY;
            if (s.equals("2")) return Difficulty.MEDIUM;
            if (s.equals("3")) return Difficulty.HARD;

            System.out.println("Try again");
        }
    }

    static void printBanner() {
        System.out.println("\n=== CARD GAME ===");
    }
}