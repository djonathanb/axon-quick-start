package io.axoniq.labs.chat.query.rooms.messages;

import io.axoniq.labs.chat.coreapi.MessagePostedEvent;
import io.axoniq.labs.chat.coreapi.RoomMessagesQuery;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventhandling.Timestamp;
import org.axonframework.queryhandling.QueryHandler;
import org.axonframework.queryhandling.QueryUpdateEmitter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Collection;

@Component
public class ChatMessageProjection {

    private static final Logger logger = LoggerFactory.getLogger(ChatMessageProjection.class);

    private final ChatMessageRepository repository;
    private final QueryUpdateEmitter updateEmitter;

    public ChatMessageProjection(ChatMessageRepository repository, QueryUpdateEmitter updateEmitter) {
        this.repository = repository;
        this.updateEmitter = updateEmitter;
    }

    @EventHandler
    public void on(MessagePostedEvent event, @Timestamp Instant timestamp) {
        logger.debug("handling projection event {}", event);
        long instant = timestamp.toEpochMilli();
        ChatMessage message = new ChatMessage(event.getParticipant(), event.getRoomId(), event.getMessage(), instant);
        repository.save(message);
    }

    @QueryHandler
    public Collection<ChatMessage> handle(RoomMessagesQuery query) {
        logger.debug("handling query event {}", query);
        return repository.findAllByRoomIdOrderByTimestamp(query.getRoomId());
    }

    // TODO: Emit updates when new message arrive to notify subscription query by modifying the event handler.
}
