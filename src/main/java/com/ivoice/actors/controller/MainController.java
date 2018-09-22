package com.ivoice.actors.controller;


import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.FSM;
import akka.actor.Props;
import com.ivoice.actors.chat.Chat;
import com.ivoice.actors.chat.ChatState;
import com.ivoice.actors.person.Person;
import com.ivoice.actors.person.PersonState;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {


    private static ActorSystem actorSystem = ActorSystem.create("actor-system");


    @GetMapping({"/", "/index"})
    public String root(Model model) throws InterruptedException {
        showStatus(model);
        return "index";
    }

    private static void showStatus(Model model) throws InterruptedException {
        actorSystem = ActorSystem.create("actor-system");

        final ActorRef chat = actorSystem.actorOf(Props.create(Chat.class), "Chat");
        final ActorRef chatState = actorSystem.actorOf(Props.create(ChatState.class), "ChatState");

        final ActorRef alice  = actorSystem.actorOf(Props.create(Person.class), "Alice");
        final ActorRef bob = actorSystem.actorOf(Props.create(Person.class), "Bob");
        final ActorRef personState = actorSystem.actorOf(Props.create(PersonState.class), "PersonState");

        chat.tell("Hi", alice);


        model.addAttribute("chatStatus", ChatState.States.values());
        model.addAttribute("personStatus", PersonState.States.values());

    }


}
