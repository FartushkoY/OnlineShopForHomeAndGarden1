package de.telran.onlineshopforhomeandgarden1.repository;

import de.telran.onlineshopforhomeandgarden1.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem,Long> {
}
