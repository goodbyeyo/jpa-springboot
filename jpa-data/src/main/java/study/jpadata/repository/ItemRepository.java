package study.jpadata.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.jpadata.entity.Item;

public interface ItemRepository extends JpaRepository<Item, Long> {
}
