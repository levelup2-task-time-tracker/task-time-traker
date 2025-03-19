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

class GeneticAlgorithmTest {

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
    void testInitializePopulation() {
        when(random.nextInt(users.size())).thenReturn(0, 1, 0);
        GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm(users, tasks, random, 0.8, 0.1, 10, 100);

        List<Chromosome> population = geneticAlgorithm.initializePopulation();

        assertEquals(10, population.size());
        for (Chromosome chromosome : population) {
            assertNotNull(chromosome);
            assertEquals(3, chromosome.getAssignments().size());
        }
    }

    @Test
    void testSelect() {
        GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm(users, tasks, random, 0.8, 0.1, 10, 100);
        List<Chromosome> population = new ArrayList<>();
        population.add(new Chromosome(List.of(0, 1, 0), users, tasks, random));
        population.add(new Chromosome(List.of(1, 0, 1), users, tasks, random));
        population.add(new Chromosome(List.of(0, 0, 0), users, tasks, random));
        when(random.nextInt(population.size())).thenReturn(0, 1, 2);
        Chromosome selected = geneticAlgorithm.select(population);

        assertNotNull(selected);
        assertTrue(population.contains(selected));
    }

    @Test
    void testGetBestChromosome() {
        // Arrange
        GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm(users, tasks, random, 0.8, 0.1, 10, 100);
        List<Chromosome> population = new ArrayList<>();
        Chromosome chromosome1 = new Chromosome(List.of(0, 1, 0), users, tasks, random);
        Chromosome chromosome2 = new Chromosome(List.of(1, 0, 1), users, tasks, random);
        Chromosome chromosome3 = new Chromosome(List.of(0, 0, 0), users, tasks, random);
        population.add(chromosome1);
        population.add(chromosome2);
        population.add(chromosome3);

        Chromosome bestChromosome = geneticAlgorithm.getBestChromosome(population);


        assertNotNull(bestChromosome);
        assertEquals(chromosome2, bestChromosome);
    }

    @Test
    void testRun() {
        when(random.nextInt(users.size())).thenReturn(0, 1, 0);
        when(random.nextInt(anyInt())).thenReturn(0);
        GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm(users, tasks, random, 0.8, 0.1, 10, 100);

        Chromosome bestChromosome = geneticAlgorithm.run();

        assertNotNull(bestChromosome);
        assertTrue(bestChromosome.evaluate() >= 0);
    }
}