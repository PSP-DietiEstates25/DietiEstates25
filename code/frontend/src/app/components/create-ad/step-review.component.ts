import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { AdDraftService } from '../../vecchioService/ad-draft.service';
import { AgentService } from '../../vecchioService/agent.service';

@Component({
  selector: 'app-step-review',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './step-review.component.html',
})
export class StepReviewComponent {
  private draft = inject(AdDraftService);
  private agent = inject(AgentService);
  private router = inject(Router);

  loading = signal(false);
  error = signal<string | null>(null);

  get d() {
    return this.draft.draft();
  }

  back() {
    this.router.navigateByUrl('/agent/ads/new/photos');
  }

  publish() {
    if (!this.draft.allValid()) {
      this.error.set('Completa i passaggi richiesti.');
      return;
    }
    this.loading.set(true);
    this.error.set(null);

    const hasPhotos = this.draft.draft().photos.length > 0;
    const body: any = hasPhotos ? this.draft.toFormData() : this.draft.draft();

    this.agent.createAd(body).subscribe({
      next: (_) => {
        this.draft.reset();
        this.router.navigateByUrl('/agent');
      },
      error: (err) => {
        this.error.set(err?.error?.message || 'Creazione annuncio fallita');
        this.loading.set(false);
      },
    });
  }
}
