package com.devtools.task_time_tracker.workload_balancer;

import java.util.*;

public class GeneticAlgorithm {
    private List<User> users;
    private List<Task> tasks;
    private Random random;
    private final double crossoverRate;
    private final double mutationRate;
    private final int populationSize;
    private final int generations;

    public GeneticAlgorithm(List<User> users, List<Task> tasks, Random random, double crossoverRate, double mutationRate, int populationSize, int generations) {
        this.users = users;
        this.tasks = tasks;
        this.random = random;
        this.crossoverRate = crossoverRate;
        this.mutationRate = mutationRate;
        this.populationSize = populationSize;
        this.generations = generations;
    }

    public Chromosome run() {
        List<Chromosome> population = initializePopulation();
        Chromosome bestChromosome = null;

        for (int generation = 0; generation < generations; generation++) {
            List<Chromosome> newPopulation = new ArrayList<>();
            int crossoverSplitPoint = (int) (crossoverRate * populationSize);
            for (int i = 0; i < crossoverSplitPoint; i++){
                Chromosome parent1 = select(population);
                Chromosome parent2 = select(population);
                Chromosome[] offspring = parent1.crossover(parent2);
                newPopulation.add(offspring[0]);
                newPopulation.add(offspring[1]);
            }

            for (int i = 0; i < crossoverSplitPoint; i++){
                Chromosome parent1 = select(population);
                Chromosome parent2 = select(population);
                Chromosome[] offspring = parent1.crossover(parent2);
                newPopulation.add(offspring[0]);
                newPopulation.add(offspring[1]);
            }

            for (int i = crossoverSplitPoint; i < populationSize; i++){
                Chromosome parent = select(population);
                newPopulation.add(parent.mutate());
            }


            population = newPopulation;
            Chromosome currentBestChromosome = getBestChromosome(population);

            bestChromosome = (bestChromosome == null || currentBestChromosome.evaluate() < bestChromosome.evaluate())
                    ? currentBestChromosome
                    : bestChromosome;
        }

        return bestChromosome;
    }

    private List<Chromosome> initializePopulation() {
        List<Chromosome> population = new ArrayList<>();
        for (int i = 0; i < populationSize; i++) {
            population.add(new Chromosome(users, tasks, random));
        }
        return population;
    }

    private Chromosome select(List<Chromosome> population) {
        int tournamentSize = 5;
        List<Chromosome> tournament = new ArrayList<>();
        for (int i = 0; i < tournamentSize; i++) {
            tournament.add(population.get(random.nextInt(population.size())));
        }

        return getBestChromosome(tournament);
    }

    private Chromosome getBestChromosome(List<Chromosome> population) {
        Chromosome best = population.getFirst();
        for (Chromosome chromosome : population) {
            if (chromosome.evaluate() < best.evaluate()) {
                best = chromosome;
            }
        }
        return best;
    }
}

