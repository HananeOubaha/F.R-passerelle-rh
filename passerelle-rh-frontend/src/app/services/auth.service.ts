import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, BehaviorSubject, tap } from 'rxjs';

export type MissionStatut = 'PENDING' | 'VALIDATED' | 'REJECTED';

export interface Mission {
  id: number;
  titre: string;
  description: string;
  dateDebut: string;
  dateFin: string;
  statut: MissionStatut;
  validatorEmail: string;
  competencesMobilisees?: Competence[];
}

export interface CreateMissionRequest {
  titre: string;
  description: string;
  dateDebut: string;
  dateFin: string;
  validatorEmail: string;
  competenceIds?: number[];
}

export interface RegisterRequest {
  nom: string;
  prenom: string;
  email: string;
  password: string;
  ville?: string;
  role?: string;
}

export interface LoginRequest {
  email: string;
  password: string;
}

export interface AuthResponse {
  accessToken: string;
  refreshToken: string;
  tokenType: string;
  userId: number;
  email: string;
  role: string;
}

export interface Competence {
  id: number;
  nom: string;
}

export interface Badge {
  id: number;
  nom: string;
  seuilScore: number;
  iconeUrl: string;
}



export interface UserProfile {
  id: number;
  nom: string;
  prenom: string;
  email: string;
  role: string;
  ville?: string;
  scoreFiabilite: number;
  competences: Competence[];
  badges?: Badge[];
  profilePublicToken?: string;
}

export interface UpdateProfileRequest {
  nom: string;
  prenom: string;
  ville: string;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://localhost:8080/api/auth';
  private usersUrl = 'http://localhost:8080/api/users';
  private currentUserSubject = new BehaviorSubject<AuthResponse | null>(this.getUserFromStorage());

  public currentUser$ = this.currentUserSubject.asObservable();

  constructor(private http: HttpClient) { }

  register(userData: RegisterRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/register`, userData).pipe(
      tap(response => this.setSession(response))
    );
  }

  login(credentials: LoginRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/login`, credentials).pipe(
      tap(response => this.setSession(response))
    );
  }

  refreshAccessToken(): Observable<AuthResponse> {
    const refreshToken = this.getRefreshToken();
    return this.http.post<AuthResponse>(`${this.apiUrl}/refresh`, { refreshToken }).pipe(
      tap(response => this.updateTokens(response))
    );
  }

  getProfile(): Observable<UserProfile> {
    return this.http.get<UserProfile>(`${this.usersUrl}/me`);
  }

  updateProfile(data: UpdateProfileRequest): Observable<UserProfile> {
    return this.http.put<UserProfile>(`${this.usersUrl}/me`, data);
  }

  addCompetence(nom: string): Observable<UserProfile> {
    return this.http.post<UserProfile>(`${this.usersUrl}/me/competences`, { nom });
  }

  removeCompetence(id: number): Observable<UserProfile> {
    return this.http.delete<UserProfile>(`${this.usersUrl}/me/competences/${id}`);
  }

  getMissions(): Observable<Mission[]> {
    return this.http.get<Mission[]>('http://localhost:8080/api/missions');
  }

  createMission(data: CreateMissionRequest): Observable<Mission> {
    return this.http.post<Mission>('http://localhost:8080/api/missions', data);
  }

  deleteMission(id: number): Observable<void> {
    return this.http.delete<void>(`http://localhost:8080/api/missions/${id}`);
  }

  // --- Validateur API ---
  getPendingMissions(): Observable<Mission[]> {
    return this.http.get<Mission[]>('http://localhost:8080/api/validation/pending');
  }

  validateMission(missionId: number, data: { competenceScores: { [key: number]: number }; commentaire: string }): Observable<any> {
    return this.http.post(`http://localhost:8080/api/validation/validate/${missionId}`, data);
  }

  rejectMission(missionId: number, reason?: string): Observable<any> {
    const params = reason ? `?reason=${encodeURIComponent(reason)}` : '';
    return this.http.post(`http://localhost:8080/api/validation/reject/${missionId}${params}`, {});
  }

  getValidationHistory(): Observable<any[]> {
    return this.http.get<any[]>('http://localhost:8080/api/validation/history');
  }

  getReceivedValidations(): Observable<any[]> {
    return this.http.get<any[]>('http://localhost:8080/api/validation/my-validations');
  }

  downloadAttestation(missionId: number): Observable<Blob> {
    return this.http.get(`http://localhost:8080/api/attestations/download/${missionId}`, {
      responseType: 'blob'
    });
  }


  logout(): void {
    localStorage.removeItem('access_token');
    localStorage.removeItem('refresh_token');
    localStorage.removeItem('user');
    this.currentUserSubject.next(null);
  }

  private setSession(authResult: AuthResponse): void {
    localStorage.setItem('access_token', authResult.accessToken);
    localStorage.setItem('refresh_token', authResult.refreshToken);
    localStorage.setItem('user', JSON.stringify(authResult));
    this.currentUserSubject.next(authResult);
  }

  private updateTokens(authResult: AuthResponse): void {
    const currentUser = this.getUser();
    const mergedUser: AuthResponse = {
      ...authResult,
      userId: authResult.userId || currentUser?.userId || 0,
      email: authResult.email || currentUser?.email || '',
      role: authResult.role || currentUser?.role || ''
    };
    localStorage.setItem('access_token', mergedUser.accessToken);
    localStorage.setItem('refresh_token', mergedUser.refreshToken);
    localStorage.setItem('user', JSON.stringify(mergedUser));
    this.currentUserSubject.next(mergedUser);
  }

  getUser(): AuthResponse | null {
    return this.currentUserSubject.value;
  }

  private getUserFromStorage(): AuthResponse | null {
    const userStr = localStorage.getItem('user');
    return userStr ? JSON.parse(userStr) : null;
  }

  getToken(): string | null {
    return localStorage.getItem('access_token');
  }

  getRefreshToken(): string | null {
    return localStorage.getItem('refresh_token');
  }

  isLoggedIn(): boolean {
    return !!this.getToken();
  }
}
