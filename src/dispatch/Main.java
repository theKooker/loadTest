package src.dispatch;

import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.lang.Thread;
import java.util.Random;

class Main {

    public static void main(String[] args) throws IOException {
        System.out.println("****Welcome to the load test of MENU DISPATCH****");
        int rcount, ocount, minDelay, maxDelay, aDelay;
        Scanner sc = new Scanner(System.in);
        System.out.println("Please give the number of stores to create :");
        rcount = sc.nextInt();
        List<Store> listOfStores = new ArrayList<Store>();
        List<Thread> storeThreads = new ArrayList<Thread>();

        System.out.println("Creating Restaurants ...");
        for (int i = 0; i < rcount; i++) {
            String storeNumber = Integer.toString(i);
            String storeName = "LoadTest-" + storeNumber;
            String storeData = "{\"name\":\"" + storeName
                    + "\",\"contactEmail\":\"test@test.de\",\"brandId\":\"957A3811-7110-4E79-10F3-34C6754CF8BB\",\"brandName\":\"Test\",\"addressLineOne\":\"Radeberger Weg 10\",\"addressLineTwo\":\"Radeberger Weg 10\",\"country\":\"0D73EDEA-E2D8-6B52-4FA3-539117244CBB\",\"city\":\"Garching bei M\u00fcnchen\",\"postcode\":\"85748\",\"storeNumber\":\""
                    + storeNumber + "\",\"lieferandoId\":\"" + storeNumber
                    + "\",\"endOfDayTime\":\"0:0\",\"defaultLanguage\":\"English\",\"soundInterval\":\"5\",\"ringtoneId\":\"F07D7A00-5E1D-0EC2-3B2B-4FD4F9E8C4E9\",\"maxDeliveryTime\":30,\"standardProductionTime\":\"50\",\"defaultCurrency\":\"EUR\",\"street\":\"Radeberger Weg 10\",\"authCode\":\"ovnNRu2TOmDXmBbVs72mgTsJRd2H27CuiY4X\",\"latitude\":0,\"longitude\":0}";
            Request a = new Request("https://menu-ecs-service-dispatch-core-playground.menu.app/api/stores", storeData);
            System.out.println(storeData);
            System.out.println(a.post(listOfStores));
            Thread th = new Thread(listOfStores.get(i));
            storeThreads.add(th);
        }
        System.out.println(" ->Restaurants have been created.");
        System.out.println("Creating drivers for Restaurants....");
        for (int i = 0; i < listOfStores.size(); i++) {
            for (int j = 0; j < 2; j++) {
                Map<String, String> params = new HashMap<>();
                listOfStores.get(i).storeNumber = getRideOfEnd(listOfStores.get(i).storeNumber);
                listOfStores.get(i).storeId = getRideOfEnd(listOfStores.get(i).storeId);
                listOfStores.get(i).brandId = getRideOfEnd(listOfStores.get(i).brandId);
                params.put("name", "Driver" + getRideOfEnd(listOfStores.get(i).storeNumber) + j);
                params.put("store_id", getRideOfEnd(listOfStores.get(i).storeId));
                params.put("brand_id", getRideOfEnd(listOfStores.get(i).brandId));
                StringBuilder postData = new StringBuilder();
                for (Map.Entry<String, String> param : params.entrySet()) {
                    if (postData.length() != 0) {
                        postData.append('&');
                    }
                    postData.append(param.getKey());
                    postData.append('=');
                    postData.append(param.getValue());
                }
                System.out.println(postData.toString());
                Request an = new Request("https://menu-ecs-service-dispatch-core-playground.menu.app/api/drivers?"
                        + postData.toString());
                System.out.println(an.postDriver());
            }

        }
        System.out.println("Driver has been created successfully !");
        System.out.println("Creating the orders for each store simultanily...");
        Random rng = new Random(400);//seed to generate the random time
        System.out.println("Please give the number of orders to create for each store :");
        ocount = sc.nextInt();
        System.out.println("Please give max delay between orders (in seconds) :");
        maxDelay = sc.nextInt();
        System.out.println("Please give min delay between orders (in seconds) :");
        minDelay = sc.nextInt();
        System.out.println("Please give auto-accept delay between orders (in minutes)  :");
        aDelay = sc.nextInt();
        sc.close();
        for (Store store : listOfStores) {
            store.delayBetweenOrders = minDelay + rng.nextInt(maxDelay - minDelay + 1);
            System.out.println("For the store "+store.numberOrders+" chosen waiting time is "+store.delayBetweenOrders);
            store.numberOrders = ocount;
        }
        double time = System.currentTimeMillis();
        for (Thread th : storeThreads) {
            System.out.println(th.getName());
            th.start();
            if(System.currentTimeMillis()-time/1000>=aDelay){
                for(Store store :listOfStores){
                    for(Order order:store.orders){
                        String updateData = "{\"status\":\"delivered\"}";
                        order.orderId = getRideOfEnd(order.orderId);
                        Request ou = new Request("https://menu-ecs-service-dispatch-core-playground.menu.app/api/orders/"
                        +order.orderId,updateData);
                        System.out.println(ou.put());
                       
                    }
                }
                time = System.currentTimeMillis();
            }
        }
        /**
         * set the order status "delivered" again (we dont estimate time here.)
         */
        for(Store store :listOfStores){
            for(Order order:store.orders){
                String updateData = "{\"status\":\"delivered\"}";
                order.orderId = getRideOfEnd(order.orderId);
                Request ou = new Request("https://menu-ecs-service-dispatch-core-playground.menu.app/api/orders/"
                +order.orderId,updateData);
                System.out.println(ou.put());
               
            }
        }

    }

    public static String getRideOfEnd(String str) {
        StringBuilder strx = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) != '\"' && str.charAt(i) != '\"')
                strx.append(str.charAt(i));
        }
        return strx.toString();
    }
}
