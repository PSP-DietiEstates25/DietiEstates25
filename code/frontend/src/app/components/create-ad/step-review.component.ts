import { Component, computed, inject } from '@angular/core';
import { CreateAdFacade } from './create-ad.facade';
import { Router } from '@angular/router';

@Component({
  selector: 'app-step-review',
  standalone: true,
  templateUrl: './step-review.component.html',
})
export class StepReviewComponent {

  facade = inject(CreateAdFacade);
  canPublish = computed(() => this.facade.allValid());
  private routerService = inject(Router); 

  previous(){
    this.routerService.navigate(['/photos']);
  }

  publish() {
    this.facade.createAd();
  }

  cancel() {
    this.routerService.navigate(['/']);
  }
}
