package com.example.schoolfes_reserve.servicec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.schoolfes_reserve.entity.Reservation;
import com.example.schoolfes_reserve.entity.SystemStatus;
import com.example.schoolfes_reserve.repository.SystemStatusRepository;

@Service
public class SystemService {
    
    @Autowired
    private SystemStatusRepository systemStatusRepository;
    
    @Autowired
    private ReservationService reservationService;
    
    @Autowired
    private EmailService emailService;
    
    // 現在のシステム状態を取得
    public SystemStatus getCurrentStatus() {
        return systemStatusRepository.findById(1L);
    }
    
    // 次の番号を呼び出し
    public void callNextNumber() {
        SystemStatus status = getCurrentStatus();
        status.setCurrentNumber(status.getCurrentNumber() + 1);
        systemStatusRepository.save(status);
        
        // 現在呼ばれた番号の予約を「案内中」に更新
        reservationService.updateReservationStatusByTicketNumber(status.getCurrentNumber(), "案内中");
        
        // メール通知処理を呼び出し
        sendNotificationEmails(status.getCurrentNumber());
    }
    
    // 現在の番号を完了にして次を呼び出し
    public void completeCurrentAndCallNext() {
        SystemStatus status = getCurrentStatus();
        
        // 現在の番号を完了に更新
        if (status.getCurrentNumber() > 0) {
            reservationService.updateReservationStatusByTicketNumber(status.getCurrentNumber(), "完了");
        }
        
        // 次の番号を呼び出し
        callNextNumber();
    }
    
    // 順番が近づいた人にメール送信
    private void sendNotificationEmails(int currentNumber) {
        // 現在の番号+2の人にメール送信（2人前になったら通知）
        int targetNumber = currentNumber + 2;
        
        Reservation reservation = reservationService.findByTicketNumber(targetNumber);
        
        if (reservation != null && !reservation.getEmailSent()) {
            emailService.sendNotificationEmail(
                reservation.getEmail(), 
                reservation.getName(), 
                reservation.getTicketNumber()
            );
            
            // メール送信フラグを更新
            reservationService.updateEmailSent(reservation.getId(), true);
        }
    }
}
