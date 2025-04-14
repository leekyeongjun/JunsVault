package Assignment1_code_Leekyeongjun_2019092824;
import java.util.Scanner;

public class MainClass {
	
	public void showErrorMsg(int errcode) {
		String msg = "";
		if(errcode == 0) {
			// Number error.(repeatable)
			// if size has unavailable size, than this message appears.
			msg = "Unavailable Size, Please Retry.";
		}
		else if(errcode == 1) {
			// Command error.(repeatable)
			// if user has typed unavailable command, than this message appears.
			msg = "Unavailable Command, Please Retry.";
		}
		else if(errcode == 2){
			// Ingredient error.(repeatable)
			// if there is no Ingredients in Pizza, than this message appears.
			msg = "Pizza has no Ingredients. Please Retry.";
		}
		else if(errcode == 3) {
			System.out.println("[Error] : Invalid Command");
			System.exit(0);
		}
		else {
			// Unknown error.
			// This should not be appeared.
			msg = "ErrorCode Index error.";
		}
		System.out.println("[Error] : " + msg);
	}
	
	public void selectAction(PizzaStore store, Scanner input) {
		
		int cmd = 0;
		System.out.println(store.toString());
		System.out.println("What would you like to do:");
		System.out.println("1: Place an order, 2: buy ingredients");
		
		cmd = input.nextInt();
		input.nextLine();
		
		while(!(cmd == 1 || cmd == 2)) {
			showErrorMsg(1);
			cmd = input.nextInt();
			input.nextLine();
		}
		
		if(cmd == 1) {
			placeOrder(store, input);
		}
		else if(cmd == 2) {
			buyIngredients(store, input);
		}

	}
	
	public void placeOrder(PizzaStore store, Scanner input) {
		int finished = 0;
		String type;
		System.out.println("What type of order?");
		System.out.println("1: In store, 2: Online, 3: back");
		type = input.nextLine();
		if(type.equals("3")) {
			finished = -1;
		}
		
		else {
			store.createOrder(type);
		}
		
		while(finished == 0) {
		    boolean hasPep= false, hasMus = false, hasChe = false;
		    System.out.println("What size pizza do you want?");
		    int size;
		    size = input.nextInt();
		    input.nextLine();
		    
		    while(size < 0) {
		    	showErrorMsg(0);
		        size = input.nextInt();
		        input.nextLine(); // flush nextLine after nextInt();
		    }
		    
		    hasPep = selectIngredient(store, input, "peperoni");
		    hasMus = selectIngredient(store, input, "mushrooms");
		    hasChe = selectIngredient(store, input, "cheese");
		    
		    
		    store.AddPizzaToOrder(size, hasPep, hasMus, hasChe);
		    
		    String cmd;
		    System.out.println("Do you want to order another Pizza? (Y/N)");
		    cmd = input.nextLine();
		    if(cmd.equals("Y")) {
		    	finished = 0;
		    }else if(cmd.equals("N")) {
		    	finished = 1;
		    }
		}
		// normal case
		if(finished == 1) {
			System.out.println("Your final order is :");
			System.out.println(store.toString());
			System.out.println("Do you want to change your order? (Y/N)");
			String cmd2 = input.nextLine();
			if(cmd2.equals("Y")) {
				changeOrder(store, input);
			}
			else if(cmd2.equals("N")){
				store.addCash();
			}
			else {
				showErrorMsg(3);
			}
		}
	}
	public void changeOrder(PizzaStore store, Scanner input) {
		boolean finished = false;
		while(finished == false) {
			String cmd;
			System.out.println("What do you want to do?");
			System.out.println("1: Add a pizza, 2: Remove a pizza, 3: Nothing");
			cmd = input.nextLine();
			
			if(cmd.equals("1")) {
			    int size;
			    boolean hasPep= false, hasMus = false, hasChe = false;
			    
			    System.out.println("What size pizza do you want?");
			    
			    size = input.nextInt();
			    
			    input.nextLine(); // flush nextLine after nextInt();
			    while(size < 0) {
			    	showErrorMsg(0);
			        size = input.nextInt();
			        input.nextLine(); // flush nextLine after nextInt();
			    }
			    
			    hasPep = selectIngredient(store, input, "peperoni");
			    hasMus = selectIngredient(store, input, "mushrooms");
			    hasChe = selectIngredient(store, input, "cheese");
			    
			    store.AddPizzaToOrder(size, hasPep, hasMus, hasChe);
			}
			else if(cmd.equals("2")) {
				System.out.println("Which pizza do you want to remove?");
				int index;
				index = input.nextInt();
				input.nextLine();
				store.removeCurrentOrder(index);
			}
			else if(cmd.equals("3")) {
				finished = true;
			}
			else {
				showErrorMsg(3);
			}
		}
		
	}
	public boolean selectIngredient(PizzaStore store, Scanner input, String label) {
	    String cmd;
	    System.out.println("Do you want "+ label +" on your pizza? Y/N");
	    cmd = input.nextLine();
	    while(!(cmd.toUpperCase().equals("Y") || cmd.toUpperCase().equals("N"))) {
	    	showErrorMsg(1);
	        cmd = input.nextLine();
	    }
	    
	    if(cmd.toUpperCase().equals("Y")) {
	        return true;
	    }else return false;
	    
	}

	public void buyIngredients(PizzaStore store, Scanner input) {
		int cmd;
		
		System.out.println("What ingredients do you want to buy?");
		System.out.println("1: peperoni, 2: mushrooms, 3: cheese, 4: none.");
		
		cmd = input.nextInt();
		while(cmd < 1 || cmd >= 5) {
			showErrorMsg(1);
			cmd = input.nextInt();
			input.nextLine();
		}
		
		
		if(cmd < 4) {
			int amount = 0;
			System.out.println("How much would you like to buy?");
			amount = input.nextInt();
			input.nextLine();
			if(cmd == 1) {
				store.restockPeperoni(amount);
			}
			if(cmd == 2) {
				store.restockMushrooms(amount);
			}
			if(cmd == 3) {
				store.restockCheese(amount);
			}
		}

		if(cmd == 4) {
			store.addCash(1);
		}
		
	}
	public static void main(String[] args) {
		Scanner scanner =  new Scanner(System.in);
		PizzaStore myStore = new PizzaStore();
		MainClass mainClassInstance = new MainClass();
		
		while(true) {
			mainClassInstance.selectAction(myStore, scanner);
		}
	
	}
}
