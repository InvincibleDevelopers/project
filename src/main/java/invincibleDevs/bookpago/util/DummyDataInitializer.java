package invincibleDevs.bookpago.util;

import com.github.javafaker.Faker;
import invincibleDevs.bookpago.Users.model.UserEntity;
import invincibleDevs.bookpago.Users.repository.UserRepository;
import invincibleDevs.bookpago.book.BookRepository;
import invincibleDevs.bookpago.common.GeometryUtil;
import invincibleDevs.bookpago.profile.ProfileRepository;
import invincibleDevs.bookpago.profile.model.Profile;
import invincibleDevs.bookpago.readingClub.model.ReadingClub;
import invincibleDevs.bookpago.readingClub.model.ReadingClubMembers;
import invincibleDevs.bookpago.readingClub.repository.ReadingClubMembersRepository;
import invincibleDevs.bookpago.readingClub.repository.ReadingClubRepository;
import invincibleDevs.bookpago.review.Review;
import invincibleDevs.bookpago.review.ReviewRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import org.locationtech.jts.geom.Point;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "dummy.data.initialize", havingValue = "true", matchIfMissing = false)
public class DummyDataInitializer {

    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final BookRepository bookRepository;
    private final ReviewRepository reviewRepository;
    private final ReadingClubRepository readingClubRepository;
    private final ReadingClubMembersRepository readingClubMembersRepository;

    // Java Faker 인스턴스 생성
    private final Faker faker = new Faker(new Locale("ko"));

    public DummyDataInitializer(UserRepository userRepository, ProfileRepository profileRepository,
            BookRepository bookRepository, ReviewRepository reviewRepository,
            ReadingClubRepository readingClubRepository,
            ReadingClubMembersRepository readingClubMembersRepository) {
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
        this.bookRepository = bookRepository;
        this.reviewRepository = reviewRepository;
        this.readingClubRepository = readingClubRepository;
        this.readingClubMembersRepository = readingClubMembersRepository;
    }

    @Bean
    @Transactional
    public CommandLineRunner loadDummyData() {
        return args -> {
            int numberOfDummyData = 10; // 생성할 더미 데이터 개수

            // 1. UserEntity 및 Profile 생성
            List<Profile> profiles = generateUserAndProfileData(numberOfDummyData);

//            // 2. Book 데이터 생성
//            List<Book> books = generateBookData(numberOfDummyData);
//
//            // 3. ReadingClub 데이터 생성
            List<ReadingClub> readingClubs = generateReadingClubData(numberOfDummyData);
//
//            // 4. ReadingClubMap 데이터 생성
            List<ReadingClubMembers> readingClubMembers = generateReadingClubMapData(
                    numberOfDummyData,
                    readingClubs, profiles);
//
//            // 5. Review 데이터 생성
            List<Review> reviews = generateReviewData(numberOfDummyData, profiles);
//
//            System.out.println("All dummy data generation complete.");
        };
    }

    private List<Review> generateReviewData(int numberOfDummyData, List<Profile> profiles) {
        List<Review> reviews = new ArrayList<>();
        long isbn = 9780306406157L;

        for (int i = 0; i < numberOfDummyData; i++) {
            Review review = Review.builder()
                                  .isbn(isbn + i - 0)
                                  .rating(3.5)
                                  .content("좋은책")
                                  .profile(profiles.get(i))
                                  .build();

            reviews.add(review);
        }
        reviewRepository.saveAll(reviews);
        return reviews;
    }

    private List<ReadingClubMembers> generateReadingClubMapData(int numberOfDummyData,
            List<ReadingClub> readingClubs, List<Profile> profiles) {
        List<ReadingClubMembers> readingClubMembersList = new ArrayList<>();
        for (int i = 1; i <= numberOfDummyData; i++) {
            ReadingClubMembers readingClubMembers = ReadingClubMembers.builder()
                                                                      .readingClub(readingClubs.get(
                                                                              i - 1))
                                                                      .clubMember(
                                                                              profiles.get(i - 1))
                                                                      .build();

            readingClubMembersList.add(readingClubMembers);
        }
        readingClubMembersRepository.saveAll(readingClubMembersList);
        return readingClubMembersList;
    }

