package colonelblotto;

import java.util.Random;

public class Strategy implements Comparable<Strategy> {
    private final int[] strategy;
    private double averageUtility;
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

    public void mutateBattlefields(int battlefield1, int battlefield2) throws Exception {
        if (strategy[battlefield1] == 0 && strategy[battlefield2] == 0) {
            throw new Exception("Both battlefields can't loose a troop.");
        }

        if (strategy[battlefield1] > 0) {
            strategy[battlefield1]--;
            strategy[battlefield2]++;
        } else {
            strategy[battlefield2]--;
            strategy[battlefield1]++;
        }
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
