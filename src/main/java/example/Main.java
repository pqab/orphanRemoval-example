package example;

import org.hibernate.reactive.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import example.model.Product;
import example.model.Shop;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Main {

    private static Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String args[]) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("mysql");
        Stage.SessionFactory sessionFactory = emf.unwrap(Stage.SessionFactory.class);

        sessionFactory.withTransaction((session, transaction) -> {
            Shop s1 = Shop.builder().name("s1").build();
            s1.addProduct(Product.builder().name("ap1").build());
            s1.addProduct(Product.builder().name("ap2").build());
            s1.addProduct(Product.builder().name("ap3").build());
            s1.addProduct(Product.builder().name("ap4").build());

            logger.info("s1: {}", s1);

            // create
            return session.merge(s1)
                        .thenCombine(session.flush(), (s, v) -> s);
        })
        .thenCompose(t1 -> {
            logger.info("t1: {}", t1);

            return sessionFactory.withTransaction((session, transaction) -> {
                // get
                return session.find(Shop.class, t1.getId())
                        .thenCompose(s2 -> {
                            logger.info("s2: {}", s2);

                            // update
                            s2.setName("s2");
                            s2.getProductList().clear();
                            s2.addProduct(Product.builder().name("bp5").build());
                            s2.addProduct(Product.builder().name("bp6").build());
                            s2.addProduct(Product.builder().name("bp7").build());

                            logger.info("s2: {}", s2);

                            // update
                            return session.merge(s2);
                        })
                        .thenCombine(session.flush(), (s, v) -> s);
            });
        })
        .thenCompose(t2 -> {
            logger.info("t2: {}", t2);
            System.exit(0);
            return null;
        })
        .exceptionally(e -> {
            e.printStackTrace();
            System.exit(1);
            return null;
        });
    }
}
