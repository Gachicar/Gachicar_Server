package gachicar.gachicarserver.repository;

import gachicar.gachicarserver.domain.DriveReport;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
@RequiredArgsConstructor
public class DriveReportRepository {

    @PersistenceContext
    private final EntityManager em;

    public void save(DriveReport driveReport) {
        em.persist(driveReport);
    }
}
