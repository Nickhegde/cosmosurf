package com.example.demo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;


@Entity
public class KWICEntry implements Comparable<KWICEntry> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String shiftedSentence;

    public KWICEntry() {
    }

    public KWICEntry(String shiftedSentence) {
        this.shiftedSentence = shiftedSentence;
    }

    public Long getId() {
        return id;
    }

    public String getShiftedSentence() {
        return shiftedSentence;
    }

    @Override
    public int compareTo(KWICEntry other) {
        return this.shiftedSentence.compareTo(other.shiftedSentence);
    }

    
}
