import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService, Mission } from '../../services/auth.service';
import { dateFutureValidator } from '../../validators/custom.validators';

@Component({
  selector: 'app-missions',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './missions.component.html',
  styleUrl: './missions.component.css'
})
export class MissionsComponent implements OnInit {
  missions: Mission[] = [];
  isLoading = true;
  isSubmitting = false;
  showForm = false;
  errorMessage = '';

  missionForm!: FormGroup;
  userCompetences: any[] = [];

  constructor(
    private fb: FormBuilder,
    private authService: AuthService
  ) { }

  ngOnInit(): void {
    this.initForm();
    this.loadMissions();
    this.loadUserProfile();
  }

  initForm(): void {
    this.missionForm = this.fb.group({
      titre: ['', [Validators.required, Validators.minLength(5)]],
      description: [''],
      dateDebut: ['', Validators.required],
      dateFin: ['', Validators.required],
      validatorEmail: ['', [Validators.required, Validators.email]],
      competenceIds: [[], Validators.required]
    }, { validators: dateFutureValidator });
  }

  // Helpers
  get titre() { return this.missionForm.get('titre'); }
  get dateDebut() { return this.missionForm.get('dateDebut'); }
  get dateFin() { return this.missionForm.get('dateFin'); }
  get validatorEmail() { return this.missionForm.get('validatorEmail'); }
  get competenceIds() { return this.missionForm.get('competenceIds'); }

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

  toggleCompetence(id: number): void {
    const currentIds: number[] = this.missionForm.get('competenceIds')?.value || [];
    const index = currentIds.indexOf(id);
    if (index > -1) {
      currentIds.splice(index, 1);
    } else {
      currentIds.push(id);
    }
    this.missionForm.patchValue({ competenceIds: currentIds });
  }

  isCompetenceSelected(id: number): boolean {
    const currentIds: number[] = this.missionForm.get('competenceIds')?.value || [];
    return currentIds.includes(id);
  }

  onSubmit(): void {
    if (this.missionForm.invalid) {
      this.missionForm.markAllAsTouched();
      return;
    }

    this.isSubmitting = true;
    this.authService.createMission(this.missionForm.value).subscribe({
      next: (created) => {
        this.missions.unshift(created);
        this.missionForm.reset({ competenceIds: [], validatorEmail: '', titre: '', description: '', dateDebut: '', dateFin: '' });
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
}
