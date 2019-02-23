package com.stl.skipthelibrary;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.UUID;

import static org.junit.Assert.*;

/**
 * Created by Luke Slevinsky on 2019-02-15.
 */
public class RequestHandlerTest {
    private RequestHandler requestHandler;
    private State state;

    @Before
    public void setUp() throws Exception {
        state = new State();
        requestHandler = new RequestHandler(state);
    }

    @Test
    public void testSetState(){
        State newState = new State();
        requestHandler.setState(newState);
        assertEquals(newState, requestHandler.getState());
    }

    @Test
    public void testSetRequestors(){
        ArrayList<String> newRequestors = new ArrayList<>();
        requestHandler.setRequestors(newRequestors);
        assertEquals(newRequestors, requestHandler.getRequestors());
    }
    @Test
    public void testLendBook(){
        requestHandler.getState().setBookStatus(BookStatus.ACCEPTED);
        requestHandler.getState().setHandoffState(HandoffState.READY_FOR_PICKUP);

        requestHandler.lendBook();

        assertEquals(requestHandler.getState().getBookStatus(), BookStatus.ACCEPTED);
        assertEquals(requestHandler.getState().getHandoffState(), HandoffState.OWNER_LENT);
    }

    @Test
    public void testConfirmBorrowed(){
        requestHandler.getState().setBookStatus(BookStatus.ACCEPTED);
        requestHandler.getState().setHandoffState(HandoffState.OWNER_LENT);

        requestHandler.confirmBorrowed();

        assertEquals(requestHandler.getState().getBookStatus(), BookStatus.BORROWED);
        assertEquals(requestHandler.getState().getHandoffState(), HandoffState.BORROWER_RECEIVED);
    }

    @Test
    public void testReturnBook(){
        requestHandler.getState().setBookStatus(BookStatus.BORROWED);
        requestHandler.getState().setHandoffState(HandoffState.BORROWER_RECEIVED);

        requestHandler.returnBook();

        assertEquals(requestHandler.getState().getBookStatus(), BookStatus.BORROWED);
        assertEquals(requestHandler.getState().getHandoffState(), HandoffState.BORROWER_RETURNED);
    }

    @Test
    public void testConfirmReturned(){
        requestHandler.getState().setBookStatus(BookStatus.BORROWED);
        requestHandler.getState().setHandoffState(HandoffState.BORROWER_RETURNED);

        requestHandler.confirmReturned();

        assertEquals(requestHandler.getState().getBookStatus(), BookStatus.AVAILABLE);
        assertEquals(requestHandler.getState().getHandoffState(), HandoffState.OWNER_RECEIVED);
    }

    @Test
    public void testAddRequestor(){
        String requestor = UUID.randomUUID().toString();

        requestHandler.addRequestor(requestor);

        assertTrue(requestHandler.getRequestors().contains(requestor));
    }

//    @Test
//    public void testAcceptRequestor(){
//        String failedRequest = UUID.randomUUID().toString();
//        String succeedRequest = UUID.randomUUID().toString();
//
//        requestHandler.addRequestor(failedRequest);
//        requestHandler.addRequestor(succeedRequest);
//
//        assertEquals(requestHandler.getRequestors().size(),2);
//
//        requestHandler.acceptRequestor(succeedRequest);
//
//        assertEquals(requestHandler.getRequestors().size(),0);
//        assertEquals(requestHandler.getAcceptedRequestor(),succeedRequest);
//        assertEquals(requestHandler.getState().getBookStatus(),BookStatus.ACCEPTED);
//        assertEquals(requestHandler.getState().getHandoffState(),HandoffState.READY_FOR_PICKUP);
//    }
//
//    @Test
//    public void testDenyRequestor(){
//        String deniedRequest = UUID.randomUUID().toString();
//        String succeedRequest = UUID.randomUUID().toString();
//
//        requestHandler.addRequestor(deniedRequest);
//        requestHandler.addRequestor(succeedRequest);
//
//        assertEquals(requestHandler.getRequestors().size(),2);
//
//        requestHandler.denyRequestor(deniedRequest);
//
//        assertEquals(requestHandler.getRequestors().size(),1);
//        assertEquals(requestHandler.getRequestors().get(0),succeedRequest);
//        assertEquals(requestHandler.getState().getBookStatus(),BookStatus.REQUESTED);
//        assertEquals(requestHandler.getState().getHandoffState(),null);
//    }

}