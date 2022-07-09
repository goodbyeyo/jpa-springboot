package jpa.shop.service;

import jpa.shop.domain.item.Book;
import jpa.shop.domain.item.Item;
import jpa.shop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional
    public void saveItem(Item item) {
        itemRepository.save(item);
    }

    public List<Item> findItems() {
        return itemRepository.findAll();
    }

    public Item findOne(Long itemId) {
        return itemRepository.findOne(itemId);
    }

    // 변경감지 기능 사용
    @Transactional
    public void updateItem(Long itemId, String name, int price, int stockQuantity) {
//    public Item updateItem(Long itemId, Book bookParam) {
        Item findItem = itemRepository.findOne(itemId);
        findItem.setPrice(price);
        findItem.setName(name);
        findItem.setStockQuantity(stockQuantity);
        // itemRepository.save(findItem);    // transaction : commit -> fulsh 생략가능
        // return findItem;
    }

    // 병합 사용
//    @Transactional
//    void update(Item itemParam) { //itemParam: 파리미터로 넘어온 준영속 상태의 엔티티
//        Item mergeItem = em.merge(item);
//    }

//    @Transactional
//    void update(Item itemParam) { //itemParam: 파리미터로 넘어온 준영속 상태의 엔티티
//        Item findItem = em.find(Item.class, itemParam.getId()); //같은 엔티티를 조회한다.
//        findItem.setPrice(itemParam.getPrice()); //데이터를 수정한다.
//    }

}
