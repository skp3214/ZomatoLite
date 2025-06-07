import models.*;
import strategies.*;

public class Main {
    public static void main(String[] args) {
        // Simulating a happy flow
        // Create ZomatoLite App Object
        ZomatoLite zomato = new ZomatoLite();

        // Simulate a user coming in (Happy Flow)
        User user = new User(1, "", "");
        System.out.print("Enter your name: ");
        String name = System.console().readLine();
        user.setName(name);
        System.out.print("Enter your address: ");
        String address = System.console().readLine();
        user.setAddress(address);
        System.out.println("User: " + user.getName() + " is active.");

        // User searches for restaurants by location
        boolean isValidLocation = false;
        String location = "";
        java.util.List<Restaurant> restaurantList = null;
        while (!isValidLocation) {

            System.out.print("Enter your location to search for restaurants: ");
            location = System.console().readLine();
            restaurantList = zomato.searchRestaurants(location);

            if (restaurantList.isEmpty()) {
                System.out.println("No restaurants found! You can enter Delhi, Kolkata, or Chennai.");
                isValidLocation = false;
            } else {
                isValidLocation = true;
            }
        }

        System.out.println("Found Restaurants:");
        int serialNumber = 1;

        // Displaying the list of restaurants
        for (Restaurant restaurant : restaurantList) {
            System.out.println(serialNumber++ + ". " + restaurant.getName() + " - " + restaurant.getLocation());
        }
        System.out.println("Please select a restaurant by entering the serial number:");
        int selectedRestaurantIndex = Integer.parseInt(System.console().readLine()) - 1;
        if (selectedRestaurantIndex < 0 || selectedRestaurantIndex >= restaurantList.size()) {
            System.out.println("Invalid selection. Please try again.");
            return;
        }

        // User selects a restaurant
        zomato.selectRestaurant(user, restaurantList.get(selectedRestaurantIndex));
        System.out.println("Selected restaurant: " + restaurantList.get(selectedRestaurantIndex).getName());

        // Displaying the menu of the selected restaurant
        System.out.println("Menu of " + restaurantList.get(selectedRestaurantIndex).getName() + ":");
        for (MenuItem item : restaurantList.get(selectedRestaurantIndex).getMenu()) {
            System.out.println(item.getCode() + ": " + item.getName() + " - Rs: " + item.getPrice());
        }
        System.out.println("Please enter the item codes you want to add to the cart (comma separated):");
        String[] itemCodes = System.console().readLine().toUpperCase().split(",");
        for (String itemCode : itemCodes) {
            zomato.addToCart(user, itemCode.trim());
        }
        System.out.println("Items added to cart successfully!");

        // Displaying the cart items

        zomato.printUserCart(user);

        // Choose a payment strategy
        System.out.println("Choose a payment method: 1. UPI 2. Credit Card");
        int paymentChoice = Integer.parseInt(System.console().readLine());
        PaymentStrategy paymentStrategy = null;
        if (paymentChoice == 1) {
            System.out.print("Enter your UPI ID: ");
            String upiId = System.console().readLine();
            paymentStrategy = new UpiPaymentStrategy(upiId);
        } else if (paymentChoice == 2) {
            System.out.print("Enter your credit card number: ");
            String cardNumber = System.console().readLine();
            paymentStrategy = new CreditCardPaymentStrategy(cardNumber);
        } else {
            System.out.println("Invalid choice. Please try again.");
            return;
        }

        // User checkout the cart
        Order order = zomato.checkoutNow(user, "Delivery", paymentStrategy);

        // User pays for the cart. If payment is successful, notification is sent.
        zomato.payForOrder(user, order);

        return;
    }
}
