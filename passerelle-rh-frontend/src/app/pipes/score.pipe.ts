import { Pipe, PipeTransform } from '@angular/core';

/**
 * Pipe qui affiche un score numérique avec un format lisible et une couleur contextuelle.
 * Usage : {{ 72.5 | score }}           → "72.5%"
 *         {{ 72.5 | score:'bar' }}     → affiche une barre de progression
 *         {{ 3.8 | score:'note' }}     → "3.8 / 5"
 */
@Pipe({
  name: 'score',
  standalone: true
})
export class ScorePipe implements PipeTransform {
  transform(value: number | null | undefined, format: 'percent' | 'note' | 'label' = 'percent'): string {
    if (value === null || value === undefined) return '—';

    switch (format) {
      case 'note':
        return `${value.toFixed(1)} / 5`;
      case 'label':
        if (value >= 80) return '🟢 Excellent';
        if (value >= 60) return '🔵 Bon';
        if (value >= 40) return '🟡 Moyen';
        if (value >= 20) return '🟠 Faible';
        return '🔴 Insuffisant';
      case 'percent':
      default:
        return `${value.toFixed(1)}%`;
    }
  }
}

