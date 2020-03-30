package co.com.filipsmcode.init.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import co.com.filipsmcode.init.entity.Product;

public interface ProductsDao extends JpaRepository<Product, Long>{

}
