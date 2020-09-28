package colonelblotto;

import java.util.Random;

public class Strategy implements Comparable<Strategy> {
    private final int[] strategy;
    private double averagePayoff;
    private double crossoverProbability;

    // Construct new strategy with random disbursement of troops
    public Strategy(int totalTroops) {
        strategy = new int[ColonelBlotto.NUMBER_OF_BATTLEFIELDS];

        int remainingTroops = totalTroops;
        Random random = new Random();
        // Randomly disburse the troops
        while (remainingTroops-- > 0) {
            strategy[random.nextInt(ColonelBlotto.NUMBER_OF_BATTLEFIELDS)]++;
        }
    }

    // Crossover parents to create child
    public Strategy(Strategy[] parents) {
        strategy = new int[ColonelBlotto.NUMBER_OF_BATTLEFIELDS];

        // Perform crossover by taking the average of troops for each battlefield from parents
        boolean roundUp = true;
        for (int battlefield = 0; battlefield < ColonelBlotto.NUMBER_OF_BATTLEFIELDS; battlefield++) {
            int parentsTroopSum = parents[0].getBattlefieldTroops(battlefield) + parents[1].getBattlefieldTroops(battlefield);
            strategy[battlefield] = (parentsTroopSum) / 2;

            // Truncation of troop average may lower total troops of child strategy,
            // so add a troop back for every other truncation occurrence
            if (parentsTroopSum % 2 == 1) {
                if (roundUp) {
                    strategy[battlefield]++;
                }
                roundUp = !roundUp;
            }
        }
    }

    public double getAveragePayoff() {
        return averagePayoff;
    }

    public void setAveragePayoff(double averagePayoff) {
        this.averagePayoff = averagePayoff;
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

    public void swapTroops(int battlefield1, int battlefield2) {
        int temp = strategy[battlefield1];
        strategy[battlefield1] = strategy[battlefield2];
        strategy[battlefield2] = temp;
    }

    @Override
    public int compareTo(Strategy strategy) {
        return Double.compare(averagePayoff, strategy.getAveragePayoff());
    }

    @Override
    public String toString() {
        StringBuilder description = new StringBuilder("|");
        for (int battlefieldTroops : strategy) {
            description.append(battlefieldTroops).append("|");
        }
        description.append(" AVG Payoff: ").append(averagePayoff);
        return description.toString();
    }
}
