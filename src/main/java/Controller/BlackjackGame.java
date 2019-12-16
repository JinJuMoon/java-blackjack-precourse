package Controller;

import Exception.DeckHasNoCardException;
import UI.Input.InputController;
import UI.Output.OutputController;
import domain.result.GameResult;
import domain.card.Deck;
import domain.user.*;

import java.util.List;

public class BlackjackGame {
    private static final int INITIAL_NUMBER_OF_CARDS = 2;
    private static final int ADDITIONAL_NUMBER_OF_CARDS = 1;

    private List<User> users;
    private Deck deck;

    public BlackjackGame() {
        this.users = UsersFactory.create(InputController.askPlayerProperties());
        this.deck = new Deck();
    }

    public void run() {
        GameResult gameResult = playAndHandleErrors();
        showGameResult(gameResult);
    }

    private GameResult playAndHandleErrors() {
        try {
            return play();
        } catch (DeckHasNoCardException e) {
            return null;
        }
    }

    private GameResult play() {
        GameResult gameResult = GameResult.create(users);

        distributeInitialCards();
        if (gameResult.hasBlackjack()) {
            return gameResult;
        }

        playTurns();
        gameResult.decideGameResult();
        return gameResult;
    }

    private void distributeInitialCards() {
        for (User user : users) {
            user.addCard(deck, INITIAL_NUMBER_OF_CARDS);
            OutputController.printInitialCards(user);
        }
    }

    private void playTurns() {
        for (User user : users) {
            playTurnForOneUser(user);
        }
    }

    private void playTurnForOneUser(User user) {
        while (user.isGettingAdditionalCard()) {
            OutputController.printMessage(user.getMessageForAdditionalCard());
            user.addCard(deck, ADDITIONAL_NUMBER_OF_CARDS);
            OutputController.printHoldingCards(user);
        }
        OutputController.printHoldingCards(user);
    }

    private void showGameResult(GameResult gameResult) {
        OutputController.printGameResult(gameResult);
    }
}