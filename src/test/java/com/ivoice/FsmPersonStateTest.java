package com.ivoice;


import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.JavaTestKit;
import com.ivoice.actors.person.PersonState;
import com.ivoice.actors.switchers.Communicating;
import com.ivoice.actors.switchers.Connected;
import com.ivoice.actors.switchers.Talked;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class FsmPersonStateTest {

    private ActorSystem system;
    private ActorRef personState;

    @Before
    public void init(){
        system = ActorSystem.create("system");
        personState = system.actorOf(Props.create(PersonState.class));
    }

    @Test
    public void checkPersonStateConnected(){
        new JavaTestKit(system){{
            personState.tell(new Connected(), getRef());
            expectMsgEquals(PersonState.States.CONNECTED);
        }};
    }

    @Test
    public void checkPersonStateCommunicating(){
        new JavaTestKit(system){{
            personState.tell(new Communicating(), getRef());
            expectMsgEquals(PersonState.States.COMMUNICATING);
        }};
    }


    @Test
    public void checkPersonStateTalked(){
        new JavaTestKit(system){{
           personState.tell(new Talked(), getRef());
           expectMsgEquals(PersonState.States.TALKED);
        }};
    }

    @After
    public void finalize(){
        system.stop(personState);
    }


}
