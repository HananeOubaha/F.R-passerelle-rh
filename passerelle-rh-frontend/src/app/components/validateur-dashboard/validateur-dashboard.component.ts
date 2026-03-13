import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService, Mission } from '../../services/auth.service';
import { AuthFacade } from '../../store/auth/auth.facade';

interface ScoreForm {
  competenceScores: { [key: number]: number };
  commentaire: string;
}

@Component({
  selector: 'app-validateur-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './validateur-dashboard.component.html',
  styleUrl: './validateur-dashboard.component.css'
})
export class ValidateurDashboardComponent implements OnInit {
  pendingMissions: Mission[] = [];
  history: any[] = [];
  isLoading = true;
  errorMessage = '';
  selectedMission: Mission | null = null;
  isAdmin: boolean = false;

  scoreForm: ScoreForm = {
    competenceScores: {},
    commentaire: ''
  };

  hoverScores: { [key: number]: number } = {};

  isSubmitting = false;
  successMessage = '';

  constructor(private authService: AuthService, private authFacade: AuthFacade, private router: Router) { }

  ngOnInit(): void {
    const user = this.authService.getUser();
    this.isAdmin = user?.role === 'ADMIN';
    this.loadData();
  }

  loadData(): void {
    this.isLoading = true;
    this.authService.getPendingMissions().subscribe({
      next: (missions) => {
        this.pendingMissions = missions;
        this.isLoading = false;
      },
      error: () => { this.errorMessage = 'Impossible de charger les missions.'; this.isLoading = false; }
    });
    this.authService.getValidationHistory().subscribe({
      next: (history) => this.history = history,
      error: () => { }
    });
  }

  selectMission(mission: Mission): void {
    this.selectedMission = mission;
    const initialScores: { [key: number]: number } = {};
    if (mission.competencesMobilisees) {
      mission.competencesMobilisees.forEach(c => initialScores[c.id] = 3);
    }
    this.scoreForm = { competenceScores: initialScores, commentaire: '' };
    this.hoverScores = {};
    this.successMessage = '';
    this.errorMessage = '';
  }

  validate(): void {
    if (!this.selectedMission) return;
    this.isSubmitting = true;
    this.authService.validateMission(this.selectedMission.id, this.scoreForm).subscribe({
      next: () => {
        this.successMessage = `Mission "${this.selectedMission!.titre}" validée avec succès !`;
        this.pendingMissions = this.pendingMissions.filter(m => m.id !== this.selectedMission!.id);
        this.selectedMission = null;
        this.isSubmitting = false;
        this.loadData();
      },
      error: () => { this.errorMessage = 'Erreur lors de la validation.'; this.isSubmitting = false; }
    });
  }

  reject(): void {
    if (!this.selectedMission) return;
    const reason = this.scoreForm.commentaire || undefined;
    this.isSubmitting = true;
    this.authService.rejectMission(this.selectedMission.id, reason).subscribe({
      next: () => {
        this.successMessage = `Mission "${this.selectedMission!.titre}" rejetée.`;
        this.pendingMissions = this.pendingMissions.filter(m => m.id !== this.selectedMission!.id);
        this.selectedMission = null;
        this.isSubmitting = false;
      },
      error: () => { this.errorMessage = 'Erreur lors du rejet.'; this.isSubmitting = false; }
    });
  }

  setHover(compId: number, star: number): void {
    this.hoverScores[compId] = star;
  }

  setScore(compId: number, star: number): void {
    this.scoreForm.competenceScores[compId] = star;
  }

  getDisplayScore(compId: number): number {
    return this.hoverScores[compId] || this.scoreForm.competenceScores[compId] || 0;
  }

  starsArray(): number[] {
    return [1, 2, 3, 4, 5];
  }

  logout(): void {
    this.authFacade.logout();
    this.router.navigate(['/login']);
  }
}
