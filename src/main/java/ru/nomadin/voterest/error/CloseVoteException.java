package ru.nomadin.voterest.error;

public class CloseVoteException extends IllegalRequestDataException {
    public CloseVoteException(String msg) {
        super(msg);
    }
}