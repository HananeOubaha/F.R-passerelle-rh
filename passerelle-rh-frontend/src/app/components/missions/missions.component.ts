import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService, Mission } from '../../services/auth.service';
import { MissionsFacade } from '../../store/missions/missions.facade';
import { dateFutureValidator } from '../../validators/custom.validators';
import { TruncatePipe } from '../../pipes/truncate.pipe';
import { DateRelativePipe } from '../../pipes/date-relative.pipe';

@Component({
  selector: 'app-missions',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, TruncatePipe, DateRelativePipe],
  templateUrl: './missions.component.html',
  styleUrl: './missions.component.css'
})
export class MissionsComponent implements OnInit {
  isSubmitting = false;
  showForm = false;
  errorMessage = '';

  missionForm!: FormGroup;
  userCompetences: any[] = [];

  readonly vm$ = this.missionsFacade.vm$;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private missionsFacade: MissionsFacade
  ) { }

  ngOnInit(): void {
    this.initForm();
    this.missionsFacade.loadMissions();
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
    // Delégué au store
    this.missionsFacade.loadMissions();
  }

  changePage(page: number): void {
    this.missionsFacade.changePage(page);
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
    if (this.missionForm.invalid) return;

    this.isSubmitting = true;
    this.errorMessage = '';

    this.authService.createMission(this.missionForm.value).subscribe({
      next: () => {
        this.isSubmitting = false;
        this.showForm = false;
        this.missionForm.reset();
        // Le store écoute createMissionSuccess mais ici on fait un appel direct
        // Pour être propre, on devrait dispatcher une action createMission,
        // mais pour l'instant un reload suffit.
        this.missionsFacade.loadMissions();
      },
      error: (err) => {
        this.isSubmitting = false;
        this.errorMessage = err?.error?.message || 'Erreur lors de la création';
      }
    });
  }

  deleteMission(id: number): void {
    this.missionsFacade.deleteMission(id);
  }

  statutClass(statut: string): string {
    switch (statut) {
      case 'VALIDATED': return 'bg-emerald-100 text-emerald-700';
      case 'REJECTED': return 'bg-red-100 text-red-700';
      default: return 'bg-amber-100 text-amber-700';
    }
  }

  statutLabel(statut: string): string {
    switch (statut) {
      case 'VALIDATED': return 'Validée';
      case 'REJECTED': return 'Rejetée';
      default: return 'En attente';
    }
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
}
