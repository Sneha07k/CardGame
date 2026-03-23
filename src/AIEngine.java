package cardgame;

import java.util.*;

enum Difficulty { EASY, MEDIUM, HARD }

public class AIEngine {

    private Difficulty difficulty;
    private Random rng = new Random();

    public AIEngine(Difficulty d) {
        difficulty = d;
    }

    public Card chooseLead(List<Card> hand) {
        if (difficulty == Difficulty.EASY)   return randomCard(hand);
        if (difficulty == Difficulty.MEDIUM) return lowest(hand);
        return hardLead(hand);
    }

    public Card chooseResponse(List<Card> hand, Card lead) {
        if (difficulty == Difficulty.EASY)   return easyResp(hand, lead);
        if (difficulty == Difficulty.MEDIUM) return mediumResp(hand, lead);
        return hardResp(hand, lead);
    }


    private Card randomCard(List<Card> hand) {
        return hand.get(rng.nextInt(hand.size()));
    }

    private Card easyResp(List<Card> hand, Card lead) {
        List<Card> same = sameSuit(hand, lead.getSuit());
        if (!same.isEmpty()) return randomCard(same);
        return randomCard(hand);
    }


    private Card lowest(List<Card> hand) {
        Card res = hand.get(0);
        for (Card c : hand)
            if (c.getValue() < res.getValue()) res = c;
        return res;
    }

    private Card highest(List<Card> hand) {
        Card res = hand.get(0);
        for (Card c : hand)
            if (c.getValue() > res.getValue()) res = c;
        return res;
    }

    private Card mediumResp(List<Card> hand, Card lead) {
        List<Card> same = sameSuit(hand, lead.getSuit());
        if (!same.isEmpty()) return lowest(same);
        return highest(hand);
    }

 

    private Card hardLead(List<Card> hand) {
        Card.Suit best = longestSuit(hand);
        return lowest(sameSuit(hand, best));
    }

    private Card hardResp(List<Card> hand, Card lead) {
        List<Card> same = sameSuit(hand, lead.getSuit());

        if (!same.isEmpty()) {
            Card winner = null;
            for (Card c : same) {
                if (c.getValue() > lead.getValue()) {
                    if (winner == null || c.getValue() < winner.getValue())
                        winner = c;
                }
            }
            if (winner != null) return winner;
            return lowest(same); 
        }

        return highest(sameSuit(hand, longestSuit(hand)));
    }



    private List<Card> sameSuit(List<Card> hand, Card.Suit suit) {
        List<Card> res = new ArrayList<>();
        for (Card c : hand)
            if (c.getSuit() == suit) res.add(c);
        return res;
    }

    private Card.Suit longestSuit(List<Card> hand) {
        Map<Card.Suit, Integer> count = new EnumMap<>(Card.Suit.class);
        for (Card c : hand)
            count.put(c.getSuit(), count.getOrDefault(c.getSuit(), 0) + 1);

        Card.Suit best = null;
        int max = -1;
        for (Card.Suit s : count.keySet()) {
            if (count.get(s) > max) {
                max = count.get(s);
                best = s;
            }
        }
        return best;
    }
}