import java.util.*;

enum Suit {
    HEARTS("♥"), DIAMONDS("♦"), CLUBS("♣"), SPADES("♠");
    
    private final String symbol;
    
    Suit(String symbol) {
        this.symbol = symbol;
    }
    
    public String getSymbol() {
        return symbol;
    }
}

enum Rank {
    TWO("2", 2), THREE("3", 3), FOUR("4", 4), FIVE("5", 5),
    SIX("6", 6), SEVEN("7", 7), EIGHT("8", 8), NINE("9", 9),
    TEN("10", 10), JACK("J", 10), QUEEN("Q", 10), KING("K", 10),
    ACE("A", 11); // ACE может быть 1 или 11
    
    private final String name;
    private final int value;
    
    Rank(String name, int value) {
        this.name = name;
        this.value = value;
    }
    
    public String getName() { return name; }
    public int getValue() { return value; }
}

class Card {
    private final Suit suit;
    private final Rank rank;
    
    public Card(Suit suit, Rank rank) {
        this.suit = suit;
        this.rank = rank;
    }
    
    public Suit getSuit() { return suit; }
    public Rank getRank() { return rank; }
    public int getValue() { return rank.getValue(); }
    
    @Override
    public String toString() {
        return rank.getName() + suit.getSymbol();
    }
}

class Deck {
    private Deque<Card> cards;
    
    public Deck() {
        cards = new ArrayDeque<>();
        initializeDeck();
    }
    
    private void initializeDeck() {
        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                cards.add(new Card(suit, rank));
            }
        }
    }
    
    public void shuffle() {
        List<Card> cardList = new ArrayList<>(cards);
        Collections.shuffle(cardList);
        cards = new ArrayDeque<>(cardList);
    }
    
    public Card drawCard() {
        return cards.poll();
    }
    
    public boolean isEmpty() {
        return cards.isEmpty();
    }
    
    public int size() {
        return cards.size();
    }
}

class Player {
    private String name;
    private List<Card> hand;
    private int score;
    private int totalWins;
    
    public Player(String name) {
        this.name = name;
        this.hand = new ArrayList<>();
        this.score = 0;
        this.totalWins = 0;
    }
    
    public String getName() { return name; }
    public List<Card> getHand() { return hand; }
    public int getScore() { return score; }
    public int getTotalWins() { return totalWins; }
    
    public void addCard(Card card) {
        hand.add(card);
        calculateScore();
    }
    
    public void clearHand() {
        hand.clear();
        score = 0;
    }
    
    public void addWin() {
        totalWins++;
    }
    
    private void calculateScore() {
        score = 0;
        int aceCount = 0;
        
        // Считаем все карты, кроме тузов
        for (Card card : hand) {
            if (card.getRank() == Rank.ACE) {
                aceCount++;
            } else {
                score += card.getValue();
            }
        }
        
        // Добавляем тузы
        for (int i = 0; i < aceCount; i++) {
            if (score + 11 <= 21) {
                score += 11;
            } else {
                score += 1;
            }
        }
    }
    
    public boolean isBusted() {
        return score > 21;
    }
    
    public boolean hasBlackjack() {
        return score == 21 && hand.size() == 2;
    }
    
    @Override
    public String toString() {
        return name + " (Очки: " + score + ", Побед: " + totalWins + ")";
    }
    
    public String showHand(boolean showAll) {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append(": ");
        if (showAll) {
            for (Card card : hand) {
                sb.append(card).append(" ");
            }
            sb.append("[").append(score).append(" очков]");
        } else {
            // Для дилера - первая карта скрыта
            sb.append("[Скрыто] ");
            for (int i = 1; i < hand.size(); i++) {
                sb.append(hand.get(i)).append(" ");
            }
        }
        return sb.toString();
    }
}

public class BlackjackGame {
    private Deck deck;
    private List<Player> players;
    private Player dealer;
    private Scanner scanner;
    private Map<String, Integer> leaderboard;
    
    public BlackjackGame() {
        deck = new Deck();
        players = new ArrayList<>();
        dealer = new Player("Дилер");
        scanner = new Scanner(System.in);
        leaderboard = new HashMap<>();
    }
    
    public void run() {
        System.out.println("=== ДОБРО ПОЖАЛОВАТЬ В БЛЭКДЖЕК! ===\n");
        
        setupPlayers();
        
        while (true) {
            System.out.println("\n=== НОВЫЙ РАУНД ===");
            playRound();
            
            System.out.print("\nХотите сыграть еще раз? (да/нет): ");
            String choice = scanner.nextLine().toLowerCase();
            if (!choice.equals("да")) {
                break;
            }
            
            // Перемешиваем колоду, если осталось мало карт
            if (deck.size() < 20) {
                deck = new Deck();
                deck.shuffle();
                System.out.println("Колода перемешана!");
            }
        }
        
        showFinalResults();
    }
    
    private void setupPlayers() {
        System.out.print("Введите количество игроков (1-4): ");
        int playerCount = scanner.nextInt();
        scanner.nextLine(); // consume newline
        
        for (int i = 1; i <= playerCount; i++) {
            System.out.print("Введите имя игрока " + i + ": ");
            String name = scanner.nextLine();
            Player player = new Player(name);
            players.add(player);
            leaderboard.put(name, 0);
        }
        
        deck.shuffle();
        System.out.println("\nИгроки созданы! Колода перемешана.");
    }
    
