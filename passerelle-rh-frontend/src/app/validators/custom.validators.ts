import { AbstractControl, ValidationErrors, ValidatorFn } from '@angular/forms';

/**
 * Validator personnalisé : vérifie que la dateFin est postérieure à la dateDebut.
 * À appliquer sur le FormGroup.
 */
export const dateFutureValidator: ValidatorFn = (group: AbstractControl): ValidationErrors | null => {
  const debut = group.get('dateDebut')?.value;
  const fin = group.get('dateFin')?.value;
  if (!debut || !fin) return null;
  return new Date(fin) > new Date(debut) ? null : { dateFuture: true };
};

/**
 * Validator personnalisé : vérifie que passwordConfirm === password.
 * À appliquer sur le FormGroup.
 */
export const passwordMatchValidator: ValidatorFn = (group: AbstractControl): ValidationErrors | null => {
  const password = group.get('password')?.value;
  const confirm = group.get('confirmPassword')?.value;
  if (!password || !confirm) return null;
  return password === confirm ? null : { passwordMismatch: true };
};

/**
 * Validator personnalisé : vérifie qu'au moins un élément est sélectionné.
 * À appliquer sur un FormControl dont la valeur est un tableau.
 */
export const minOneSelectedValidator: ValidatorFn = (control: AbstractControl): ValidationErrors | null => {
  const value: any[] = control.value;
  if (!Array.isArray(value) || value.length === 0) {
    return { minOneSelected: true };
  }
  return null;
};

