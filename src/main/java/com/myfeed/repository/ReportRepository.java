package com.myfeed.repository;

import com.myfeed.model.report.Report;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ReportRepository extends JpaRepository<Report, Long> {
    Page<Report> findByPostPid(long pid, Pageable pageable);

    Page<Report> findByReplyRid(long rid, Pageable pageable);
}
