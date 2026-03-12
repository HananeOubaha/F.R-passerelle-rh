import { Pipe, PipeTransform } from '@angular/core';

/**
 * Pipe qui tronque un texte long.
 * Usage : {{ 'Description longue...' | truncate:50 }}    -> "Description longue..."
 *         {{ 'Court' | truncate:50 }}                    -> "Court"
 */
@Pipe({
  name: 'truncate',
  standalone: true
})
export class TruncatePipe implements PipeTransform {
  transform(value: string | null | undefined, limit: number = 100, trail: string = '...'): string {
    if (!value) return '';
    if (value.length <= limit) return value;
    return value.substring(0, limit).trimEnd() + trail;
  }
}

