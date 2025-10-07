// src/app/components/create-ad/step-review.component.ts
import { Component, computed, inject, signal } from '@angular/core';
import { Router } from '@angular/router';
import { CreateAdFacade } from './create-ad.facade';
import { DecimalPipe } from '@angular/common';

@Component({
  selector: 'app-step-review',
  standalone: true,
  templateUrl: './step-review.component.html',
  imports: [DecimalPipe],
})
export class StepReviewComponent {
  private router = inject(Router);
  private facade = inject(CreateAdFacade);

  loading = signal(false);
  error = signal<string | null>(null);

  d = this.facade.draft;

  back() {
    this.router.navigateByUrl('/agent/ads/new/photos');
  }

  publish() {
    this.error.set(null);
    this.loading.set(true);

    this.facade.submit().subscribe({
      next: () => {
        this.facade.reset();
        this.router.navigateByUrl('/agent'); // dashboard agente
      },
      error: (e: any) => {
        this.error.set(
          e?.error?.message || e?.message || 'Creazione annuncio fallita'
        );
        this.loading.set(false);
      },
      complete: () => {
        this.loading.set(false);
      },
    });
  }
}
