package colonelblotto;

public class GA {
    public static void evaluation(StrategySet strategySetA, StrategySet strategySetB) {
        strategySetA.calculateAverageUtilities(strategySetB);
        strategySetB.calculateAverageUtilities(strategySetA);
    }

    public static void selection(StrategySet strategySet) {

    }

    public static void crossover(StrategySet strategySet) {

    }

    public static void mutation(StrategySet strategySet) {

    }
}
