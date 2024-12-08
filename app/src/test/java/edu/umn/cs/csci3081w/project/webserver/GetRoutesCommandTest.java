package edu.umn.cs.csci3081w.project.webserver;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.gson.JsonObject;
import edu.umn.cs.csci3081w.project.model.Line;
import edu.umn.cs.csci3081w.project.model.Position;
import edu.umn.cs.csci3081w.project.model.Route;
import edu.umn.cs.csci3081w.project.model.Stop;
import java.util.Arrays;
import java.util.Collections;
import org.junit.jupiter.api.Test;


public class GetRoutesCommandTest {
  @Test
  void testExecuteWithEmptyLines() {
    VisualTransitSimulator simulator = mock(VisualTransitSimulator.class);
    WebServerSession session = mock(WebServerSession.class);
    when(simulator.getLines()).thenReturn(Collections.emptyList());

    GetRoutesCommand command = new GetRoutesCommand(simulator);
    command.execute(session, new JsonObject());

    // Verify session.sendJson() called with empty routes
    verify(session).sendJson(argThat(json ->
        json.getAsJsonArray("routes").size() == 0
    ));
  }

  @Test
  void testExecuteWithNonEmptyLines() {
    VisualTransitSimulator simulator = mock(VisualTransitSimulator.class);
    WebServerSession session = mock(WebServerSession.class);
    Line line = mock(Line.class);
    Route outboundRoute = mock(Route.class);
    Route inboundRoute = mock(Route.class);
    when(simulator.getLines()).thenReturn(Arrays.asList(line));
    when(line.getOutboundRoute()).thenReturn(outboundRoute);
    when(line.getInboundRoute()).thenReturn(inboundRoute);

    GetRoutesCommand command = new GetRoutesCommand(simulator);
    command.execute(session, new JsonObject());

    verify(session).sendJson(argThat(json ->
        json.getAsJsonArray("routes").size() == 2
    ));
  }

}
