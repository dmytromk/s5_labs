package com.uni.common;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Data
public class Note implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String id;
    private String title;
    private String content;
    private Date lastModified;
    private int importance; // 1 to 5

    public Note(String title, String content, int importance) {
        this.id = String.valueOf(UUID.randomUUID());
        this.title = title;
        this.content = content;
        this.importance = importance;
        this.lastModified = new Date();
    }

    public Note(String id, String title, String content, int importance) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.importance = importance;
        this.lastModified = new Date();
    }

    public Note() {
        this.id = String.valueOf(UUID.randomUUID());
        this.lastModified = new Date();
    }

    @Override
    public String toString() {
        return "Note{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", lastModified=" + lastModified +
                ", importance=" + importance +
                '}';
    }
}
