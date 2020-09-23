package colonelblotto;

import java.util.Random;

public class GA {
    private static final double ELITISM_RATE = .05;
    private static final double MUTATION_RATE = .01;

    public static void evaluation(StrategySet[] strategySets) {
        // Calculate every strategy's average utility
        strategySets[0].calculateAverageUtilities(strategySets[1]);
        strategySets[1].calculateAverageUtilities(strategySets[0]);

        // Calculate every strategy's probability to be chosen as a parent
        strategySets[0].calculateCrossoverProbability();
        strategySets[1].calculateCrossoverProbability();
    }

    public static void evolve(StrategySet strategySet) {
        // Create new strategy set and copy {eliteCount}th highest average utility strategies
        int eliteCount = (int) Math.round(ELITISM_RATE * strategySet.getSize());
        StrategySet newStrategySet = strategySet.createNextGenerationWithElitism(eliteCount);

        // Use crossover to fill in the rest of the new strategy set
        crossover(strategySet, newStrategySet, eliteCount);
    }

    private static void crossover(StrategySet strategySet, StrategySet newStrategySet, int start) {
        // Randomly choose a strategy as a parent
        Random random = new Random();

        double probabilitySum = 0;
        for (Strategy strategy : strategySet) {
            probabilitySum += strategy.getCrossoverProbability();
        }
        int index = random.nextInt((int) Math.round(probabilitySum) * 100);

        double sum = 0;
        int i = 0;
        while (sum < index) {
            sum += strategySet.getStrategy(i++).getCrossoverProbability() * 100;
        }
        strategySet.getStrategy(i);
    }

    private static void mutation(StrategySet strategySet) {

    }
}
