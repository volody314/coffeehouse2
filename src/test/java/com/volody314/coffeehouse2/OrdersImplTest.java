package com.volody314.coffeehouse2;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Assert.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

class OrdersImplTest {

    //OrdersImpl oimp = new OrdersImpl();
    OrderItem tstItem1 = new OrderItem();
    OrderItem tstItem2 = new OrderItem();

    //@Before
    public void setup() {
        tstItem1.setId(1);
        tstItem1.setName("Coffee");
        tstItem1.setCount(2);
        tstItem1.setPrise(100.0F);
        tstItem2.setId(1);
        tstItem2.setName("Coffee better");
        tstItem2.setCount(1);
        tstItem2.setPrise(200.0F);
    }

    @Test
    void create() {
        OrdersImpl oimp = new OrdersImpl();
        int tst = oimp.create();
        Assertions.assertEquals(1, tst);
    }

    @Test
    void putItem() {
        setup();
        OrdersImpl oimp = new OrdersImpl();
        int tst = oimp.create();
        int tstRez = oimp.putItem(1, tstItem1);
        Assertions.assertEquals(1, tstRez);
        tstRez = oimp.putItem(1, tstItem2);
        Assertions.assertEquals(2, tstRez);
        oimp.deleteItem(1, 0);
        oimp.deleteItem(1, 0);
        //oimp.closeOrder(1);
    }

    @Test
    void read() {
        setup();
        OrdersImpl oimp = new OrdersImpl();
        oimp.create();
        oimp.putItem(1, tstItem1);
        oimp.putItem(1, tstItem2);
        List<OrderItem> tstList = oimp.read(1);
        OrderItem tst = tstList.get(0);
        Assertions.assertEquals(0, tst.getId());
        Assertions.assertEquals("Coffee", tst.getName());
        Assertions.assertEquals(2, tst.getCount());
        Assertions.assertEquals(100.0F, tst.getPrise());
        tst = tstList.get(1);
        Assertions.assertEquals(1, tst.getId());
        Assertions.assertEquals("Coffee better", tst.getName());
        Assertions.assertEquals(1, tst.getCount());
        Assertions.assertEquals(200.0F, tst.getPrise().floatValue());
        oimp.deleteItem(1, 0);
        oimp.deleteItem(1, 0);
        //oimp.closeOrder(1);
    }

    @Test
    void deleteItem() {
        setup();
        OrdersImpl oimp = new OrdersImpl();
        //oimp.closeOrder(1);
        oimp.create();
        oimp.putItem(1, tstItem1);
        oimp.putItem(1, tstItem2);
        List<OrderItem> tstList = oimp.read(1);
        //Assertions.assertEquals(2, tstList.size());
        tstList = oimp.deleteItem(1, 0);
        //tstList = oimp.read(1);
        Assertions.assertEquals(1, tstList.size());
        OrderItem tst = tstList.get(0);
        Assertions.assertEquals(0, tst.getId());
        Assertions.assertEquals("Coffee better", tst.getName());
        Assertions.assertEquals(1, tst.getCount());
        Assertions.assertEquals(200.0F, tst.getPrise().floatValue());
        //System.out.println("Elements "+(oimp.read(1)).size());
        oimp.deleteItem(1, 0);
        oimp.deleteItem(1, 0);
        //oimp.closeOrder(1);
    }

//    @Test
//    void produceOrder() {
//    }
//
//    @Test
//    void showProducing() {
//    }
//
//    @Test
//    void showDistribution() {
//    }
//
//    @Test
//    void closeOrder() {
//    }
//
//    @Test
//    void orderExists() {
//    }
}