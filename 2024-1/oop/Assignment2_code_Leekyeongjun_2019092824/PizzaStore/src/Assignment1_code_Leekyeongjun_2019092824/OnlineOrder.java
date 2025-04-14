package Assignment1_code_Leekyeongjun_2019092824;

public class OnlineOrder extends Order {

	private String deliveryAddress;
	
	public OnlineOrder(String address) {
		super();
		deliveryAddress = address;
	}
	
	@Override
	public double calculateOrderPrice() {
		double sum = 0;
		for(int i = 0; i<super.pizza.length; i++) {
			sum += super.pizza[i].getPrice();
		}
		return sum+3;
	}
	
	public String toString() {
		String ret = "Online Order\n";
		
		for(int i = 0; i<super.pizza.length; i++) {
			ret += "Pizza["+i+"] : " + super.pizza[i].toString() + '\n';
		}ret += "Total Price : " + calculateOrderPrice() + '\n';
		ret += "Address : " + deliveryAddress + '\n';
		return ret;
	}

}
