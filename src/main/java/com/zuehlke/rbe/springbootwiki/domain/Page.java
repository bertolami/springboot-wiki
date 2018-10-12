package com.zuehlke.rbe.springbootwiki.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class Page  {
    private static final AtomicLong counter = new AtomicLong();
    private final long id;
    private String title;
    private String content;

    @JsonIgnore
    private List<Page> subpages = new ArrayList<>();

    public Page() {
        id = counter.getAndIncrement();
    }

    public long getIdentifier() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void addSubpage(Page page) {
        this.subpages.add(page);
    }

    public void removeSubpage(Page page) {
        this.subpages.remove(page);
    }

    public List<Page> getSubpages() {
        return subpages;
    }
}
