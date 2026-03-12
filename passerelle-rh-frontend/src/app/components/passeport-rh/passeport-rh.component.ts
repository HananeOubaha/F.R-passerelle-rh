import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BaseChartDirective } from 'ng2-charts';
import { ChartConfiguration, ChartData, ChartType } from 'chart.js';
import { AuthService, UserProfile } from '../../services/auth.service';
import { Router, RouterModule, ActivatedRoute } from '@angular/router';
import { ScorePipe } from '../../pipes/score.pipe';
import { DateRelativePipe } from '../../pipes/date-relative.pipe';

@Component({
    selector: 'app-passeport-rh',
    standalone: true,
    imports: [CommonModule, BaseChartDirective, RouterModule, ScorePipe, DateRelativePipe],
    templateUrl: './passeport-rh.component.html',
    styleUrl: './passeport-rh.component.css'
})
export class PasseportRHComponent implements OnInit {
    userProfile: UserProfile | null = null;
    validations: any[] = [];
    isLoading = true;

    // Horizontal Bar Chart for Competencies
    public barChartOptions: ChartConfiguration['options'] = {
        indexAxis: 'y',
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
            legend: { display: false },
            tooltip: {
                backgroundColor: '#1E293B',
                titleColor: '#F8FAFC',
                bodyColor: '#CBD5E1',
                padding: 14,
                cornerRadius: 12,
                displayColors: true,
                boxPadding: 6,
                callbacks: {
                    label: (context) => ` Niveau: ${context.parsed.x}/5`
                }
            }
        },
        scales: {
            x: {
                suggestedMin: 0,
                suggestedMax: 5,
                grid: { color: 'rgba(226, 232, 240, 0.4)' },
                ticks: { stepSize: 1, color: '#94A3B8', font: { weight: 'bold', size: 11 } },
                border: { display: false }
            },
            y: {
                grid: { display: false },
                ticks: {
                    font: { weight: 'bold', size: 12 },
                    color: '#334155'
                },
                border: { display: false }
            }
        }
    };
    public barChartType: ChartType = 'bar';
    public barChartData: ChartData<'bar'> = {
        labels: [],
        datasets: [{ data: [], label: 'Niveau moyen' }]
    };

    // Doughnut Chart for Competency Distribution
    public doughnutChartOptions: ChartConfiguration<'doughnut'>['options'] = {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
            legend: {
                position: 'bottom',
                labels: {
                    padding: 16,
                    usePointStyle: true,
                    pointStyle: 'circle',
                    font: { weight: 'bold', size: 11 },
                    color: '#475569'
                }
            },
            tooltip: {
                backgroundColor: '#1E293B',
                titleColor: '#F8FAFC',
                bodyColor: '#CBD5E1',
                padding: 14,
                cornerRadius: 12,
                displayColors: true,
                boxPadding: 6,
                callbacks: {
                    label: (context) => ` ${context.label}: ${(context.parsed as number).toFixed(1)}/5`
                }
            }
        },
        cutout: '65%'
    };
    public doughnutChartType = 'doughnut' as const;
    public doughnutChartData: ChartData<'doughnut'> = {
        labels: [],
        datasets: [{ data: [] }]
    };

    // Radar Chart for Skills Profile
    public radarChartOptions: ChartConfiguration['options'] = {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
            legend: { display: false },
            tooltip: {
                backgroundColor: '#1E293B',
                titleColor: '#F8FAFC',
                bodyColor: '#CBD5E1',
                padding: 14,
                cornerRadius: 12,
                callbacks: {
                    label: (context) => ` ${context.dataset.label}: ${context.parsed.r}/5`
                }
            }
        },
        scales: {
            r: {
                beginAtZero: true,
                max: 5,
                ticks: {
                    stepSize: 1,
                    display: false
                },
                pointLabels: {
                    font: { weight: 'bold', size: 11 },
                    color: '#334155'
                },
                grid: { color: 'rgba(226, 232, 240, 0.5)' },
                angleLines: { color: 'rgba(226, 232, 240, 0.5)' }
            }
        }
    };
    public radarChartType: ChartType = 'radar';
    public radarChartData: ChartData<'radar'> = {
        labels: [],
        datasets: []
    };

    // Line Chart for Score Evolution
    public lineChartOptions: ChartConfiguration['options'] = {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
            legend: { display: false },
            tooltip: {
                mode: 'index',
                intersect: false,
                backgroundColor: '#1E293B',
                titleColor: '#F8FAFC',
                bodyColor: '#CBD5E1',
                padding: 14,
                cornerRadius: 12
            }
        },
        scales: {
            y: {
                suggestedMin: 0,
                suggestedMax: 100,
                grid: { color: 'rgba(226, 232, 240, 0.4)' },
                ticks: { stepSize: 20, color: '#94A3B8', font: { weight: 'bold', size: 11 } },
                border: { display: false }
            },
            x: {
                grid: { display: false },
                ticks: { color: '#94A3B8', font: { weight: 'bold', size: 11 } },
                border: { display: false }
            }
        }
    };
    public lineChartType: ChartType = 'line';
    public lineChartData: ChartData<'line'> = {
        labels: [],
        datasets: [{
            data: [],
            label: 'Score (%)',
            fill: true,
            tension: 0.4,
            borderColor: '#10B981',
            backgroundColor: 'rgba(16, 185, 129, 0.08)',
            pointBackgroundColor: '#10B981',
            pointBorderColor: '#fff',
            pointHoverRadius: 8,
            pointRadius: 5,
            pointBorderWidth: 3,
            borderWidth: 3
        }]
    };

    // Status bar chart for mission breakdown
    public statusBarChartOptions: ChartConfiguration['options'] = {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
            legend: { display: false },
            tooltip: {
                backgroundColor: '#1E293B',
                titleColor: '#F8FAFC',
                bodyColor: '#CBD5E1',
                padding: 14,
                cornerRadius: 12,
            }
        },
        scales: {
            y: {
                beginAtZero: true,
                grid: { color: 'rgba(226, 232, 240, 0.4)' },
                ticks: { stepSize: 1, color: '#94A3B8', font: { weight: 'bold' } },
                border: { display: false }
            },
            x: {
                grid: { display: false },
                ticks: { color: '#475569', font: { weight: 'bold', size: 12 } },
                border: { display: false }
            }
        }
    };
    public statusBarChartType: ChartType = 'bar';
    public statusBarChartData: ChartData<'bar'> = {
        labels: ['En attente', 'Validée', 'Refusée'],
        datasets: [{ data: [0, 0, 0], label: 'Missions' }]
    };

    // KPI values
    totalValidations = 0;
    avgScore = 0;
    topCompetence = '—';

    constructor(private authService: AuthService, private router: Router, private route: ActivatedRoute) { }

    ngOnInit(): void {
        // Profil pré-chargé par le profileResolver
        this.route.data.subscribe(data => {
            if (data['profile']) {
                this.userProfile = data['profile'];
                this.loadValidationsOnly();
            } else {
                this.loadData();
            }
        });
    }

    /**
     * Charge uniquement les validations quand le profil est déjà résolu.
     */
    loadValidationsOnly(): void {
        this.authService.getReceivedValidations().subscribe(validations => {
            this.validations = validations.sort((a: any, b: any) =>
                new Date(a.dateValidation).getTime() - new Date(b.dateValidation).getTime()
            );
            this.prepareCharts();
            this.isLoading = false;
        });
    }

    loadData(): void {
        this.isLoading = true;
        this.authService.getProfile().subscribe(profile => {
            this.userProfile = profile;
            this.authService.getReceivedValidations().subscribe(validations => {
                this.validations = validations.sort((a: any, b: any) =>
                    new Date(a.dateValidation).getTime() - new Date(b.dateValidation).getTime()
                );
                this.prepareCharts();
                this.isLoading = false;
            });
        });
    }

    prepareCharts(): void {
        if (!this.userProfile || !this.validations.length) return;

        // Color palette
        const skillColors = [
            '#6366F1', // Indigo - tech
            '#10B981', // Emerald - soft skills
            '#F97316', // Orange - project
            '#EC4899', // Pink - creativity
            '#06B6D4', // Cyan - misc
            '#8B5CF6', // Violet - advanced
        ];

        // 1. Competency averages
        const compScores: { [key: string]: { total: number, count: number } } = {};
        this.validations.forEach((v: any) => {
            if (v.competenceScores) {
                Object.entries(v.competenceScores).forEach(([name, score]) => {
                    if (!compScores[name as string]) compScores[name as string] = { total: 0, count: 0 };
                    compScores[name as string].total += score as number;
                    compScores[name as string].count += 1;
                });
            }
        });

        const labels = Object.keys(compScores);
        const data = labels.map(l => Math.round((compScores[l].total / compScores[l].count) * 10) / 10);

        // KPIs
        this.totalValidations = this.validations.length;
        this.avgScore = data.length ? Math.round((data.reduce((a, b) => a + b, 0) / data.length) * 10) / 10 : 0;
        if (data.length) {
            const maxIdx = data.indexOf(Math.max(...data));
            this.topCompetence = labels[maxIdx];
        }

        // Horizontal Bar Chart
        this.barChartData = {
            labels: labels,
            datasets: [{
                data: data,
                label: 'Niveau moyen',
                backgroundColor: skillColors.slice(0, labels.length),
                borderRadius: 10,
                barThickness: 28,
                maxBarThickness: 36
            }]
        };

        // Doughnut Chart
        this.doughnutChartData = {
            labels: labels,
            datasets: [{
                data: data,
                backgroundColor: skillColors.slice(0, labels.length),
                borderWidth: 0,
                hoverOffset: 8
            }]
        };

        // Radar Chart
        this.radarChartData = {
            labels: labels,
            datasets: [
                {
                    data: data,
                    label: 'Mon profil',
                    backgroundColor: 'rgba(16, 185, 129, 0.15)',
                    borderColor: '#10B981',
                    pointBackgroundColor: '#10B981',
                    pointBorderColor: '#fff',
                    pointBorderWidth: 2,
                    pointRadius: 5,
                    borderWidth: 2.5,
                    fill: true
                },
                {
                    data: labels.map(() => 3),
                    label: 'Moyenne',
                    backgroundColor: 'rgba(148, 163, 184, 0.08)',
                    borderColor: '#CBD5E1',
                    pointBackgroundColor: '#CBD5E1',
                    pointBorderColor: '#fff',
                    pointBorderWidth: 2,
                    pointRadius: 3,
                    borderWidth: 1.5,
                    borderDash: [6, 4],
                    fill: true
                }
            ]
        };

        // 2. Line Chart (Score Evolution)
        let currentScore = 50;
        const historyDates: string[] = ['Début'];
        const historyScores: number[] = [50];

        this.validations.forEach((v: any) => {
            const scores = Object.values(v.competenceScores as object);
            const avg = (scores as number[]).reduce((a, b) => a + b, 0) / scores.length;
            currentScore = Math.round(((currentScore + avg * 20) / 2) * 10.0) / 10.0;
            historyDates.push(new Date(v.dateValidation).toLocaleDateString());
            historyScores.push(currentScore);
        });

        this.lineChartData = {
            labels: historyDates,
            datasets: [{
                data: historyScores,
                label: 'Score de fiabilité (%)',
                fill: true,
                tension: 0.4,
                borderColor: '#10B981',
                backgroundColor: 'rgba(16, 185, 129, 0.08)',
                pointBackgroundColor: '#10B981',
                pointBorderColor: '#fff',
                pointHoverRadius: 8,
                pointRadius: 5,
                pointBorderWidth: 3,
                borderWidth: 3
            }]
        };

        // 3. Status bar chart (mock from validations count)
        this.statusBarChartData = {
            labels: ['En attente', 'Validée', 'Refusée'],
            datasets: [{
                data: [0, this.validations.length, 0],
                label: 'Missions',
                backgroundColor: ['#FBBF24', '#10B981', '#EF4444'],
                borderRadius: 10,
                barThickness: 40
            }]
        };
    }

    shareProfile(): void {
        if (!this.userProfile?.profilePublicToken) {
            alert('Lien de partage non disponible.');
            return;
        }
        const publicLink = `${window.location.origin}/p/${this.userProfile.profilePublicToken}`;
        navigator.clipboard.writeText(publicLink).then(() => {
            alert('Lien de profil copié dans le presse-papier !');
        });
    }

    logout(): void {
        this.authService.logout();
        this.router.navigate(['/login']);
    }
}
