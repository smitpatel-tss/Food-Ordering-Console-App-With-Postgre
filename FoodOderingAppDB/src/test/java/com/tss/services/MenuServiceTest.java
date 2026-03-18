package com.tss.services;

import com.tss.model.CuisineType;
import com.tss.model.FoodItem;
import com.tss.repositories.MenuRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MenuServiceTest {

    @Mock
    private MenuRepo menuRepo;

    @InjectMocks
    private MenuService menuService;

    private CuisineType indianCuisine;
    private FoodItem paneerBiryani;

    @BeforeEach
    void setUp() {
        indianCuisine = new CuisineType(1L, "Indian");
        paneerBiryani = new FoodItem(101L, "Paneer Biryani", 250.0, indianCuisine);
    }

    @Test
    void isEmptyTest() {
        when(menuRepo.isMenuEmpty()).thenReturn(true);
        assertTrue(menuService.isEmpty());
    }

    @Test
    void isNotEmptyTest() {
        when(menuRepo.isMenuEmpty()).thenReturn(false);
        assertFalse(menuService.isEmpty());
    }

    @Test
    void getItemFromIdTest() {
        when(menuRepo.getItemFromId(101L)).thenReturn(paneerBiryani);
        FoodItem result = menuService.getItemFromId(101L);
        assertNotNull(result);
        assertEquals("Paneer Biryani", result.getName());
        assertEquals(250.0, result.getPrice());
    }

    @Test
    void getItemFromIdEmptyTest() {
        when(menuRepo.getItemFromId(999L)).thenReturn(null);
        assertNull(menuService.getItemFromId(999L));
    }

    @Test
    void removeItemTest() {
        when(menuRepo.removeItem(101L)).thenReturn(true);
        assertTrue(menuService.removeItem(101L));
        verify(menuRepo, times(1)).removeItem(101L);
    }

    @Test
    void removeItemEmptyTest() {
        when(menuRepo.removeItem(999L)).thenReturn(false);
        assertFalse(menuService.removeItem(999L));
    }

    @Test
    void removeCuisineTest() {
        when(menuRepo.removeCuisine(1L)).thenReturn(true);
        assertTrue(menuService.removeCuisine(1L));
        verify(menuRepo, times(1)).removeCuisine(1L);
    }

    @Test
    void removeCuisineEmptyTest() {
        when(menuRepo.removeCuisine(999L)).thenReturn(false);
        assertFalse(menuService.removeCuisine(999L));
    }

    @Test
    void addNewFoodItemTest() {
        FoodItem newItem = new FoodItem(102L, "Paneer Butter Masala", 180.0, indianCuisine);
        menuService.addNewFoodItem(newItem);
        verify(menuRepo, times(1)).addNewFoodItem(newItem);
    }

    @Test
    void addNewCuisineTest() {
        String cuisineName = "Chinese";
        menuService.addNewCuisine(cuisineName);
        verify(menuRepo, times(1)).addNewCuisine(cuisineName);
    }

    @Test
    void getAllCuisinesTest() {
        List<CuisineType> cuisines = Arrays.asList(indianCuisine, new CuisineType(2L, "Chinese"));
        when(menuRepo.getAllCuisines()).thenReturn(cuisines);
        List<CuisineType> result = menuService.getAllCuisines();
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void getAllCuisinesEmptyTest() {
        when(menuRepo.getAllCuisines()).thenReturn(Collections.emptyList());
        List<CuisineType> result = menuService.getAllCuisines();
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void changePriceTest() {
        long itemId = 101L;
        double newPrice = 300.0;
        menuService.changePrice(itemId, newPrice);
        verify(menuRepo, times(1)).changePrice(itemId, newPrice);
    }
}