import { Component, computed, inject } from '@angular/core';
import { CreateAdFacade } from './create-ad.facade';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { DiscardDialogComponent } from '../dialog/discard-dialog/discard-dialog.component';

@Component({
  selector: 'app-step-review',
  standalone: true,
  imports: [DiscardDialogComponent],
  templateUrl: './step-review.component.html',
})
export class StepReviewComponent {
  private toastrService = inject(ToastrService);
  private routerService = inject(Router);
  private activatedRoute = inject(ActivatedRoute);

  facade = inject(CreateAdFacade);
  canPublish = computed(() => this.facade.allValid());
  isDiscardModalOpen = false;

  openDiscardModal() {
    this.isDiscardModalOpen = true;
  }

  closeDiscardModal() {
    this.isDiscardModalOpen = false;
  }

  confirmDiscard() {
    this.closeDiscardModal();
    this.facade.clearSavedData();
    this.routerService.navigate(['/']);
    this.toastrService.error('Creazione annuncio interrotta!', 'Cancellazione');
  }

  previous() {
    this.routerService.navigate(['../photos'], {
      relativeTo: this.activatedRoute,
    });
  }

  publish() {
    this.facade.createAd();
  }

  discard() {
    this.facade.clearSavedData();
    this.routerService.navigate(['/']);
    this.toastrService.error('Creazione annuncio interrotta!', 'Cancellazione');
  }
}
