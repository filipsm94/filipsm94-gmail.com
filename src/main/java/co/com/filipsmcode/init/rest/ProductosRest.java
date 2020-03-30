package co.com.filipsmcode.init.rest;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import co.com.filipsmcode.init.dao.ProductsDao;
import co.com.filipsmcode.init.entity.Product;
	
@RestController
@RequestMapping("/products")
public class ProductosRest {
	
	@Autowired
	private ProductsDao productDao;

	@GetMapping
	public ResponseEntity<List<Product>> getProduct() {
		List<Product> products = productDao.findAll();
		return ResponseEntity.ok(products);
	}
	
	@RequestMapping(value = "{productId}")
	public ResponseEntity<Product> getProductById(@PathVariable("productId") Long productId) {
		Optional<Product> products = productDao.findById(productId);
		if(products.isPresent()) {
			return ResponseEntity.ok(products.get());
		} else {
			return ResponseEntity.noContent().build();	
		}
		
	}
	
	@PostMapping()
	public ResponseEntity<Product> getProductById(@RequestBody Product product) {
		Product newProducts = productDao.save(product);
			return ResponseEntity.ok(newProducts);
	}
	
	@RequestMapping(value = "{productId}",method = RequestMethod.PUT)
	public ResponseEntity<Product> updateProductbyId(@RequestBody Product product,@PathVariable("productId") Long productId){
		Optional<Product> existProduct = productDao.findById(productId);
		if(existProduct.isPresent()) {
			Product update = existProduct.get();
			update.setName(product.getName());
			Product updatedProduct = productDao.save(update);
			return ResponseEntity.ok(updatedProduct);
		} else {
			return ResponseEntity.noContent().build();	
		}
	}
	
	@RequestMapping(value = "{productId}",method = RequestMethod.DELETE)
	public ResponseEntity<String> updateProductbyId(@PathVariable("productId") Long productId){
		Optional<Product> existProduct = productDao.findById(productId);
		if(existProduct.isPresent()) {productDao.delete(existProduct.get());
			return ResponseEntity.ok("Eliminado con exito");
		} else {
			return ResponseEntity.noContent().build();	
		}
	}
}
