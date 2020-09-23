package colonelblotto;

import java.util.Random;

public class Strategy {
    private int[] strategy;
    private double averageUtility;

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
