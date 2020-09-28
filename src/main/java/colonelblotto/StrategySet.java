package colonelblotto;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;

public class StrategySet implements Iterable<Strategy> {
    private final Strategy[] strategySet;
    private final char player;

    // Initialize strategy set of random strategies
    public StrategySet(int size, int totalTroops, char player) {
        strategySet = new Strategy[size];
        this.player = player;
        for (int i = 0; i < size; i++) {
            strategySet[i] = new Strategy(totalTroops);
        }
    }

    // Construct next generation for previous strategy set
    public StrategySet(StrategySet previousGeneration) {
        strategySet = new Strategy[previousGeneration.getSize()];
        this.player = previousGeneration.player;
    }

    // Calculate fitness for every strategy
    public void calculateAveragePayoffs(StrategySet opponentStrategySet) {
        // Calculate average payoff for every strategy in this set
        for (Strategy thisStrategy : this) {
            long payoffSum = 0;

            // Compare strategy to every one of the opponent's strategies
            for (Strategy opponentStrategy : opponentStrategySet) {
                // Compare troops on each battlefield
                for (int battlefield = 0; battlefield < ColonelBlotto.NUMBER_OF_BATTLEFIELDS; battlefield++) {
                    int thisStrategyTroops = thisStrategy.getBattlefieldTroops(battlefield);
                    int opponentTroops = opponentStrategy.getBattlefieldTroops(battlefield);
                    if (player == 'A') {
                        if (thisStrategyTroops >= opponentTroops) {
                            payoffSum += ColonelBlotto.BATTLEFIELD_PAYOFFS[battlefield];
                        }
                    } else {
                        if (thisStrategyTroops > opponentTroops) {
                            payoffSum += ColonelBlotto.BATTLEFIELD_PAYOFFS[battlefield];
                        }
                    }
                }
            }
            thisStrategy.setAveragePayoff(payoffSum / (double) opponentStrategySet.getSize());
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

    public void calculateCrossoverProbability() {
        for (int rank = 1; rank <= strategySet.length; rank++) {
            double probability = 2 * (getSize() + 1 - rank) / (double) (getSize() * (getSize() + 1));
            strategySet[rank - 1].setCrossoverProbability(probability);
        }
    }

    public boolean hasConverged() {
        final double THRESHOLD = .0001;
        double averagePayoff = strategySet[0].getAveragePayoff();
        for (int i = 1; i < strategySet.length; i++) {
            if (Math.abs(averagePayoff - strategySet[i].getAveragePayoff()) > THRESHOLD) {
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
