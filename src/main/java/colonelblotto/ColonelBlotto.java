package colonelblotto;

public class ColonelBlotto {
    // Set instance of Colonel Blotto
    static final int[] BATTLEFIELD_UTILITIES = new int[]{4, 8, 10, 11, 13, 2, 16, 20, 1, 21};
    static final int NUMBER_OF_BATTLEFIELDS = BATTLEFIELD_UTILITIES.length;
    static final int TOTAL_TROOPS_A = 100;
    static final int TOTAL_TROOPS_B = 100;

    public static void main(String[] args) {
        // Initialize strategy set with random strategies for player A & B
        StrategySet[] strategySets = new StrategySet[]{
                new StrategySet(50, TOTAL_TROOPS_A),
                new StrategySet(50, TOTAL_TROOPS_B)
        };

        final int iterations = 1;
        for (int i = 1; i <= iterations; i++) {
            GA.evaluation(strategySets);

            // Perform same operations to both strategy sets
            for (StrategySet strategySet : strategySets) {
                GA.evolve(strategySet);
            }
        }

        System.out.println("Fittest A: " + strategySets[0].getFittest());
        System.out.println("Fittest B: " + strategySets[1].getFittest());
    }
}
