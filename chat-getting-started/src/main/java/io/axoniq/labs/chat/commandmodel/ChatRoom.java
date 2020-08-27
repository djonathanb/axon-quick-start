package io.axoniq.labs.chat.commandmodel;

import io.axoniq.labs.chat.coreapi.CreateRoomCommand;
import io.axoniq.labs.chat.coreapi.RoomCreatedEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

@Aggregate
public class ChatRoom {

    private static final Logger logger = LoggerFactory.getLogger(ChatRoom.class);

    @AggregateIdentifier
    private String roomId;
    private String name;

    public ChatRoom() {
        // Required by Axon
    }

    @CommandHandler
    public ChatRoom(CreateRoomCommand command) {
        logger.debug("handling command {}", command);
        apply(new RoomCreatedEvent(command.getRoomId(), command.getName()));
    }

    @EventSourcingHandler
    public void on(RoomCreatedEvent event) {
        logger.debug("handling sourcing event {}", event);
        roomId = event.getRoomId();
        name = event.getName();
    }

}
