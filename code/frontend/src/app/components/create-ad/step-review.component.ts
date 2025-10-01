import { Component, computed, inject, signal } from '@angular/core';
import { Router } from '@angular/router';
import { CreateAdFacade } from './create-ad.facade';
import { DecimalPipe } from '@angular/common';

@Component({
  selector: 'app-step-review',
  standalone: true,
  templateUrl: './step-review.component.html',
  imports: [DecimalPipe]
})
export class StepReviewComponent {
  private router = inject(Router);
  private facade = inject(CreateAdFacade);

  loading = signal(false);
  error = signal<string | null>(null);

  // View-model per il tuo template: d.title, d.city, d.type, d.photos[], ecc.
  d = computed(() => {
    const draft = this.facade.draft();

    // Genera nomi sintetici per le foto (l’HTML mostra f.name)
    const photos = (draft.imagesBase64 ?? []).map((_, i) => ({
      name: `foto-${i + 1}.jpg`,
    }));

    return {
      title: draft.title ?? '',
      price: draft.price ?? null,
      city: draft.city ?? '',
      type: draft.category ?? '', // mappa category -> type (come nel tuo HTML)
      size: draft.size ?? null,
      latitude: draft.latitude ?? null,
      longitude: draft.longitude ?? null,
      description: draft.description ?? '',
      photos,
    };
  });

  get draft() {
    return this.facade.draft();
  }

  back() {
    this.router.navigateByUrl('/agent/ads/new/photos');
  }

  async publish() {
    this.error.set(null);
    this.loading.set(true);
    try {
      await this.facade.submit(); // Sequenza: Geo → Cadastral → Details → RealEstate
      this.facade.reset();
      this.router.navigateByUrl('/agent'); // vai alla dashboard agente
    } catch (e: any) {
      this.error.set(
        e?.error?.message || e?.message || 'Creazione annuncio fallita'
      );
    } finally {
      this.loading.set(false);
    }
  }
}
