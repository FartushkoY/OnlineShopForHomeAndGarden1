package de.telran.onlineshopforhomeandgarden1.service;

import de.telran.onlineshopforhomeandgarden1.dto.request.CartRequestDto;
import de.telran.onlineshopforhomeandgarden1.entity.Cart;
import de.telran.onlineshopforhomeandgarden1.entity.CartItem;
import de.telran.onlineshopforhomeandgarden1.entity.Product;
import de.telran.onlineshopforhomeandgarden1.mapper.CartItemMapper;
import de.telran.onlineshopforhomeandgarden1.mapper.CartMapper;
import de.telran.onlineshopforhomeandgarden1.repository.CartItemRepository;
import de.telran.onlineshopforhomeandgarden1.repository.CartRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mockito;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

class CartServiceTest {
    private static CartService service;
    private static CartRepository cartRepository;
    private static CartMapper cartMapper;
    private static CartItemRepository cartItemRepository;
    private static CartItemMapper cartItemMapper;

    @BeforeEach
    public void init() {
        cartRepository = Mockito.mock(CartRepository.class);
        cartMapper = Mappers.getMapper(CartMapper.class);
        cartItemRepository = Mockito.mock(CartItemRepository.class);
        cartItemMapper = Mappers.getMapper(CartItemMapper.class);
        service = new CartService(cartRepository, cartMapper, cartItemRepository, cartItemMapper);
    }

    @Test
    void getCartItems() {
        service.getCartItems();
        Mockito.verify(cartRepository).findCartsByUserId(8l);
    }

    @Test
    void addCartItem() {
        Cart cart = new Cart();
        cart.setId(2L);
        Set<CartItem> cartItems = cart.getCartItems();

        CartItem cartItem = new CartItem();
        Product product = new Product();
        product.setId(1L);
        cartItem.setProduct(product);
        cartItem.setQuantity(2);

        cartItems.add(cartItem);

        cartItem = new CartItem();
        product.setId(6L);
        cartItem.setProduct(product);
        cartItem.setQuantity(5);

        cartItems.add(cartItem);

        Mockito.when(cartRepository.findByUserId(anyLong())).thenReturn(cart);
        Mockito.when(cartRepository.save(cart)).thenReturn(cart);
        CartRequestDto cartRequestDto = service.addCartItem(cartItemMapper.entityToRequestDto(cartItem));

        Mockito.when(cartMapper.entityToRequestDto(cart)).thenReturn(cartRequestDto);

        assertEquals(cart.getCartItems().size(), 2);
    }
    @Test
    void addCartItemWhenItemExist() {
        Cart cart = new Cart();
        cart.setId(2L);

        CartItem cartItem = new CartItem();
        Product product = new Product();
        product.setId(1L);
        cartItem.setProduct(product);
        cartItem.setQuantity(2);

        Set<CartItem> cartItems = new LinkedHashSet<>();
        cartItems.add(cartItem);
        cart.setCartItems(cartItems);

        CartItem updatedCartItem = new CartItem();
        updatedCartItem.setProduct(product);
        updatedCartItem.setQuantity(3);

        Mockito.when(cartRepository.findByUserId(anyLong())).thenReturn(cart);
        Mockito.when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        CartRequestDto result = service.addCartItem(cartItemMapper.entityToRequestDto(updatedCartItem));

        assertNotNull(result);
        assertEquals(1, cart.getCartItems().size());
        CartItem updatedItem = cart.getCartItems().iterator().next();
        assertEquals(5, updatedItem.getQuantity());
    }

    @Test
    void updateCartItemInCart() {
        Cart cart = new Cart();
        cart.setId(2L);
        Set<CartItem> cartItems = cart.getCartItems();

        CartItem cartItem = new CartItem();
        Product product = new Product();
        product.setId(1L);
        cartItem.setProduct(product);
        cartItem.setQuantity(2);

        cartItems.add(cartItem);

        CartItem updateCartItem = new CartItem();
        product.setId(1L);
        updateCartItem.setProduct(product);
        updateCartItem.setQuantity(5);

        cartItems.clear();
        cartItems.add(updateCartItem);

        Mockito.when(cartRepository.findByUserId(anyLong())).thenReturn(cart);
        Mockito.when(cartRepository.save(cart)).thenReturn(cart);
        CartRequestDto cartRequestDto = service.updateCartItemInCart(cartItemMapper.entityToRequestDto(updateCartItem));

        Mockito.when(cartMapper.entityToRequestDto(cart)).thenReturn(cartRequestDto);

        assertEquals(cart.getCartItems().size(), 1);
        assertNotNull(cartRequestDto);
        CartItem updatedItem = cart.getCartItems().iterator().next();
        assertEquals(5, updatedItem.getQuantity());
    }

    @Test
    void deleteCartItemInCartSuccessfully() {
        Cart cart = new Cart();
        cart.setId(1L);
        Set<CartItem> cartItems = new LinkedHashSet<>();

        CartItem cartItem = new CartItem();
        cartItem.setId(1L);
        cartItems.add(cartItem);

        Mockito.when(cartRepository.findByUserId(anyLong())).thenReturn(cart);
        Mockito.when(cartItemRepository.findAllByCartId(cart.getId())).thenReturn(cartItems);

        Optional<Cart> result = service.deleteCartItemInCart();

        assertTrue(result.isPresent());
        Mockito.verify(cartItemRepository, Mockito.times(1)).deleteAllByCart(cart);
    }

    @Test
    void deleteCartItemInCartWhenCartIsEmpty() {
        Cart cart = new Cart();
        cart.setId(1L);
        Set<CartItem> cartItems = new LinkedHashSet<>();

        Mockito.when(cartRepository.findByUserId(anyLong())).thenReturn(cart);
        Mockito.when(cartItemRepository.findAllByCartId(cart.getId())).thenReturn(cartItems);

        Optional<Cart> result = service.deleteCartItemInCart();

        assertFalse(result.isPresent());
        Mockito.verify(cartItemRepository, Mockito.never()).deleteAllByCart(cart);
    }

}