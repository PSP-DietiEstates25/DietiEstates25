import { Injectable, signal, effect, inject } from '@angular/core';
import { Router } from '@angular/router';
import { LocalStorageService } from '../../vecchioService/local-storage/local-storage.service';

@Injectable({ providedIn: 'root' })
export class SearchService {
  private readonly KEY = 'de.search.q';
  private ls = inject(LocalStorageService);
  private router = inject(Router);

  // Stato query (replica del pattern signals di FakeRestaurant)
  q = signal<string>('');

  constructor() {
    // ripristino
    const saved = this.ls.getItem(this.KEY);
    if (saved) this.q.set(saved);

    // persistenza auto
    effect(() => {
      const val = this.q();
      this.ls.setItem(this.KEY, val ?? '');
    });
  }

  setQuery(v: string) {
    this.q.set((v ?? '').trimStart());
  }

  goToSearch() {
    const query = this.q().trim();
    this.router.navigate(['/search'], { queryParams: { q: query || null } });
  }
}
