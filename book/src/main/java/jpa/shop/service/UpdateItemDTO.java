package jpa.shop.service;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateItemDTO {

    private Long id;

    private String name;
    private int price;
    private int stockQuantity;
}
