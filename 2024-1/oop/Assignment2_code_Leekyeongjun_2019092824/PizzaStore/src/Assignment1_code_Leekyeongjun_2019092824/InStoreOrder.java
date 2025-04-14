package Assignment1_code_Leekyeongjun_2019092824;

public class InStoreOrder extends Order {

	private int tableNumber;
	
	public InStoreOrder(int tableNumber) {
		super();
		this.tableNumber = tableNumber;
	}

	public double calculateOrderPrice() {
		double sum = 0;
		for(int i = 0; i<super.pizza.length; i++) {
			sum += super.pizza[i].getPrice();
		}
		return sum*1.15;
	}
	
	public String toString() {
		String ret = "InStore Order\n";
		
		for(int i = 0; i<super.pizza.length; i++) {
			ret += "Pizza["+i+"] : " + super.pizza[i].toString() + '\n';
		}ret += "Total Price : " + calculateOrderPrice() + '\n';
		ret += "TableNumber : " + tableNumber + '\n';
		return ret;
	}

}
