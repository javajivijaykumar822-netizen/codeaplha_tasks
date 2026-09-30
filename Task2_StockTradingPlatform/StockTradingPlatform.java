import java.util.*;

public class StockTradingPlatform {
    static class Stock {
        String symbol;
        String company;
        double price;

        Stock(String symbol, String company, double price) {
            this.symbol = symbol;
            this.company = company;
            this.price = price;
        }
    }

    static class Holding {
        String symbol;
        int quantity;
        double averageBuyPrice;

        Holding(String symbol, int quantity, double averageBuyPrice) {
            this.symbol = symbol;
            this.quantity = quantity;
            this.averageBuyPrice = averageBuyPrice;
        }
    }

    static Map<String, Stock> market = new LinkedHashMap<>();
    static Map<String, Holding> portfolio = new LinkedHashMap<>();
    static double cash = 100000.00;
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        loadStocks();

        while (true) {
            System.out.println("\n===== STOCK TRADING PLATFORM =====");
            System.out.printf("Available Cash: Rs. %.2f%n", cash);
            System.out.println("1. View Market");
            System.out.println("2. Buy Stock");
            System.out.println("3. Sell Stock");
            System.out.println("4. View Portfolio");
            System.out.println("5. Exit");
            System.out.print("Enter choice: ");

            int choice = readInt();

            switch (choice) {
                case 1 -> viewMarket();
                case 2 -> buyStock();
                case 3 -> sellStock();
                case 4 -> viewPortfolio();
                case 5 -> {
                    System.out.println("Thank you for using the platform.");
                    return;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    static void loadStocks() {
        market.put("TCS", new Stock("TCS", "Tata Consultancy Services", 3800));
        market.put("INFY", new Stock("INFY", "Infosys", 1750));
        market.put("RELIANCE", new Stock("RELIANCE", "Reliance Industries", 2900));
        market.put("HDFC", new Stock("HDFC", "HDFC Bank", 1800));
        market.put("WIPRO", new Stock("WIPRO", "Wipro", 550));
    }

    static void viewMarket() {
        System.out.println("\n----- MARKET -----");
        System.out.printf("%-12s %-30s %-12s%n", "Symbol", "Company", "Price");

        for (Stock s : market.values()) {
            System.out.printf("%-12s %-30s Rs. %-10.2f%n",
                    s.symbol, s.company, s.price);
        }
    }

    static void buyStock() {
        viewMarket();
        System.out.print("Enter stock symbol: ");
        String symbol = sc.nextLine().toUpperCase();

        Stock stock = market.get(symbol);
        if (stock == null) {
            System.out.println("Stock not found.");
            return;
        }

        System.out.print("Enter quantity: ");
        int quantity = readInt();

        if (quantity <= 0) {
            System.out.println("Quantity must be positive.");
            return;
        }

        double cost = stock.price * quantity;

        if (cost > cash) {
            System.out.println("Insufficient cash.");
            return;
        }

        Holding old = portfolio.get(symbol);

        if (old == null) {
            portfolio.put(symbol, new Holding(symbol, quantity, stock.price));
        } else {
            int newQuantity = old.quantity + quantity;
            double totalCost =
                    (old.quantity * old.averageBuyPrice) + cost;
            old.quantity = newQuantity;
            old.averageBuyPrice = totalCost / newQuantity;
        }

        cash -= cost;

        System.out.printf("Bought %d shares of %s for Rs. %.2f%n",
                quantity, symbol, cost);
    }

    static void sellStock() {
        if (portfolio.isEmpty()) {
            System.out.println("Portfolio is empty.");
            return;
        }

        viewPortfolio();
        System.out.print("Enter stock symbol: ");
        String symbol = sc.nextLine().toUpperCase();

        Holding holding = portfolio.get(symbol);

        if (holding == null) {
            System.out.println("You do not own this stock.");
            return;
        }

        System.out.print("Enter quantity to sell: ");
        int quantity = readInt();

        if (quantity <= 0 || quantity > holding.quantity) {
            System.out.println("Invalid quantity.");
            return;
        }

        double currentPrice = market.get(symbol).price;
        double proceeds = currentPrice * quantity;

        cash += proceeds;
        holding.quantity -= quantity;

        if (holding.quantity == 0) {
            portfolio.remove(symbol);
        }

        System.out.printf("Sold %d shares of %s for Rs. %.2f%n",
                quantity, symbol, proceeds);
    }

    static void viewPortfolio() {
        System.out.println("\n----- PORTFOLIO -----");

        if (portfolio.isEmpty()) {
            System.out.println("No stocks in portfolio.");
            return;
        }

        double totalInvestment = 0;
        double currentValue = 0;

        System.out.printf("%-10s %-10s %-15s %-15s%n",
                "Symbol", "Qty", "Buy Avg", "Current Value");

        for (Holding h : portfolio.values()) {
            double currentPrice = market.get(h.symbol).price;
            double investment = h.quantity * h.averageBuyPrice;
            double value = h.quantity * currentPrice;

            totalInvestment += investment;
            currentValue += value;

            System.out.printf("%-10s %-10d Rs. %-10.2f Rs. %-10.2f%n",
                    h.symbol, h.quantity, h.averageBuyPrice, value);
        }

        double profitLoss = currentValue - totalInvestment;

        System.out.printf("%nTotal Invested : Rs. %.2f%n", totalInvestment);
        System.out.printf("Current Value  : Rs. %.2f%n", currentValue);
        System.out.printf("Profit/Loss    : Rs. %.2f%n", profitLoss);
    }

    static int readInt() {
        while (true) {
            try {
                return Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Enter a valid number: ");
            }
        }
    }
}
