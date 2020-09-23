package colonelblotto;

import java.util.Arrays;
import java.util.Iterator;

public class StrategySet implements Iterable<Strategy> {
    private Strategy[] strategySet;

    // Initialize strategy set of random strategies
    public StrategySet(int size, boolean fillRandom, int totalTroops) {
        strategySet = new Strategy[size];
        if (fillRandom) {
            for (int i = 0; i < size; i++) {
                Strategy newStrategy = new Strategy(totalTroops, true);
                strategySet[i] = newStrategy;
            }
        }
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

            thisStrategy.setAverageUtility(utilitySum / (double)opponentStrategySet.getSize());
        }
    }

    public int getSize() {
        return strategySet.length;
    }

    public Strategy getStrategy(int index) {
        return strategySet[index];
    }

    public Strategy getFittest() {
        Strategy fittest = strategySet[0];
        for (Strategy strategy : this) {
            if (fittest.getAverageUtility() < strategy.getAverageUtility()) {
                fittest = strategy;
            }
        }
        return fittest;
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