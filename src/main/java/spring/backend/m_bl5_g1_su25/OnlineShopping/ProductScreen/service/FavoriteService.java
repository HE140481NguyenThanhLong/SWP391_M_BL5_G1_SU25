package spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.service;

import jakarta.servlet.http.HttpSession;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spring.backend.m_bl5_g1_su25.OnlineShopping.AuthorizedScreen.repository.AuthorizedRepo;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.entity.Favorite;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.entity.Product;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.repository.FavoriteRepository;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.repository.ProductRepository;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.entity.User;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FavoriteService {
    @Autowired
    FavoriteRepository favoriteRepository;
    @Autowired
    AuthorizedRepo authorizedRepo;
    @Autowired
    ProductRepository repository;
    public void addFavoriteProduct(String username, Integer productId) {
        User user = findUserByUsername(username);
        Product product = repository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));

        if (user == null) {
            throw new RuntimeException("User not found: " + username);
        }



        if (!favoriteRepository.existsByUserAndProduct(user, product)) {

            Favorite newFavorite = Favorite.builder()
                    .user(user)
                    .product(product)
                    .build();
            favoriteRepository.save(newFavorite);
        }

    }
    public void deleteFavoriteProduct(String username, Integer productId) {

        User user = findUserByUsername(username);
        Product product = repository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));

        if (user == null) {
            throw new RuntimeException("User not found: " + username);
        }


        favoriteRepository.deleteByUserAndProduct(user, product);
    }
    public List<Product> getFavoriteProducts(String username) {

        if (username == null) {
            return Collections.emptyList();
        }
        User user = findUserByUsername(username);
        if (user == null) {
            return Collections.emptyList();
        }
        List<Favorite> favorites= favoriteRepository.findByUser(user);
        return favorites.stream().map(Favorite::getProduct).collect(Collectors.toList());
    }

    public User findUserByUsername(String username) {
        return authorizedRepo.findByUsername(username).orElse(null);
    }


}
