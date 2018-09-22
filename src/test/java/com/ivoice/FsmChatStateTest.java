package com.ivoice;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.JavaTestKit;
import com.ivoice.actors.chat.ChatState;
import com.ivoice.actors.switchers.AwaitingRecipient;
import com.ivoice.actors.switchers.Online;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import java.util.List;

import static org.mockito.Mockito.mock;


public class FsmChatStateTest {

    private ActorSystem system;
    private ActorRef chatState;


    @Before
    public void init(){
        system = ActorSystem.create("system");
        chatState = system.actorOf(Props.create(ChatState.class));
    }

    @Test
    public void checkChatStateAwaitingRecipient(){
        new JavaTestKit(system){{
            chatState.tell(new AwaitingRecipient(), getRef());
            expectMsgEquals(ChatState.States.AWAITING_RECIPIENT);
        }};
    }

    @Test
    public void checkChatStateOnline(){
        new JavaTestKit(system){{
            chatState.tell(new Online(), getRef());
            expectMsgEquals(ChatState.States.ONLINE);
        }};
    }


    @After
    public void finalize(){
        system.stop(chatState);
    }

}
