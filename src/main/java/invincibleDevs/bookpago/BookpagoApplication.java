package invincibleDevs.bookpago;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
@SpringBootApplication
public class BookpagoApplication {

    public static void main(String[] args) {

        SpringApplication.run(BookpagoApplication.class, args);
    }

//    @Bean
//    public CommandLineRunner loadData(EntityManagerFactory emf) {
//        return args -> {
//            Faker faker = new Faker();
//            EntityManager em = emf.createEntityManager();
//            EntityTransaction transaction = em.getTransaction();
//
//            transaction.begin();
//
//            try {
//                for (int i = 1; i <= 1000000; i++) {
//                    UserEntity userEntity = UserEntity.builder()
//                                                      .kakaoId(faker.number().randomNumber())
//                                                      .password(faker.internet().password())
//                                                      .nickname(faker.name().username())
//                                                      .role("USER")
//                                                      .gender(faker.demographic().sex())
//                                                      .age(faker.number().numberBetween(18, 80))
//                                                      .created_at(LocalDateTime.now())
//                                                      .build();
//
//                    Profile profile = new Profile();
//                    profile.setUserEntity(userEntity);
//                    userEntity.setProfile(profile);
//
//                    em.persist(userEntity);
//
//                    if (i % 1000 == 0) {
//                        em.flush();
//                        em.clear();
//                        System.out.println(i + " records inserted");
//                    }
//                }
//
//                transaction.commit();
//                System.out.println("Data insertion completed!");
//
//            } catch (Exception e) {
//                transaction.rollback();
//                e.printStackTrace();
//            } finally {
//                em.close();
//            }
//        };
//    }

}
