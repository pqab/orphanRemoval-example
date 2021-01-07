package example.model;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.EAGER;

@AllArgsConstructor
@Builder
@Data
@Entity
@NoArgsConstructor
@Table(name = "SHOP")
@ToString
public class Shop {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "NAME")
    private String name;

    @Version
    @Column(name = "VERSION")
    private Long version;

    @OneToMany(mappedBy = "shop", cascade = ALL, fetch = EAGER, orphanRemoval = true)
    @EqualsAndHashCode.Exclude
    @Builder.Default
    private List<Product> productList = new ArrayList<>();

    public void setProductList(List<Product> productList) {
        for (Product product : productList) {
            product.setShop(this);
        }
        this.productList = productList;
    }

    public void addProduct(Product product) {
        product.setShop(this);
        productList.add(product);
    }

    public void addAllProduct(List<Product> product) {
        for (Product p : product) {
            addProduct(p);
        }
    }
}
