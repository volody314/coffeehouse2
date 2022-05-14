package com.volody314.coffeehouse2;

import java.util.ArrayList;

/**
 * Заказ
 */
public class Order {
    private final Integer id; // Идентификатор заказа
    //private Float overralSum = 0.0;  // Сумма чека
    private Integer posCounter = 0; // Счётчик позиций
    private final ArrayList<OrderItem> orderItems = new ArrayList<>();  // Позиции заказа


    public Order(Integer id_) {
        this.id = id_;
    }

    public Integer getId() {
        return id;
    }

    /**
     * Возвращает список позиций в заказе
     * @return Список позиций
     */
    public ArrayList<OrderItem> getItems() {
        return orderItems;
    }

    /**
     * Добавляет позицию в заказ
     * @param orderItem Описание позиции
     * @return Номер позиции в заказе
     */
    public Integer addItem(OrderItem orderItem) {
        //this.overralSum += item.prise * item.count;
        this.orderItems.add(orderItem);
        itemsRenumerate();
        return posCounter++;
    }

    public ArrayList<OrderItem> deleteItem(int toDel) {
        if (toDel < this.orderItems.size()) {
            //overralSum -= this.items.get(toDel).prise * this.items.get(toDel).count;
            //System.out.println("Deleting item "+toDel);
            this.orderItems.remove(toDel);
            posCounter--;
            itemsRenumerate();
        }
        return this.orderItems;
    }

    /**
     * Проставляет актуальные номера позиций в заказе, необходимые для выдачи клиенту,
     * чтоб указать номер удаляемой позиции
     */
    private void itemsRenumerate() {
        for (int i = 0; i < orderItems.size(); ++i) {
            orderItems.get(i).setId(i);
        }
    }
}
