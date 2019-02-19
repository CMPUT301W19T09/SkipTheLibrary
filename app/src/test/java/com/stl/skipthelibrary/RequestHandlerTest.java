package com.stl.skipthelibrary;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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
        User requestor = new User();

        requestHandler.addRequestor(requestor);

        assertTrue(requestHandler.getRequestors().contains(requestor));
    }

    @Test(expected = RequestorsUnavailableException.class)
    public void testAcceptRequestor(){
        User failedRequest = new User();
        User succeedRequest = new User();

        requestHandler.addRequestor(failedRequest);
        requestHandler.addRequestor(succeedRequest);

        assertEquals(requestHandler.getRequestors().size(),2);

        requestHandler.acceptRequestor(succeedRequest);

        assertEquals(requestHandler.getRequestors().size(),0);
        assertEquals(requestHandler.getAcceptedRequestor(),succeedRequest);
        assertEquals(requestHandler.getState().getBookStatus(),BookStatus.ACCEPTED);
        assertEquals(requestHandler.getState().getHandoffState(),HandoffState.READY_FOR_PICKUP);
    }

    @Test
    public void testDenyRequestor(){
        User deniedRequest = new User();
        User succeedRequest = new User();

        requestHandler.addRequestor(deniedRequest);
        requestHandler.addRequestor(succeedRequest);

        assertEquals(requestHandler.getRequestors().size(),2);

        requestHandler.denyRequestor(deniedRequest);

        assertEquals(requestHandler.getRequestors().size(),1);
        assertEquals(requestHandler.getRequestors().get(0),succeedRequest);
        assertEquals(requestHandler.getState().getBookStatus(),BookStatus.REQUESTED);
        assertEquals(requestHandler.getState().getHandoffState(),null);
    }

}