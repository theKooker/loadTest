package src.dispatch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class Store implements Runnable {
    public String storeId;
    public String storeNumber;
    public String brandId = "957A3811-7110-4E79-10F3-34C6754CF8BB";// Constant brand id
    public int numberOrders;
    public int delayBetweenOrders, minDelay, maxDelay;
    public List<Order> orders;
    private Random rng = new Random(400);

    Store(String id, String number) {
        this.storeId = id;
        this.storeNumber = number;
        orders = new ArrayList<Order>();
    }

    public String toString() {
        return "store_id :" + this.storeId + ", store_number:" + this.storeNumber;
    }

    @Override
    public void run() {
        for (int i = 0; i < numberOrders; i++) {

            UUID uuid = UUID.randomUUID();
            Address address = Main.generateAddress();
            String orderJson = "{\"id\":\"" + uuid.toString()
                    + "\",\"isPaid\":true,\"remark\":null,\"courier\":\"loadTest\",\"version\":\"1.2\",\"customer\":{\"city\":\"AAH\",\"name\":\"Dont touch me\",\"street\":\""
                    + address.street + "\",\"postalCode\":\"" + address.postalCode
                    + "\",\"companyName\":null,\"phoneNumber\":\"1134250429\",\"streetNumber\":\"" + address.street
                    + "\",\"extraAddressInfo\":\"Per\u00fa 346\"},\"orderKey\":\"HITEW\",\"platform\":\"Menu\",\"products\":[{\"name\":\"Papas fritas\",\"count\":1,\"price\":\"150.00\",\"remark\":null,\"category\":null,\"sideDishes\":[]}],\"coordinates\":{\"latitude\":0,\"longitude\":0},\"clientKey\":\"clientKey\",\"orderDate\":\"2021-05-18 17:14:58\",\"orderType\":\"delivery\",\"totalPrice\":\"150.00\",\"restaurantId\":\""
                    + this.storeNumber
                    + "\",\"deliveryCosts\":\"0.00\",\"paymentMethod\":\"online\",\"totalDiscount\":0,\"publicReference\":\"HITEW\",\"requestedDeliveryTime\":\"2021-05-18 17:27:58\"}";
                    Request a = new Request("https://menu-ecs-service-dispatch-core-playground.menu.app/api/orders/inject",
                    orderJson);
            try {
                Order order = a.postOrder(this);
                if(order.orderId != null)
                this.orders.add(order);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                delayBetweenOrders = minDelay + rng.nextInt(maxDelay - minDelay + 1);
                System.out.println("for the store " + storeNumber + " please wait : " + delayBetweenOrders + "s");
                Thread.sleep(this.delayBetweenOrders * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

}
