package com.devtools.task_time_tracker.workload_balancer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ChromosomeTest {

    @Mock
    private Random random;

    private List<User> users;
    private List<Task> tasks;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        users = new ArrayList<>();
        users.add(new User(UUID.randomUUID(), "User1", 10.0));
        users.add(new User(UUID.randomUUID(), "User2", 5.0));

        tasks = new ArrayList<>();
        tasks.add(new Task(UUID.randomUUID(), 3.0, "Task1"));
        tasks.add(new Task(UUID.randomUUID(), 2.0, "Task2"));
        tasks.add(new Task(UUID.randomUUID(), 4.0, "Task3"));
    }

    @Test
    void testEvaluate() {
        List<Integer> assignments = List.of(0, 1, 0);
        Chromosome chromosome = new Chromosome(assignments, users, tasks, random);

        double fitness = chromosome.evaluate();

        assertEquals(10, fitness);
    }

    @Test
    void testCrossover() {
        List<Integer> parent1Assignments = List.of(0, 1, 0);
        List<Integer> parent2Assignments = List.of(1, 0, 1);
        Chromosome parent1 = new Chromosome(parent1Assignments, users, tasks, random);
        Chromosome parent2 = new Chromosome(parent2Assignments, users, tasks, random);

        when(random.nextInt(tasks.size())).thenReturn(1, 2);

        Chromosome[] children = parent1.crossover(parent2);

        assertEquals(List.of(0, 0, 1), children[0].getAssignments());
        assertEquals(List.of(1, 1, 0), children[1].getAssignments());
    }

    @Test
    void testMutate() {
        // Arrange
        List<Integer> assignments = List.of(0, 1, 0);
        Chromosome chromosome = new Chromosome(assignments, users, tasks, random);

        when(random.nextInt(assignments.size())).thenReturn(1);
        when(random.nextInt(users.size())).thenReturn(0);

        Chromosome mutatedChromosome = chromosome.mutate();
        assertEquals(List.of(0, 0, 0), mutatedChromosome.getAssignments());
    }
}