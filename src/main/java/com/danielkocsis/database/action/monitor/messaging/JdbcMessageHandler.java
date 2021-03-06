package com.danielkocsis.database.action.monitor.messaging;

import com.danielkocsis.database.action.monitor.service.WebSocketService;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@AllArgsConstructor
public class JdbcMessageHandler {

  private final WebSocketService webSocketService;

  public void handleJdbcMessage(final Message<List<Map<String, Object>>> message) {
    final MessageHeaders messageHeaders = message.getHeaders();
    final List<Map<String, Object>> payload = message.getPayload();

    log.info(payload.size() + " new database records are inserted");

    for (Map<String, Object> columns : payload) {
      columns.keySet().stream()
          .filter("ID"::equalsIgnoreCase)
          .findFirst()
          .map(c -> (long) columns.get(c))
          .ifPresent(c -> webSocketService.broadcastMessage(messageHeaders.getTimestamp(), c));
    }
  }

}