    private void playRound() {
        // Очищаем руки
        dealer.clearHand();
        for (Player player : players) {
            player.clearHand();
        }
        
        // Раздача первых двух карт
        System.out.println("\n=== РАЗДАЧА КАРТ ===");
        for (Player player : players) {
            player.addCard(deck.drawCard());
            player.addCard(deck.drawCard());
            System.out.println(player.showHand(true));
        }
        
        dealer.addCard(deck.drawCard());
        dealer.addCard(deck.drawCard());
        System.out.println(dealer.showHand(false));
        
        // Ходы игроков
        for (Player player : players) {
            playerTurn(player);
        }
        
        // Ход дилера
        dealerTurn();
        
        // Определение победителей
        determineWinners();
    }
    
    private void playerTurn(Player player) {
        System.out.println("\n=== ХОД " + player.getName().toUpperCase() + " ===");
        
        while (true) {
            System.out.println(player.showHand(true));
            
            if (player.hasBlackjack()) {
                System.out.println("БЛЭКДЖЕК!");
                break;
            }
            
            if (player.isBusted()) {
                System.out.println("ПЕРЕБОР!");
                break;
            }
            
            System.out.print("1. Взять карту\n2. Остановиться\nВыберите действие: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline
            
            if (choice == 1) {
                Card card = deck.drawCard();
                player.addCard(card);
                System.out.println("Вы взяли: " + card);
                
                if (player.isBusted()) {
                    System.out.println("ПЕРЕБОР! " + player.getScore() + " очков");
                    break;
                }
            } else {
                System.out.println("Вы остановились на " + player.getScore() + " очках");
                break;
            }
        }
    }
    
    private void dealerTurn() {
        System.out.println("\n=== ХОД ДИЛЕРА ===");
        System.out.println(dealer.showHand(true));
        
        // Дилер берет карты, пока у него меньше 17
        while (dealer.getScore() < 17 && !dealer.isBusted()) {
            Card card = deck.drawCard();
            dealer.addCard(card);
            System.out.println("Дилер берет: " + card);
            System.out.println(dealer.showHand(true));
            
            try {
                Thread.sleep(1000); // Пауза для драматизма
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        
        if (dealer.isBusted()) {
            System.out.println("Дилер ПЕРЕБРАЛ!");
        } else {
            System.out.println("Дилер остановился на " + dealer.getScore() + " очках");
        }
    }
    
    private void determineWinners() {
        System.out.println("\n=== РЕЗУЛЬТАТЫ РАУНДА ===");
        
        int dealerScore = dealer.getScore();
        boolean dealerBusted = dealer.isBusted();
        
        for (Player player : players) {
            int playerScore = player.getScore();
            boolean playerBusted = player.isBusted();
            boolean playerBlackjack = player.hasBlackjack();
            
            System.out.print(player.getName() + ": " + playerScore + " очков - ");
            
            if (playerBusted) {
                System.out.println("ПРОИГРАЛ (перебор)");
            } else if (playerBlackjack && !dealer.hasBlackjack()) {
                System.out.println("ВЫИГРАЛ (блэкджек)!");
                player.addWin();
                leaderboard.put(player.getName(), leaderboard.get(player.getName()) + 3);
            } else if (dealerBusted) {
                System.out.println("ВЫИГРАЛ (дилер перебрал)!");
                player.addWin();
                leaderboard.put(player.getName(), leaderboard.get(player.getName()) + 2);
            } else if (playerScore > dealerScore) {
                System.out.println("ВЫИГРАЛ!");
                player.addWin();
                leaderboard.put(player.getName(), leaderboard.get(player.getName()) + 2);
            } else if (playerScore == dealerScore) {
                System.out.println("НИЧЬЯ!");
                leaderboard.put(player.getName(), leaderboard.get(player.getName()) + 1);
            } else {
                System.out.println("ПРОИГРАЛ");
            }
        }
        
        showLeaderboard();
    }
    
    private void showLeaderboard() {
        System.out.println("\n=== ТАБЛИЦА ЛИДЕРОВ ===");
        
        List<Map.Entry<String, Integer>> sorted = new ArrayList<>(leaderboard.entrySet());
        sorted.sort((a, b) -> b.getValue().compareTo(a.getValue()));
        
        for (Map.Entry<String, Integer> entry : sorted) {
            System.out.printf("%-15s: %d очков%n", entry.getKey(), entry.getValue());
        }
    }
    
    private void showFinalResults() {
        System.out.println("\n=== ИТОГОВЫЕ РЕЗУЛЬТАТЫ ===");
        System.out.println("Спасибо за игру!\n");
        
        for (Player player : players) {
            System.out.println(player.getName() + 
                " - Побед: " + player.getTotalWins() + 
                ", Очков в лидерборде: " + leaderboard.get(player.getName()));
        }
        
        // Определяем чемпиона
        String champion = Collections.max(leaderboard.entrySet(), 
            Map.Entry.comparingByValue()).getKey();
        System.out.println("\n🏆 ЧЕМПИОН: " + champion + " 🏆");
    }
    
    public static void main(String[] args) {
        BlackjackGame game = new BlackjackGame();
        game.run();
    }
}
