package com.passerellerh.controller;

import com.passerellerh.dto.AdminStatsDTO;
import com.passerellerh.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/stats")
    public ResponseEntity<AdminStatsDTO> getStats() {
        return ResponseEntity.ok(adminService.getGlobalStats());
    }

    @GetMapping("/users")
    public ResponseEntity<java.util.List<com.passerellerh.dto.UserResponseDTO>> getAllUsers() {
        return ResponseEntity.ok(adminService.getAllUsers());
    }

    @org.springframework.web.bind.annotation.PutMapping("/users/{userId}/role")
    public ResponseEntity<Void> updateUserRole(
            @org.springframework.web.bind.annotation.PathVariable Long userId,
            @org.springframework.web.bind.annotation.RequestParam com.passerellerh.enums.Role role) {
        adminService.updateUserRole(userId, role);
        return ResponseEntity.ok().build();
    }

    @org.springframework.web.bind.annotation.DeleteMapping("/users/{userId}")
    public ResponseEntity<Void> deleteUser(@org.springframework.web.bind.annotation.PathVariable Long userId) {
        adminService.deleteUser(userId);
        return ResponseEntity.ok().build();
    }
}
