package de.telran.onlineshopforhomeandgarden1.service;

import de.telran.onlineshopforhomeandgarden1.dto.ProductDto;
import de.telran.onlineshopforhomeandgarden1.dto.request.ProductRequestDto;
import de.telran.onlineshopforhomeandgarden1.entity.Category;
import de.telran.onlineshopforhomeandgarden1.entity.Product;
import de.telran.onlineshopforhomeandgarden1.mapper.ProductMapper;
import de.telran.onlineshopforhomeandgarden1.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mockito;
import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;


public class ProductServiceTest {

    private static ProductService productService;
    private static ProductRepository repository;
    private static ProductMapper productMapper;

    @BeforeEach
    public void init() {
        repository = Mockito.mock(ProductRepository.class);
        productMapper = Mappers.getMapper(ProductMapper.class);
        productService = new ProductService(repository, productMapper);
    }

    @Test
    public void getProductByIdTest() {
        Product product = new Product();
        product.setId(1L);

        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(product));
        ProductDto result = productService.getProductById(1L).get();

        Mockito.verify(repository).findById(1L);
        assertEquals(product.getId(), result.getId());
    }

    @Test
    public void getProductByIdIncorrectTest() {
        Product product = new Product();
        product.setId(1L);

        Mockito.when(repository.findById(111L)).thenReturn(Optional.empty());
        Optional<ProductDto> optional = productService.getProductById(111L);

        Mockito.verify(repository).findById(111L);
        assertTrue(optional.isEmpty());
    }


    @Test
    public void getAllTest() {
        Product product = new Product();
        Pageable pageable = PageRequest.of(0, 10, Sort.by("name").ascending());
        Long categoryId = null;
        Boolean hasDiscount = null;
        Integer minPrice = null;
        Integer maxPrice = null;
        Page<Product> products = new PageImpl<>(Collections.singletonList(product));
//        ProductWithDiscountPriceResponseDto responseDto = new ProductWithDiscountPriceResponseDto();

        Mockito.when(repository.getAllWithFilters(categoryId, false, false, BigDecimal.valueOf(0), BigDecimal.valueOf(Integer.MAX_VALUE), pageable))
                .thenReturn(products);
        productService.getAll(categoryId, hasDiscount, minPrice, maxPrice, pageable);
        Mockito.verify(repository).getAllWithFilters(categoryId, false, false, BigDecimal.valueOf(0), BigDecimal.valueOf(Integer.MAX_VALUE), pageable);

    }

    @Test
    public void addProductTest() {
        Category category = new Category();
        category.setId(1L);
        Product product = new Product();
        product.setName("Test name");
        product.setDescription("Test description");
        product.setPrice(BigDecimal.valueOf(10.4));
        product.setCategory(category);
        product.setImageUrl("https://raw.githubusercontent.com/tel-ran-de");
        product.setDiscountPrice(null);

        Mockito.when(repository.save(product)).thenReturn(product);
        ProductRequestDto savedProduct = productService.addProduct(productMapper.entityToRequestDto(product));

        Mockito.verify(repository).save(eq(product));
        assertEquals(product.getName(), savedProduct.getName());
        assertEquals(product.getPrice(), savedProduct.getPrice());
    }

    @Test
    public void updateProductTest() {
        Category category = new Category();
        category.setId(4L);
        Product oldProduct = new Product();
        oldProduct.setId(22L);
        oldProduct.setName("Test name");
        oldProduct.setDescription("Test description");
        oldProduct.setPrice(BigDecimal.valueOf(10.4));
        oldProduct.setCategory(category);
        oldProduct.setImageUrl("https://raw.githubusercontent.com/tel-ran-de");
        oldProduct.setDiscountPrice(null);

        Product updatedProduct = new Product();
        updatedProduct.setId(22L);
        updatedProduct.setName("New test name");
        updatedProduct.setDescription("New test description");
        updatedProduct.setPrice(BigDecimal.valueOf(10));
        updatedProduct.setCategory(category);
        updatedProduct.setImageUrl("https://raw.githubusercontent.com/tel-ran-de");
        updatedProduct.setDiscountPrice(null);

        Mockito.when(repository.findById(updatedProduct.getId())).thenReturn(Optional.of(oldProduct));
        Mockito.when(repository.save(updatedProduct)).thenReturn(updatedProduct);
        productService.updateProduct(productMapper.entityToRequestDto(updatedProduct));
        Mockito.verify(repository).save(eq(updatedProduct));
    }

    @Test
    public void updateProductNotFoundTest() {
        Product updatedProduct = new Product();
        updatedProduct.setId(555L);

        Mockito.when(repository.findById(updatedProduct.getId())).thenReturn(Optional.empty());
        ProductRequestDto result = productService.updateProduct(productMapper.entityToRequestDto(updatedProduct));
        assertNull(result);
    }
}