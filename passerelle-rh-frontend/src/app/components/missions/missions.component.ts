import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AuthService, Mission, CreateMissionRequest } from '../../services/auth.service';

@Component({
  selector: 'app-missions',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './missions.component.html',
  styleUrl: './missions.component.css'
})
export class MissionsComponent implements OnInit {
  missions: Mission[] = [];
  isLoading = true;
  isSubmitting = false;
  showForm = false;
  errorMessage = '';

  newMission: CreateMissionRequest = {
    titre: '',
    description: '',
    dateDebut: '',
    dateFin: '',
    validatorEmail: '',
    competenceIds: []
  };

  userCompetences: any[] = [];
  selectedCompetenceIds: number[] = [];

  constructor(private authService: AuthService) { }

  ngOnInit(): void {
    this.loadMissions();
    this.loadUserProfile();
  }

  loadUserProfile(): void {
    this.authService.getProfile().subscribe({
      next: (profile) => {
        this.userCompetences = profile.competences;
      }
    });
  }

  loadMissions(): void {
    this.isLoading = true;
    this.authService.getMissions().subscribe({
      next: (missions) => {
        this.missions = missions;
        this.isLoading = false;
      },
      error: () => {
        this.errorMessage = 'Impossible de charger les missions.';
        this.isLoading = false;
      }
    });
  }

  onSubmit(): void {
    if (!this.newMission.titre.trim()) return;
    this.newMission.competenceIds = this.selectedCompetenceIds;
    this.isSubmitting = true;

    this.authService.createMission(this.newMission).subscribe({
      next: (created) => {
        this.missions.unshift(created);
        this.resetForm();
        this.isSubmitting = false;
        this.showForm = false;
      },
      error: () => {
        this.errorMessage = 'Erreur lors de la création de la mission.';
        this.isSubmitting = false;
      }
    });
  }

  deleteMission(id: number): void {
    if (!confirm('Supprimer cette mission ?')) return;
    this.authService.deleteMission(id).subscribe({
      next: () => {
        this.missions = this.missions.filter(m => m.id !== id);
      },
      error: () => {
        this.errorMessage = 'Impossible de supprimer cette mission.';
      }
    });
  }

  downloadAttestation(id: number, titre: string): void {
    this.authService.downloadAttestation(id).subscribe({
      next: (blob) => {
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = `Attestation_${titre.replace(/\s+/g, '_')}.pdf`;
        document.body.appendChild(a);
        a.click();
        document.body.removeChild(a);
        window.URL.revokeObjectURL(url);
      },
      error: () => {
        this.errorMessage = 'Erreur lors du téléchargement de l\'attestation.';
      }
    });
  }

  resetForm(): void {
    this.newMission = { titre: '', description: '', dateDebut: '', dateFin: '', validatorEmail: '', competenceIds: [] };
    this.selectedCompetenceIds = [];
    this.errorMessage = '';
  }

  statutLabel(statut: string): string {
    return { PENDING: 'En attente', VALIDATED: 'Validée', REJECTED: 'Rejetée' }[statut] ?? statut;
  }

  statutClass(statut: string): string {
    return {
      PENDING: 'bg-amber-50 text-amber-700 border border-amber-200',
      VALIDATED: 'bg-emerald-50 text-emerald-700 border border-emerald-200',
      REJECTED: 'bg-red-50 text-red-700 border border-red-200'
    }[statut] ?? 'bg-slate-100 text-slate-600';
  }

  toggleCompetence(id: number): void {
    const index = this.selectedCompetenceIds.indexOf(id);
    if (index > -1) {
      this.selectedCompetenceIds.splice(index, 1);
    } else {
      this.selectedCompetenceIds.push(id);
    }
  }
}
