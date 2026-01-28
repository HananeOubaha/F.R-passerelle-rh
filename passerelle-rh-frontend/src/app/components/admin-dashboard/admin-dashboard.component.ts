import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { BaseChartDirective } from 'ng2-charts';
import { ChartConfiguration, ChartData, ChartType } from 'chart.js';
import { AdminService, AdminStats } from '../../services/admin.service';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-admin-dashboard',
  standalone: true,
  imports: [CommonModule, RouterModule, BaseChartDirective],
  templateUrl: './admin-dashboard.component.html',
  styles: [`
    .kpi-card { @apply p-6 rounded-3xl transition-all duration-300 hover:shadow-xl hover:-translate-y-1; }
    .section-title { @apply text-sm font-black text-slate-800 uppercase tracking-[0.2em] flex items-center gap-2; }
    .dot { @apply w-2 h-2 rounded-full; }
  `]
})
export class AdminDashboardComponent implements OnInit {
  stats: AdminStats | null = null;
  isLoading = true;

  // Roles Doughnut
  public rolesChartOptions: ChartConfiguration['options'] = {
    responsive: true,
    plugins: {
      legend: { position: 'bottom', labels: { usePointStyle: true, font: { weight: 'bold' } } }
    }
  };
  public rolesChartData: ChartData<'doughnut'> = {
    labels: [],
    datasets: [{
      data: [],
      backgroundColor: ['#6366F1', '#10B981', '#F59E0B']
    }]
  };

  // Missions Bar
  public missionsChartOptions: ChartConfiguration['options'] = {
    responsive: true,
    scales: {
      y: { beginAtZero: true, grid: { display: false } },
      x: { grid: { display: false } }
    },
    plugins: { legend: { display: false } }
  };
  public missionsChartData: ChartData<'bar'> = {
    labels: [],
    datasets: [{ data: [], backgroundColor: '#10B981', borderRadius: 8 }]
  };

  constructor(private adminService: AdminService, private authService: AuthService) { }

  ngOnInit(): void {
    this.loadStats();
  }

  loadStats(): void {
    this.isLoading = true;
    this.adminService.getStats().subscribe({
      next: (data) => {
        this.stats = data;
        this.prepareCharts();
        this.isLoading = false;
      }
    });
  }

  prepareCharts(): void {
    if (!this.stats) return;

    // Roles
    this.rolesChartData.labels = Object.keys(this.stats.usersByRole);
    this.rolesChartData.datasets[0].data = Object.values(this.stats.usersByRole);
    (this.rolesChartData.datasets[0] as any).cutout = '70%';
    (this.rolesChartData.datasets[0] as any).borderRadius = 10;

    // Missions
    this.missionsChartData.labels = Object.keys(this.stats.missionsByStatus);
    this.missionsChartData.datasets[0].data = Object.values(this.stats.missionsByStatus);
  }

  // Comparator to keep the original order in keyvalue pipe
  returnZero() {
    return 0;
  }

  logout(): void {
    this.authService.logout();
  }
}
