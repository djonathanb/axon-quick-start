package io.axoniq.labs.chat.query.rooms.summary;

import io.axoniq.labs.chat.coreapi.AllRoomsQuery;
import io.axoniq.labs.chat.coreapi.ParticipantJoinedRoomEvent;
import io.axoniq.labs.chat.coreapi.ParticipantLeftRoomEvent;
import io.axoniq.labs.chat.coreapi.RoomCreatedEvent;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class RoomSummaryProjection {

    private static final Logger logger = LoggerFactory.getLogger(RoomSummaryProjection.class);

    private final RoomSummaryRepository roomSummaryRepository;

    public RoomSummaryProjection(RoomSummaryRepository roomSummaryRepository) {
        this.roomSummaryRepository = roomSummaryRepository;
    }

    @EventHandler
    public void on(RoomCreatedEvent event) {
        logger.debug("handling projection event {}", event);
        RoomSummary summary = new RoomSummary(event.getRoomId(), event.getName());
        roomSummaryRepository.save(summary);
    }

    @EventHandler
    public void on(ParticipantJoinedRoomEvent event) {
        logger.debug("handling projection event {}", event);
        RoomSummary summary = roomSummaryRepository.getOne(event.getRoomId());
        summary.addParticipant();
        roomSummaryRepository.save(summary);
    }

    @EventHandler
    public void on(ParticipantLeftRoomEvent event) {
        logger.debug("handling projection event {}", event);
        RoomSummary summary = roomSummaryRepository.getOne(event.getRoomId());
        summary.removeParticipant();
        roomSummaryRepository.save(summary);
    }

    @QueryHandler
    public Collection<RoomSummary> handle(AllRoomsQuery query) {
        logger.debug("handling query event {}", query);
        return roomSummaryRepository.findAll();
    }

}
