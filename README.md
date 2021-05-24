# loadTest
Parameters:
          bearer token
          restaurant count: Rcount (z.B. 100)
          Orders per restaurant: Ocount (z.B. 50 Bestellungen)
          max delay between orders: MAX-Odelay (z.B 40sek)
          min delay between orders: MIN-Odelay (z.B. 15sek)
          autocomplete orders after: Adelay (z.B.: 2min)
          BAG-Size
          Using SuperAdmin to verify operation
          
          
          
          
Step 1:
          create Rcount of restaurants along one street: Leopoldstr. 9 - 250 using: POST /api/store - save store-uuids/lieferando_id in array
          z.B. Restaurant Name: LOAD-TEST<Rcount>
          create two drivers per restaurant - set active, set GPS to fixed location in Feilitzschstr. 14 - bag-size: BAG-SIZE
          Enable auto-assign 
          
          
          
          
          
Step 2:
          start Thread for each Restaurant - so those restaurants “work” in parallel
          For i=1 to Ocount
          Inject an order for this restaurant (api/orders/inject)
          wait a random time between MIN-Odelay and Odelay
          Set all orders to “delivered” which are older than Adelay from now PUT /api/orders/<order-id>
          wenn ALLE Threads fertig:
          
          
          
          
          
          
Step 3 (manuell via DB):
          delete all restaurants from DB (this should automatically delete all orders and drivers - verify in DB)
          delete * from tbl_stores where name like “LOAD%”
          
          
          
          
          
          
