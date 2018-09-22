package com.ivoice.actors.chat;

import akka.actor.AbstractFSM;
import com.ivoice.actors.switchers.AwaitingRecipient;
import com.ivoice.actors.switchers.Online;

public class ChatState extends AbstractFSM<ChatState.States, ChatState.Data> {

    {
        startWith(States.AVAILABLE, new Data());

        when(States.AVAILABLE, matchEvent(AwaitingRecipient.class, Data.class, (changeStatus, noData) ->
                goTo((States.AWAITING_RECIPIENT)).replying(States.AWAITING_RECIPIENT)));

        when(States.AVAILABLE, matchEvent(Online.class, Data.class, (changeStatus, noData) ->
                goTo((States.ONLINE)).replying(States.ONLINE)));


        when(States.AWAITING_RECIPIENT, matchEvent(Online.class, Data.class, (changeStatus, noData) ->
                goTo(States.ONLINE).replying(States.ONLINE)));



        initialize();

    }


    class Data {
        // empty data
    }

    public enum States {
        AVAILABLE, AWAITING_RECIPIENT, ONLINE
    }

}


