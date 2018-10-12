package com.zuehlke.rbe.springbootwiki;

import com.zuehlke.rbe.springbootwiki.domain.Page;
import com.zuehlke.rbe.springbootwiki.domain.Wiki;
import org.springframework.hateoas.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
public class WikiController {

    public static Wiki staticWiki = new Wiki();

    @GetMapping(value = "/wiki", produces = {MediaType.APPLICATION_JSON_VALUE, "application/hal+json"})
    public ResponseEntity<Resource<Page>> root() {
        return page(staticWiki.getRoot().getIdentifier());
    }

    @GetMapping(value = "/wiki/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, "application/hal+json"})
    public ResponseEntity<Resource<Page>> page(@PathVariable(value = "id") long id) {
        Page page = staticWiki.getPage(id);
        if (page == null) {
            return ResponseEntity.notFound().build();
        }
        return toPageResource(page);
    }

    private ResponseEntity<Resource<Page>> toPageResource(Page page) {
        Resource<Page> resource = new Resource<>(page);
        resource.add(linkTo(methodOn(WikiController.class).root()).withRel("root"));
        resource.add(linkTo(methodOn(WikiController.class).page(page.getIdentifier())).withSelfRel());
        page.getSubpages().forEach(subpage -> resource.add(linkTo(methodOn(WikiController.class).page(subpage.getIdentifier())).withRel("sub-" + subpage.getIdentifier())));
        return ResponseEntity.ok(resource);
    }


    @PostMapping(value = "/wiki/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, "application/hal+json"})
    private ResponseEntity<Resource<Page>> postPage(@PathVariable(value = "id") long id, @RequestBody Page page) {
        Page parent = staticWiki.getPage(id);
        if (parent == null) {
            return ResponseEntity.notFound().build();
        }
        parent.addSubpage(page);
        return toPageResource(page);
    }


    @PutMapping(value = "/wiki/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, "application/hal+json"})
    private ResponseEntity<Resource<Page>> put(@PathVariable(value = "id") long id, @RequestBody Page page) {
        Page existing = staticWiki.getPage(id);
        if (existing == null) {
            return ResponseEntity.notFound().build();
        }
        existing.setTitle(page.getTitle());
        existing.setContent(page.getContent());
        return toPageResource(existing);
    }

    @DeleteMapping(value = "/wiki/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, "application/hal+json"})
    private ResponseEntity<Resource<Page>> delete(@PathVariable(value = "id") long id) {
        Page page = staticWiki.getPage(id);
        if (page == null) {
            return ResponseEntity.notFound().build();
        }
        staticWiki.removePage(page);
        return toPageResource(page);
    }

}
