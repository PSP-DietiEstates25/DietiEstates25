import { Component, computed, inject, OnDestroy } from '@angular/core';
import { CreateAdFacade } from './create-ad.facade';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { DiscardDialogComponent } from '../dialog/discard-dialog/discard-dialog.component';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-step-review',
  standalone: true,
  imports: [DiscardDialogComponent],
  templateUrl: './step-review.component.html',
})
export class StepReviewComponent implements OnDestroy {
  private toastrService = inject(ToastrService);
  private routerService = inject(Router);
  private activatedRoute = inject(ActivatedRoute);

  facade = inject(CreateAdFacade);

  canPublish = computed(() => this.facade.allValid());
  isDiscardModalOpen = false;

  private sub = new Subscription();

  constructor() {
    const anyFacade = this.facade as any;

    const success$ = anyFacade?.published$ ?? anyFacade?.saved$;

    if (success$ && typeof success$.subscribe === 'function') {
      this.sub.add(
        success$.subscribe(() => {
          const isEdit = anyFacade?.mode === 'edit';
          this.toastrService.success(
            isEdit ? 'Modifiche salvate.' : 'Annuncio creato.',
            'Successo',
          );
        }),
      );
    }
  }

  openDiscardModal() {
    this.isDiscardModalOpen = true;
  }

  closeDiscardModal() {
    this.isDiscardModalOpen = false;
  }

  confirmDiscard() {
    const anyFacade = this.facade as any;
    const isEdit = anyFacade?.mode === 'edit';

    this.closeDiscardModal();

    if (typeof anyFacade?.clearSavedData === 'function') {
      anyFacade.clearSavedData();
    }

    this.routerService.navigate([isEdit ? '/agent' : '/']);
    this.toastrService.error(
      isEdit ? 'Modifica annuncio interrotta!' : 'Creazione annuncio interrotta!',
      'Cancellazione',
    );
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
    const anyFacade = this.facade as any;
    const isEdit = anyFacade?.mode === 'edit';

    if (typeof anyFacade?.clearSavedData === 'function') {
      anyFacade.clearSavedData();
    }

    this.routerService.navigate([isEdit ? '/agent' : '/']);
    this.toastrService.error(
      isEdit ? 'Modifica annuncio interrotta!' : 'Creazione annuncio interrotta!',
      'Cancellazione',
    );
  }

  ngOnDestroy() {
    this.sub.unsubscribe();
  }
}
