package domain;

public class ClientCardNumberWithNumberOfTransactions {
    private final int clientCardNumber;
    private final int numberOfTransactions;

    public ClientCardNumberWithNumberOfTransactions(int clientCardNumber, int numberOfTransactions) {
        this.clientCardNumber = clientCardNumber;
        this.numberOfTransactions = numberOfTransactions;
    }

    public int getClientCardNumber() {
        return clientCardNumber;
    }

    public int getNumberOfTransactions() {
        return numberOfTransactions;
    }
}
