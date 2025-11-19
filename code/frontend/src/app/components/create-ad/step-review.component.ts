import { Component, computed, inject } from '@angular/core';
import { CreateAdFacade } from './create-ad.facade';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-step-review',
  standalone: true,
  templateUrl: './step-review.component.html',
})
export class StepReviewComponent {

  facade = inject(CreateAdFacade);
  canPublish = computed(() => this.facade.allValid());
  private toastrService = inject(ToastrService);
  private routerService = inject(Router); 

  previous(){
    this.routerService.navigate(['/photos']);
  }

  publish() {
    this.facade.createAd();
  }

  discard(){
    this.facade.clearSavedData();
    this.routerService.navigate(['/']);
    this.toastrService.error('Creazione annuncio interrotta!', "Cancellazione");
  }
}
