package com.passerellerh.controller;

import com.passerellerh.dto.AdminStatsDTO;
import com.passerellerh.dto.UserResponseDTO;
import com.passerellerh.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<java.util.List<UserResponseDTO>> getAllUsers() {
        return ResponseEntity.ok(adminService.getAllUsers());
    }

    /**
     * Endpoint paginé pour la gestion des utilisateurs.
     * Supporte tri (?sort=nom,asc) et recherche (?search=dupont).
     */
    @GetMapping("/users/page")
    public ResponseEntity<Page<UserResponseDTO>> getUsersPaginated(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) com.passerellerh.enums.Role role,
            @PageableDefault(size = 20, sort = "nom") Pageable pageable) {
        return ResponseEntity.ok(adminService.getUsersPaginated(search, role, pageable));
    }

    @PutMapping("/users/{userId}/role")
    public ResponseEntity<Void> updateUserRole(
            @PathVariable Long userId,
            @RequestParam com.passerellerh.enums.Role role) {
        adminService.updateUserRole(userId, role);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        adminService.deleteUser(userId);
        return ResponseEntity.ok().build();
    }
}
