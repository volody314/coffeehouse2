package com.volody314.coffeehouse2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
public class OrderController {
    private final Orders orders;

    @Autowired
    public OrderController(Orders orders) {
        this.orders = orders;
    }

    // Создаёт заказ
    @GetMapping(value = "/orders")
    public ResponseEntity<Integer> create() {
        //System.out.println(" *** Creating *** ");
        return new ResponseEntity<>(orders.create(), HttpStatus.CREATED);
    }

    // Выводит содержимое заказа
    @GetMapping(value = "/orders/{id}")
    public ResponseEntity<List<OrderItem>> read(@PathVariable(name = "id") Integer id) {
        //System.out.println(" *** Get-ting *** " + id);
        if (orders.orderExists(id)) {
            //final List<Item> items = orders.read(id);
            //return new ResponseEntity<>(items, HttpStatus.OK);
            return new ResponseEntity<>(orders.read(id), HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Помещает позицию в заказ
    @PutMapping(value = "/orders/{id}")
    public ResponseEntity<Integer> putItem(@PathVariable(name = "id") Integer id, @RequestBody OrderItem orderItem) {
        if (orders.orderExists(id)) {
            return new ResponseEntity<>(orders.putItem(id, orderItem), HttpStatus.OK);
        } else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Удаляет позицию из заказа и обновляет список
    @DeleteMapping(value = "/orders/{ordId}/items/{itId}")
    public ResponseEntity<List<OrderItem>> deleteItem(@PathVariable(name = "ordId") Integer orderId,
                                                      @PathVariable(name = "itId") Integer itemId) {
        if (orders.orderExists(orderId)) {
            return new ResponseEntity<>(orders.deleteItem(orderId, itemId), HttpStatus.OK);
        } else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Удаляет заказ
    @DeleteMapping(value = "/orders/{ordId}")
    public ResponseEntity<?> deleteOrder(@PathVariable(name = "ordId") Integer orderId) {
        if (orders.orderExists(orderId)) {
            orders.closeOrder(orderId);
            return new ResponseEntity<>(HttpStatus.OK);
        } else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Отправляет заказ в производство
    @PostMapping(value = "/orders/{id}")
    public ResponseEntity<?> produceOrder(@PathVariable(name = "id") Integer id) {
        if (orders.orderExists(id)) {
            return new ResponseEntity<>(orders.produceOrder(id), HttpStatus.OK);
        } else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Возвращает список заказов в производстве
    @GetMapping(value = "/product")
    public ResponseEntity<List<Integer>> showProd() {
        return new ResponseEntity<>(orders.showProduced(), HttpStatus.OK);
    }

    // Отправляет заказ на выдачу
//    @PostMapping(value = "/product/{id}")
//    public ResponseEntity<?> distributeOrder(@PathVariable(name = "id") Integer id) {
//        if (orders.productExists(id)) {
//            return new ResponseEntity<>(orders.distributeOrder(id), HttpStatus.OK);
//        } else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//    }

    // Возвращает список заказов на выдаче
    @GetMapping(value = "/distrib")
    public ResponseEntity<List<Integer>> showDistrib() {
        return new ResponseEntity<>(orders.showDistribution(), HttpStatus.OK);
    }

    // Закрывает заказ при выдаче
//    @PostMapping(value = "/distrib/{ordId}")
//    public ResponseEntity<?> closeOrder(@PathVariable(name = "ordId") Integer orderId) {
//        if (orders.distribExists(orderId)) {
//            orders.closeOrder(orderId);
//            return new ResponseEntity<>(HttpStatus.OK);
//        } else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//    }
}
