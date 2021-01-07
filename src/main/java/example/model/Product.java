package example.model;

import lombok.*;

import javax.persistence.*;

import static javax.persistence.CascadeType.ALL;

@AllArgsConstructor
@Builder
@Data
@Entity
@NoArgsConstructor
@Table(name = "PRODUCT")
@ToString
public class Product {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "NAME")
    private String name;

    @ToString.Exclude
    @ManyToOne(targetEntity = Shop.class, cascade = ALL)
    @JoinColumn(name = "SHOP_ID", referencedColumnName = "ID")
    private Shop shop;

    @Version
    @Column(name = "VERSION")
    private Long version;
}
