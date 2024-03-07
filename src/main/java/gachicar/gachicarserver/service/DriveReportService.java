package gachicar.gachicarserver.service;

import gachicar.gachicarserver.domain.DriveReport;
import gachicar.gachicarserver.repository.DriveReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DriveReportService {

    public final DriveReportRepository reportRepository;

    @Transactional
    public void createReport(DriveReport report) {
        reportRepository.save(report);
    }


}
