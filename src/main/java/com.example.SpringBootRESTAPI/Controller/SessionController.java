package com.example.SpringBootRESTAPI.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/session")
public class SessionController {
    
    @PostMapping("/set")
    public Map<String, String> setSessionAttribute(HttpSession session, 
                                                  @RequestParam String key, 
                                                  @RequestParam String value) {
        session.setAttribute(key, value);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Session attribute set successfully");
        response.put("sessionId", session.getId());
        return response;
    }
    
    @GetMapping("/get")
    public Map<String, Object> getSessionAttribute(HttpSession session, 
                                                  @RequestParam String key) {
        Object value = session.getAttribute(key);
        
        Map<String, Object> response = new HashMap<>();
        response.put("sessionId", session.getId());
        response.put(key, value);
        return response;
    }
    
    @GetMapping("/invalidate")
    public Map<String, String> invalidateSession(HttpSession session) {
        String sessionId = session.getId();
        session.invalidate();
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Session invalidated");
        response.put("sessionId", sessionId);
        return response;
    }
}
