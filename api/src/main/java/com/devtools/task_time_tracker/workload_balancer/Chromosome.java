package com.devtools.task_time_tracker.workload_balancer;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Chromosome {
    public List<Integer> getAssignments() {
        return assignments;
    }

    private List<Integer> assignments;
    private  Double fitness;
    private List<User> users;
    private List<Task> tasks;
    private Random random;

    public  Chromosome(List<Integer> assignments, List<User> users, List<Task> tasks, Random random){
        this.assignments = assignments;
        fitness = null;
        this.users = users;
        this.tasks = tasks;
        this.random = random;
    }


    public Chromosome(List<User> users, List<Task> tasks, Random random) {
        this.assignments = new ArrayList<>();
        this.random = random;
        for (int i = 0; i < tasks.size(); i++) {
            this.assignments.add(random.nextInt(users.size()));
        }

        this.tasks = tasks;
        this.users = users;
    }

    public double evaluate() {
        if (fitness == null) {
            double[] userLoads = new double[users.size()];

            for (int i = 0; i < assignments.size(); i++) {
                double hours = tasks.get(i).getStoryPoints();
                int userIndex = assignments.get(i);
                userLoads[userIndex] += hours;
            }


            for (int i = 0; i < users.size(); i++) {
                userLoads[i] += users.get(i).getWorkLoad();
            }

            fitness = 0.0;
            for (int i = 0; i < userLoads.length; i++) {
                for (int j = i + 1; j < userLoads.length; j++) {
                    fitness += Math.abs(userLoads[i] - userLoads[j]);
                }
            }
        }
        return fitness;
    }

    public Chromosome[] crossover(Chromosome other) {
        List<Integer> parent1Assignments = new ArrayList<>(this.assignments);
        List<Integer> parent2Assignments = new ArrayList<>(other.assignments);

        int point1 = random.nextInt(assignments.size());
        int point2 = random.nextInt(assignments.size());

        if (point1 > point2) {
            int temp = point1;
            point1 = point2;
            point2 = temp;
        }

        List<Integer> child1Assignments = new ArrayList<>(parent1Assignments);
        List<Integer> child2Assignments = new ArrayList<>(parent2Assignments);

        for (int i = point1; i <= point2; i++) {
            child1Assignments.set(i, parent2Assignments.get(i));
            child2Assignments.set(i, parent1Assignments.get(i));
        }

        return new Chromosome[]{ new Chromosome(child1Assignments, users, tasks, random), new Chromosome(child2Assignments, users, tasks, random) };
    }


    public Chromosome mutate(int numUsers) {
        List<Integer> mutatedAssignments = new ArrayList<>(this.assignments);
        int numberOfMutations = random.nextInt(mutatedAssignments.size()) + 1;

        for (int i = 0; i < numberOfMutations; i++) {
            int index = random.nextInt(mutatedAssignments.size());
            int newUser = random.nextInt(numUsers);
            mutatedAssignments.set(index, newUser);
        }

        return new Chromosome(mutatedAssignments, users, tasks, random);
    }

}
