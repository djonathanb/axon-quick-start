package io.axoniq.labs.chat.query.rooms.participants;

import io.axoniq.labs.chat.coreapi.ParticipantJoinedRoomEvent;
import org.axonframework.eventhandling.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

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

    // TODO: Create the query handler to read data from this model.

}
