package com.volody314.coffeehouse2;

import java.util.List;
//import java.util.concurrent.atomic.AtomicInteger;

/**
 * Список заказов
 */
public interface Orders {
    /**
     * Создаёт заказ
     * @return Номер заказа
     */
    Integer create();

    /**
     * Возвращает список позиций в заказе
     * @param orderId Номер заказа
     * @return Список позиций в заказе
     */
    List<OrderItem> read(Integer orderId);

    /**
     * Добавляет позицию заказа
     * @param orderItem Описание позиции
     * @return Номер позиции в заказе
     */
    Integer putItem(Integer orderId, OrderItem orderItem);

    /**
     * Удаляет позицию из заказа
     * @param orderId Номер заказа
     * @param itemId Позиция
     */
    List<OrderItem> deleteItem(Integer orderId, Integer itemId);

    /**
     * Отправляет заказ в производство
     * @param orderId Номер заказа
     */
    boolean produceOrder(Integer orderId);

    /**
     * Перечень заказов в производстве
     * @return Количество заказов в производстве
     */
    List<Integer> showProduced();

    /**
     * Отправляет заказ на выдачу
     * @param orderId Номер заказа
     * @return Успешность выполнения
     */
    //boolean distributeOrder(Integer orderId);

    /**
     * Выводит перечень заказов на выдаче
     * @return Количество заказов на выдаче
     */
    List<Integer> showDistribution();

    /**
     * Закрывает заказ (удаляет из очередей)
     * @param orderId Номер заказа
     */
    void closeOrder(Integer orderId);

    /**
     * Проверяет наличие заказа в составляемых
     * @param orderId Номер заказа
     * @return Есть?
     */
    boolean orderExists(Integer orderId);

    /**
     * Проверяет наличие заказа в производстве
     * @param orderId Номер заказа
     * @return Есть?
     */
    //boolean productExists(Integer orderId);

    /**
     * Проверяет наличие заказа в выдаче
     * @param orderId Номер заказа
     * @return Есть?
     */
    //boolean distribExists(Integer orderId);
}
