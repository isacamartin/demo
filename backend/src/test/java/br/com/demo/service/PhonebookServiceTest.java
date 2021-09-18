package br.com.demo.service;

import br.com.demo.exceptions.PhonebookAlreadyExistsException;
import br.com.demo.exceptions.PhonebookNotFoundException;
import br.com.demo.helpers.Mocks;
import br.com.demo.model.Phonebook;
import br.com.demo.persistence.PhonebookPersistence;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

class PhonebookServiceTest{
    private PhonebookPersistence persistence = Mocks.getPersistence();
    private List<Phonebook> mockList = Mocks.getList();
    private Phonebook mockItem = Mocks.getItem();
    private Phonebook mockNewItem = Mocks.getNewItem();
    private Phonebook mockExistingItem = Mocks.getExistingItem();
    
    @Test
    void list(){
        Mockito.when(persistence.findAll()).thenReturn(mockList);
    
        PhonebookService service = new PhonebookService(persistence);
        List<Phonebook> list = service.list();
    
        Assertions.assertEquals(list, mockList);
    }
    
    @Test
    void findById(){
        Mockito.when(persistence.findById(1)).thenReturn(Optional.of(mockItem));
        Mockito.when(persistence.findById(2)).thenReturn(Optional.empty());
        Mockito.when(persistence.findById(null)).thenThrow(IllegalArgumentException.class);
        
        PhonebookService service = new PhonebookService(persistence);
    
        try{
            Phonebook item = service.findById(1);
            
            Assertions.assertEquals(item, mockItem);
        }
        catch(PhonebookNotFoundException e){
            Assertions.assertTrue(false);
        }
    
        try{
            service.findById(2);
    
            Assertions.assertTrue(false);
        }
        catch(PhonebookNotFoundException e){
            Assertions.assertTrue(true);
        }
    
        try{
            service.findById(null);
    
            Assertions.assertTrue(false);
        }
        catch(PhonebookNotFoundException e){
            Assertions.assertTrue(true);
        }
    }
    
    @Test
    void findByName(){
        Mockito.when(persistence.findByName("Luke Skywalker")).thenReturn(mockList);
        Mockito.when(persistence.findByName("Han Solo")).thenReturn(null);
        
        PhonebookService service = new PhonebookService(persistence);
        List<Phonebook> list = service.findByName("Luke Skywalker");
    
        Assertions.assertEquals(list, mockList);
    
        list = service.findByName("Han Solo");
    
        Assertions.assertNull(list);
    }
    
    @Test
    void findByNameContaining(){
        Mockito.when(persistence.findByNameContaining("Luke")).thenReturn(mockList);
        Mockito.when(persistence.findByNameContaining("Han")).thenReturn(null);
        
        PhonebookService service = new PhonebookService(persistence);
        List<Phonebook> list = service.findByNameContaining("Luke");
        
        Assertions.assertEquals(list, mockList);
        
        list = service.findByNameContaining("Han");
        
        Assertions.assertNull(list);
    }
    
    @Test
    void delete(){
        PhonebookService service = new PhonebookService(persistence);
        
        try{
            service.delete(mockItem);
    
            Assertions.assertTrue(true);
        }
        catch(PhonebookNotFoundException e){
            Assertions.assertTrue(false);
        }
    
        try{
            service.delete(null);
        
            Assertions.assertTrue(false);
        }
        catch(PhonebookNotFoundException e){
            Assertions.assertTrue(true);
        }
    }
    
    @Test
    void save(){
        Mockito.when(persistence.findByName(mockItem.getName())).thenReturn(null);
        Mockito.when(persistence.save(mockItem)).thenReturn(mockItem);
        
        PhonebookService service = new PhonebookService(persistence);
    
        try{
            Assertions.assertEquals(service.save(mockItem), mockItem);
        }
        catch(PhonebookNotFoundException e){
            Assertions.assertTrue(false);
        }
        catch(PhonebookAlreadyExistsException e){
            Assertions.assertTrue(false);
        }
    
        Mockito.when(persistence.findByName(mockItem.getName())).thenReturn(Collections.EMPTY_LIST);
    
        try{
            Assertions.assertEquals(service.save(mockItem), mockItem);
        }
        catch(PhonebookNotFoundException e){
            Assertions.assertTrue(false);
        }
        catch(PhonebookAlreadyExistsException e){
            Assertions.assertTrue(false);
        }

        Mockito.when(persistence.findByName(mockItem.getName())).thenReturn(mockList);
    
        try{
            service.save(mockItem);
        }
        catch(PhonebookNotFoundException e){
            Assertions.assertTrue(false);
        }
        catch(PhonebookAlreadyExistsException e){
            Assertions.assertTrue(true);
        }

        try{
            service.save(null);
        }
        catch(PhonebookNotFoundException e){
            Assertions.assertTrue(true);
        }
        catch(PhonebookAlreadyExistsException e){
            Assertions.assertTrue(false);
        }
    }
}