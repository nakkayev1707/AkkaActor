package com.ivoice.actors.person;

import akka.actor.AbstractFSM;
import com.ivoice.actors.switchers.Communicating;
import com.ivoice.actors.switchers.Connected;
import com.ivoice.actors.switchers.Talked;

public class PersonState extends AbstractFSM<PersonState.States, PersonState.Data> {

    {
        startWith(States.NEEDCHAT, new Data());

        when(States.NEEDCHAT, matchEvent(Connected.class, Data.class, (changeStatus, noData) ->
                goTo(States.CONNECTED).replying(States.CONNECTED)));

        when(States.CONNECTED, matchEvent(Communicating.class, Data.class, (changeStatus, noData) ->
                goTo(States.COMMUNICATING).replying(States.COMMUNICATING)));

        when(States.COMMUNICATING, matchEvent(Talked.class, Data.class, (changeStatus, noData) ->
                goTo(States.TALKED).replying(States.TALKED)));


        initialize();

    }

    public enum States {
        NEEDCHAT, CONNECTED, COMMUNICATING, TALKED
    }

    class Data{

    }

}