    private List<ReadingClub> generateReadingClubData(int numberOfDummyData) {
        List<ReadingClub> readingClubs = new ArrayList<>();
        // List<Integer>로 요일을 나타내는 숫자 목록 생성
        List<Integer> weekDay = new ArrayList<>();

        // 예시로 1 = Monday, 2 = Tuesday ... 7 = Sunday
        weekDay.add(1); // Monday
        weekDay.add(2); // Tuesday
        weekDay.add(3); // Wednesday
        weekDay.add(4); // Thursday
        weekDay.add(5); // Friday
        weekDay.add(6); // Saturday
        weekDay.add(7); // Sunday

        for (int i = 1; i <= numberOfDummyData; i++) {
            String clubName = "Club" + i;

            // 위도와 경도를 기반으로 Point 생성
            double latitude = 47.785834 + (i * 0.1);
            double longitude = -102.406417 - (i * 0.1);
            Point location = GeometryUtil.createPoint(latitude, longitude);

            ReadingClub readingClub = ReadingClub.builder()
                                                 .clubName(clubName)
                                                 .location(location)
                                                 .description("club description " + i)
                                                 .time("meeting time " + i)
                                                 .repeatCycle(3)
                                                 .weekDay(weekDay)
                                                 .build();
            readingClubs.add(readingClub);
        }
        readingClubRepository.saveAll(readingClubs);
        return readingClubs;

    }

    // UserEntity 및 Profile 생성 메소드
    private List<Profile> generateUserAndProfileData(int numberOfDummyData) {
        List<UserEntity> users = new ArrayList<>();
        List<Profile> profiles = new ArrayList<>();
        Set<Long> generatedKakaoIds = new HashSet<>();

        for (int i = 0; i < numberOfDummyData; i++) {
            long kakaoId;
            do {
                kakaoId = (long) faker.number().numberBetween(1000000, 9999999);
            } while (generatedKakaoIds.contains(kakaoId) || userRepository.existsByKakaoId(
                    kakaoId));

            generatedKakaoIds.add(kakaoId);

            UserEntity user = UserEntity.builder()
                                        .kakaoId(kakaoId)
                                        .password(faker.internet().password())
                                        .nickname(generateNickname())
                                        .role("USER")
                                        .gender(convertGender(faker.demographic().sex()))
                                        .age(faker.number().numberBetween(18, 60))
                                        .created_at(LocalDateTime.now())
                                        .build();

            Profile profile = Profile.builder()
                                     .userEntity(user)
                                     .profileImgUrl(
                                             "https://invincibledevs.s3.amazonaws.com/60b21208-f4c1-40fa-ab3f-3b4c8f8dd58f_2024100415374448023_1728023863.jpg?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Date=20241006T075519Z&X-Amz-SignedHeaders=host&X-Amz-Expires=86400&X-Amz-Credential=AKIATKHCK2JANRTZDEMR%2F20241006%2Fus-east-1%2Fs3%2Faws4_request&X-Amz-Signature=9591f9ecdf894da6dacf370be71b663420215db8b40db9b9d9b6e70702cffaf4")
                                     .nickName(user.getNickname())
                                     .introduce(generateKoreanIntroduction())
                                     .build();

            users.add(user);
            profiles.add(profile);
        }

        // 데이터베이스에 저장
        userRepository.saveAll(users);
        profileRepository.saveAll(profiles);

        System.out.println("Dummy data generation complete.");

        return profiles;
    }

//    // Book 데이터 생성 메소드
//    private List<Book> generateBookData(int numberOfDummyData) {
//        List<Book> books = new ArrayList<>();
//        for (int i = 0; i < numberOfDummyData; i++) {
//            Book book = new Book(
//                    (long) faker.number().numberBetween(1000000000, 9999999999L), // 임의의 ISBN 생성
//                    faker.number().randomFloat(2, 3, 5) // 임의의 평점 생성
//            );
//            books.add(book);
//        }
//
//        // 데이터베이스에 저장
//        bookRepository.saveAll(books);
//
//        return books;
//    }

    private static String convertGender(String gender) {
        if ("Female".equalsIgnoreCase(gender)) {
            return "F";
        } else if ("Male".equalsIgnoreCase(gender)) {
            return "M";
        } else {
            return "U";  // Unspecified 또는 Unknown
        }
    }

    private String generateNickname() {
        String firstName = faker.name().lastName();
        String lastName = faker.name().firstName();

        return (firstName + lastName).replace(" ", "");
    }

    private String generateKoreanIntroduction() {
        String[] introductions = {
                "안녕하세요, 저는 책을 사랑하는 사람입니다.",
                "독서를 통해 지식을 넓히는 것을 좋아합니다.",
                "여러분과 함께 독서 모임을 만들어가고 싶습니다.",
                "서로의 생각을 나누며 성장하고 싶습니다.",
                "책은 제 인생의 동반자입니다.",
                "다양한 책을 읽고 새로운 관점을 배우고 싶습니다."
        };
        return introductions[faker.number().numberBetween(0, introductions.length)];
    }
}
