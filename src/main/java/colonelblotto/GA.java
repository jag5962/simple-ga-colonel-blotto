package colonelblotto;

import java.util.Random;

public class GA {
    private static final double ELITISM_RATE = .05;
    private static final double MUTATION_RATE = .01;

    public static void evaluation(StrategySet[] strategySets) {
        // Calculate every strategy's average utility
        strategySets[0].calculateAveragePayoffs(strategySets[1]);
        strategySets[1].calculateAveragePayoffs(strategySets[0]);

        // Calculate every strategy's probability to be chosen as a parent
        strategySets[0].calculateCrossoverProbability();
        strategySets[1].calculateCrossoverProbability();
    }

    public static StrategySet evolve(StrategySet strategySet) {
        // Create empty strategy set
        StrategySet newStrategySet = new StrategySet(strategySet);

        // Use reproduction and mutation to fill the new strategy set
        for (int i = 0; i < newStrategySet.getSize(); i++) {
            // Select 2 parent strategies for crossover
            Strategy[] parents = selectParents(strategySet);

            // Use crossover to produce child strategy
            Strategy child = crossover(parents);

            // Mutate with probability
            Random random = new Random();
            if (random.nextDouble() < MUTATION_RATE) {
                mutation(child);
            }

            newStrategySet.setStrategy(i, child);
        }
        return newStrategySet;
    }

    public static StrategySet evolveWithElitism(StrategySet strategySet) {
        // Create empty strategy set
        StrategySet newStrategySet = new StrategySet(strategySet);
        int eliteCount = (int) Math.ceil(ELITISM_RATE * strategySet.getSize());

        // Use reproduction and mutation to fill the new strategy set
        Strategy strategy;
        for (int i = 0; i < newStrategySet.getSize(); i++) {
            if (i < eliteCount) {
                strategy = strategySet.getStrategy(i);
            } else {
                // Select 2 parent strategies for crossover
                Strategy[] parents = selectParents(strategySet);

                // Use crossover to produce child strategy
                strategy = crossover(parents);

                // Mutate with probability
                Random random = new Random();
                if (random.nextDouble() < MUTATION_RATE) {
                    mutation(strategy);
                }
            }

            newStrategySet.setStrategy(i, strategy);
        }
        return newStrategySet;
    }

    private static Strategy[] selectParents(StrategySet strategySet) {
        // Randomly choose two different strategies as parents
        Random random = new Random();

        // Prepare mapping of crossover probabilities
        int indexSum1 = random.nextInt(100), indexSum2;
        do {
            indexSum2 = random.nextInt(100);
        } while (indexSum1 == indexSum2);
        int higherUtilityParentIndexSum = Math.max(indexSum1, indexSum2);
        int lowerUtilityParentIndexSum = Math.min(indexSum1, indexSum2);

        // Set limit to stop looking for higher indexed parent strategy
        double sum = 0;
        int lowerUtilityParent = -1, higherUtilityParent = -1;
        while (sum < higherUtilityParentIndexSum) {
            sum += strategySet.getStrategy(++higherUtilityParent).getCrossoverProbability() * 100;
            // Save lower indexed parent strategy without stopping loop
            if (lowerUtilityParent == -1 && sum >= lowerUtilityParentIndexSum) {
                lowerUtilityParent = higherUtilityParent;
            }
        }
        return new Strategy[]{strategySet.getStrategy(higherUtilityParent),
                strategySet.getStrategy(lowerUtilityParent)};
    }

    private static Strategy crossover(Strategy[] parents) {
        return new Strategy(parents);
    }

    private static void mutation(Strategy strategy) {
        Random random = new Random();
        int battlefield1 = random.nextInt(ColonelBlotto.NUMBER_OF_BATTLEFIELDS), battlefield2;
        do {
            battlefield2 = random.nextInt(ColonelBlotto.NUMBER_OF_BATTLEFIELDS);
        } while (battlefield1 == battlefield2);

        strategy.swapTroops(battlefield1, battlefield2);
    }
}
