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

    public static StrategySet evolve(StrategySet strategySet) {
        // Create new strategy set and copy {eliteCount}th highest average utility strategies
        int eliteCount = (int) Math.ceil(ELITISM_RATE * strategySet.getSize());
        StrategySet newStrategySet = strategySet.createNextGenerationWithElitism(eliteCount);

        // Use reproduction and mutation to fill in the rest of the new strategy set
        for (int i = eliteCount; i < strategySet.getSize(); i++) {
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
        boolean exceptionOccurred;
        do {
            exceptionOccurred = false;
            int mutateBattlefield1 = random.nextInt(ColonelBlotto.NUMBER_OF_BATTLEFIELDS), mutateBattlefield2;
            do {
                mutateBattlefield2 = random.nextInt(ColonelBlotto.NUMBER_OF_BATTLEFIELDS);
            } while (mutateBattlefield1 == mutateBattlefield2);

            try {
                strategy.mutateBattlefields(mutateBattlefield1, mutateBattlefield2);
            } catch (Exception e) {
                exceptionOccurred = true;
            }
        } while (exceptionOccurred);
    }
}
