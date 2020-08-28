package io.axoniq.labs.chat.query.rooms.participants;

import io.axoniq.labs.chat.coreapi.ParticipantJoinedRoomEvent;
import io.axoniq.labs.chat.coreapi.ParticipantLeftRoomEvent;
import io.axoniq.labs.chat.coreapi.RoomParticipantsQuery;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.stream.Collectors;

@Component
public class RoomParticipantsProjection {

    private static final Logger logger = LoggerFactory.getLogger(RoomParticipantsProjection.class);

    private final RoomParticipantsRepository repository;

    public RoomParticipantsProjection(RoomParticipantsRepository repository) {
        this.repository = repository;
    }

    @EventHandler
    public void on(ParticipantJoinedRoomEvent event) {
        logger.debug("handling projection event {}", event);
        RoomParticipant roomParticipant = new RoomParticipant(event.getRoomId(), event.getParticipant());
        repository.save(roomParticipant);
    }

    @EventHandler
    public void on(ParticipantLeftRoomEvent event) {
        logger.debug("handling projection event {}", event);
        repository.deleteByParticipantAndRoomId(event.getParticipant(), event.getRoomId());
    }

    @QueryHandler
    public Collection<String> handle(RoomParticipantsQuery query) {
        logger.debug("handling query event {}", query);
        return repository.findRoomParticipantsByRoomId(query.getRoomId()).stream()
                .map(RoomParticipant::getParticipant)
                .collect(Collectors.toList());
    }

}
