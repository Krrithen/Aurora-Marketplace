package com.example.auroramarketplace.repository;

import com.example.auroramarketplace.entities.Products;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomProductsRepository extends MongoRepository<Products, String> {

    // Custom queries with proper indexing
    @Query("{ 'productName': { $regex: ?0, $options: 'i' } }")
    List<Products> findByProductNameContainingIgnoreCase(@Param("productName") String productName);

    @Query("{ 'category': ?0, 'productQuantity': { $gt: 0 } }")
    List<Products> findAvailableProductsByCategory(@Param("category") String category);

    @Query("{ 'merchantId': ?0, 'productQuantity': { $gt: 0 } }")
    List<Products> findAvailableProductsByMerchantId(@Param("merchantId") String merchantId);

    @Query("{ 'price': { $gte: ?0, $lte: ?1 } }")
    List<Products> findProductsByPriceRange(@Param("minPrice") Double minPrice, @Param("maxPrice") Double maxPrice);

    @Query("{ 'brand': ?0 }")
    List<Products> findByBrand(@Param("brand") String brand);

    @Query("{ 'productName': { $regex: ?0, $options: 'i' }, 'category': ?1 }")
    List<Products> findByProductNameAndCategory(@Param("productName") String productName, @Param("category") String category);

    @Query(value = "{ 'productQuantity': { $lte: ?0 } }", fields = "{ 'productId': 1, 'productName': 1, 'productQuantity': 1 }")
    List<Products> findLowStockProducts(@Param("threshold") Integer threshold);

    @Query("{ 'merchantId': ?0 }")
    List<Products> findAllByMerchantId(@Param("merchantId") String merchantId);

    @Query(value = "{}", sort = "{ 'createdAt': -1 }")
    List<Products> findRecentProducts();

    @Query(value = "{}", sort = "{ 'price': 1 }")
    List<Products> findProductsSortedByPriceAsc();

    @Query(value = "{}", sort = "{ 'price': -1 }")
    List<Products> findProductsSortedByPriceDesc();

    // Optimistic locking support
    @Query("{ '_id': ?0, 'version': ?1 }")
    Optional<Products> findByIdAndVersion(@Param("id") String id, @Param("version") Long version);

    // Transaction support queries
    @Query(value = "{ '_id': ?0 }", fields = "{ 'productQuantity': 1 }")
    Optional<Products> findQuantityById(@Param("id") String id);

    // Aggregation queries
    @Query(value = "{}", fields = "{ 'category': 1 }")
    List<String> findDistinctCategories();

    @Query(value = "{}", fields = "{ 'brand': 1 }")
    List<String> findDistinctBrands();

    @Query(value = "{}", fields = "{ 'merchantId': 1 }")
    List<String> findDistinctMerchantIds();
}
