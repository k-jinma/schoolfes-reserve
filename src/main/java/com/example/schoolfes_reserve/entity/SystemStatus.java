package com.example.schoolfes_reserve.entity;

public class SystemStatus {
    private Long id;
    private Integer currentNumber;
    private Integer nextNumber;
    
    // デフォルトコンストラクタ
    public SystemStatus() {}
    
    // コンストラクタ
    public SystemStatus(Long id, Integer currentNumber, Integer nextNumber) {
        this.id = id;
        this.currentNumber = currentNumber;
        this.nextNumber = nextNumber;
    }
    
    // getter/setter
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Integer getCurrentNumber() {
        return currentNumber;
    }
    
    public void setCurrentNumber(Integer currentNumber) {
        this.currentNumber = currentNumber;
    }
    
    public Integer getNextNumber() {
        return nextNumber;
    }
    
    public void setNextNumber(Integer nextNumber) {
        this.nextNumber = nextNumber;
    }
}
