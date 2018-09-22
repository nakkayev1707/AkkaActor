package com.ivoice.actors.person;


import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.ivoice.actors.chat.Chat;
import com.ivoice.actors.utils.AdditionalFunc;

import static com.ivoice.actors.utils.AdditionalFunc.logToFile;

public class Person extends AbstractActor {

    private String name;
    private ActorSystem system;
    private ActorRef chat;
    private ActorRef personState;

    public Person(){
        system = ActorSystem.create("person-actors");
        chat = system.actorOf(Props.create(Chat.class));
        personState = system.actorOf(Props.create(PersonState.class));
    }


    @Override
    public Receive createReceive() {
        return receiveBuilder().match(String.class, msg -> {
            if (!(getSender().path().name().equals("Bob"))){
                logToFile("Bob received from " + getSender().path().name() + ": " + msg, "BOB");
                chat.tell("Hi " + getSender().path().name(), getSelf());
            }
        }).matchAny(o -> AdditionalFunc.logToFile("Bob received unknown message.", "BOB")).build();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
