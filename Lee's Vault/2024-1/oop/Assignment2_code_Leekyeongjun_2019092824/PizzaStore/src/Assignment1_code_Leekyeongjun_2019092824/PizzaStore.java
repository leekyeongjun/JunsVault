package Assignment1_code_Leekyeongjun_2019092824;
import java.util.Scanner;

public class PizzaStore {
	private double cash;
	private Order currentOrder;
	private int peperoniStock;
	private int mushroomStock;
	private int cheeseStock;
	
	private static int tableNumber = 1;
	
	PizzaStore() {
		cash = 2.5;
		peperoniStock = 3;
		mushroomStock = 3;
		cheeseStock = 3;
	}
	
	public void addCash(double amount) {
		if(amount >= 0) cash += amount;
	}
	
	public void addCash() {
		if(currentOrder != null) {
			cash += currentOrder.calculateOrderPrice();
			currentOrder = null;
		}
	}
	
	public void removeCurrentOrder(int index) {
		if(currentOrder != null) {
			currentOrder.removePizza(index);
		}
	}
	
	
	public void createOrder(String type) {
		Scanner scanner =  new Scanner(System.in);
		if(type.equals("2")) {
			System.out.println("Chosen : Online");
			System.out.println("What is delivery address?");
			String address = scanner.nextLine();
			OnlineOrder onlineorder = new OnlineOrder(address);
			currentOrder = onlineorder;
		}
		else if(type.equals("1")) {
			System.out.println("Chosen : In Store");
			InStoreOrder instoreorder = new InStoreOrder(tableNumber);
			tableNumber++;
			currentOrder = instoreorder;
		}
		else {
			System.out.println("Error : Invalid Order type");
			scanner.close();
			System.exit(0);
		}
	}
	
	public void AddPizzaToOrder(int pizzaSize, boolean hasPeperoni, boolean hasMushrooms, boolean hasCheese) {
		boolean availableOrder = true;
		int tmpPep = peperoniStock;
		int tmpMus = mushroomStock;
		int tmpChe = cheeseStock;
		
		if(hasPeperoni && availableOrder) {
			if(peperoniStock >= 1){peperoniStock -= 1;}
			else availableOrder = false;
		}
			
		if(hasMushrooms && availableOrder) {
			if(mushroomStock >= 1) {mushroomStock -= 1;} 
			else availableOrder = false;
		}
			
		if(hasCheese && availableOrder) {
			if(cheeseStock >= 1) {cheeseStock -= 1;}
			else availableOrder = false;
		}
		
		if(availableOrder) {
			Pizza newpizza = new Pizza(pizzaSize, hasPeperoni, hasMushrooms, hasCheese);
			currentOrder.addPizza(newpizza);
		}
		else {
			System.out.println("The order is Not Available.");
			peperoniStock = tmpPep;
			mushroomStock = tmpMus;
			cheeseStock = tmpChe;
		}
	}

	public void restockPeperoni(int amount) {
		double price = amount * 1;
		if(cash >= price) {
			cash -= price;
			peperoniStock += amount;
			System.out.println(amount + " of peperoni purchased. Your current Cash is " + cash);
		}
		else {
			System.out.println("Not enough Cash. Your current cash is " + cash + " and the price is " + price );
		}
	}
	
	public void restockMushrooms(int amount) {
		double price = amount * 1.5;
		if(cash >= price) {
			cash -= price;
			mushroomStock += amount;
			System.out.println(amount + " of mushrooms purchased. Your current Cash is " + cash);
		}
		else {
			System.out.println("Not enough Cash. Your current cash is " + cash + " and the price is " + price );
		}
	}
	
	public void restockCheese(int amount) {
		double price = amount * .75;
		if(cash >= price) {
			cash -= price;
			cheeseStock += amount;
			System.out.println(amount + " of peperoni purchased. Your current Cash is " + cash);
		}
		else {
			System.out.println("Not enough Cash. Your current cash is " + cash + " and the price is " + price );
		}
	}
	
	public String toString() {
		if(currentOrder != null) {
			return currentOrder.toString();
		}
		else{
			String ret = "PizzaStore : cash: $" + cash + ", peperoni:" + peperoniStock + ", mushrooms:" + mushroomStock+ ", cheeses:" + cheeseStock +".";
			return ret;
		}
	}
}
