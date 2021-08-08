package com.team.search;

import com.team.search.dto.SearchOutput;
import com.team.search.dto.response.SearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchController {
    private final SearchService searchService;

    @GetMapping
    public ResponseEntity<SearchResponse> search(@RequestParam("keyword") String keyword) {
        SearchOutput output = searchService.search(keyword);
        return ResponseEntity.ok(new SearchResponse(output));
    }
}
