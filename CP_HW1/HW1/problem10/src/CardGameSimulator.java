

public class CardGameSimulator {
	private static final Player[] players = new Player[2];

	public static void simulateCardGame(String inputA, String inputB) {
		char currentLetter, currentNumber;
		Card[] nextDeck;

		Card tmpCard;
		players[0] = new Player(inputA, "A");
		players[1] = new Player(inputB, "B");
		nextDeck = players[0].getDeck();
		tmpCard = nextDeck[0];

		for (int i = 1; i<10; i++) {
			if(nextDeck[i].isUsed) {
				continue;
			}
			if(nextDeck[i].getNumber() > tmpCard.getNumber()) {
				tmpCard = nextDeck[i];
			}
			else if(nextDeck[i].getNumber()==tmpCard.getNumber() && nextDeck[i].getLetter()<tmpCard.getLetter()) {
				tmpCard = nextDeck[i];
			}
		}
		tmpCard.setUsed();
		players[0].deckCount--;
		currentLetter = tmpCard.getLetter();
		currentNumber = tmpCard.getNumber();

		players[0].playCard(tmpCard);
		int player = 1;

		while(true) {
			tmpCard = new Card('0', (char)('D'+1));
			nextDeck = players[player].getDeck();

			for(Card card:nextDeck) {
				if(card.isUsed) continue;
				if(card.getNumber()==currentNumber && card.getLetter() < tmpCard.getLetter() ) {
					tmpCard = card;
				}
			}
			if(tmpCard.getNumber()=='0') {
				for(Card card:nextDeck) {
					if(card.isUsed) continue;
					if(card.getLetter()==currentLetter && card.getNumber() > tmpCard.getNumber() ) {
						tmpCard = card;
					}
				}
			}

			if(tmpCard.getNumber()=='0') {
				CardGameSimulator.printLoseMessage(players[player]);
				break;
			}

			else {
				tmpCard.setUsed();
				players[player].deckCount--;
				currentLetter = tmpCard.getLetter();
				currentNumber = tmpCard.getNumber();
				players[player].playCard(tmpCard);
				player = 1-player;
			}
		}
		System.out.println();
	}

	private static void printLoseMessage(Player player) {
		System.out.printf("Player %s loses the game!\n", player);
	}
}

class Player {
	private String name;
	private Card[] deck;
	public int deckCount;
	Player(String input, String name) {
		this.name = name;
		this.deck = new Card[10];
		deckCount = 10;
		String[] parsedCard = input.split(" ");
		int i = 0;
		for (String card: parsedCard) {
			this.deck[i] = new Card(card.charAt(0), card.charAt(1));
			i++;
		}
	}
	public void playCard(Card card) {
		System.out.printf("Player %s: %s\n", name, card);
	}

	public Card[] getDeck() {
		return deck;
	}

	@Override
	public String toString() {
		return name;
	}
}

class Card {
	private char number; // int
	private char letter;
	public boolean isUsed;
	Card(char number, char letter) {
		this.number = number;
		this.letter = letter;
		this.isUsed = false;
	}

	public char getNumber() {
		return number;
	}

	public char getLetter() {
		return letter;
	}

	public void setUsed() {
		isUsed = true;
	}

	@Override
	public String toString() {
		return "" + number + letter;
	}
}
