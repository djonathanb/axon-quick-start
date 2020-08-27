package io.axoniq.labs.chat.commandmodel;

import io.axoniq.labs.chat.coreapi.CreateRoomCommand;
import io.axoniq.labs.chat.coreapi.JoinRoomCommand;
import io.axoniq.labs.chat.coreapi.ParticipantJoinedRoomEvent;
import io.axoniq.labs.chat.coreapi.RoomCreatedEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

@Aggregate
public class ChatRoom {

    private static final Logger logger = LoggerFactory.getLogger(ChatRoom.class);

    @AggregateIdentifier
    private String roomId;
    private String name;

    private Set<String> participants;

    public ChatRoom() {
        // Required by Axon
    }

    @CommandHandler
    public ChatRoom(CreateRoomCommand command) {
        logger.debug("handling command {}", command);
        apply(new RoomCreatedEvent(command.getRoomId(), command.getName()));
    }

    @CommandHandler
    public void handle(JoinRoomCommand command) {
        logger.debug("handling command {}", command);
        logger.debug("handling command {}", participants);
        logger.debug("### does participant already joined? {}", participants.contains(command.getParticipant()));
        if (!participants.contains(command.getParticipant())) {
            apply(new ParticipantJoinedRoomEvent(command.getRoomId(), command.getParticipant()));
        }
    }

    @EventSourcingHandler
    public void on(RoomCreatedEvent event) {
        logger.debug("handling sourcing event {}", event);
        roomId = event.getRoomId();
        name = event.getName();
        participants = new HashSet<>();
    }

    @EventSourcingHandler
    public void on(ParticipantJoinedRoomEvent event) {
        logger.debug("handling sourcing event {}", event);
        participants.add(event.getParticipant());
    }

}
