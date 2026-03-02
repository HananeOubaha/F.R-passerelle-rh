package com.passerellerh.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminStatsDTO {
    private long totalUsers;
    private long totalMissions;
    private long totalValidations;

    private Map<String, Long> usersByRole;
    private Map<String, Long> missionsByStatus;
    private Map<String, Long> topCompetences;

    private List<DailyActivity> recentActivities;

    @Data
    @AllArgsConstructor
    public static class DailyActivity {
        private String date;
        private long count;
    }
}
