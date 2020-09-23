package colonelblotto;

import java.util.Random;

public class Strategy implements Comparable<Strategy> {
    private final int[] strategy;
    private double averageUtility;
    private double crossoverProbability;

    public Strategy(int totalTroops, boolean randomize) {
        strategy = new int[ColonelBlotto.NUMBER_OF_BATTLEFIELDS];
        if (randomize) {
            int remainingTroops = totalTroops;
            Random random = new Random();
            // Randomly choose a battlefield and add a random number of troops
            while (remainingTroops > 0) {
                strategy[random.nextInt(ColonelBlotto.NUMBER_OF_BATTLEFIELDS)] += 1;
                remainingTroops -= 1;
            }
        }
    }

    public void setAverageUtility(double averageUtility) {
        this.averageUtility = averageUtility;
    }

    public double getAverageUtility() {
        return averageUtility;
    }

    public int getBattlefieldTroops(int index) {
        return strategy[index];
    }

    public double getCrossoverProbability() {
        return crossoverProbability;
    }

    public void setCrossoverProbability(double crossoverProbability) {
        this.crossoverProbability = crossoverProbability;
    }

    @Override
    public int compareTo(Strategy strategy) {
        return Double.compare(this.getAverageUtility(), strategy.getAverageUtility());
    }

    @Override
    public String toString() {
        StringBuilder description = new StringBuilder("|");
        for (int battlefieldTroops : strategy) {
            description.append(battlefieldTroops).append("|");
        }
        description.append(" AVG Utility: ").append(averageUtility);
        return description.toString();
    }
}
