import { Component, computed, inject } from '@angular/core';
import { CreateAdFacade } from './create-ad.facade';

@Component({
  selector: 'app-step-review',
  standalone: true,
  templateUrl: './step-review.component.html',
})
export class StepReviewComponent {
  facade = inject(CreateAdFacade);
  canPublish = computed(() => this.facade.allValid());

  publish() {
    this.facade.createAd();
  }
}
