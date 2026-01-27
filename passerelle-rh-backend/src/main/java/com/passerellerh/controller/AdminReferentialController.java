package com.passerellerh.controller;

import com.passerellerh.dto.BadgeDTO;
import com.passerellerh.dto.CompetenceDTO;
import com.passerellerh.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/referential")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminReferentialController {

    private final AdminService adminService;

    // --- Competences ---

    @GetMapping("/competences")
    public ResponseEntity<List<CompetenceDTO>> getAllCompetences() {
        return ResponseEntity.ok(adminService.getAllCompetences());
    }

    @PostMapping("/competences")
    public ResponseEntity<CompetenceDTO> saveCompetence(@RequestBody CompetenceDTO dto) {
        return ResponseEntity.ok(adminService.saveCompetence(dto));
    }

    @DeleteMapping("/competences/{id}")
    public ResponseEntity<Void> deleteCompetence(@PathVariable Long id) {
        adminService.deleteCompetence(id);
        return ResponseEntity.ok().build();
    }

    // --- Badges ---

    @GetMapping("/badges")
    public ResponseEntity<List<BadgeDTO>> getAllBadges() {
        return ResponseEntity.ok(adminService.getAllBadges());
    }

    @PostMapping("/badges")
    public ResponseEntity<BadgeDTO> saveBadge(@RequestBody BadgeDTO dto) {
        return ResponseEntity.ok(adminService.saveBadge(dto));
    }

    @DeleteMapping("/badges/{id}")
    public ResponseEntity<Void> deleteBadge(@PathVariable Long id) {
        adminService.deleteBadge(id);
        return ResponseEntity.ok().build();
    }
}
