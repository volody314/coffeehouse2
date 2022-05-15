package com.volody314.coffeehouse2;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Заказ
 */
public class Order implements Serializable {
    private final Integer id; // Идентификатор заказа
    //private Float overralSum = 0.0;  // Сумма чека
    private Integer posCounter = 0; // Счётчик позиций

    private boolean produced = false;   // Флаг готовности заказа к выдаче

    private boolean removed = false;    // Флаг завершения заказа

    private int timeStamp = -1;  // Метка начала производства
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

    /**
     * Удаляет позицию из заказа по номеру
     * @param toDel Номер позиции для удаления
     * @return Список позиций в заказе после удаления
     */
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
        posCounter = orderItems.size();
    }


    public boolean isProduced() {
        return produced;
    }

    public void setProduced() {
        this.produced = true;
    }

    public int getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(int timeStamp) {
        this.timeStamp = timeStamp;
    }

    public boolean isRemoved() {
        return removed;
    }

    public void setRemoved(boolean removed) {
        this.removed = removed;
    }

}

