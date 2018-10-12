package com.zuehlke.rbe.springbootwiki.domain;


import java.util.Objects;

public class Wiki {

    private Page root;

    public Wiki(Wiki other) {
        this.root = other.root;
    }

    public Wiki() {
        root = new Page();
        root.setTitle("Root page");
        root.setContent("Hallo Welt");
    }

    public Page getRoot() {
        return root;
    }

    public void addPage(Page parent, Page newPage) {
        addPage(root, parent, newPage);
    }

    public void removePage(Page page) {
        removePage(root, page);
    }

    private void removePage(Page currentRoot, Page page) {
        if (currentRoot.getSubpages().contains(page)) {
            currentRoot.removeSubpage(page);
        } else {
            currentRoot.getSubpages().forEach(sub -> removePage(sub, page));
        }
    }


    private void addPage(Page currentRoot, Page parent, Page newPage) {
        if (currentRoot == parent) {
            currentRoot.addSubpage(newPage);
        } else {
            currentRoot.getSubpages().forEach(sub -> addPage(sub, parent, newPage));
        }
    }

    public Page getPage(Long id) {
        return getPage(root, id);
    }

    private Page getPage(Page currentRoot, long id) {
        if (currentRoot.getIdentifier() == id) {
            return currentRoot;
        }
        return currentRoot.getSubpages().stream()
                .map(sub -> getPage(sub, id))
                .filter(Objects::nonNull)
                .findAny()
                .orElse(null);
    }
}
