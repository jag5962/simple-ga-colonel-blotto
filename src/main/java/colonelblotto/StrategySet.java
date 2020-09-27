package colonelblotto;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;

public class StrategySet implements Iterable<Strategy> {
    private final Strategy[] strategySet;

    // Initialize strategy set of random strategies
    public StrategySet(int size, int totalTroops) {
        strategySet = new Strategy[size];
        for (int i = 0; i < size; i++) {
            strategySet[i] = new Strategy(totalTroops);
        }
    }

    private StrategySet(int size, Strategy[] fittestStrategies) {
        strategySet = new Strategy[size];
        System.arraycopy(fittestStrategies, 0, strategySet, 0, fittestStrategies.length);
    }

    // Calculate fitness for every strategy
    public void calculateAverageUtilities(StrategySet opponentStrategySet) {
        // Calculate average utility for every strategy in this set
        for (Strategy thisStrategy : this) {
            int utilitySum = 0;

            // Compare troops on each battlefield for this strategy set's strategy to every strategy
            // of the opponent's strategy set
            for (int battlefieldIndex = 0; battlefieldIndex < ColonelBlotto.NUMBER_OF_BATTLEFIELDS; battlefieldIndex++) {
                int thisStrategyTroops = thisStrategy.getBattlefieldTroops(battlefieldIndex);

                // Compare strategy to every one of opponent's strategies
                for (Strategy opponentStrategy : opponentStrategySet) {
                    int opponentTroops = opponentStrategy.getBattlefieldTroops(battlefieldIndex);
                    int utilitySign = thisStrategyTroops >= opponentTroops ? 1 : -1;
                    int battlefieldUtility = ColonelBlotto.BATTLEFIELD_UTILITIES[battlefieldIndex];
                    utilitySum += utilitySign * battlefieldUtility;
                }
            }
            thisStrategy.setAverageUtility(utilitySum / (double) opponentStrategySet.getSize());
        }
        Arrays.sort(strategySet, Collections.reverseOrder());
    }

    public int getSize() {
        return strategySet.length;
    }

    public Strategy getStrategy(int index) {
        return strategySet[index];
    }

    public void setStrategy(int index, Strategy strategy) {
        strategySet[index] = strategy;
    }

    public Strategy getFittest() {
        return strategySet[0];
    }

    public StrategySet createNextGenerationWithElitism(int eliteCount) {
        return new StrategySet(getSize(), Arrays.copyOf(strategySet, eliteCount));
    }

    public void calculateCrossoverProbability() {
        for (int rank = 1; rank <= strategySet.length; rank++) {
            double probability = 2 * (getSize() + 1 - rank) / (double) (getSize() * (getSize() + 1));
            strategySet[rank - 1].setCrossoverProbability(probability);
        }
    }

    public boolean hasConverged() {
        final double THRESHOLD = .0001;
        double averageUtility = strategySet[0].getAverageUtility();
        for (int i = 1; i < strategySet.length; i++) {
            if (Math.abs(averageUtility - strategySet[i].getAverageUtility()) > THRESHOLD) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Iterator<Strategy> iterator() {
        return Arrays.stream(strategySet).iterator();
    }

    @Override
    public String toString() {
        StringBuilder description = new StringBuilder();
        for (int i = 0; i < strategySet.length; i++) {
            description.append(i + 1).append(": ").append(strategySet[i]).append("\n");
        }
        return description.toString();
    }
}
