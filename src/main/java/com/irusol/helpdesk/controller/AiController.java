package com.irusol.helpdesk.controller;

import com.irusol.helpdesk.service.AIService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/v1/helpdesk")
@RequiredArgsConstructor
//@CrossOrigin("http://localhost:5173")
public class AiController {


    private  final AIService service;

    @GetMapping("/ai")
    public ResponseEntity<String> fetRes(@RequestBody String str) {
        return ResponseEntity.ok(service.getAsist(str));
    }

    @PostMapping
    public ResponseEntity<String >  getResponse(@RequestBody  String query, @RequestHeader("ConversationId") String conversationId){
        return ResponseEntity.ok(service.getResponseFromAssistant(query,conversationId));
    }


    @PostMapping(value = "/stream")
    public Flux<String> streamResponse(@RequestBody  String query, @RequestHeader("ConversationId") String conversationId){
        return this.service.streamResponseFromAssistant(query,conversationId) ;
    }



}
