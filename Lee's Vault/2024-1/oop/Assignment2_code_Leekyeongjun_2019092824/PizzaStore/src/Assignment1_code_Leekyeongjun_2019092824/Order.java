package Assignment1_code_Leekyeongjun_2019092824;

public abstract class Order {
	protected Pizza[] pizza;

	
	public void addPizza(Pizza pizza) {
		Pizza newPizza = new Pizza(pizza);
		if(this.pizza == null) { 
			// there was no pizzalist.
			this.pizza = new Pizza[1];
			this.pizza[0] = newPizza;
		}else {
			// there was pizzalist.
			int curCount = this.pizza.length+1;
			Pizza[] newPizzaList = new Pizza[curCount];
			for(int i = 0; i<this.pizza.length; i++) {
				newPizzaList[i] = new Pizza(this.pizza[i]);
			}newPizzaList[curCount-1] = newPizza;
			this.pizza = newPizzaList;
		}
		
	}
	
	public void removePizza(int index) {
		if(this.pizza == null) {
			System.out.println("error : There is No PizzaList.");
			System.exit(0);
		}
		if(this.pizza.length == 0) {
			System.out.println("error : There is No Pizza in PizzaList.");
			System.exit(0);
		}
		if(index > this.pizza.length) {
			System.out.println("error : Out of bound error.");
			System.exit(0);	
		}
		else {
			Pizza[] removedList = new Pizza[this.pizza.length-1];
			for(int i = 0; i<removedList.length; i++) {
				if(i < index) removedList[i] = new Pizza(this.pizza[i]);
				else if(i >= index) {/* Remove it!*/
					removedList[i] = new Pizza(this.pizza[i+1]);
				}
			}
			this.pizza = removedList;
			System.out.println("removed Pizza[" + index + "].");
 		}
	}
	
	abstract public double calculateOrderPrice();
	
	public String toString() {
		String ret;
		// ret = pizza.toString();
		ret = "Your total will be $" + calculateOrderPrice() + ".";
		return ret;
	}
}
