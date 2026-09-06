package ru.netology.delivery.data;

import lombok.Value;

@Value
public class DeliveryInfo {
    String city;
    String date;
    String name;
    String phone;
}