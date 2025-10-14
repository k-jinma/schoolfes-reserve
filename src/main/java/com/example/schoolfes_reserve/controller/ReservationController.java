package com.example.schoolfes_reserve.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.schoolfes_reserve.entity.Reservation;
import com.example.schoolfes_reserve.entity.SystemStatus;
import com.example.schoolfes_reserve.servicec.EmailService;
import com.example.schoolfes_reserve.servicec.ReservationService;
import com.example.schoolfes_reserve.servicec.SystemService;

@Controller
public class ReservationController {

    private final SystemService systemService;

    private ReservationService reservationService;
    
    private EmailService emailService;

    public ReservationController(SystemService systemService,ReservationService reservationService,EmailService emailService) {
        this.systemService = systemService;
        this.reservationService = reservationService;
        this.emailService = emailService;
    }
    
    // 1. 予約画面（一般利用者用）
    @GetMapping("/")
    public String index(Model model) {
        // 待ち時間の目安を表示
        int waitingCount = reservationService.getWaitingCount();
        SystemStatus status = systemService.getCurrentStatus();
        
        model.addAttribute("waitingCount", waitingCount);
        model.addAttribute("currentNumber", status.getCurrentNumber());
        model.addAttribute("estimatedWaitTime", waitingCount * 20); // 1人5分想定
        
        return "index";
    }
    
    // 2. 予約作成
    @PostMapping("/reserve")
    public String createReservation(@RequestParam("name") String name, 
                                  @RequestParam("email") String email, 
                                  Model model) {
        // 入力チェック（名前は10文字以内）
        if (name == null || name.trim().isEmpty() || 
            email == null || email.trim().isEmpty()) {
            model.addAttribute("error", "名前とメールアドレスを入力してください");
            return "index";
        }
        
        if (name.trim().length() > 10) {
            model.addAttribute("error", "名前は10文字以内で入力してください");
            return "index";
        }
        
        try {
            Reservation reservation = reservationService.createReservation(name.trim(), email.trim());
            model.addAttribute("reservation", reservation);
            return "ticket";
        } catch (Exception e) {
            model.addAttribute("error", "予約に失敗しました。もう一度お試しください。");
            return "index";
        }
    }
    
    // 3. モニター表示画面（ブース前）
    @GetMapping("/monitor")
    public String monitor(Model model) {
        SystemStatus status = systemService.getCurrentStatus();
        model.addAttribute("currentNumber", status.getCurrentNumber());
        return "monitor";
    }
    
    // 4. 管理画面（スタッフ用）
    @GetMapping("/admin")
    public String admin(Model model) {
        List<Reservation> waitingList = reservationService.getWaitingList();
        List<Reservation> allReservations = reservationService.getAllReservations();
        SystemStatus status = systemService.getCurrentStatus();
        
        model.addAttribute("waitingList", waitingList);
        model.addAttribute("allReservations", allReservations);
        model.addAttribute("currentNumber", status.getCurrentNumber());
        model.addAttribute("nextNumber", status.getNextNumber());
        
        return "admin";
    }
    
    // 5. 次の番号を呼び出し（スタッフ操作）
    @PostMapping("/admin/next")
    public String callNext() {
        systemService.completeCurrentAndCallNext();
        return "redirect:/admin";
    }

    //6. 前の番号を呼び出し　(スタッフ操作)
    @PostMapping("/admin/previous")
    public String callPrevious() {
        systemService.callPreviousNumber();
        return "redirect:/admin";
    }

    
    // 7. テスト用メール送信
    @GetMapping("/test-email")
    @ResponseBody
    public String testEmail(@RequestParam("email") String email) {
        try {
            emailService.sendTestEmail(email);
            return "テストメール送信完了: " + email;
        } catch (Exception e) {
            return "テストメール送信失敗: " + e.getMessage();
        }
    }

//     @GetMapping("/api/waiting-count")
//     @ResponseBody
//     public int getWaitingCount() {
//         return reservationService.getWaitingCount();
// }

    @GetMapping("/api/waiting-count")
    @ResponseBody
    public int waitingCount() {
        return systemService.countRemainingAfterCurrent();
    }
}