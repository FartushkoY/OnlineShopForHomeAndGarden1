package de.telran.onlineshopforhomeandgarden1.service;

import de.telran.onlineshopforhomeandgarden1.dto.response.OrderResponseDto;
import de.telran.onlineshopforhomeandgarden1.entity.Order;
import de.telran.onlineshopforhomeandgarden1.entity.User;
import de.telran.onlineshopforhomeandgarden1.enums.Status;
import de.telran.onlineshopforhomeandgarden1.mapper.OrderMapper;
import de.telran.onlineshopforhomeandgarden1.repository.OrderRepository;
import de.telran.onlineshopforhomeandgarden1.repository.ProductRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private static final Logger logger = LogManager.getLogger(OrderService.class);

    private final OrderRepository repository;
    private final OrderMapper orderMapper;
    private final ProductRepository productRepository;


    @Autowired
    public OrderService(OrderRepository repository, OrderMapper orderMapper, ProductRepository productRepository) {
        this.repository = repository;
        this.orderMapper = orderMapper;
        this.productRepository = productRepository;
    }

    public List<OrderResponseDto> getOrdersHistory()  {
        List<Order> orders = repository.findOrdersByUserId(this.getAutheticateUser().getId());
        return orderMapper.entityListToDto(orders);
    }

    public Optional<OrderResponseDto> getOrderStatus(Long id) {
        Optional<Order> order = repository.findById(id);
        logger.debug("Order retrieved from DB: id =  {}", () -> order.orElse(null));
        return order.map(orderMapper::entityToDto);

    }

    private User getAutheticateUser() {
        User user = new User();
        user.setId(1l);
        return user;
    }
}
