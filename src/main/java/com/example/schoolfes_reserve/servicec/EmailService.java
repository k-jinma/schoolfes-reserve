package com.example.schoolfes_reserve.servicec;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }
    
    public void sendNotificationEmail(String to, String name, int ticketNumber) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("あなたのgmail@gmail.com"); // 送信者メール
            message.setTo(to);                          // 受信者
            message.setSubject("【KATWALK体験】もうすぐあなたの番です");
            message.setText(
                name + "様\n\n" +
                "番号：" + ticketNumber + "\n" +
                "もうすぐあなたの順番です。\n" +
                "ブースまでお越しください。\n\n" +
                "KATWALK体験ブース"
            );
            
            mailSender.send(message);
            System.out.println("メール送信成功: " + to + " (番号:" + ticketNumber + ")");
            
        } catch (Exception e) {
            System.err.println("メール送信失敗: " + e.getMessage());
            // エラーが発生してもシステムは継続
        }
    }
    
    // テスト用メール送信
    public void sendTestEmail(String to) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("jinma.kazuhiro@gmail.com");
            message.setTo(to);
            message.setSubject("【テスト】KATWALK予約システム");
            message.setText("これはテストメールです。");
            
            mailSender.send(message);
            System.out.println("テストメール送信成功: " + to);
            
        } catch (Exception e) {
            System.err.println("テストメール送信失敗: " + e.getMessage());
        }
    }
}
