package com.example.schoolfes_reserve.servicec;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.schoolfes_reserve.entity.Reservation;
import com.example.schoolfes_reserve.entity.SystemStatus;
import com.example.schoolfes_reserve.repository.ReservationRepository;
import com.example.schoolfes_reserve.repository.SystemStatusRepository;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;

    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public int countRemainingAfter(int currentNumber) {
        return reservationRepository.countRemainingAfter(currentNumber);
    }
    
    @Autowired
    private SystemStatusRepository systemStatusRepository;
    
    // 新規予約作成
    public Reservation createReservation(String name, String email) {
        SystemStatus status = systemStatusRepository.findById(1L);
        
        // 新しい予約を作成
        Reservation reservation = new Reservation(name, email, status.getNextNumber());
        
        // 予約を保存
        reservationRepository.save(reservation);
        
        // 次の番号を更新
        status.setNextNumber(status.getNextNumber() + 1);
        systemStatusRepository.save(status);
        
        return reservation;
    }
    
    // 待機中の人数を取得
    public int getWaitingCount() {
        return reservationRepository.countWaitingReservations();
    }
    
    // 待機中の予約リストを取得
    public List<Reservation> getWaitingList() {
        return reservationRepository.findWaitingList();
    }
    
    // 全ての予約を取得
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }
    
    // 番号で予約を検索
    public Reservation findByTicketNumber(int ticketNumber) {
        return reservationRepository.findByTicketNumber(ticketNumber);
    }
    
    // 予約状況を更新
    public void updateReservationStatus(Long id, String status) {
        reservationRepository.updateStatus(id, status);
    }
    
    // 番号で予約状況を更新
    public void updateReservationStatusByTicketNumber(int ticketNumber, String status) {
        reservationRepository.updateStatusByTicketNumber(ticketNumber, status);
    }
    
    // メール送信フラグを更新
    public void updateEmailSent(Long id, boolean emailSent) {
        reservationRepository.updateEmailSent(id, emailSent);
    }
}
