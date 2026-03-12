import { Pipe, PipeTransform } from '@angular/core';

/**
 * Pipe qui affiche une date sous forme relative.
 * Usage : {{ '2026-03-10T14:00:00' | dateRelative }}  -> "il y a 2 jours"
 */
@Pipe({
  name: 'dateRelative',
  standalone: true
})
export class DateRelativePipe implements PipeTransform {
  transform(value: string | Date | null | undefined): string {
    if (!value) return '—';

    const date = new Date(value);
    const now = new Date();
    const diffMs = now.getTime() - date.getTime();

    if (diffMs < 0) {
      const futureDays = Math.floor(Math.abs(diffMs) / (1000 * 60 * 60 * 24));
      if (futureDays === 0) return "aujourd'hui";
      if (futureDays === 1) return 'demain';
      if (futureDays < 30) return 'dans ' + futureDays + ' jours';
      return 'dans ' + Math.floor(futureDays / 30) + ' mois';
    }

    const seconds = Math.floor(diffMs / 1000);
    const minutes = Math.floor(seconds / 60);
    const hours = Math.floor(minutes / 60);
    const days = Math.floor(hours / 24);
    const months = Math.floor(days / 30);
    const years = Math.floor(days / 365);

    if (seconds < 60) return "a l'instant";
    if (minutes < 60) return 'il y a ' + minutes + ' min';
    if (hours < 24) return 'il y a ' + hours + 'h';
    if (days === 1) return 'hier';
    if (days < 30) return 'il y a ' + days + ' jours';
    if (months === 1) return 'il y a 1 mois';
    if (months < 12) return 'il y a ' + months + ' mois';
    if (years === 1) return 'il y a 1 an';
    return 'il y a ' + years + ' ans';
  }
}

