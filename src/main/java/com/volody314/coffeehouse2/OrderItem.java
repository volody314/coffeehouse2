package com.volody314.coffeehouse2;

import java.io.Serializable;

/**
 * Позиция заказа
 */
public class OrderItem implements Serializable {
    private Integer id;     // Номер позиции
    private String name;    // Название
    private Float prise;    // Цена
    private Integer count;  // Количество

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getPrise() {
        return prise;
    }

    public void setPrise(Float prise) {
        this.prise = prise;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

}
