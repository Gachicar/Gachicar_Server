package gachicar.gachicarserver.repository;

import gachicar.gachicarserver.domain.Car;
import gachicar.gachicarserver.domain.DriveReport;
import gachicar.gachicarserver.domain.ReportStatus;
import gachicar.gachicarserver.domain.User;
import gachicar.gachicarserver.dto.UsageCountsDto;
import gachicar.gachicarserver.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
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
    public DriveReport findRecentByUser(Long userId, ReportStatus type) {
        try {
            String jpql = "SELECT dr FROM DriveReport dr " +
                    "WHERE dr.user.id = :userId AND dr.type = :type " +
                    "ORDER BY dr.startTime DESC";

            return em.createQuery(jpql, DriveReport.class)
                    .setParameter("userId", userId)
                    .setParameter("type", type)
                    .setMaxResults(1)
                    .getSingleResult();

        } catch (NoResultException e) {
            return null;
        }
    }

    // 그룹 내 최다 사용자 조회
    public UsageCountsDto findUserWithMostUsageForCar(Long carId) {
        try {
            Object[] result = em.createQuery("SELECT dr.user, COUNT(dr)  FROM DriveReport dr " +
                    "WHERE dr.car.id = :carId AND dr.type = :type " +
                    "GROUP BY dr.user ORDER BY COUNT(dr.user) DESC", Object[].class)
                    .setParameter("carId", carId)
                    .setParameter("type", ReportStatus.COMPLETE)
                    .setMaxResults(1)
                    .getSingleResult();

            UserDto userDto = new UserDto((User) result[0]);
            Long count = (Long) result[1];

            return new UsageCountsDto(userDto, count);

        } catch (NoResultException e) {
            return null;
        }
    }

    // 그룹원별 공유차량 사용 횟수 조회
    public List<UsageCountsDto> getUserUsageCountsForCar(Long carId) {
        try {
            List<Object[]> results = em.createQuery("SELECT dr.user, COUNT(dr) FROM DriveReport dr " +
                            "WHERE dr.car.id = :carId AND dr.type = :type " +
                            "GROUP BY dr.user", Object[].class)
                    .setParameter("carId", carId)
                    .setParameter("type", ReportStatus.COMPLETE)
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

    // 특정 사용자의 주행 리포트 또는 예약 내역 전체 조회
    public List<DriveReport> findAllByUser(Long userId, ReportStatus type) {
        try {
            return em.createQuery(
                            "SELECT dr FROM DriveReport dr " +
                                    "WHERE dr.user.id = :userId AND dr.type = :type", DriveReport.class)
                    .setParameter("userId", userId)
                    .setParameter("type", type)
                    .getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }

    // 특정 그룹의 주행 리포트 또는 예약 내역 전체 조회
    public List<DriveReport> findAllByGroup(Long carId, ReportStatus type) {
        try {
            return em.createQuery(
                            "SELECT dr FROM DriveReport dr " +
                                    "WHERE dr.car.id = :carId AND dr.type = :type", DriveReport.class)
                    .setParameter("carId", carId)
                    .setParameter("type", type)
                    .getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }


    // 시작시간보다 늦게 끝나는 예약이 있는지 확인
    public boolean existsByStartTimeAndUserCar(LocalDateTime startTime, Car car) {
        List<Long> result = em.createQuery("SELECT COUNT(r) FROM DriveReport r " +
                        "WHERE r.endTime > :startTime AND r.car = :car AND r.type = :type", Long.class)
                .setParameter("startTime", startTime)
                .setParameter("car", car)
                .setParameter("type", ReportStatus.RESERVE)
                .getResultList();
        return !result.isEmpty() && result.get(0) > 0;
    }

    // 종료시간보다 일찍 시작하는 예약이 있는지 확인
    public boolean existsByEndTimeAndUserCar(LocalDateTime endTime, Car car) {
        List<Long> result = em.createQuery("SELECT COUNT(r) FROM DriveReport r " +
                        "WHERE r.endTime < :startTime AND r.car = :car AND r.type = :type", Long.class)
                .setParameter("startTime", endTime)
                .setParameter("car", car)
                .setParameter("type", ReportStatus.RESERVE)
                .getResultList();
        return !result.isEmpty() && result.get(0) > 0;
    }

    public String getFavoriteDestination(Long carId) {
        return (String) em.createQuery("SELECT d.destination " +
                        "FROM DriveReport d " +
                        "WHERE d.car.id = :carId AND d.type = :type " +
                        "GROUP BY d.destination " +
                        "ORDER BY COUNT(d.destination) DESC")
                .setParameter("carId", carId)
                .setParameter("type", ReportStatus.COMPLETE)
                .setMaxResults(1) // 최대 결과를 1개로 제한하여 가장 많은 목적지 1개만 반환합니다.
                .getSingleResult();
    }

    // 특정 시간 사이의 예약 내역 조회
    public List<DriveReport> findByReservationTimeBetween(LocalDateTime now, LocalDateTime end) {
        return em.createQuery(
                "SELECT d FROM DriveReport d WHERE d.type = :type AND " +
                        "d.startTime BETWEEN :startTime AND :endTime", DriveReport.class)
                .setParameter("type", ReportStatus.RESERVE)
                .setParameter("startTime", now)
                .setParameter("endTime", end)
                .getResultList();
    }

}