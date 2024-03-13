package gachicar.gachicarserver.repository;

import gachicar.gachicarserver.domain.DriveReport;
import gachicar.gachicarserver.domain.User;
import gachicar.gachicarserver.dto.UsageCountsDto;
import gachicar.gachicarserver.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class DriveReportRepository {

    @PersistenceContext
    private final EntityManager em;

    public void save(DriveReport driveReport) {
        em.persist(driveReport);
    }

    // 사용자의 가장 최근 주행 리포트 조회
    public DriveReport findRecentByUser(Long userId) {
        try {
            String jpql = "SELECT dr FROM DriveReport dr " +
                    "WHERE dr.user.id = :userId " +
                    "ORDER BY dr.startTime DESC";

            return em.createQuery(jpql, DriveReport.class)
                    .setParameter("userId", userId)
                    .setMaxResults(1)
                    .getSingleResult();

        } catch (NoResultException e) {
            return null;
        }
    }

    // 그룹 내 최다 사용자 조회
    public User findUserWithMostUsageForCar(Long carId) {
        try {
            return em.createQuery("SELECT dr.user FROM DriveReport dr " +
                    "WHERE dr.car.id = :carId " +
                    "GROUP BY dr.user ORDER BY COUNT(dr.user) DESC", User.class)
                    .setParameter("carId", carId)
                    .setMaxResults(1)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    // 그룹원별 공유차량 사용 횟수 조회
    public List<UsageCountsDto> getUserUsageCountsForCar(Long carId) {
        try {
            List<Object[]> results = em.createQuery("SELECT dr.user, COUNT(dr) FROM DriveReport dr " +
                            "WHERE dr.car.id = :carId " +
                            "GROUP BY dr.user", Object[].class)
                    .setParameter("carId", carId)
                    .getResultList();

            List<UsageCountsDto> countsDtoList = new ArrayList<>();
            for (Object[] result : results) {
                UserDto userDto = new UserDto((User) result[0]);
                Long count = (Long) result[1];
                UsageCountsDto usageCountsDto = new UsageCountsDto(userDto, count);
                countsDtoList.add(usageCountsDto);
            }

            return countsDtoList;
        } catch (NoResultException e) {
            return null;
        }
    }

    // 특정 사용자의 주행 리포트 전체 조회
    public List<DriveReport> findAllByUser(Long userId) {
        try {
            return em.createQuery(
                            "SELECT dr FROM DriveReport dr " +
                                    "WHERE dr.user.id = :userId", DriveReport.class)
                    .setParameter("userId", userId)
                    .getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }

}
