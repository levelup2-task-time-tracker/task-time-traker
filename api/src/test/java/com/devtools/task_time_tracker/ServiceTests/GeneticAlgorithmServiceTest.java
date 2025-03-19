package com.devtools.task_time_tracker.ServiceTests;

import com.devtools.task_time_tracker.dts.AssignmentDto;
import com.devtools.task_time_tracker.model.ProjectModel;
import com.devtools.task_time_tracker.repository.ProjectMemberRepository;
import com.devtools.task_time_tracker.repository.ProjectRepository;
import com.devtools.task_time_tracker.repository.TaskRepository;
import com.devtools.task_time_tracker.service.GeneticAlgorithmService;
import com.devtools.task_time_tracker.workload_balancer.Chromosome;
import com.devtools.task_time_tracker.workload_balancer.GeneticAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GeneticAlgorithmServiceTest {

    @Mock
    private ProjectMemberRepository projectMemberRepository;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private GeneticAlgorithmService geneticAlgorithmService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetSuggestions_WhenProjectExists_ReturnsAssignments() {
        UUID projectId = UUID.randomUUID();
        String role = "DEVELOPER";

        ProjectModel projectModel = new ProjectModel();
        when(projectRepository.findById(projectId)).thenReturn(Optional.of(projectModel));

        List<Object> userResultSet = new ArrayList<>();
        Object[] userRow1 = new Object[]{UUID.randomUUID(), "User1", BigDecimal.valueOf(10.0)};
        Object[] userRow2 = new Object[]{UUID.randomUUID(), "User2", BigDecimal.valueOf(5.0)};
        userResultSet.add(userRow1);
        userResultSet.add(userRow2);
        when(projectMemberRepository.sumByUser(projectId, role)).thenReturn(userResultSet);

        List<Object> taskResultSet = new ArrayList<>();
        Object[] taskRow1 = new Object[]{UUID.randomUUID(), "Task1", BigDecimal.valueOf(3.0)};
        Object[] taskRow2 = new Object[]{UUID.randomUUID(), "Task2", BigDecimal.valueOf(2.0)};
        taskResultSet.add(taskRow1);
        taskResultSet.add(taskRow2);
        when(projectMemberRepository.sumByTask(projectId)).thenReturn(taskResultSet);

        Chromosome mockChromosome = mock(Chromosome.class);
        when(mockChromosome.getAssignments()).thenReturn(List.of(0, 1));
        GeneticAlgorithm mockGeneticAlgorithm = mock(GeneticAlgorithm.class);
        when(mockGeneticAlgorithm.run()).thenReturn(mockChromosome);

        List<AssignmentDto> assignments = geneticAlgorithmService.getSuggestions(projectId, role);

        assertNotNull(assignments);
        assertEquals(1, assignments.size());
        assertEquals("User2", assignments.get(0).user().getUserName());
        assertEquals("Task1", assignments.get(0).tasks().getFirst().getTaskName());
        assertEquals("Task2", assignments.get(0).tasks().getLast().getTaskName());
    }

    @Test
    void testGetSuggestions_WhenProjectDoesNotExist_ThrowsNotFoundException() {
        UUID projectId = UUID.randomUUID();
        String role = "DEVELOPER";
        when(projectRepository.findById(projectId)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            geneticAlgorithmService.getSuggestions(projectId, role);
        });

        assertEquals("Project not found", exception.getReason());
    }

    @Test
    void testGetSuggestions_WhenNoTasksOrUsers_ReturnsEmptyList() {
        UUID projectId = UUID.randomUUID();
        String role = "DEVELOPER";

        ProjectModel projectModel = new ProjectModel();
        when(projectRepository.findById(projectId)).thenReturn(Optional.of(projectModel));

        when(projectMemberRepository.sumByUser(projectId, role)).thenReturn(new ArrayList<>());
        when(projectMemberRepository.sumByTask(projectId)).thenReturn(new ArrayList<>());

        List<AssignmentDto> assignments = geneticAlgorithmService.getSuggestions(projectId, role);

        assertNotNull(assignments);
        assertTrue(assignments.isEmpty());
    }
}