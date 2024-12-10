package edu.umn.cs.csci3081w.project.webserver;

import com.google.gson.JsonObject;
import edu.umn.cs.csci3081w.project.model.Line;
import edu.umn.cs.csci3081w.project.model.Route;
import edu.umn.cs.csci3081w.project.model.Stop;
import edu.umn.cs.csci3081w.project.model.Position;
import edu.umn.cs.csci3081w.project.model.Issue;
import edu.umn.cs.csci3081w.project.model.RandomPassengerGenerator;
import edu.umn.cs.csci3081w.project.model.PassengerGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class LineIssueCommandTest {
  private VisualTransitSimulator mockSimulator;
  private LineIssueCommand lineIssueCommand;

  @BeforeEach
  public void setUp() {
    mockSimulator = mock(VisualTransitSimulator.class);
    lineIssueCommand = new LineIssueCommand(mockSimulator);
  }

  /**
   * Test line issue injection with valid line ID.
   */
  @Test
  public void testExecuteWithValidLineId() {
    // Create mock lines
    Line mockLine1 = mock(Line.class);
    when(mockLine1.getId()).thenReturn(1);
    Line mockLine2 = mock(Line.class);
    when(mockLine2.getId()).thenReturn(2);

    List<Line> lines = List.of(mockLine1, mockLine2);
    when(mockSimulator.getLines()).thenReturn(lines);

    JsonObject command = new JsonObject();
    command.addProperty("id", 2);

    WebServerSession mockSession = mock(WebServerSession.class);
    lineIssueCommand.execute(mockSession, command);

    verify(mockLine2).createIssue();
    verify(mockLine1, never()).createIssue();
  }

  @Test
  public void testExecuteWithInvalidLineId() {

    Line mockLine1 = mock(Line.class);
    when(mockLine1.getId()).thenReturn(1);
    Line mockLine2 = mock(Line.class);
    when(mockLine2.getId()).thenReturn(2);

    List<Line> lines = List.of(mockLine1, mockLine2);
    when(mockSimulator.getLines()).thenReturn(lines);


    JsonObject command = new JsonObject();
    command.addProperty("id", 3);


    WebServerSession mockSession = mock(WebServerSession.class);
    lineIssueCommand.execute(mockSession, command);


    verify(mockLine1, never()).createIssue();
    verify(mockLine2, never()).createIssue();
  }

  @Test
  public void testExecuteWithNoLines() {
    when(mockSimulator.getLines()).thenReturn(new ArrayList<>());

    JsonObject command = new JsonObject();
    command.addProperty("id", 1);

    WebServerSession mockSession = mock(WebServerSession.class);
    lineIssueCommand.execute(mockSession, command);

    verifyNoInteractions(mockSession);
  }
}
