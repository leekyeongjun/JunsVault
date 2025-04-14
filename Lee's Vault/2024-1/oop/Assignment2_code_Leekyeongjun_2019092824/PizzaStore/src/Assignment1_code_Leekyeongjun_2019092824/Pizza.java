package Assignment1_code_Leekyeongjun_2019092824;

public class Pizza {
	private int size;
	private boolean hasPeperoni;
	private boolean hasMushrooms;
	private boolean hasCheese;
	
	Pizza(int _size, boolean _hasPeperoni, boolean _hasMushrooms, boolean _hasCheese){

        size = _size;
        hasPeperoni = _hasPeperoni;
        hasMushrooms = _hasMushrooms;
        hasCheese = _hasCheese;

        if(size <= 0) {
            System.out.println("error! : Pizza has Invalid Size : " + size);
            System.exit(0);
        }
	}
	
	public Pizza(Pizza originPizza) {
		size = originPizza.size;
		hasPeperoni = originPizza.hasPeperoni;
		hasMushrooms = originPizza.hasMushrooms;
		hasCheese = originPizza.hasCheese;
	}
	
	public String toString() {
		String ret;
		
		// ret example : "[Size = 28 / Ingredients = Peperoni Mushrooms ]"
	
		ret = "[Size = " + size + " / Ingredients = ";
		if(hasPeperoni) ret += "Peperoni ";
		if(hasMushrooms) ret += "Mushrooms ";
		if(hasCheese) ret += "Cheese";
		ret += "]";
		return ret;
	}
	
	public double getPrice() {
		double offset = 0;
		
		if(hasPeperoni) offset ++;
		if(hasMushrooms) offset ++;
		if(hasCheese) offset ++;
		
		double price = size*0.25*((100-(10*offset))/100);
		return price;
	}
	
}
