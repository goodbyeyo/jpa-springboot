package jpa.shop.domain.item;

import jpa.shop.domain.Category;
import jpa.shop.exception.NotEnoutgStockException;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

// @BatchSize(size = 100)  // toOne관계 -> application.yml : default_batch_fetch_size 의 세부설정
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
@Getter
@Setter
public abstract class Item {

    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

    @ManyToMany(mappedBy = "items") // 실무에서는 사용 X, 중간테이블에 다른 값 넣을수가 없다
    private List<Category> categories = new ArrayList<Category>();

    /* 비지니스 로직 : Domain 주도 개발에서는 Entity안에 비지니스 로직을 넣는것이 객체지향적이다 */

    // stock(재고) 증가
    public void addStock(int quantity) {
        this.stockQuantity += quantity;
    }

    // stock(재고) 감소
    public void removeStock(int quantity) {
        int restStock = this.stockQuantity - quantity;
        if (restStock < 0) {
            throw new NotEnoutgStockException("need nore stock");
        }
        this.stockQuantity = restStock;
    }

}
