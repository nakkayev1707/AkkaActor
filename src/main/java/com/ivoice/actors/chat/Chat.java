package com.ivoice.actors.chat;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.ivoice.actors.switchers.AwaitingRecipient;
import com.ivoice.actors.switchers.Online;

import java.util.ArrayList;
import java.util.List;

import static com.ivoice.actors.utils.AdditionalFunc.logToFile;


public class Chat extends AbstractActor {

    private List<ActorRef> connectedActors;
    private ActorSystem system;
    private ActorRef chatState;

    public Chat() {
        connectedActors = new ArrayList<>(2);
        system = ActorSystem.create("chat-actors");
        chatState = system.actorOf(Props.create(ChatState.class));
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder().match(String.class, this::messageHandler)
                .matchAny(o -> logToFile("Chat received unknown message.", "CHAT"))
                .build();
    }

    private void messageHandler(String msg) {
        try {
            connectedActors.add(getSender());
            logToFile("Connected new actor: " + getSender().path().name(), "CHAT");
            if (connectedActors.size() == 1) {
                chatState.tell(new AwaitingRecipient(), getSender());
                getSender().tell("no recipient", getSender());
                logToFile("Chat switched to state: AWAITING_RECIPIENT", "CHAT");
            } else if (connectedActors.size() == 2) {
                chatState.tell(new Online(), getSender());
                logToFile("Chat switched to state: ONLINE", "CHAT");
                ActorRef actor1 = connectedActors.get(0);
                ActorRef actor2 = connectedActors.get(1);
                if (sender().path().name().equals(actor1.path().name())) {
                    actor2.tell(msg, actor1);
                }
                if (sender().path().name().equals(actor2.path().name())) {
                    actor1.tell(msg, actor2);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
