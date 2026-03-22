package cardgame;

import java.util.*;

public class AIEngine {

    private Difficulty difficulty;
    private Random rng = new Random();

    private Set<Card> seen = new HashSet<>();
    private Map<Card.Suit, Integer> voids = new EnumMap<>(Card.Suit.class);

    public AIEngine(Difficulty d) {
        difficulty = d;
    }

    public Card chooseLead(List<Card> hand) {
        if (difficulty == Difficulty.EASY) return randomCard(hand);
        if (difficulty == Difficulty.MEDIUM) return lowest(hand);
        return hardLead(hand);
    }

    public Card chooseResponse(List<Card> hand, Card lead) {
        if (difficulty == Difficulty.EASY) return easyResp(hand, lead);
        if (difficulty == Difficulty.MEDIUM) return mediumResp(hand, lead);
        return hardResp(hand, lead);
    }

    public void recordPlayed(Card c) {
        seen.add(c);
    }

    public void recordPlayerVoid(Card.Suit s) {
        voids.put(s, voids.getOrDefault(s, 0) + 1);
    }

    private Card randomCard(List<Card> hand) {
        return hand.get(rng.nextInt(hand.size()));
    }

    private Card easyResp(List<Card> hand, Card lead) {
        List<Card> same = filter(hand, lead.getSuit());
        if (!same.isEmpty()) return randomCard(same);
        return randomCard(hand);
    }

    private Card lowest(List<Card> hand) {
        Card res = hand.get(0);
        for (Card c : hand) {
            if (c.getValue() < res.getValue()) res = c;
        }
        return res;
    }

    private Card highest(List<Card> hand) {
        Card res = hand.get(0);
        for (Card c : hand) {
            if (c.getValue() > res.getValue()) res = c;
        }
        return res;
    }

    private Card mediumResp(List<Card> hand, Card lead) {
        List<Card> same = filter(hand, lead.getSuit());
        if (!same.isEmpty()) return lowest(same);
        return highest(hand);
    }

    private Card hardLead(List<Card> hand) {
        Map<Card.Suit, List<Card>> map = group(hand);

        Card.Suit best = null;
        int max = -1;

        for (Card.Suit s : map.keySet()) {
            int size = map.get(s).size();
            if (size > max) {
                max = size;
                best = s;
            }
        }

        return lowest(map.get(best));
    }

    private Card hardResp(List<Card> hand, Card lead) {
        List<Card> same = filter(hand, lead.getSuit());

        if (!same.isEmpty()) {
            Card best = null;

            for (Card c : same) {
                if (c.getValue() > lead.getValue()) {
                    if (best == null || c.getValue() < best.getValue()) {
                        best = c;
                    }
                }
            }

            if (best != null) return best;
            return lowest(same);
        }

        Map<Card.Suit, List<Card>> map = group(hand);

        Card.Suit bestSuit = null;
        int max = -1;

        for (Card.Suit s : map.keySet()) {
            int size = map.get(s).size();
            if (size > max) {
                max = size;
                bestSuit = s;
            }
        }

        return highest(map.get(bestSuit));
    }

    private List<Card> filter(List<Card> hand, Card.Suit suit) {
        List<Card> res = new ArrayList<>();
        for (Card c : hand) {
            if (c.getSuit() == suit) res.add(c);
        }
        return res;
    }

    private Map<Card.Suit, List<Card>> group(List<Card> hand) {
        Map<Card.Suit, List<Card>> map = new EnumMap<>(Card.Suit.class);

        for (Card c : hand) {
            map.putIfAbsent(c.getSuit(), new ArrayList<>());
            map.get(c.getSuit()).add(c);
        }

        return map;
    }

    public Set<Card> getSeenCards() {
        return seen;
    }
}