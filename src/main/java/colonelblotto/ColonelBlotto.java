package colonelblotto;

import java.util.Arrays;

public class ColonelBlotto {
    // Set instance of Colonel Blotto
    static final int[] BATTLEFIELD_PAYOFFS = new int[]{4, 8, 10, 11, 13, 2, 16, 20, 1, 21};
    static final int NUMBER_OF_BATTLEFIELDS = BATTLEFIELD_PAYOFFS.length;
    static final int TOTAL_TROOPS_A = 100;
    static final int TOTAL_TROOPS_B = 100;

    public static void main(String[] args) {
        // Initialize strategy set with random strategies for player A & B
        StrategySet[] strategySets = new StrategySet[]{
                new StrategySet(50, TOTAL_TROOPS_A, 'A'),
                new StrategySet(50, TOTAL_TROOPS_B, 'B')
        };
        GA.evaluation(strategySets);
        System.out.println("OldA: " + strategySets[0]);
        System.out.println("OldB: " + strategySets[1]);

        // Terminate upon convergence for both strategy sets
        int generationCount = 0;
        while (!strategySets[0].hasConverged() || !strategySets[1].hasConverged()) {
            // Perform same operations to both players' strategy sets
            for (int player = 0; player < 2; player++) {
                strategySets[player] = GA.evolve(strategySets[player]);
            }
            GA.evaluation(strategySets);
            generationCount++;
        }

        System.out.println("NewA: " + strategySets[0]);
        System.out.println("NewB: " + strategySets[1]);
        System.out.println("Fittest A: " + strategySets[0].getFittest());
        System.out.println("Fittest B: " + strategySets[1].getFittest());
        System.out.println("Maximum payoff: " + Arrays.stream(BATTLEFIELD_PAYOFFS).sum());
        System.out.println("Generations: " + generationCount);
    }
}
