package com.myfeed.service.report;

import com.myfeed.exception.*;
import com.myfeed.model.post.BlockStatus;
import com.myfeed.model.post.Post;
import com.myfeed.model.post.PostDto;
import com.myfeed.model.reply.Reply;
import com.myfeed.model.report.ProcessStatus;
import com.myfeed.model.report.Report;
import com.myfeed.model.report.ReportDto;
import com.myfeed.model.report.ReportType;
import com.myfeed.repository.jpa.PostRepository;
import com.myfeed.repository.jpa.ReplyRepository;
import com.myfeed.repository.jpa.ReportRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportServiceImpl implements ReportService {
    @Autowired
    ReportRepository reportRepository;
    @Autowired
    ReplyRepository replyRepository;
    @Autowired
    PostRepository postRepository;

    // 신고 불러오기
    @Override
    public Report findByReportId(Long id) {
        return reportRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("신고 내역을 찾을 수 없습니다."));
    }

    // 신고 게시글 리스트 (동시성)
    @Override
    public Page<Report> getPagedReportsByPost(int page, Post post) {
        Pageable pageable = PageRequest.of(page - 1, PAGE_SIZE, Sort.by("createdDate").descending());
        Page<Report> reports = reportRepository.findPagedReportsByPost(post, pageable);

        for (Report report: reports) {
            if (post.getUser() == null || report.getPost().getUser().isDeleted()) {
                throw new UserDeletedException("삭제된 사용자의 댓글이 신고 리스트에 포함 되어 있습니다.");
            }
            if (post.getStatus() == BlockStatus.NORMAL_STATUS) {
                throw new PostUnBlockedException("차딘되지 않은 게시글이 존재 합니다.");
            }
        }

        List<Report> filteredReports = reports.getContent().stream()
                .filter(report -> report.getPost().getUser() != null && !report.getPost().getUser().isDeleted())
                .toList();

        return new PageImpl<>(filteredReports, pageable, reports.getTotalElements());
    }

    // 게시글 신고 내역 상세 보기
    @Override
    public List<Report> getReportsByPost(Long postId) {
        List<Report> reports = reportRepository.findReportByPostId(postId);
        Post post = postRepository.findById(postId).orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));

        for (Report report: reports) {
            if (post.getUser() == null || report.getPost().getUser().isDeleted()) {
                throw new UserDeletedException("삭제된 사용자의 댓글이 신고 리스트에 포함 되어 있습니다.");
            }
            if (post.getStatus() == BlockStatus.NORMAL_STATUS) {
                throw new PostUnBlockedException("차딘되지 않은 게시글이 존재 합니다.");
            }
        }

        return reports;
    }

    // 신고 댓글 리스트 (동시성)
    @Override
    public Page<Report> getPagedReportsByReply(int page, Reply reply) {
        Pageable pageable = PageRequest.of(page - 1, PAGE_SIZE, Sort.by("createdDate").descending());
        Page<Report> reports = reportRepository.findPagedReportsByReply(reply, pageable);

        for (Report report: reports) {
            if (reply.getUser() == null || report.getReply().getUser().isDeleted()) {
                throw new UserDeletedException("삭제된 사용자의 댓글이 신고 리스트에 포함 되어 있습니다.");
            }
            if (reply.getStatus() == BlockStatus.NORMAL_STATUS) {
                throw new ReplyUnBlockedException("차딘되지 않은 댓글이 존재 합니다.");
            }
        }

        List<Report> filteredReports = reports.getContent().stream()
                .filter(report -> report.getReply().getUser() != null && !report.getReply().getUser().isDeleted())
                .toList();

        return new PageImpl<>(filteredReports, pageable, reports.getTotalElements());
    }

    // 댓글 신고 내역 상세 보기
    @Override
    public List<Report> getReportsByReply(Long replyId) {
        List<Report> reports = reportRepository.findReportByReplyId(replyId);
        Reply reply = replyRepository.findById(replyId).orElseThrow(() -> new EntityNotFoundException("댓글을 찾을 수 없습니다."));

        for (Report report: reports) {
            if (reply.getUser() == null || report.getReply().getUser().isDeleted()) {
                throw new UserDeletedException("삭제된 사용자의 댓글이 신고 리스트에 포함 되어 있습니다.");
            }
            if (reply.getStatus() == BlockStatus.NORMAL_STATUS) {
                throw new ReplyUnBlockedException("차딘되지 않은 댓글이 존재 합니다.");
            }
        }

        return reports;
    }

    // 게시글 신고
    @Override
    public Report reportPost(Long postId, ReportDto reportDto) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));

        if (post.getUser() == null || post.getUser().isDeleted()) {
            throw new UserDeletedException("삭제된 사용자는 게시글을 신고할 수 없습니다.");
        }
        if (post.getStatus() == BlockStatus.BLOCK_STATUS) {
            throw new PostBlockedException("이미 차단된 게시글 입니다.");
        }

        Report report = Report.builder()
                .post(post).type(ReportType.valueOf(reportDto.getType()))
                .description(reportDto.getDescription()).status(ProcessStatus.PENDING)
                .build();

        return reportRepository.save(report);
    }

    // 댓글 신고
    @Override
    public Report reportReply(Long replyId, ReportDto reportDto) {
        Reply reply = replyRepository.findById(replyId).orElseThrow(() -> new EntityNotFoundException("댓글을 찾을 수 없습니다."));

        if (reply.getUser() == null || reply.getUser().isDeleted()) {
            throw new UserDeletedException("삭제된 사용자는 게시글을 신고할 수 없습니다.");
        }
        if (reply.getStatus() == BlockStatus.BLOCK_STATUS) {
            throw new ReplyBlockedException("이미 차단된 댓글 입니다.");
        }

        Report report = Report.builder()
                .reply(reply).type(ReportType.valueOf(reportDto.getType()))
                .description(reportDto.getDescription()).status(ProcessStatus.PENDING)
                .build();

        return reportRepository.save(report);
    }

    // 게시글 차단
    @Override
    public void BlockPost(Long id, Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));
        Report report = findByReportId(id);

        if (post.getUser() == null || post.getUser().isDeleted()) {
            throw new UserDeletedException("삭제된 사용자는 게시글을 차단할 수 없습니다.");
        }
        if (post.getStatus() == BlockStatus.BLOCK_STATUS) {
            throw new PostBlockedException("이미 차단된 게시글 입니다.");
        }
        if (report.getStatus() == ProcessStatus.COMPLETED) {
            throw new ReportCompletedException("신고 처리 완료된 게시글 입니다.");
        }

        post.setStatus(BlockStatus.BLOCK_STATUS);
        report.setStatus(ProcessStatus.COMPLETED);
        postRepository.save(post);
    }

    // 게시글 해제
    @Override
    public void unBlockPost(Long id, Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));
        Report report = findByReportId(id);

        if (post.getUser() == null || post.getUser().isDeleted()) {
            throw new UserDeletedException("삭제된 사용자는 게시글을 차단할 수 없습니다.");
        }
        if (post.getStatus() == BlockStatus.NORMAL_STATUS) {
            throw new PostUnBlockedException("이미 차단 해제된 게시글 입니다.");
        }
        if (report.getStatus() == ProcessStatus.PENDING) {
            throw new ReportPendingException("신고 접수 대기 중인 게시글 입니다.");
        }

        post.setStatus(BlockStatus.NORMAL_STATUS);
        report.setStatus(ProcessStatus.RELEASED);
        postRepository.save(post);
    }

    // 댓글 차단
    @Override
    public void BlockReply(Long id , Long replyId) {
        Reply reply = replyRepository.findById(replyId).orElseThrow(() -> new EntityNotFoundException("댓글을 찾을 수 없습니다."));
        Report report = findByReportId(id);

        if (reply.getUser() == null || reply.getUser().isDeleted()) {
            throw new UserDeletedException("삭제된 사용자는 게시글을 차단할 수 없습니다.");
        }
        if (reply.getStatus() == BlockStatus.BLOCK_STATUS) {
            throw new ReplyBlockedException("이미 차단된 댓글 입니다.");
        }
        if (report.getStatus() == ProcessStatus.COMPLETED) {
            throw new ReportCompletedException("신고 처리 완료된 댓글 입니다.");
        }

        reply.setStatus(BlockStatus.BLOCK_STATUS);
        report.setStatus(ProcessStatus.COMPLETED);
        replyRepository.save(reply);
    }

    // 댓글 해제
    @Override
    public void unBlockReply(Long id , Long replyId) {
        Reply reply = replyRepository.findById(replyId).orElseThrow(() -> new EntityNotFoundException("댓글을 찾을 수 없습니다."));
        Report report = findByReportId(id);

        if (reply.getUser() == null || reply.getUser().isDeleted()) {
            throw new UserDeletedException("삭제된 사용자는 게시글을 차단할 수 없습니다.");
        }
        if (reply.getStatus() == BlockStatus.NORMAL_STATUS) {
            throw new PostUnBlockedException("이미 차단 해제된 댓글 입니다.");
        }
        if (report.getStatus() == ProcessStatus.PENDING) {
            throw new ReportPendingException("신고 접수 대기 중인 댓글 입니다.");
        }

        reply.setStatus(BlockStatus.NORMAL_STATUS);
        report.setStatus(ProcessStatus.RELEASED);
        replyRepository.save(reply);
    }
}
