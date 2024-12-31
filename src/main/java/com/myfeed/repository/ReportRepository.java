package com.myfeed.repository;

import com.myfeed.model.report.ProcessStatus;
import com.myfeed.model.report.Report;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ReportRepository extends JpaRepository<Report, Long> {
//    Page<Report> findReportByPendingStatus(Pageable pageable, ProcessStatus status);
//
//    Page<Report> findReportByCompletedStatus(Pageable pageable, ProcessStatus status);
    Page<Report> findReportByProcessStatus(Pageable pageable, ProcessStatus status);


}
