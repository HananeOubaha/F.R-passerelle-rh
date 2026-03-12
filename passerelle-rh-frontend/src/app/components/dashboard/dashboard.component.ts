import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule, ActivatedRoute } from '@angular/router';
import { AuthService, UserProfile, UpdateProfileRequest } from '../../services/auth.service';
import { MissionsComponent } from '../missions/missions.component';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule, MissionsComponent, RouterModule],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css'
})
export class DashboardComponent implements OnInit {
  userProfile: UserProfile | null = null;
  isEditing: boolean = false;
  isLoading: boolean = true;
  errorMessage: string = '';

  editForm: UpdateProfileRequest = {
    nom: '',
    prenom: '',
    ville: ''
  };

  availableValidators: any[] = [];
  selectedValidatorId: number | null = null;
  isUpdatingValidator = false;

  newCompetence: string = '';
  isAddingCompetence: boolean = false;

  constructor(
    private authService: AuthService,
    private router: Router,
    private route: ActivatedRoute
  ) { }

  addCompetence(): void {
    if (!this.newCompetence.trim()) return;

    this.isAddingCompetence = true;
    this.authService.addCompetence(this.newCompetence).subscribe({
      next: (updatedProfile) => {
        this.userProfile = updatedProfile;
        this.newCompetence = '';
        this.isAddingCompetence = false;
      },
      error: (error) => {
        console.error('Error adding competence', error);
        this.errorMessage = 'Erreur lors de l\'ajout de la compétence';
        this.isAddingCompetence = false;
      }
    });
  }

  removeCompetence(id: number): void {
    if (!confirm('Êtes-vous sûr de vouloir supprimer cette compétence ?')) return;

    this.isLoading = true;
    this.authService.removeCompetence(id).subscribe({
      next: (updatedProfile) => {
        this.userProfile = updatedProfile;
        this.isLoading = false;
      },
      error: (error) => {
        console.error('Error removing competence', error);
        this.errorMessage = 'Erreur lors de la suppression de la compétence';
        this.isLoading = false;
      }
    });
  }

  ngOnInit(): void {
    // Données pré-chargées par le profileResolver
    this.route.data.subscribe(data => {
      if (data['profile']) {
        this.userProfile = data['profile'];
        this.isLoading = false;
        this.resetForm();
      } else {
        // Fallback si pas de resolver (accès direct)
        this.loadProfile();
      }
    });
  }


  loadProfile(): void {
    this.isLoading = true;
    this.authService.getProfile().subscribe({
      next: (profile) => {
        this.userProfile = profile;
        this.isLoading = false;
        this.resetForm();
      },
      error: (error) => {
        console.error('Error loading profile', error);
        this.errorMessage = 'Impossible de charger le profil';
        this.isLoading = false;

        // If unauthorized, redirect to login
        if (error.status === 401 || error.status === 403) {
          this.authService.logout();
          this.router.navigate(['/login']);
        }
      }
    });
  }

  resetForm(): void {
    if (this.userProfile) {
      this.editForm = {
        nom: this.userProfile.nom,
        prenom: this.userProfile.prenom,
        ville: this.userProfile.ville || ''
      };
    }
  }

  toggleEdit(): void {
    this.isEditing = !this.isEditing;
    if (!this.isEditing) {
      this.resetForm();
    }
  }

  onSubmit(): void {
    if (!this.userProfile) return;

    this.isLoading = true;
    this.authService.updateProfile(this.editForm).subscribe({
      next: (updatedProfile) => {
        this.userProfile = updatedProfile;
        this.isEditing = false;
        this.isLoading = false;
        this.errorMessage = '';
      },
      error: (error) => {
        console.error('Error updating profile', error);
        this.errorMessage = 'Erreur lors de la mise à jour du profil';
        this.isLoading = false;
      }
    });
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
